package platformer;
import jgame.*;

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
    private Platformer game;
    private double jumpBase = 3000.0; //The position where the player was when a jump starts
                                        //initialized so far down in order to allow player to fall first time
    private final double maxJump = 80.0; //The maximum height for our player to jump
    
    //Constructor
    PlayerObject(int numLives, Platformer game){
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
        this.game = game;
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
   
   public int getTempScore(){
       return tempScore;
   }
   
   public void setTempScore(int t){
       tempScore = t;
   }

    public void move() 
    {
        //Instead of using x or yspeed, we alter position ourselves
        //This gives us finer control over player position, and allows
        //the player object to stop moving as soon as the button is released
        if(game.getKey(game.KeyRight)){
            x +=1;
            setImage("myanim_l1");
        }
        if(game.getKey(game.KeyLeft)){
            x -= 1;
            setImage("myanim_r1");
        }
        if(!(game.getKey(game.KeyRight)||game.getKey(game.KeyLeft)))
            xspeed = 0;

        //KeyUp is the jump key
        //It is important to remember that y values INCREASE as we go DOWN the screen
        if(game.getKey(game.KeyUp))
        {

            if(jumping){
                if(y >= jumpBase){
                    if(y == jumpBase)
                        game.playAudio("jump");
                    fall();
                }

                if(y <= jumpBase - maxJump){

                    hitJumpApex = true;
                    fall();
                }
                else if((y < jumpBase)&&hitJumpApex)
                    fall();
                else if((y > jumpBase - maxJump)&&!hitJumpApex&&(jumpCount<=1)){

                    yspeed = -2;
                }
            }
            else
                jumping = true;


           jumpCount++;
        }
        //This covers us when we release the jump key in the middle of a jump
       if(!game.getKey(game.KeyUp))
           //If we're not at the jump base whenever the UP key isn't pressed,
           fall();
    }

      public void hit_bg(int tilecid) {

       if (and(checkBGCollision(0,1),3)) {
           //Here we have to manually check if the player is in the middle
           //    of any blocks in the level, and if so, get him out and make
           //    him fall
            if(((y >= 460)&&(y <= 496)) || 
                    ((y >= 396)&&(y <= 432)) || 
                    ((y >= 348)&&(y<= 384)) || 
                    ((y >= 108)&&(y<=144)) ||
                    ((y >= 160) && (y<=194)))
            {
                y+=5;
                fall();
            }
            else{
                jumpBase = y;
                hitJumpApex = false;
            }
       }                                           
    }

    public void fall(){
            if(!and(checkBGCollision(0,0),3))
            {
                yspeed = jumpSpeed;
            }
            else{
                jumping = false;
                yspeed = 0;
                jumpCount = 0;
            }
            if(y > 500)
            {
               game.playAudio("mario_die");
               life--;
                game.getPlayer().remove();
                game.setPlayer(new PlayerObject(life, game));
            }

    }
     public void hit(JGObject obj) {
        game.playAudio("mario_die");
        if ((checkCollision(3,-1.0,-1.0)==0) || (checkCollision(3,1.0,-1.0)==0)) {
            if (life==1)
                game.setGameState("GameOver");
            else
            {
               life--;
                game.getPlayer().remove();
                game.setPlayer(new PlayerObject(life, game));
            }
        }
    }

}