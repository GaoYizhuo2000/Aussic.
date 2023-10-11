package au.edu.anu.Aussic.models.observer;

import java.io.IOException;

import au.edu.anu.Aussic.controller.homePages.Adapter.ItemSpec;

public interface OnItemSpecClickListener {
    void onItemClicked(ItemSpec itemSpec) throws IOException;
}
