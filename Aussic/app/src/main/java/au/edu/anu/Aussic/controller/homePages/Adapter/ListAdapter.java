package au.edu.anu.Aussic.controller.homePages.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import au.edu.anu.Aussic.R;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private List<ItemSpec> items;


    public ListAdapter(List<ItemSpec> items) {
        this.items = items;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
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

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView listImage;
        public TextView listTitle;
        public TextView listArtist;

        public ListViewHolder(View view) {
            super(view);
            listImage = view.findViewById(R.id.item_image);
            listTitle = view.findViewById(R.id.item_title);
            listArtist = view.findViewById(R.id.item_artist);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {

            }
        }
    }
}

