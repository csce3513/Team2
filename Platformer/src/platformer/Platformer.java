//Remember: Audio for death
//  player xspeed to 1

import jgame.*;
import jgame.platform.*;
import java.util.Random;

public class Platformer extends JGEngine{
    private PlayerObject player;
    private double jumpBase = 3000.0; //The position where the player was when a jump starts
                                        //initialized so far down in order to allow player to fall first time
    private final double maxJump = 80.0; //The maximum height for our player to jump
    private boolean playerWin;		//boolean flag to see if the player has won
    public boolean gameStart;
    private EnemyObject enemy;
    private EnemyObject enemy2;
    private EnemyObject enemy3;
    private String gameState;
    private int numEnemies;
    private Random gen = new Random();
    private  int leftFrame = 3000;
    private int leftSec;
    
    public Platformer(JGPoint size){
        initEngine(size.x, size.y);
        player = new PlayerObject(3);
        enemy = new EnemyObject(15,5345);
        enemy3 = new EnemyObject(15,5345);
        enemy2 = new EnemyObject(15,5345);
        gameStart = true;
        gameState = "";
        numEnemies = 3;
    }
    
    public void initCanvas(){
        //This method holds anything we need to initialize
        setCanvasSettings(80,35,16,16,null,null,null);
    }
    
    public void initGame(){
       
        
        setFrameRate(50,4);        
        defineMedia("Platformer.tbl");
        setBGImage("background");   
        setGameState("StartGame"); 
        setTiles(0,30, new String[] {"##########"});
        setTiles(9, 26, new String[] {"#######"});
        setTiles(17, 23, new String[] {"#######"});
        setTiles(20,30, new String[] {"##########"});
        setTiles(27, 26, new String[] {"#######.....###"});
        setTiles(32,30, new String[] {"##########"}); 
        setTiles(44, 23, new String[] {"#######"});
        setTiles(48, 30, new String[] {"#######"});
        setTiles(52, 26, new String[] {"#######"});
        setTiles(60, 30, new String[] {"##################"});
        setTiles(0,11, new String[] {"###############"});
        setTiles(7, 8, new String[] {"#####"});
        setTileSettings("#", 2, 0);
        
    }
    public void startStartGame(){
            clearKey(KeyEsc);
            gameState = "StartGame";
            removeObjects(null, 0, true);
            clearKey(KeyEnter);
    }
    
    public void doFrameStartGame(){
        
        if(getKey(KeyEnter)){
            clearKey(KeyEnter);
            //addGameState("InGame");
            setGameState("InGame");
        }
        else if(getKey(KeyEsc))
            exitEngine(null);
    }
    
    public void paintFrameStartGame(){
        drawString("Welcome to Definitely Not Mario!", pfWidth()/2, 20, 0, null, JGColor.black);
        drawString("Press Enter to Begin! Or ESC to exit", pfWidth()/2, 50, 0, null, JGColor.black);

    }
    
    //--------------------------------------------------------------
    // Game State: InGame
    // Called: When the game begins or restarts
    // Calls: GameOver (when all lives lost) or
    //        WinGame (when goal is reached)
    //--------------------------------------------------------------
    
    public void startInGame(){
        removeObjects(null, 0);
        player = new PlayerObject(3);
        enemy = new EnemyObject(470, 350);
        enemy2 = new EnemyObject(310, 300);
        enemy3 = new EnemyObject(1000, 430);
        gameState = "InGame";
        leftFrame = 3000;
       
    }
        
    public void paintFrameInGame(){
        //This method holds anything we need to draw every frame
        this.computeScore();
        drawString("Score : " + player.getScore(), 0, 5, -1, null, JGColor.black);
        drawString("Lives : " + player.life, pfWidth()-3, 5, 1, null, JGColor.black);
        drawImage(1150, 450, "myanim_l3");
        leftFrame--;
        leftSec = leftFrame/50;
        drawString( "Time Left: " + leftSec + " Sec", pfWidth()/2, 5, 1, null, JGColor.black);
    }

   

    public void doFrameInGame(){
        moveObjects();
        checkWin();	//check to see if we've passed the end-of-level marker
       checkBGCollision(
			1+2, // collide with the ground and border tiles
			1    // cids of our objects
		);
        checkCollision(
			1, // cids of objects that our objects should collide with
			3  // cids of the objects whose hit() should be called
		);
        checkDeath();
        if(getKey(KeyEsc))
            setGameState("StartGame");
        new JGTimer(3000, // number of frames to tick until alarm
	            true, // true means one-shot, false means run again
		    "InGame" 
			) {
				// the alarm method is called when the timer ticks to zero
				public void alarm() {
					setGameState("TimeUp");
                                        removeObjects(null, 0, true);
				}
			};
    }
    
