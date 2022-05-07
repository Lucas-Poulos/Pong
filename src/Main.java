import javax.swing.*;

public class Main extends JFrame

{
    public Main(){
        add(new Board2());
        setTitle("brick Braker");
        setSize(500,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }
    public static void main (String[] args){
        new Main();
    }


}

