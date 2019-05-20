package nl.hogeschoolrotterdam.projectb.fragment;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import nl.hogeschoolrotterdam.projectb.R;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Image;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Media;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Memory;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Video;
import nl.hogeschoolrotterdam.projectb.util.AnalyticsUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maartendegoede on 2019-05-19.
 * Copyright © 2019 insertCode.eu. All rights reserved.
 */
public class ShareFragment extends BottomSheetDialogFragment {
    private Memory memory;

    public ShareFragment(Memory memory) {
        this.memory = memory;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NestedScrollView view = (NestedScrollView) inflater.inflate(R.layout.fragment_share, container);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                NestedScrollView view = (NestedScrollView) getView();
                ((ViewGroup) view.getChildAt(0)).removeAllViews();
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());

                List<Media> media = memory.getMedia();


                // Original source for the filtering: https://gist.github.com/rafayali/8c9c0563782464427e55
                // Action is type [Intent.ACTION_SEND_MULTIPLE] if media size > 1, [Intent.ACTION_SEND] otherwise
                String action = media.size() > 1 ? Intent.ACTION_SEND_MULTIPLE : Intent.ACTION_SEND;

                // share type is text if it does not contain images
                // But it is image if it contains images
                // or video if it contains videos
                // and it is type all if it contains both videos and images
                String shareType = "text/*";
                for (Media m : media) {
                    if (m instanceof Image)
                        shareType = shareType.equals("video/*") ? "*/*" : "image/*";
                    if (m instanceof Video)
                        shareType = shareType.equals("image/*") ? "*/*" : "video/*";//todo: apparently this does not work
                }

                // Set
                Intent sendIntent = new Intent(action);
                sendIntent.setType(shareType);
                sendIntent.putExtra(Intent.EXTRA_TITLE, memory.getTitle());
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, memory.getTitle());
                sendIntent.putExtra(Intent.EXTRA_TEXT, memory.getTitle() + ":\n" + memory.getDescription());
                //todo: extra date
                //todo: extra location

                // Add all images and videos to the intent
                if (media.size() > 0) {
                    ArrayList<Uri> uris = new ArrayList<>();
                    for (Media m : media) {
                        try {
                            String uri = getContentUriForMedia(m);
                            uris.add(Uri.parse(uri));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    sendIntent.putExtra(Intent.EXTRA_STREAM, uris);
                    sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }


                // Filter out everything that does not work
                PackageManager packageManager = getContext().getPackageManager();
                List<ResolveInfo> resInfos = packageManager.queryIntentActivities(sendIntent, 0);
                List<LabeledIntent> intentList = new ArrayList<>();

                for (ResolveInfo resInfo : resInfos) {
                    final String packageName = resInfo.activityInfo.packageName;
                    final Intent intent = new Intent(sendIntent.getAction());
                    intent.putExtra(Intent.EXTRA_TEXT, memory.getTitle() + ":\n" + memory.getDescription());

                    Log.wtf("package", "packageName:" + packageName);
                    if (packageName.contains("facebook") ||//todo: remove, facebook will only be done with their sdk
                            packageName.equals("com.whatsapp") ||
                            packageName.contains("telegram") ||
                            packageName.equals("org.thunderdog.challegram") || // telegramX
                            packageName.equals("com.google.android.youtube") ||
                            packageName.contains("android.gm")) { // GMail

                        intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                        intent.putExtra(Intent.EXTRA_TEXT, sendIntent.getStringExtra(Intent.EXTRA_TEXT));
                        intent.setType(sendIntent.getType());
                        intent.putExtra(Intent.EXTRA_STREAM, (ArrayList) sendIntent.getExtras().get(Intent.EXTRA_STREAM));
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        if (Build.VERSION.SDK_INT < 28) {
                            // video sharing doesn't work on older devices for YouTube, Facebook and Gmail
                            if (packageName.contains("facebook") || packageName.contains("android.gm")) {
                                if (shareType.equals("video/*"))
                                    continue;
                            }
                            if (packageName.equals("com.google.android.youtube"))
                                continue;
                        }
                        if (packageName.contains("facebook")) {
                            // Apparently Facebook does not work properly with just text
                            if (shareType.equals("text/*"))
                                continue;
                        }
                        if (packageName.equals("com.whatsapp")) {
                            //Apparently WhatsApp does not work properly with just text
                            if (shareType.equals("text/*"))
                                continue;
                        }
                        if (packageName.equals("org.thunderdog.challegram") || packageName.contains("telegram")) {
                            //Apparently in Telegram and TelegramX only ACTION_SEND_MULTIPLE works and ACTION_SEND does not
                            if (shareType.equals("text/*"))
                                intent.setAction(Intent.ACTION_SEND);
                            else intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                        }
                        if (packageName.equals("com.google.android.youtube")) {
                            //Apparently YouTube only ACTION_SEND_MULTIPLE works and ACTION_SEND does not
                            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                        }
                        if (packageName.contains("android.gm")) {
                            //Apparently in GMail only ACTION_SEND_MULTIPLE works and ACTION_SEND does not
                            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                            intent.putExtra(Intent.EXTRA_SUBJECT, memory.getTitle());
                            intent.setType("message/rfc822");
                        }

                        intentList.add(new LabeledIntent(intent, packageName, resInfo.loadLabel(packageManager), resInfo.icon));


                        View shareView = layoutInflater.inflate(R.layout.item_share, view, false);
                        ViewGroup.LayoutParams params = shareView.getLayoutParams();
                        params.width = view.getMeasuredWidth() / 5;
                        shareView.setLayoutParams(params);
                        ((ViewGroup) view.getChildAt(0)).addView(shareView);
                        ((TextView) shareView.findViewById(R.id.share_app_name)).setText(resInfo.loadLabel(packageManager));
                        ((ImageView) shareView.findViewById(R.id.share_app_icon)).setImageDrawable(resInfo.loadIcon(packageManager));

                        shareView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(intent);
                                AnalyticsUtil.share(getContext(), packageName);
                                dismiss();
                            }
                        });
                    }
                }
            }
        });
    }


    private String getContentUriForMedia(Media media) throws FileNotFoundException {
        ContentResolver contentResolver = requireContext().getContentResolver();

        if (media instanceof Video) {
            String filePath = ((Video) media).getVideoPath();
            Uri videosUri = MediaStore.Video.Media.getContentUri("external");
            String[] projection = {MediaStore.Video.VideoColumns._ID};

            Cursor cursor = contentResolver.query(videosUri, projection, MediaStore.Video.VideoColumns.DATA + " LIKE ?", new String[]{filePath}, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                    long videoId = cursor.getLong(columnIndex);

                    return videosUri.toString() + "/" + videoId;
                }
            } finally {
                if (cursor != null)
                    cursor.close();

            }
            return filePath;
        } else {
            String filePath = ((Image) media).getImagePath();
            return MediaStore.Images.Media.insertImage(contentResolver, filePath, media.getMemoryId() + ":" + media.getId(), "description");
        }
    }

}