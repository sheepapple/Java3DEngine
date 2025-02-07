import java.awt.Toolkit;
import javax.swing.JFrame;
import java.awt.Dimension;


public class Main
{
  //Sets screen size to a Dimension variable
    static Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
    static JFrame F = new JFrame();

    public static void main(String[] args)
    {  
      //adds our Display object (basically the entire game)
      // the only reason I have this main class is cuz repl like needs you to have a main class named main or something I dont really know but I dont really care tbh lol
        Display object = new Display(); F.add(object);
        F.setUndecorated(true); F.setSize(2560, 1440); F.setVisible(true); F.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }  
}