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
            case 0:
                holder.circleTextView.setText("See Where You Have Been");
                holder.bodyText.setText("Easily view all the places where you have been on the map page!");
                holder.circleImageView.setImageResource(R.drawable.img_theme_red);
                break;
            case 1:
                holder.circleTextView.setText("Share Your Memories");
                holder.bodyText.setText("Simply share your memories on WhatsApp, Telegram or Facebook!");
                holder.circleImageView.setImageResource(R.drawable.img_theme_red);
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