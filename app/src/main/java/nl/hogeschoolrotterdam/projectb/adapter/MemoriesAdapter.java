package nl.hogeschoolrotterdam.projectb.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import nl.hogeschoolrotterdam.projectb.MemoryDetailActivity;
import nl.hogeschoolrotterdam.projectb.R;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Memory;
import nl.hogeschoolrotterdam.projectb.util.AnalyticsUtil;

import java.util.ArrayList;
import java.util.List;


public class MemoriesAdapter extends RecyclerView.Adapter<MemoriesAdapter.MemoriesViewholder> {

    private List<Memory> data;
    private Context ctx;
    //private List<Inbox> items;
    private View.OnClickListener onClickListener = null;

    private SparseBooleanArray selected_items;
    private int current_selected_idx = -1;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public MemoriesAdapter(List<Memory> data) {
        this.data = data;

    }

    public void setData(List<Memory> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MemoriesViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_memory, parent, false);
        return new MemoriesViewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MemoriesViewholder holder, final int position) {
        final Memory memory = data.get(position);
        holder.Textviewtitle.setText(memory.getTitle());
        holder.Textviewdate.setText(memory.getDateText());
        holder.imageView.setImageBitmap(memory.getThumbnail());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MemoryDetailActivity.class);
                intent.putExtra("EXTRA_SESSION_ID", memory.getId());
                v.getContext().startActivity(intent);
                AnalyticsUtil.selectContent(v.getContext(), "SearchOrList");
            }
        });

        holder.lyt_parent.setActivated(selected_items.get(position, false));

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener == null) return;
                onClickListener.onItemClick(v, memory, position);
            }
        });

        holder.lyt_parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onClickListener == null) return false;
                onClickListener.onItemLongClick(v, memory, position);
                return true;
            }
        });

        toggleCheckedIcon(holder, position);
        //displayImage(holder, memory);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MemoriesViewholder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView imageView;
        TextView Textviewtitle;
        TextView Textviewdate;

        public RelativeLayout lyt_checked, lyt_image;
        public View lyt_parent;

        MemoriesViewholder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.imageView);
            Textviewtitle = itemView.findViewById(R.id.Textviewtitle);
            Textviewdate = itemView.findViewById(R.id.Textviewdate);
            lyt_checked = itemView.findViewById(R.id.lyt_checked);
            lyt_image = itemView.findViewById(R.id.lyt_image);
            lyt_parent =  itemView.findViewById(R.id.lyt_parent);
        }
    }


    //Nieuwe shit code
    public MemoriesAdapter(Context mContext, List<Memory> items) {
        this.ctx = mContext;
        this.data = items;
        selected_items = new SparseBooleanArray();
    }

    /*
    private void displayImage(MemoriesViewholder holder, Memory inbox) {
        if (inbox.image != null) {
            holder.imageView.setImageResource(inbox.image);
            holder.imageView.setColorFilter(null);
        } else {
            holder.imageView.setImageResource(R.drawable.shape_circle);
            holder.imageView.setColorFilter(inbox.color);
        }
    }*/

    private void toggleCheckedIcon(MemoriesViewholder holder, int position) {
        if (selected_items.get(position, false)) {
            holder.lyt_image.setVisibility(View.GONE);
            holder.lyt_checked.setVisibility(View.VISIBLE);
            if (current_selected_idx == position) resetCurrentIndex();
        } else {
            holder.lyt_checked.setVisibility(View.GONE);
            holder.lyt_image.setVisibility(View.VISIBLE);
            if (current_selected_idx == position) resetCurrentIndex();
        }
    }

    public Memory getItem(int position) {
        return data.get(position);
    }


    public void toggleSelection(int pos) {
        current_selected_idx = pos;
        if (selected_items.get(pos, false)) {
            selected_items.delete(pos);
        } else {
            selected_items.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selected_items.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selected_items.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selected_items.size());
        for (int i = 0; i < selected_items.size(); i++) {
            items.add(selected_items.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        data.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        current_selected_idx = -1;
    }

    public interface OnClickListener {
        void onItemClick(View view, Memory obj, int pos);

        void onItemLongClick(View view, Memory obj, int pos);
    }
}
