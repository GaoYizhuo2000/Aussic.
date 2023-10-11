package au.edu.anu.Aussic.controller.homePages.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.models.observer.OnItemSpecClickListener;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<ItemSpec> itemSpecList;
    private OnItemSpecClickListener listener;


    public CardAdapter(List<ItemSpec> cardDataList, OnItemSpecClickListener listener) {

        this.itemSpecList = cardDataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        ItemSpec itemSpec = itemSpecList.get(position);
        holder.description.setText(itemSpec.getName());

        // TODO: Load the image into holder.image using an image loading library like Glide or Picasso
        Glide.with(holder.image.getContext())
                .load(itemSpec.getImageUrl())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return itemSpecList.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView description;

        OnItemSpecClickListener listener;

        List<ItemSpec> itemSpecList;


        public CardViewHolder(@NonNull View itemView, CardAdapter cardAdapter) {
            super(itemView);
            image = itemView.findViewById(R.id.home_hor_cardview_image);
            description = itemView.findViewById(R.id.home_hor_cardview_description);
            listener = cardAdapter.listener;
            itemSpecList = cardAdapter.itemSpecList;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                try {
                    listener.onItemClicked(itemSpecList.get(position));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
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

    public static String adjustLength(String text){
        if(text.length() > 18) return text.substring(0, 17) + "...";
        else return text;
    }
}

