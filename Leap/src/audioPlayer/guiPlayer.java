package audioPlayer;

import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


public class guiPlayer implements Runnable{

    private Player playMP3;
    private Thread playerThread;
    private enum threadStates {Running,Stopped,Suspended,Killed};
    private threadStates threadState;
    private  FileInputStream fis;

    public guiPlayer(String uri){
        try{
            fis = new FileInputStream(uri);
            playMP3 = new Player(fis);
            playerThread = new Thread(this);
            threadState=threadStates.Stopped;
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void play() {
        if(threadState==threadStates.Stopped){
            playerThread.start();
            threadState=threadStates.Running;
        }
        else if(threadState==threadStates.Suspended){
            playerThread.resume();
            threadState=threadStates.Running;
        }
        else if(threadState==threadStates.Running){
            playerThread.suspend();
            threadState=threadStates.Suspended;
        }
        else if(threadState==threadStates.Killed){
            try {
                playMP3 = new Player(fis);
            } catch (JavaLayerException ex) {
                Logger.getLogger(guiPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
            playerThread = new Thread(this);
            playerThread.start();
            threadState=threadStates.Running;
        }
    }
        
    public void stop(){
        if(threadState==threadStates.Suspended || threadState==threadState.Running){
            playerThread.stop();
            playMP3.close();
            threadState=threadStates.Killed;
        }
    }

    @Override
    public void run() {
        try {
            playMP3.play();
        } catch (JavaLayerException ex) {
            Logger.getLogger(guiPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
