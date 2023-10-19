package au.edu.anu.Aussic.controller.Runtime.Adapter;

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

/**
 * @author: u7516507, Evan Cheung
 * the cardview adapter for the given songs
 */
public class CardSongAdapter extends RecyclerView.Adapter<CardSongAdapter.CardViewHolder> {

    protected List<GeneralItem> generalItemList;
    protected OnGeneralItemClickListener listener;


    public CardSongAdapter(List<GeneralItem> cardDataList, OnGeneralItemClickListener listener) {

        this.generalItemList = cardDataList;
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
        GeneralItem generalItem = generalItemList.get(position);
        holder.description.setText(generalItem.getSongName());

        Glide.with(holder.image.getContext())
                .load(generalItem.getSongImageUrl())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return generalItemList.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView description;

        OnGeneralItemClickListener listener;

        List<GeneralItem> generalItemList;


        public CardViewHolder(@NonNull View itemView, CardSongAdapter cardSongAdapter) {
            super(itemView);
            image = itemView.findViewById(R.id.home_hor_cardview_image);
            description = itemView.findViewById(R.id.home_hor_cardview_description);
            listener = cardSongAdapter.listener;
            generalItemList = cardSongAdapter.generalItemList;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                try {
                    listener.onItemClicked(generalItemList.get(position));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}

