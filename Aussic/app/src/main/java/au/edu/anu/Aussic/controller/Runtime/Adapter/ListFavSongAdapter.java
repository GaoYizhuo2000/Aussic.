package au.edu.anu.Aussic.controller.Runtime.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import au.edu.anu.Aussic.R;

public class ListFavSongAdapter extends ListSongAdapter {

    private OnDeleteBtnClickListener deleteListener;

    public ListFavSongAdapter(List<ItemSpec> items, OnDeleteBtnClickListener listener) {
        super(items, listener);
        this.deleteListener = listener;
    }

    @Override
    public ListSongFavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_song_layout, parent, false);
        return new ListSongFavViewHolder(itemView, this);
    }


    public class ListSongFavViewHolder extends ListSongViewHolder {
        private ImageView deleteBtn;
        private OnDeleteBtnClickListener deleteListener;

        public ListSongFavViewHolder(View view, ListFavSongAdapter listFavSongAdapter) {
            super(view, listFavSongAdapter);
            deleteBtn = view.findViewById(R.id.item_delete_button);
            deleteListener = listFavSongAdapter.deleteListener;
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(deleteListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            deleteListener.onDeleteBtnClicked(position);
                        }
                    }
                }
            });
        }
    }
}

