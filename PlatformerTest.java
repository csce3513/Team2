/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package platformer;

import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import jgame.*;
import jgame.platform.*;
/**
 *
 * @author jrios
 */
public class PlatformerTest {
    
    public PlatformerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        /*System.out.println("main");
        String[] args = null;
        Platformer.main(args);*/
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    //Incomplete test for Game Creation
    /*@Test
    public void gameExists(){
        Platformer test = new Platformer();
        Assert
    }*/
    
    @Test
    public void playerExists(){
        Platformer test = new Platformer(new JGPoint(640,480));
        Assert.assertNotNull(test.getPlayer());
        
    }
    
    @Test
    public void playerPop(){
        Platformer test=new Platformer(new JGPoint(640,480));
        Assert.assertEquals(30.0, test.getPlayer().x);
        Assert.assertEquals(150.0,test.getPlayer().y);
    }
    
    @Test 
    public void playerMove(){
        Platformer test=new Platformer(new JGPoint(640,480));
        Assert.assertEquals(30.0, test.getPlayer().x);
        Assert.assertEquals(150.0,test.getPlayer().y);
        test.setKey(39);              //39= right arrow key
        test.getPlayer().move();
        Assert.assertEquals(31.0,test.getPlayer().x);
        test.clearKey(39);
        test.setKey(37);  //37= left arrrow key
        test.getPlayer().move();
        Assert.assertEquals(30.0,test.getPlayer().x);
        test.clearKey(37);
        test.setKey(38);
        for(int i=0;i<12;i++)
            test.getPlayer().move();
        Assert.assertEquals(138.0, test.getPlayer().y);
        test.clearKey(38);
        for(int i=0; i<12; i++)
            test.getPlayer().move();
        Assert.assertEquals(150.0, test.getPlayer().y);
    }
    /**
     * Test of main method, of class Platformer.
     */
    //Unused Test (kept for example of what to do)
    /*@Test
    public void testMain() {
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