    public PlayerObject getPlayer(){
        return player;
    }
    
    public EnemyObject getEnemy(){
        return enemy;
    }
    public String getGameState(){
        return gameState;
    }
    //--------------------------------------------------------------
    // Game State: GameOver
    // Called: When the player loses all lives
    // Calls: InGame state or exits
    //--------------------------------------------------------------
    public void startGameOver(){
        gameState = "GameOver";
        removeObjects(null, 0, true);
        clearKey(KeyShift);
    }
    
    public void paintFrameGameOver(){
        drawString("Game Over!", pfWidth()/2, 10, 0, null, JGColor.black);
        drawString("You lost all lives", pfWidth()/2, 30, 0, null, JGColor.black);
        drawString("            Your Score was: " + player.getScore(),pfWidth()/2, 60, 0, null, JGColor.black);
        drawString("Press Enter to restart or Escape to quit", pfWidth()/2, 90, 0, null, JGColor.black);
    }
    
    public void doFrameGameOver(){
        if(getKey(KeyEnter))
        {
            clearKey(KeyShift);
            setGameState("StartGame");
        }
        else if(getKey(KeyEsc))
            exitEngine("Closed Game");
    }
    //--------------------------------------------------------------
    // Game State: TimeUP
    // Called: When the 60 sec are over before win or lost
    // Calls: InGame state or exits
    //--------------------------------------------------------------
      public void startTimeUP(){
        gameState = "TimeUp";
       
    }
     public void paintFrameTimeUp(){
        drawString("Your Time has Expired!", pfWidth()/2, 10, 0, null, JGColor.black);
        drawString("            Your Score was: " + player.getScore(),pfWidth()/2, 40, 0, null, JGColor.black);
        drawString("Press Enter to restart or Escape to quit", pfWidth()/2, 70, 0, null, JGColor.black);
    }
    
    public void doFrameTimeUp(){
        if(getKey(KeyEnter))
        {
            clearKey(KeyShift);
            setGameState("StartGame");
        }
        else if(getKey(KeyEsc))
            exitEngine("Closed Game");
    }
    
    //--------------------------------------------------------------
    // Game State: Win Game
    // Called: When the player reaches the goal point of the level
    // Calls: InGame state or exits
    //--------------------------------------------------------------
    
    public void startWinGame(){
        removeObjects(null, 0, true);
        
    }
    
    public void paintFrameWinGame(){
        drawString("Your princess is in another....I mean, you won!", pfWidth()/2, 30, 0, null, JGColor.black);
        drawString("            Your  Final Score is: " + player.getScore(),pfWidth()/2, 60, 0, null, JGColor.black);
        drawString("Press Enter to start again! Or Esc to Exit", pfWidth()/2, 90, 0, null, JGColor.black);
    }
    
    public void doFrameWinGame(){
        if(getKey(KeyEnter))
        {
            setGameState("StartGame");
            player.setScore(0);
            player.tempScore = 0;
            this.playerWin = false;
        }
        else if(getKey(KeyEsc))
            exitEngine(null);
        
    }
   public void checkWin(){
        if(player.x >= 1150.0)
        {
            playerWin = true;
            setGameState("WinGame");
        }
    }
   
    public void checkDeath(){
        if(getKey(KeyShift))
            setGameState("GameOver");
    }
   
    public boolean getPlayerWin(){
        return playerWin;
    } 
    //--------------------------------------------------------------
    // Method : computeScore
    // Called:  To Compute the score during and at the end of the game
    // Called: in InGame state
    //--------------------------------------------------------------

