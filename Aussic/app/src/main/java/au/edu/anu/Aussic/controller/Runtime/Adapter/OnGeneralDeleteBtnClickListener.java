package au.edu.anu.Aussic.controller.Runtime.Adapter;

/**
 * @author: u7516507, Evan Cheung
 * An interface that define the required behavior of deletable item
 */

public interface OnGeneralDeleteBtnClickListener extends OnGeneralItemClickListener {
    void onDeleteBtnClicked(int position);
}
