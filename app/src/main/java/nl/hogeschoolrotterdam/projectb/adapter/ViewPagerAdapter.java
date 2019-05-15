package nl.hogeschoolrotterdam.projectb.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import nl.hogeschoolrotterdam.projectb.R;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Image;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Media;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Video;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.PagerVH> {
    final private int TYPE_VIDEO = 0;
    final private int TYPE_IMAGE = 1;
    private List<Media> media = new ArrayList<>();

    public void setMedia(List<Media> media) {
        this.media = media;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PagerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_IMAGE)
            return new PagerVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page, parent, false));
        if (viewType == TYPE_VIDEO)
            return new PagerVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false));
        throw new IllegalStateException("UNKOWN TYPE: " + viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (media.get(position) instanceof Video)
            return TYPE_VIDEO;
        if (media.get(position) instanceof Image)
            return TYPE_IMAGE;
        return -1;
    }

    @Override
    public int getItemCount() {
        return media.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final PagerVH holder, int position) {
        final Media m = media.get(position);
        if (m instanceof Image)
            holder.pageImage.setImageBitmap(((Image) m).getImage());
        if (m instanceof Video) {
            holder.pageImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(((Video) m).getVideoPath()));
                    intent.setDataAndType(Uri.parse(((Video) m).getVideoPath()), "video/mp4");
                    holder.pageImage.getContext().startActivity(intent) ;
                }
            });
            holder.pageImage.setImageBitmap(((Video) m).getThumbnail());
        }
    }

    class PagerVH extends RecyclerView.ViewHolder {
        ImageView pageImage;
        View container;

        PagerVH(@NonNull View itemView) {
            super(itemView);
            pageImage = itemView.findViewById(R.id.pageImage);
            container = itemView.findViewById(R.id.container);
        }
    }
}