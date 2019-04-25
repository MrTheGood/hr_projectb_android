package nl.hogeschoolrotterdam.projectb.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import nl.hogeschoolrotterdam.projectb.MemoryDetailActivity;
import nl.hogeschoolrotterdam.projectb.R;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Memory;

import java.util.List;


public class Memories_Adapter extends RecyclerView.Adapter<Memories_Adapter.Memories_Viewholder> {

    private List<Memory> data;

    public Memories_Adapter(List<Memory> data) {
        this.data = data;

    }

    public void setData(List<Memory> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Memories_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_memory, parent, false);
        return new Memories_Viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Memories_Viewholder holder, int position) {
        final Memory memory = data.get(position);
        holder.Textviewtitle.setText(memory.getTitle());
        holder.Textviewdate.setText(memory.getDateText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MemoryDetailActivity.class);
                intent.putExtra("EXTRA_SESSION_ID", memory.getId());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Memories_Viewholder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView imageView;
        TextView Textviewtitle;
        TextView Textviewdate;

        public Memories_Viewholder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.imageView);
            Textviewtitle = itemView.findViewById(R.id.Textviewtitle);
            Textviewdate = itemView.findViewById(R.id.Textviewdate);


        }
    }
}
