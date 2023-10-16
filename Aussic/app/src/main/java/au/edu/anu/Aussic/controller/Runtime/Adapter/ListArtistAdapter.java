package au.edu.anu.Aussic.controller.Runtime.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.List;

import au.edu.anu.Aussic.R;

public class ListArtistAdapter extends ListSongAdapter{


    public ListArtistAdapter(List<ItemSpec> items, OnItemSpecClickListener listener) {
        super(items, listener);
    }

    @Override
    public void onBindViewHolder(ListSongViewHolder holder, int position) {
        ItemSpec itemSpec = items.get(position);
        holder.listTitle.setText(itemSpec.getArtistName());
        holder.listArtist.setText("artist");

        // Load image from the web using Glide
        Glide.with(holder.listImage.getContext())
                .load(itemSpec.getSongImageUrl())
                .circleCrop()
                .apply(new RequestOptions().override((int)(360 * 0.8), (int)(360 * 0.8)))
                .into(holder.listImage);
    }
}
