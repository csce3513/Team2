package platformer;


import java.io.IOException;
import jgame.*;
import jgame.platform.*;
import java.util.Random;

public class Platformer extends JGEngine{
    private PlayerObject player;
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
    private Project Pj;
    
    
    public Platformer(JGPoint size, Project pj){
        initEngine(size.x, size.y);
        player = new PlayerObject(3, this);
        enemy = new EnemyObject(15,5345);
        enemy3 = new EnemyObject(15,5345);
        enemy2 = new EnemyObject(15,5345);
        gameStart = true;
        gameState = "";
        numEnemies = 3;
        Pj = pj;
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
            setGameState("InGame");
        }
        else if(getKey(KeyEsc))
            exitEngine(null);
    }
    
    public void paintFrameStartGame(){
        drawString("Hello "+ Pj.getUserName(), pfWidth()/2 , 20, 0, null, JGColor.black);
        drawString("Welcome to Definitely Not Mario!", pfWidth()/2, 50, 0, null, JGColor.black);
        drawString("Your last score was " + Pj.getUserScore(), pfWidth()/2,80 , 0, null, JGColor.black);
        drawString("Press Enter to Begin! Or ESC to exit", pfWidth()/2, 110, 0, null, JGColor.black);

    }
    
    //--------------------------------------------------------------
    // Game State: InGame
    // Called: When the game begins or restarts
    // Calls: GameOver (when all lives lost) or
    //        WinGame (when goal is reached)
    //--------------------------------------------------------------
    
    public void startInGame(){
        
        removeObjects(null, 0);        
        player = new PlayerObject(3, this);
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
        drawString("Lives : " + player.getLife(), pfWidth()-3, 5, 1, null, JGColor.black);
        drawImage(1150, 450, "myanim_l3");
        leftFrame--;
        leftSec = leftFrame/50;
        drawString( "Time Left: " + leftSec + " Sec", pfWidth()/2, 5, 1, null, JGColor.black);
        drawString( "Use keyboard arrows to move", pfWidth()/2, 50, 1, null, JGColor.black);
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
    
    //--------------------------------------------------------------
    // Game State: GameOver
    // Called: When the player loses all lives
    // Calls: InGame state or exits
    //--------------------------------------------------------------
    public void startGameOver() throws IOException{
        gameState = "GameOver";
        removeObjects(null, 0, true);
        clearKey(KeyShift);
        Pj.UpdateScore(player.getScore());
        Pj.setUserScore(""+player.getScore());
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
      public void startTimeUP() throws IOException{
         gameState = "TimeUp";
         Pj.UpdateScore(player.getScore());
         Pj.setUserScore(""+player.getScore());
       
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
    
    public void startWinGame() throws IOException{
        removeObjects(null, 0, true);
        Pj.UpdateScore(player.getScore());
        Pj.setUserScore(""+player.getScore());
        
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
            player.setTempScore(0);
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
        player.setTempScore((int) ((player.getPos() * player.getLife() * 7) + (12 * (player.x/1150))));
        if(this.playerWin )
              {
                  if(leftSec >= 30)
               player.setTempScore(player.getTempScore() + 25); //if you win in 30 sec or less you get the max bonus points
                  else if(leftSec < 30)
                      player.setTempScore(player.getTempScore() + 25*leftSec/30);
               }
        if(player.getScore() < player.getTempScore())
            player.setScore(player.getTempScore());
    }
    
    //---------------------------------------------
    // Below are the getters and setters
    //---------------------------------------------
    
    
    public PlayerObject getPlayer(){
        return player;
    }
    
    public void setPlayer(PlayerObject p){
        player = p;
    }
    
    public EnemyObject getEnemy(){
        return enemy;
    }
    public String getGameState(){
        return gameState;
    }
    
}