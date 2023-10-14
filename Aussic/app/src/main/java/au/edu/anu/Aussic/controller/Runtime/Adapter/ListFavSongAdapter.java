package au.edu.anu.Aussic.controller.Runtime.Adapter;

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

public class ListFavSongAdapter extends RecyclerView.Adapter<ListFavSongAdapter.ListSongFavViewHolder> {

    private List<ItemSpec> items;
    private OnDeleteBtnClickListener listener;


    public ListFavSongAdapter(List<ItemSpec> items, OnDeleteBtnClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public ListSongFavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_song_layout, parent, false);
        return new ListSongFavViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(ListSongFavViewHolder holder, int position) {
        ItemSpec itemSpec = items.get(position);
        holder.listTitle.setText(itemSpec.getName());
        holder.listArtist.setText(itemSpec.getArtistName());

        // Load image from the web using Glide
        Glide.with(holder.listImage.getContext())
                .load(itemSpec.getImageUrl())
                .apply(new RequestOptions().override((int)(360 * 0.8), (int)(360 * 0.8)))
                .into(holder.listImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ListSongFavViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView listImage;
        public TextView listTitle;
        public TextView listArtist;
        private ImageView deleteBtn;
        private List<ItemSpec> items;
        private OnDeleteBtnClickListener listener;

        public ListSongFavViewHolder(View view, ListFavSongAdapter listFavSongAdapter) {
            super(view);
            listImage = view.findViewById(R.id.item_image);
            listTitle = view.findViewById(R.id.item_title);
            listArtist = view.findViewById(R.id.item_artist);
            deleteBtn = view.findViewById(R.id.item_delete_button);
            items = listFavSongAdapter.items;
            listener = listFavSongAdapter.listener;


            view.setOnClickListener(this);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onDeleteBtnClicked(position);
                        }
                    }
                }
            });
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

