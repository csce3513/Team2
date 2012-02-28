

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.event.ActionEvent;
import java.io.IOException;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Denis Rugira
 */
public class ProjectTest {
    protected Project test;
    private double score =0.0;
    
    public ProjectTest() {
    }

    @Before
    public void setUp() throws IOException {
        test= new Project();
        
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
