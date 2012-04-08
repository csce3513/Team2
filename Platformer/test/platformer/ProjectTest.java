package platformer;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.event.ActionEvent;
import java.io.IOException;
import org.junit.*;
import static org.junit.Assert.*;
import jgame.*;

/**
 *
 * @author Denis Rugira
 */
public class ProjectTest {
    protected Project test;
    protected Platformer testPlat;
	private double score =0.0;
	
    public ProjectTest() {
    }

    @Before
    public void setUp() throws IOException {
        test= new Project();
	testPlat = new Platformer(new JGPoint(640, 480));
        
    }
    
    @After
    public void tearDown() {
        test = null;
    }

    /**
     * Test of Write_File method, of class Project.
     */
    @Test
    public void CheckValidUserName() throws Exception {
        //If the user submitted a valid name
        boolean result=test.CheckUserName("denis");
        boolean expResult = true;
        assertEquals(expResult, result);        
        
        //If the user submitted a character with white space
        result=test.CheckUserName("habimana jp");
        expResult = false;
        assertEquals(expResult, result);   
        
        //If the user submitted an 9 or less character long name.
        result=test.CheckUserName("Habimana_thierry");
        expResult = false;
        assertEquals(expResult, result);   
    }
    @Test
    public void Write_Test() throws IOException{
        String expResult="Deaton";
        test.addNewUser(expResult, score);
        String result= test.getUserName();
        assertEquals(expResult, result);
        
        double Result=test.getUserScore();
        double exResult = score;
        assertEquals(exResult, Result, 0.0); 
        
    }

    /**
     * Test of addNewUser method, of class Project.
     */
    @Test
    public void Read_test() throws Exception {
        String expResult="Deaton";
        test.Read_File(expResult);
        String result = test.getUserName();
        assertEquals(expResult, result);  
        
    }
   
	/**
	* Test that the player exists in Platformer
	**/
    @Test
    public void playerExists(){
        assertNotNull(testPlat.getPlayer());
        
    }
    
	/**
	* Test that the player has a position
	**/
    @Test
    public void playerPos(){
        assertEquals(30.0, testPlat.getPlayer().x, 0.5);
		assertEquals(150.0,testPlat.getPlayer().y, 0.5);
    }
    
	/**
	* Test that the player can move left and right
	**/
    @Test 
    public void playerMove(){
		//Check initial position
		assertEquals(30.0, testPlat.getPlayer().x, 0.5);
		assertEquals(150.0,testPlat.getPlayer().y, 0.5);
		//Set input to to right arrow key (39 = right arrow)
        testPlat.setKey(39);
		//Move the player with right key pressed down
        testPlat.getPlayer().move();
		//Assert that player has moved to the right
        assertEquals(31.0,testPlat.getPlayer().x, 0.5);
        testPlat.clearKey(39);
		//Set input to left arrow key (37 = right arrow)
        testPlat.setKey(37);
		//Move the player with the left key pressed
        testPlat.getPlayer().move();
		//Assert that player has moved to the left
        assertEquals(30.0,testPlat.getPlayer().x, 0.5);
        testPlat.clearKey(37);
    }
	
	/**
	* Test that the player can jump
	**/
    @Test
	public void playerJump(){
		//Check initial position
		assertEquals(30.0, testPlat.getPlayer().x, 0.5);
		assertEquals(150.0,testPlat.getPlayer().y, 0.5);
		//Set input to up arrow key
		testPlat.setKey(38);
		//This for loop has the player move up for 12 frames.
		//This should pu thte player 12 y-coordinates lower
        for(int i=0;i<12;i++)
            testPlat.getPlayer().move();
        assertEquals(138.0, testPlat.getPlayer().y, 0.5);
        testPlat.clearKey(38);
		//This for loop should land the player back where it started
        for(int i=0; i<12; i++)
            testPlat.getPlayer().move();
        assertEquals(150.0, testPlat.getPlayer().y, 0.5);
	
	}
	
	/**
	* Test to check that a win condition occurs when the player
	* passes a certain point on the screen
	**/
    @Test
    public void playerWin(){
		//Move to the right
        testPlat.setKey(39);
		//This for loop moves the player as the game engine would
		//By calling doFrame and player.move()
        for(int i = 0; i < 71; i++)
        {
            testPlat.doFrame();
            testPlat.getPlayer().move();
        }
		//Stop moving the player
        testPlat.clearKey(39);
		//Assert that the player has moved the right amount (past 100)
		//And that the PlayerWin flag has gone off
        assertEquals(101.0, testPlat.getPlayer().x, 0.5);
        assertTrue(testPlat.getPlayerWin());       
    }
	
	/**
	* Test that game starts after entering a valid username
	**/
	@Test
   public void testStateChange() throws IOException
   {
      test.setUserName("Valid");
      try {
        test.build_Information(test.CheckUserName(test.getUserName()));
                
		} 
      catch (IOException ex) {
			System.err.println(ex);
        }
      assertEquals(true, test.getGame().gameStart);
      assertNotNull(test.getGame().getPlayer());
      
   }
	
    /**
     * Test of main method, of class Project.
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        Project.main(args);
        // TODO review the generated test code and remove the default call to fail.
        
    }


   
}
