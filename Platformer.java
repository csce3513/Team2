/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package platformer;
import jgame.*;
import jgame.platform.*;
/**
 *
 * @author jrios
 */
public class Platformer extends JGEngine{
    private PlayerObject player;
    private final double jumpBase = 150.0;
    private final double maxJump = 12.0;
    
    public Platformer(JGPoint size){
        initEngine(size.x, size.y);
        player = new PlayerObject();
        System.out.println("Done with constructor");
    }
    
    public void initCanvas(){
        setCanvasSettings(20,15,16,16,null,null,null);
        System.out.println("In initCanvas()");
    }
    
    public void initGame(){
        setFrameRate(35,2);
        System.out.println("In initGame()");
    }
    
    public void doFrame(){
        moveObjects("Player",1);
        System.out.println("In doFrame()");
        if(existsObject("Player"))
            System.out.println("existsObject worked!");
        
    }
    
    public PlayerObject getPlayer(){
        return player;
    }
    
    public void paintFame(){
        System.out.println("In paintFrame()");
    }
    /**
     * main will start the game
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new Platformer(new JGPoint(640,480));
    }

    //Player class currently in Engine class in order to use draw methods
    public class PlayerObject extends JGObject{
        private boolean hitJumpApex;
        //Constructor
        PlayerObject(){
            super("Player", true, 30,150,1,null);
            xspeed=0;
            yspeed=0;
            hitJumpApex = false;
        }
        
        public void move() 
        {
            if(getKey(KeyRight))
                player.x=player.x+1;
            if(getKey(KeyLeft))
                player.x=player.x-1;
            if(getKey(KeyUp))
            {
                if(player.y == jumpBase - maxJump)
                    hitJumpApex = true;
                if((player.y > jumpBase - maxJump)&&!hitJumpApex)
                    player.y=player.y-1;
                else if((player.y > jumpBase)&&hitJumpApex)
                    player.y = player.y +1;
            }
            if(!getKey(KeyUp))
                if(player.y != jumpBase)
                {
                    player.y = player.y+1;
                    hitJumpApex = false;
                }
        }

        /** Draw the object. */
        public void paint() {
            setColor(JGColor.blue);
            drawOval(x,y,16,16,true,true);
        }
    }

}
