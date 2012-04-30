package platformer;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import jgame.*;


public class Project extends JFrame implements ActionListener
{
    private JPanel Welcome, Username, Information;
    private JButton button1;
    private JTextField textfield1;
    private String user_name;
    double userScore;
    private JTextArea welcome_info;
    private JTextArea display;
    private String invalid_username_message;
    private Platformer ourGame;
            
    public Project() throws IOException
    { 
        super("TEAM 2: SOFTWARE ENGINNERING PROJECT");
        this.setBackground(Color.gray); 
        this.setLayout(new BorderLayout());
        this.setSize(600,600);
        build_Welcome();
   
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        
        invalid_username_message = "The username you entered is invalid \n"+
                "Make sure your input does not contain any white space\n"+
                " and that it is not more than 9 characters\n";
    
    }
    public void setUserScore(String score)
    {
        userScore = Double.parseDouble(score);
    }
    public double getUserScore()
    {
        return userScore;
    }
    public void setUserName(String name)
    {
        user_name = name;
    }
            
    public String getUserName()
    {
        return user_name;
    }
    
    public Platformer getGame(){
        return ourGame;
    }
    
    
    public void build_Welcome ()
    {
        Welcome=new JPanel();
        Welcome.setLayout(new BorderLayout());
        button1=new JButton("NEXT");
        button1.addActionListener(this);
        
        welcome_info = new JTextArea(5,20);
        welcome_info.setEditable(false);
        welcome_info.setLineWrap(true);
        welcome_info.setWrapStyleWord(true);
        welcome_info.setText("Welcome to Our Game!\n\n"+ 
                "The idea of this game came from the well-known"+
                " game called Super Mario. Therefore, it is played"+
                " in a way kind of similar to Super Mario."+
                " You will use your keyboard's arrows to move "+
                " left, right or up until you reach the goal.\n"+
                "To get to the goal you will need to pass 3 enemies, "+
                " and any contact with the enemy yu will lose a life"+
                " You score is determined accoerding to your speed,."+
                " your position on the screen and the number or lives left.\n\n"+
                " Enjoy Playing Our Game!\n\n\n\n\n\n\n\n" +
                " Designers and developers: \n\n" +
                " Jean Pierre\n" +
                " Juan\n" +
                " Denis\n" +
                " Andrew");
       
  
        Welcome.setBorder(BorderFactory.createEtchedBorder(Color.yellow, Color.black));
        Welcome.add(welcome_info,BorderLayout.WEST); 
        Welcome.add(button1,BorderLayout.EAST);
            this.add(Welcome,BorderLayout.CENTER);
    }
    
    
    public void build_Username() 
    {
        Username=new JPanel();
        Username.setLayout(new BorderLayout());
        textfield1=new JTextField("Enter your username");
        Username.add(textfield1,BorderLayout.NORTH); 
        textfield1.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
               {
                 user_name = e.getActionCommand();
                try {
                    build_Information(CheckUserName(user_name));
                } catch (IOException ex) {
                    Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
                }
                 Information.setVisible(true);                
               }     
        });
        this.add(Username,BorderLayout.CENTER);
    }
   
    public void build_Information(Boolean choice) throws IOException
    {
        Information=new JPanel();
        display = new JTextArea(5,20);
        display.setEditable(false);
        if(choice)
        {
            try 
            {
               if( Read_File(this.getUserName()))
               display.setText(this.getUserName() + ", the last time you played you scored " + this.getUserScore());
               else
               {
                   this.addNewUser(this.getUserName(),0);
                   display.setText("The user name :"+ this.getUserName() + " has been added to the system");
               }
               
            } 
            catch (FileNotFoundException ex) 
            {
                Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
            }
            ourGame = new Platformer(new JGPoint(1000,600),this);
        }
         
        else
        display.setText(invalid_username_message);
       
        Information.add(display);
        this.add(Information,BorderLayout.SOUTH);  
        Information.setVisible(false);
	
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
            Welcome.setVisible(false);
            build_Username();
            
    }
    
    public Boolean CheckUserName(String user)
    {
        Boolean pass = true;
        char[] name;
        name = user.toCharArray();
        for(char letter : name)
        {
            if(letter == ' ')
                pass = false;
        }
        if(name.length>9)
            pass = false;
        return pass;
    }
    public boolean Read_File( String name) throws FileNotFoundException
    {
        Boolean user_exists = false;
        String[] words;      //Declale array of strings to read every line of the file
        try
        {
        FileInputStream fstream = new FileInputStream("ScoreBoard.txt");
        DataInputStream Din = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(Din));
        String input = br.readLine();
 
        while(input != null)
        {
           
          words = input.split(" ");
          if(   words[0].equals(name))
          {
              this.setUserScore(words[1]);
              this.setUserName(words[0]);
              user_exists = true;
          }
          input = br.readLine();  
        }
        Din.close();
        }
        catch(Exception e)
                {
                   System.err.print("Error: e.getMessage()") ;
                }
        return user_exists;
    }
    
    public void addNewUser(String name, int score) throws IOException
    {
        this.setUserName(name);
        BufferedWriter bw = new BufferedWriter(new FileWriter("ScoreBoard.txt",true));
        bw.write (name + " " + score);
        bw.newLine();
        bw.flush();
        bw.close();
       
    }
    
    public void DeleteOldScore(String name)
    {
     String[] words;    
     
    try {

      File inFile = new File("ScoreBoard.txt");
      
      if (!inFile.isFile()) {
        System.out.println("Parameter is not an existing file");
        return;
      }
       
      //Construct the new file that will later be renamed to the original filename. 
      File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
      
      BufferedReader br = new BufferedReader(new FileReader("ScoreBoard.txt"));
      PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
      
      String line = null;

      //Read from the original file and write to the new 
      //unless content matches data to be removed.
      while ((line = br.readLine()) != null) {
          words = line.split(" ");
        
        if (!words[0].equals(name)) {

          pw.println(line);
          pw.flush();
        }
      }
      pw.close();
      br.close();
      
      //Delete the original file
      if (!inFile.delete()) {
        System.out.println("Could not delete file");
        return;
      } 
      
      //Rename the new file to the filename the original file had.
      if (!tempFile.renameTo(inFile))
        System.out.println("Could not rename file");
      
    }
    catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
  
    }
  public void UpdateScore(int score) throws IOException
  {
      this.DeleteOldScore(this.getUserName());
      this.addNewUser(this.getUserName(),score);
  }
  public static void main(String[] args) throws IOException
  {
      Project pj = new Project();
  }
      
}
