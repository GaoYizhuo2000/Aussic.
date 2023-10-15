package au.edu.anu.Aussic.controller.Runtime.Adapter;

import java.util.List;

public class ListGenreAdapter extends ListSongAdapter{
    public ListGenreAdapter(List<ItemSpec> items, OnItemSpecClickListener listener) {
        super(items, listener);
    }
}
