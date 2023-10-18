package au.edu.anu.Aussic.controller.Runtime.Adapter;

/**
 * @author: u7516507, Evan Cheung
 */

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.List;

public class CardGenreAdapter extends CardSongAdapter {

    public CardGenreAdapter(List<GeneralItem> cardDataList, OnGeneralItemClickListener listener) {
        super(cardDataList, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        GeneralItem generalItem = generalItemList.get(position);
        holder.description.setText(generalItem.getGenreName());

        // TODO: Load the image into holder.image using an image loading library like Glide or Picasso
        Glide.with(holder.image.getContext())
                .load(generalItem.getGenreImageUrl())
                .into(holder.image);
    }
}
