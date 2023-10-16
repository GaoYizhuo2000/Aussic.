package au.edu.anu.Aussic.controller.Runtime.Adapter;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.List;

public class ListGenreAdapter extends CardAdapter{

    public ListGenreAdapter(List<ItemSpec> cardDataList, OnItemSpecClickListener listener) {
        super(cardDataList, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        ItemSpec itemSpec = itemSpecList.get(position);
        holder.description.setText(itemSpec.getGenreName());

        // TODO: Load the image into holder.image using an image loading library like Glide or Picasso
        Glide.with(holder.image.getContext())
                .load(itemSpec.getGenreImageUrl())
                .into(holder.image);
    }
}
