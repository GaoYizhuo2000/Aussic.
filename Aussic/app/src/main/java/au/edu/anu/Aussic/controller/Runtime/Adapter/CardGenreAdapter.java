package au.edu.anu.Aussic.controller.Runtime.Adapter;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * @author: u7516507, Evan Cheung
 */
public class CardGenreAdapter extends CardSongAdapter {

    public CardGenreAdapter(List<GeneralItem> cardDataList, OnGeneralItemClickListener listener) {
        super(cardDataList, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        GeneralItem generalItem = generalItemList.get(position);
        holder.description.setText(generalItem.getGenreName());

        Glide.with(holder.image.getContext())
                .load(generalItem.getGenreImageUrl())
                .into(holder.image);
    }
}
