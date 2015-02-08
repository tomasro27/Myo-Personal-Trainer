package hackatbrown.com.myopersonaltrainer;

/**
 * Created by Sam on 2/7/2015.
 */
public class Song {
    private long id;
    private String title;
    private String artist;
    private int cuepos=10;

    public Song(long songID, String songTitle, String songArtist) {
        id=songID;
        title=songTitle;
        artist=songArtist;
    }

    public Song(long songID, String songTitle, String songArtist, int cue) {
        id=songID;
        title=songTitle;
        artist=songArtist;
        cuepos=cue;
    }

    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
    public int getCuePos() {return cuepos;}

    public void setCuePos(int s) {cuepos=s;}
}
