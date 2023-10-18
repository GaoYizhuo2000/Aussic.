package au.edu.anu.Aussic.controller.Runtime.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.controller.entityPages.ArtistActivity;
import au.edu.anu.Aussic.controller.entityPages.GenreActivity;
import au.edu.anu.Aussic.controller.entityPages.SongActivity;
import au.edu.anu.Aussic.controller.homePages.P2P.MessageActivity;
import au.edu.anu.Aussic.models.GsonLoader.GsonLoader;
import au.edu.anu.Aussic.models.entity.Session;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;

/**
 * @author: u7516507, Evan Cheung
 */

public interface OnGeneralItemClickListener {
    default void onItemClicked(GeneralItem generalItem) throws IOException {
        FirestoreDao firestoreDao;
        Intent intent = null;
        switch (generalItem.getType()){
            case "songs":
                firestoreDao = new FirestoreDaoImpl();
                RuntimeObserver.setCurrentSong(generalItem.getSong());
                // Set up real time listener for this song
                firestoreDao.setSongRealTimeListener(RuntimeObserver.getCurrentSong());

                RuntimeObserver.getCurrentMediaPlayer().pause();
                RuntimeObserver.getCurrentMediaPlayer().release();
                RuntimeObserver.setMediaPlayer(new MediaPlayer());
                RuntimeObserver.getCurrentMediaPlayer().setDataSource(generalItem.getSong().getUrlToListen());
                RuntimeObserver.getCurrentMediaPlayer().prepare();
                RuntimeObserver.getCurrentMediaPlayer().setLooping(true);
                RuntimeObserver.getCurrentMediaPlayer().start();
                if(this instanceof Fragment) intent = new Intent(((Fragment)this).getContext(), SongActivity.class);
                else if(this instanceof Activity) intent = new Intent(((Activity)this), SongActivity.class);
                break;
            case "artists":
                RuntimeObserver.currentDisplayingArtist = generalItem.getArtist();
                if(this instanceof Fragment) intent = new Intent(((Fragment)this).getContext(), ArtistActivity.class);
                else if(this instanceof Activity) intent = new Intent(((Activity)this), ArtistActivity.class);
                break;
            case "genres":
                RuntimeObserver.currentDisplayingGenre = generalItem.getGenre();
                if(this instanceof Fragment) intent = new Intent(((Fragment)this).getContext(), GenreActivity.class);
                else if(this instanceof Activity) intent = new Intent(((Activity)this), GenreActivity.class);
                break;
            case "users":
                String sessionName = RuntimeObserver.currentUser.username + "&" + generalItem.getUserName();
                String sessionNameAlt =  generalItem.getUserName() + "&" + RuntimeObserver.currentUser.username;

                Session newSession = new Session(sessionName);
                Session newSessionAlt = new Session(sessionNameAlt);

                if(this instanceof Fragment) intent = new Intent(((Fragment)this).getContext(), MessageActivity.class);
                else if(this instanceof Activity) intent = new Intent(((Activity)this), MessageActivity.class);

                // if and else if will check whether the session already exist in the runtime records
                if(RuntimeObserver.currentUserSessions.contains(newSession)){
                    int index = RuntimeObserver.currentUserSessions.indexOf(newSession);
                    Session messageSession = RuntimeObserver.currentUserSessions.get(index);
                    RuntimeObserver.currentMessagingSession = messageSession;
                } else if(RuntimeObserver.currentUserSessions.contains(newSessionAlt)){
                    int index = RuntimeObserver.currentUserSessions.indexOf(newSessionAlt);
                    Session messageSession = RuntimeObserver.currentUserSessions.get(index);
                    RuntimeObserver.currentMessagingSession = messageSession;
                } // Otherwise, the program will create and look for the message session on the firestore
                else {
                    firestoreDao = new FirestoreDaoImpl();
                    firestoreDao.createSession(generalItem.getUserName());
                    Intent finalIntent = intent;
                    firestoreDao.getSession(sessionName).thenAccept(result->{
                        List<Map> maps = new ArrayList<>();
                        maps.add(result);
                        Session session = GsonLoader.loadSession(maps.get(0));
                        // The if will check if the real time listener have already response, if not load the session into the memory and set up real time listener on it
                        if(!RuntimeObserver.currentUserSessions.contains(session)){
                            RuntimeObserver.currentMessagingSession = session;
                            firestoreDao.setSessionRealTimeListener(RuntimeObserver.currentMessagingSession);
                            RuntimeObserver.currentUserSessions.add(RuntimeObserver.currentMessagingSession);
                        }
                        // Otherwise, load the session from memory which have already been load into memory by the collection real time listener
                        else {
                            int index = RuntimeObserver.currentUserSessions.indexOf(session);
                            Session existedSession = RuntimeObserver.currentUserSessions.get(index);
                            RuntimeObserver.currentMessagingSession = existedSession;
                        }
                        RuntimeObserver.notifyOnDataChangeListeners();
                        if(finalIntent != null && this instanceof Fragment) ((Fragment)this).startActivity(finalIntent);
                        else if(finalIntent != null && this instanceof Activity) ((Activity)this).startActivity(finalIntent);
                    });
                    intent = null;
                }
                break;
        }
        if(intent != null && this instanceof Fragment) ((Fragment)this).startActivity(intent);
        else if(intent != null && this instanceof Activity) ((Activity)this).startActivity(intent);
    }
}
