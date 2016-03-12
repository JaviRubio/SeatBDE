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

    public guiPlayer(){
        try{
            fis = new FileInputStream("/Users/JaviRubio/Documents/SeatBDE/SeatBDE/Leap/assets/rustyCage.mp3");
            playMP3 = new Player(fis);
            playerThread = new Thread(this);
            threadState=threadStates.Stopped;
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void play() {
        if(null!=threadState)switch (threadState) {
            case Stopped:
                playerThread.start();
                threadState=threadStates.Running;
                break;
            case Suspended:
                playerThread.resume();
                threadState=threadStates.Running;
                break;
            case Running:
                playerThread.suspend();
                threadState=threadStates.Suspended;
                break;
            case Killed:
                playerThread = new Thread(this);
                try {
                    playMP3=new Player(fis);
                    playMP3.play(0);
                } catch (JavaLayerException ex) {
                    Logger.getLogger(guiPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }   playerThread.start();
                threadState=threadStates.Running;
                break;
            default:
                break;
        }
    }
        
    public void stop(){
        if(threadState==threadStates.Suspended || threadState==threadState.Running){
            playerThread.stop();
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
