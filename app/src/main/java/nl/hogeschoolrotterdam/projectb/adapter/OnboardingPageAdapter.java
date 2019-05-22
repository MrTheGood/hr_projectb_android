package nl.hogeschoolrotterdam.projectb.adapter;

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
        switch (position) {
            case 1:
                break;
            case 2:
                break;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleTextView circleTextView;
        ImageView circleImageView;
        TextView bodyText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleTextView = itemView.findViewById(R.id.circleText);
            circleImageView = itemView.findViewById(R.id.circleImageView);
            bodyText = itemView.findViewById(R.id.bodyText);
        }
    }
}