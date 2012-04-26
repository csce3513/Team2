package platformer;
import jgame.*;
import jgame.platform.*;
import javax.swing.JOptionPane;

public class Platformer extends JGEngine{
    private PlayerObject player;
    private double jumpBase = 3000.0; //The position where the player was when a jump starts
                                        //initialized so far down in order to allow player to fall first time
    private final double maxJump = 50.0; //The maximum height for our player to jump
    private boolean playerWin;		//boolean flag to see if the player has won
    public boolean gameStart;
    private EnemyObject enemy;
    
    public Platformer(JGPoint size){
        initEngine(size.x, size.y);
        player = new PlayerObject();
        enemy = new EnemyObject();
        gameStart = true;
    }
    
    public void initCanvas(){
        //This method holds anything we need to initialize
        setCanvasSettings(20,15,16,16,null,null,null);
        //System.out.println("In initCanvas()");
    }
    
    public void initGame(){
        setFrameRate(35,2);
        defineMedia("Platformer.tbl");
        setBGImage("background");
        setGameState("InGame");
    }
    
    public void startStartGame(){
            removeObjects(null, 0, true);
            clearKey(KeyEnter);
    }
    
    public void doFrameStartGame(){
        
        if(getKey(KeyEnter)){
            clearKey(KeyEnter);
            //addGameState("InGame");
            setGameState("InGame");
        }
    }
    
    public void paintFrameStartGame(){
        drawString("Welcome to Definitely Not Mario!", pfWidth()/2, 20, 0, null, JGColor.black);
        drawString("Press Enter to Begin!", pfWidth()/2, 30, 0, null, JGColor.black);

    }
    
    public void startInGame(){
        //player = new PlayerObject();
        //enemy = new EnemyObject();
        setTiles(0,11, new String[] {"###############"});
        setTiles(7, 9, new String[] {"#####"});
        setTileSettings("#", 2, 0);
    }
    
    public void doFrameInGame(){
        moveObjects();	//Moves the "Player" object with collision id of 1
        checkWin();	//check to see if we've passed the end-of-level marker
        checkBGCollision(
			1+2, // collide with the ground and border tiles
			1    // cids of our objects
		);
        checkDeath();
    }
    
    public PlayerObject getPlayer(){
        return player;
    }
    
    public EnemyObject getEnemy(){
        return enemy;
    }
    
    public void paintFrameInGame(){
		//This method holds anything we need to draw every frame
        drawString("Score : 0", 0, 5, -1, null, JGColor.black);
    }
    
    public void startGameOver(){
        removeObjects(null, 0, true);
        clearKey(KeyShift);
    }
    
    public void paintFrameGameOver(){
        drawString("This is the GameOver screen", pfWidth()/2, 0, 0, null, JGColor.black);
    }
    
    public void doFrameGameOver(){
        if(getKey(KeyShift))
        {
            clearKey(KeyShift);
            setGameState("InGame");
        }
    }
    
    public void checkWin(){
        //System.out.println("In checkWin()");
        if(player.x == 100.0)
        {
            playerWin = true;
            //setGameState("WinGame");
        }
    }
    
    public void checkDeath(){
        if(getKey(KeyShift))
            setGameState("GameOver");
    }
   
    public boolean getPlayerWin(){
        return playerWin;
    }
    
    public static void main() {
        new Platformer(new JGPoint(640,480));
    }
    

    //Player class currently in Engine class in order to use draw methods
    public class PlayerObject extends JGObject{
        private boolean hitJumpApex; //boolean to tell us when to stop going up and start coming down
					//from a jump
        private int lives;
        private final int jumpSpeed = 2;
        //Constructor
        PlayerObject(){
            super("Player", true, 30,150,1,"myanim_l1");
            xspeed=0;
            yspeed=0;
            hitJumpApex = false;
            lives = 3;
        }
        
        public void move() 
        {
            //Instead of using x or yspeed, we alter position ourselves
            //This gives us finer control over player position, and allows
            //the player object to stop moving as soon as the button is released
            if(getKey(KeyRight))
                player.xspeed = 1;
            if(getKey(KeyLeft))
                player.xspeed = -1;
            if(!(getKey(KeyRight)||getKey(KeyLeft)))
                player.xspeed = 0;
				
            //KeyUp is the jump key
            //It is important to remember that y values INCREASE as we go DOWN the screen
            if(getKey(KeyUp))
            {
                //if the player's position is now at the apex of the jump, we set the flag
                if(player.y <= jumpBase - maxJump)
                    hitJumpApex = true;
                //if we're below the max height and we haven't hit the apex,
                //keep going up
                if((player.y > jumpBase - maxJump)&&!hitJumpApex)
                    player.yspeed = -jumpSpeed;
                //If we're still above where we started the jump and
                //we previously hit the apex, we come back down
                else if((player.y < jumpBase)&&hitJumpApex)
                    fall();
            }
            //This covers us when we release the jump key in the middle of a jump
            if(!getKey(KeyUp))
            //If we're not at the jump base whenever the UP key isn't pressed,
            fall();
        }

        public void hit_bg(int tilecid) {

            if (and(checkBGCollision(0,player.y + 16),3)) {
                    jumpBase = player.y;
                    hitJumpApex = false;

            } else if (and(checkBGCollision(player.x+16,0),3)) {
                    player.x--;

            } else if (and(checkBGCollision(player.x-16, 0),3)) {
                    player.x++;

            } else if (and(checkBGCollision(0,player.y-16), 3)){
                fall();
            }
                        
                        
        }
        
        private void fall(){
                if(!and(checkBGCollision(0,0),3))
                {
                    player.yspeed = jumpSpeed;// = player.y+1;
                }
                else
                    player.yspeed = 0;
        }
    }

    //Enemy Class
    public class EnemyObject extends JGObject{
            
            //Constructor
            EnemyObject(){
                super("Enemy",true,100,80,3,"myanim_l2");
                xspeed=0;
                yspeed=0;
            }
            public void move(){
              if(enemy.x<=125)
                  enemy.xspeed=1;
              if(enemy.x>=185)
                  enemy.xspeed=-1;
              fall();
            }
               
            private void fall(){
                if(!and(checkBGCollision(0,0),3))
                    enemy.y = enemy.y+1;
            }
        }
    
}
