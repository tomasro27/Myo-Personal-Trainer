package hackatbrown.com.myopersonaltrainer;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by Sam on 2/8/2015.
 */
public class MyApp extends Application {
    ArrayList<Song> cued;

    public ArrayList<Song> getCued(){
        return cued;
    }

    public void setCued(ArrayList<Song> s){
        cued = s;
    }
}