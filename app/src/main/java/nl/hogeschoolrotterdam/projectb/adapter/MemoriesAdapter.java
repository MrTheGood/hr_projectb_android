package nl.hogeschoolrotterdam.projectb.adapter;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import nl.hogeschoolrotterdam.projectb.R;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Memory;

import java.util.ArrayList;
import java.util.List;


public class MemoriesAdapter extends RecyclerView.Adapter<MemoriesAdapter.MemoriesViewholder> {

    private List<Memory> data;
    private OnClickListener onClickListener;

    private SparseBooleanArray selected_items;
    private int current_selected_idx = -1;

    public MemoriesAdapter(List<Memory> data, OnClickListener clickListener) {
        this.data = data;
        onClickListener = clickListener;
        selected_items = new SparseBooleanArray();

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

        holder.itemView.setActivated(selected_items.get(position, false));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onItemClick(v, memory, position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClickListener.onItemLongClick(v, memory, position);
                return true;
            }
        });

        toggleCheckedIcon(holder, position);
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

        View lyt_checked;

        MemoriesViewholder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.imageView);
            Textviewtitle = itemView.findViewById(R.id.Textviewtitle);
            Textviewdate = itemView.findViewById(R.id.Textviewdate);
            lyt_checked = itemView.findViewById(R.id.lyt_checked);
        }
    }

    private void toggleCheckedIcon(MemoriesViewholder holder, int position) {
        if (selected_items.get(position, false)) {
            holder.imageView.setVisibility(View.INVISIBLE);
            holder.lyt_checked.setVisibility(View.VISIBLE);
            if (current_selected_idx == position) resetCurrentIndex();
        } else {
            holder.lyt_checked.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            if (current_selected_idx == position) resetCurrentIndex();
        }
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

    public List<Memory> getSelectedItems() {
        List<Memory> items = new ArrayList<>(selected_items.size());
        for (int i = 0; i < selected_items.size(); i++) {
            items.add(data.get(selected_items.keyAt(i)));
        }
        return items;
    }

    public void removeData(Memory memory) {
        data.remove(memory);
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
