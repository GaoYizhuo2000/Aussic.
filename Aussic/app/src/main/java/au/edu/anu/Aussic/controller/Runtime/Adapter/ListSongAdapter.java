package au.edu.anu.Aussic.controller.Runtime.Adapter;

/**
 * @author: u7516507, Evan Cheung
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.List;

import au.edu.anu.Aussic.R;

public class ListSongAdapter extends RecyclerView.Adapter<ListSongAdapter.ListSongViewHolder> {

    protected List<GeneralItem> items;
    protected OnGeneralItemClickListener listener;


    public ListSongAdapter(List<GeneralItem> items, OnGeneralItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }


    @Override
    public ListSongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_song_layout, parent, false);
        return new ListSongViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(ListSongViewHolder holder, int position) {
        GeneralItem generalItem = items.get(position);
        holder.listTitle.setText(generalItem.getSongName());
        holder.listArtist.setText(generalItem.getSongArtistName());

        // Load image from the web using Glide
        Glide.with(holder.listImage.getContext())
                .load(generalItem.getSongImageUrl())
                .apply(new RequestOptions().override((int)(360 * 0.8), (int)(360 * 0.8)))
                .into(holder.listImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ListSongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView listImage;
        protected TextView listTitle;
        protected TextView listArtist;
        protected List<GeneralItem> items;
        protected OnGeneralItemClickListener listener;

        public ListSongViewHolder(View view, ListSongAdapter listSongAdapter) {
            super(view);
            listImage = view.findViewById(R.id.item_image);
            listTitle = view.findViewById(R.id.item_title);
            listArtist = view.findViewById(R.id.item_artist);
            items = listSongAdapter.items;
            listener = listSongAdapter.listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                try {
                    listener.onItemClicked(items.get(position));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

