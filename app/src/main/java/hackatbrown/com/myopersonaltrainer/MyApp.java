package hackatbrown.com.myopersonaltrainer;

import android.app.Application;
import android.content.ServiceConnection;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sam on 2/8/2015.
 */
public class MyApp extends Application {
    ArrayList<Song> cued=new ArrayList<Song>();
    ArrayList<Song> songList=new ArrayList<Song>();
    MusicService musicSrv;

    public ArrayList<Song> getCued(){
        return cued;
    }

    public void setCued(ArrayList<Song> s){
        cued = s;
    }

    public ArrayList<Song> getSongList(){
        return songList;
    }

    public void setSongList(ArrayList<Song> s){
        songList = s;
    }

    public HashMap<String, ArrayList<String>> workoutsMap = new HashMap<>();

}