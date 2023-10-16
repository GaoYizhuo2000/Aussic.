package au.edu.anu.Aussic.controller.Runtime.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;

import androidx.fragment.app.Fragment;

import java.io.IOException;

import au.edu.anu.Aussic.controller.Runtime.Adapter.ItemSpec;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.controller.entityPages.ArtistActivity;
import au.edu.anu.Aussic.controller.entityPages.GenreActivity;
import au.edu.anu.Aussic.controller.entityPages.SongActivity;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;

public interface OnItemSpecClickListener{
    default void onItemClicked(ItemSpec itemSpec) throws IOException {

        Intent intent = null;
        switch (itemSpec.getType()){
            case "songs":
                FirestoreDao firestoreDao = new FirestoreDaoImpl();
                RuntimeObserver.setCurrentSong(itemSpec.getSong());
                // Set up real time listener for this song
                firestoreDao.setSongRealTimeListener(RuntimeObserver.getCurrentSong());

                RuntimeObserver.getCurrentMediaPlayer().pause();
                RuntimeObserver.getCurrentMediaPlayer().release();
                RuntimeObserver.setMediaPlayer(new MediaPlayer());
                RuntimeObserver.getCurrentMediaPlayer().setDataSource(itemSpec.getSong().getUrlToListen());
                RuntimeObserver.getCurrentMediaPlayer().prepare();
                RuntimeObserver.getCurrentMediaPlayer().setLooping(true);
                RuntimeObserver.getCurrentMediaPlayer().start();
                if(this instanceof Fragment) intent = new Intent(((Fragment)this).getContext(), SongActivity.class);
                else if(this instanceof Activity) intent = new Intent(((Activity)this), SongActivity.class);
                break;
            case "artists":
                RuntimeObserver.currentDisplayingArtist = itemSpec.getArtist();
                if(this instanceof Fragment) intent = new Intent(((Fragment)this).getContext(), ArtistActivity.class);
                else if(this instanceof Activity) intent = new Intent(((Activity)this), ArtistActivity.class);
                break;
            case "genres":
                RuntimeObserver.currentDisplayingGenre = itemSpec.getGenre();
                if(this instanceof Fragment) intent = new Intent(((Fragment)this).getContext(), GenreActivity.class);
                else if(this instanceof Activity) intent = new Intent(((Activity)this), GenreActivity.class);
                break;
        }
        if(intent != null) ((Fragment)this).startActivity(intent);
    }
}
