package nl.hogeschoolrotterdam.projectb.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
                        String uri = m instanceof Video ? ((Video) m).getVideoPath() : ((Image) m).getImagePath();
                        uris.add(Uri.parse(uri));
                    }
                    sendIntent.putExtra(Intent.EXTRA_STREAM, uris);
                    sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }


                // Filter out everything that does not work
                PackageManager packageManager = getContext().getPackageManager();
                List<ResolveInfo> resInfos = packageManager.queryIntentActivities(sendIntent, 0);
                List<LabeledIntent> intentList = new ArrayList<>();

                for (ResolveInfo resInfo : resInfos) {
                    String packageName = resInfo.activityInfo.packageName;
                    final Intent intent = new Intent(sendIntent.getAction());
                    intent.putExtra(Intent.EXTRA_TEXT, memory.getTitle() + ":\n" + memory.getDescription());

                    Log.wtf("package", "packageName:" + packageName);
                    if (packageName.contains("twitter") ||
                            packageName.contains("facebook") ||//todo: remove, facebook will only be done with their sdk
                            packageName.contains("mms") ||
                            packageName.contains("android.email") ||
                            packageName.equals("com.whatsapp") ||
                            packageName.contains("instagram") ||
                            packageName.contains("telegram") ||
                            packageName.equals("org.thunderdog.challegram") || // telegramX
                            packageName.equals("com.google.android.youtube") ||
                            packageName.contains("android.gm")) {

                        intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                        intent.putExtra(Intent.EXTRA_TEXT, sendIntent.getStringExtra(Intent.EXTRA_TEXT));
                        intent.setType(sendIntent.getType());
                        intent.putExtra(Intent.EXTRA_STREAM, (ArrayList) sendIntent.getExtras().get(Intent.EXTRA_STREAM));
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        if (packageName.contains("android.email")) {
                            intent.setType("vnd.android.cursor.dir/email");
                        } else if (packageName.contains("twitter")) {
                            //todo: does this work properly?
                        } else if (packageName.contains("facebook")) {
                            //todo: replace this

                            // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
                            // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
                            // will show the <meta content ="..."> text from that page with our link in Facebook.
                        } else if (packageName.contains("android.gm")) {
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
                                AnalyticsUtil.share(getContext());
                                dismiss();
                            }
                        });
                    }
                }
            }
        });
    }
}