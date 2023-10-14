package au.edu.anu.Aussic.controller.Runtime.Adapter;

import java.io.IOException;

import au.edu.anu.Aussic.controller.Runtime.Adapter.ItemSpec;

public interface OnItemSpecClickListener {
    void onItemClicked(ItemSpec itemSpec) throws IOException;
}
