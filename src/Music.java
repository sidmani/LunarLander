/**
 * Created by Anish Katukam on 5/14/2016.
 */

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;


public class Music {

    static Media com64;

    static MediaPlayer soundPlayer;
    static MediaPlayer musicPlayer;

    public static void fetchTracks() {
        com64 = new Media(new File("src/Com64.mp3").toURI().toString());
    }

    public static void playIntro() {
        musicPlayer = new MediaPlayer(com64);
        musicPlayer.play();
    }
}