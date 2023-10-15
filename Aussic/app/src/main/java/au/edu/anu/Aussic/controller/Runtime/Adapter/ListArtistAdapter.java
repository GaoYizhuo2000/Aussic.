package au.edu.anu.Aussic.controller.Runtime.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import au.edu.anu.Aussic.R;

public class ListArtistAdapter extends ListSongAdapter{
    public ListArtistAdapter(List<ItemSpec> items, OnItemSpecClickListener listener) {
        super(items, listener);
    }
    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        return new ListItemViewHolder(itemView, this);
    }
    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        ItemSpec itemSpec = this.items.get(position);
        holder.listTitle.setText(itemSpec.getArtistName());
        holder.listArtist.setText(itemSpec.getSongArtistName());

        // Load image from the web using Glide
        Glide.with(holder.listImage.getContext())
                .load(itemSpec.getSongImageUrl())
                .apply(new RequestOptions().override((int)(360 * 0.8), (int)(360 * 0.8)))
                .circleCrop()
                .into(holder.listImage);
    }
}
