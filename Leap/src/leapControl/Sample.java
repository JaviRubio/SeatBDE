package leapControl;
import audioPlayer.guiPlayer;
import carGui.gui;
import java.io.IOException;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;
import java.util.Date;

class SampleListener extends Listener {
    
        gui gui = new gui();
        String ruta="";
        int count=0;
        audioPlayer.guiPlayer reproductor;
        String [] listaCanciones={"assets/rustyCage.mp3","assets/youcan.mp3"};
        int cancionActual;
        
   
    // System out wrapper with date
    private void sout(String msg) {
        Date date = new Date();
        System.out.println("[" + date.toString() + "] " + msg);
    }
    
    public void onInit(Controller controller) {
        sout("Initialized");
        gui.setVisible(true);
        cancionActual=0;
        reproductor = new guiPlayer(listaCanciones[cancionActual]);
    }

    public void onConnect(Controller controller) {
        sout("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        sout("Disconnected");
    }

    public void onExit(Controller controller) {
        sout("Bye");
    }

    public void onFrame(Controller controller) {

        
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
        /*System.out.println("Frame id: " + frame.id()
                         + ", timestamp: " + frame.timestamp()
                         + ", hands: " + frame.hands().count()
                         + ", fingers: " + frame.fingers().count()
                         + ", tools: " + frame.tools().count()
                         + ", gestures " + frame.gestures().count());*/

        //Get hands
        for(Hand hand : frame.hands()) {
            String handType = hand.isLeft() ? "Left hand" : "Right hand";
           /* System.out.println("  " + handType + ", id: " + hand.id()
                             + ", palm position: " + hand.palmPosition());*/

            // Get the hand's normal vector and direction
            Vector normal = hand.palmNormal();
            Vector direction = hand.direction();

            // Calculate the hand's pitch, roll, and yaw angles
           /* System.out.println("  pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
                             + "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
                             + "yaw: " + Math.toDegrees(direction.yaw()) + " degrees");*/

            // Get arm bone
            Arm arm = hand.arm();
            /*System.out.println("  Arm direction: " + arm.direction()
                             + ", wrist position: " + arm.wristPosition()
                             + ", elbow position: " + arm.elbowPosition());*/

            // Get fingers
            for (Finger finger : hand.fingers()) {
                /*System.out.println("    " + finger.type() + ", id: " + finger.id()
                                 + ", length: " + finger.length()
                                 + "mm, width: " + finger.width() + "mm");*/

                //Get Bones
                for(Bone.Type boneType : Bone.Type.values()) {
                    Bone bone = finger.bone(boneType);
                 /*   System.out.println("      " + bone.type()
                                     + " bone, start: " + bone.prevJoint()
                                     + ", end: " + bone.nextJoint()
                                     + ", direction: " + bone.direction());*/
                }
            }
        }

        // Get tools
        for(Tool tool : frame.tools()) {
          /*  System.out.println("  Tool id: " + tool.id()
                             + ", position: " + tool.tipPosition()
                             + ", direction: " + tool.direction());*/
        }

        GestureList gestures = frame.gestures();
        for (int i = 0; i < gestures.count(); i++) {
            Gesture gesture = gestures.get(i);

            switch (gesture.type()) {
                case TYPE_CIRCLE:
                    
                    CircleGesture circle = new CircleGesture(gesture);

                    // Calculate clock direction using the angle between circle normal and pointable
                    String clockwiseness;
                    if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/2) {
                        // Clockwise if angle is less than 90 degrees
                        clockwiseness = "clockwise";
                    } else {
                        clockwiseness = "counterclockwise";
                    }

                    // Calculate angle swept since last frame
                    double sweptAngle = 0;
                    if (circle.state() != State.STATE_START) {
                        CircleGesture previousUpdate = new CircleGesture(controller.frame(1).gesture(circle.id()));
                        sweptAngle = (circle.progress() - previousUpdate.progress()) * 2 * Math.PI;
                    }

                   /* System.out.println("  Circle id: " + circle.id()
                               + ", " + circle.state()
                               + ", progress: " + circle.progress()
                               + ", radius: " + circle.radius()
                               + ", angle: " + Math.toDegrees(sweptAngle)
                               + ", " + clockwiseness);*/
                    
                    String aux="/carGui/rotor";
                    if(clockwiseness=="clockwise")
                        if(count<4){
                            count++;
                        }
                    if(clockwiseness=="counterclockwise")
                        if(count>0){
                            count--;
                        }   
                    aux=aux+count;
                    aux=aux+".png";
                    System.out.println(aux);
                    ruta=aux;
                    sout("CIRCLE " + clockwiseness);
                    break;
                case TYPE_SWIPE:
                    SwipeGesture swipe = new SwipeGesture(gesture);
                    sout("SWIPE");
                   /* System.out.println("  Swipe id: " + swipe.id()
                               + ", " + swipe.state()
                               + ", position: " + swipe.position()
                               + ", direction: " + swipe.direction()
                               + ", speed: " + swipe.speed());*/
                    ruta="/carGui/Boton ANTERIOR_mini.png";
                    break;
                case TYPE_SCREEN_TAP:
                    ScreenTapGesture screenTap = new ScreenTapGesture(gesture);
                    sout("SCREEN_TAP");
                   /* System.out.println("  Screen Tap id: " + screenTap.id()
                               + ", " + screenTap.state()
                               + ", position: " + screenTap.position()
                               + ", direction: " + screenTap.direction());*/
                    ruta="/carGui/Botón PLAY_mini.png";
                   
                    reproductor.play();
                    break;
                case TYPE_KEY_TAP:
                    KeyTapGesture keyTap = new KeyTapGesture(gesture);
                    sout("KEY_TAP");
                    /*System.out.println("  Key Tap id: " + keyTap.id()
                               + ", " + keyTap.state()
                               + ", position: " + keyTap.position()
                               + ", direction: " + keyTap.direction());*/
                    ruta="/carGui/Botón No_mini.png";
                    cancionActual++;
                    reproductor.stop();
                    reproductor = new guiPlayer(listaCanciones[cancionActual%2]);
                    break;
                default:
                    //System.out.println("Unknown gesture type.");
                    sout("UNKNOWN");
                    break;
            }
            try {
                Thread.sleep(700);
            } catch (Exception e){
            }
            gui.setImage(ruta);
            
        }

        if (!frame.hands().isEmpty() || !gestures.isEmpty()) {
            //System.out.println();
        }
    }
}

class Sample {
    public static void main(String[] args) {
        
        // Create a sample listener and controller
        SampleListener listener = new SampleListener();
        Controller controller = new Controller();

        // Have the sample listener receive events from the controller
        controller.addListener(listener);
        
        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the sample listener when done
        controller.removeListener(listener);
    }
}
