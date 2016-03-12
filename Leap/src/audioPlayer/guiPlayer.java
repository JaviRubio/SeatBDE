package audioPlayer;

import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


public class guiPlayer {

    Player playMP3;

    public guiPlayer(){
        try{
        FileInputStream fis = new FileInputStream("/Users/JaviRubio/Documents/SeatBDE/SeatBDE/Leap/assets/rustyCage.mp3");
            playMP3 = new Player(fis);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void play() {
        try {
            playMP3.play();
        } catch (JavaLayerException ex) {
            Logger.getLogger(guiPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
