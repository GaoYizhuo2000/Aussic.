package au.edu.anu.Aussic.controller.homePages.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import au.edu.anu.Aussic.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<CardSpec> cardSpecList;

    public CardAdapter(List<CardSpec> cardDataList) {
        this.cardSpecList = cardDataList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardSpec cardSpec = cardSpecList.get(position);
        holder.description.setText(cardSpec.getDescription());

        // TODO: Load the image into holder.image using an image loading library like Glide or Picasso
        Glide.with(holder.image.getContext())
                .load(cardSpec.getImageUrl())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return cardSpecList.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView description;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.home_hor_cardview_image);
            description = itemView.findViewById(R.id.home_hor_cardview_description);
        }
    }

    public static String makeImageUrl(int width, int height, String rawUrl){
        StringBuilder out = new StringBuilder();
        out.append(rawUrl);
        int wIndex = out.indexOf("{w}");
        if (wIndex != -1) {
            out.replace(wIndex, wIndex + 3, String.valueOf(width));
        }

        int hIndex = out.indexOf("{h}");
        if (hIndex != -1) {
            out.replace(hIndex, hIndex + 3, String.valueOf(height));
        }

        return out.toString();
    }
}