    public void computeScore()
    {
        if(player.x>1000)
            player.setPos(3);
        else if(player.x > 470)
            player.setPos(2);
        else if(player.x > 310)
            player.setPos(1);
        else if(player.x <= 310)
            player.setPos(0);
        player.tempScore = (int) ((player.pos * player.life * 7) + (12 * (player.x/1150)));
        if(this.playerWin )
              {
                  if(leftSec >= 30)
               player.tempScore += 25; //if you win in 30 sec or less you get the max bonus points
                  else if(leftSec < 30)
                      player.tempScore += 25*leftSec/30;
               }
        if(player.getScore() < player.tempScore)
            player.setScore(player.tempScore);
    }
    //Player class currently in Engine class in order to use draw methods
    public class PlayerObject extends JGObject{
        private boolean hitJumpApex; //boolean to tell us when to stop going up and start coming down
					//from a jump
        private int life;
        private final int jumpSpeed = 2;
        private boolean jumping;
        private int jumpCount;
        private int score;
        private int tempScore;
        private int pos; //# of enemy the player has passed
        //Constructor
        PlayerObject(int numLives){
            super("Player", true, 30,150,1,"myanim_l1");
            xspeed=0;
            yspeed=0;
            hitJumpApex = false;
            life = numLives;
            jumping = false;
            jumpCount = 0;
            score = 0;
            pos = 0;
            tempScore = 0;
        }
        public int getLife(){
            return life;
        }
        
        public void setLife(int numLives){
            life = numLives;
        }
        
        public void setScore( int Score)
        {
            score = Score;
        }
       public int getScore()
       {
           return score;
       }
       public void setPos( int Pos)
        {
            pos = Pos;
        }
       public int getPos()
       {
           return pos;
       }
        
        public void move() 
        {
            //Instead of using x or yspeed, we alter position ourselves
            //This gives us finer control over player position, and allows
            //the player object to stop moving as soon as the button is released
            if(getKey(KeyRight))
                player.x +=1;
            if(getKey(KeyLeft))
                player.x -= 1;
            if(!(getKey(KeyRight)||getKey(KeyLeft)))
                player.xspeed = 0;
            
            //KeyUp is the jump key
            //It is important to remember that y values INCREASE as we go DOWN the screen
            if(getKey(KeyUp))
            {
                
                if(jumping){
                    if(player.y >= jumpBase){
                        if(player.y == jumpBase)
                            playAudio("jump");
                        fall();
                    }
                    
                    if(player.y <= jumpBase - maxJump){
                         
                        hitJumpApex = true;
                        fall();
                    }
                    else if((player.y < jumpBase)&&hitJumpApex)
                        fall();
                    else if((player.y > jumpBase - maxJump)&&!hitJumpApex&&(jumpCount<=1)){
                        
                        player.yspeed = -2;
                    }
                }
                else
                    jumping = true;
                
                               
               jumpCount++;
            }
            //This covers us when we release the jump key in the middle of a jump
           if(!getKey(KeyUp))
               //If we're not at the jump base whenever the UP key isn't pressed,
               fall();
        }
        
          public void hit_bg(int tilecid) {

           if (and(checkBGCollision(0,1),3)) {
                if(((player.y >= 460)&&(player.y <= 496)) || ((player.y >= 396)&&(player.y <= 432)) || ((player.y >= 348)&&(player.y<= 384)) || ((player.y >= 108)&&(player.y<=144))){
                    player.y+=5;
                    fall();
                }
                else
                    jumpBase = player.y;
                    hitJumpApex = false;
                    System.out.println("on a block\njumpBase: " + jumpBase + "\nplayer.y: " + player.y);

           }                                           
        }
        
        private void fall(){
                if(!and(checkBGCollision(0,0),3))
                {
                    player.yspeed = jumpSpeed;
                }
                else{
                    //System.out.println("Definitely on a block");
                    jumping = false;
                    player.yspeed = 0;
                    jumpCount = 0;
                }
                if(player.y > 500)
                {
                    player.life--;
                    player.remove();
                    player = new PlayerObject(life);
                }
                    
        }
         public void hit(JGObject obj) {
            //playAudio("mario_die");
            if ((checkCollision(3,-1.0,-1.0)==0) || (checkCollision(3,1.0,-1.0)==0)) {
                if (player.life==1)
                    setGameState("GameOver");
                else
                {
                    player.life--;
                    player.remove();
                    player = new PlayerObject(life);
                }
            }
        }
         
    }

     //Enemy Class
    public class EnemyObject extends JGObject{
       
        private int moveRange;
        private int initPos;
            //Constructor
            EnemyObject(int initPos, int initY){
                super("Enemy",true,initPos,initY,3,"myanim_l2");
                xspeed=1;
                yspeed=0;
                moveRange = 50;
                this.initPos = initPos;

            }
            
            public void move(){
              if(this.x<=(initPos - moveRange))
                  this.xspeed=1;
              if(this.x>=(initPos + moveRange))
                  this.xspeed=-1;
              fall();
            }
               
            private void fall(){
                if(!and(checkBGCollision(0,0),3))
                    this.y +=1;
            }
        }
}