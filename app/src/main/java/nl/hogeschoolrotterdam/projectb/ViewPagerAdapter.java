package nl.hogeschoolrotterdam.projectb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.PagerVH> {

    private int[] colors = new int[]{
            android.R.color.black,
            android.R.color.holo_red_light,
            android.R.color.holo_blue_dark,
            android.R.color.holo_purple
    };

    @NonNull
    @Override
    public PagerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PagerVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page, parent, false));
    }

    @Override
    public int getItemCount() {
        return colors.length;
    }

    @Override
    public void onBindViewHolder(PagerVH holder, int position) {
        holder.tvTitle.setText("item " + position);
        holder.container.setBackgroundResource(colors[position]);
    }

    class PagerVH extends RecyclerView.ViewHolder {
        TextView tvTitle;
        View container;

        PagerVH(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            container = itemView.findViewById(R.id.container);
        }
    }
}