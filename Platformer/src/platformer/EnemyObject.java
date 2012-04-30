/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package platformer;
import jgame.*;
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