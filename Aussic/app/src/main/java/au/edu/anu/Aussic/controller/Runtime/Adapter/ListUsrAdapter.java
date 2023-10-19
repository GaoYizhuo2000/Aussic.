package au.edu.anu.Aussic.controller.Runtime.Adapter;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * @author: u7516507, Evan Cheung
 * An adapter for user list
 */

public class ListUsrAdapter extends ListSongAdapter{
    public ListUsrAdapter(List<GeneralItem> items, OnGeneralItemClickListener listener) {
        super(items, listener);
    }
    @Override
    public void onBindViewHolder(ListSongViewHolder holder, int position) {
        GeneralItem generalItem = items.get(position);
        holder.listTitle.setText(generalItem.getUserName());
        holder.listArtist.setText("user");

        // Load image from the web using Glide
        Glide.with(holder.listImage.getContext())
                .load(generalItem.getUserImageUrl())
                .into(holder.listImage);
    }
}
