package au.edu.anu.Aussic.controller.Runtime.Adapter;

/**
 * @author: u7516507, Evan Cheung
 */

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ListArtistAdapter extends ListSongAdapter{


    public ListArtistAdapter(List<GeneralItem> items, OnGeneralItemClickListener listener) {
        super(items, listener);
    }

    @Override
    public void onBindViewHolder(ListSongViewHolder holder, int position) {
        GeneralItem generalItem = items.get(position);
        holder.listTitle.setText(generalItem.getArtistName());
        holder.listArtist.setText("artist");

        // Load image from the web using Glide
        Glide.with(holder.listImage.getContext())
                .load(generalItem.getArtistImageUrl())
                .apply(new RequestOptions().override((int)(360 * 0.8), (int)(360 * 0.8)))
                .circleCrop()
                .into(holder.listImage);
    }
}
