package nl.hogeschoolrotterdam.projectb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import nl.hogeschoolrotterdam.projectb.R;
import nl.hogeschoolrotterdam.projectb.adapter.OnboardingPageAdapter.ViewHolder;
import nl.hogeschoolrotterdam.projectb.util.CircleTextView;

public class OnboardingPageAdapter extends RecyclerView.Adapter<ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding, parent, false));
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        switch (position) {
            case 0:
                holder.circleTextView.setText(context.getString(R.string.str_onboarding_map));
                holder.bodyText.setText(R.string.str_onboarding_map_description);
                holder.circleImageView.setImageResource(R.drawable.img_onboarding_map);
                holder.circleImageView.setPadding(0, 0, 0, 0);
                break;
            case 1:
                holder.circleTextView.setText(context.getString(R.string.str_onboarding_share));
                holder.bodyText.setText(R.string.str_onboarding_share_description);
                holder.circleImageView.setBackgroundResource(R.drawable.background_onboarding_share);
                holder.circleImageView.setImageResource(R.drawable.img_onboarding_share);
                holder.circleImageView.setPadding(0, 16, 32, 16);
                break;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        CircleTextView circleTextView;
        ImageView circleImageView;
        TextView bodyText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            circleTextView = itemView.findViewById(R.id.circleText);
            circleImageView = itemView.findViewById(R.id.circleImageView);
            bodyText = itemView.findViewById(R.id.bodyText);
        }
    }
}