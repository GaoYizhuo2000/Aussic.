package au.edu.anu.Aussic.controller.Runtime.Adapter;

import android.content.Intent;
import android.media.MediaPlayer;

import androidx.fragment.app.Fragment;

import java.io.IOException;

import au.edu.anu.Aussic.controller.Runtime.Adapter.ItemSpec;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.controller.entityPages.SongActivity;

public interface OnItemSpecClickListener{
    default void onItemClicked(ItemSpec itemSpec) throws IOException {
        Intent intent = null;
        switch (itemSpec.getType()){
            case "songs":

                RuntimeObserver.setCurrentSong(itemSpec.getSong());
                RuntimeObserver.getCurrentMediaPlayer().pause();
                RuntimeObserver.getCurrentMediaPlayer().release();
                RuntimeObserver.setMediaPlayer(new MediaPlayer());
                RuntimeObserver.getCurrentMediaPlayer().setDataSource(itemSpec.getSong().getUrlToListen());
                RuntimeObserver.getCurrentMediaPlayer().prepare();
                RuntimeObserver.getCurrentMediaPlayer().setLooping(true);
                RuntimeObserver.getCurrentMediaPlayer().start();
                intent = new Intent(((Fragment)this).getContext(), SongActivity.class);

                break;
            case "artist":
                break;
            case "genres":
                break;
        }
        if(intent != null) ((Fragment)this).startActivity(intent);
    }
}
