import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.Rectangle;

public class Board2 extends JPanel implements Runnable, MouseListener
{
    boolean inGame = false;
    boolean gameOver = false;
    private final Dimension d;
    int BOARD_WIDTH=500;
    int BOARD_HEIGHT=500;
    String message = "Click Board to Start";
    private Thread animator;
    int bx = 100; //20;
    int by = 100; //200;
    int xSpeed = 4;
    int ySpeed = 5;
    int ballW = 20;

    Brick[] bricks = new Brick[24];
    boolean[] showBrick = new boolean[24]; //show brick or not (if we hit a break we don't show it)
    int paddleX = BOARD_WIDTH/2;
    int paddleY = BOARD_HEIGHT-150;
    int paddleW = 100;
    int paddleH = 10;
    boolean padRight = false;
    boolean padLeft = false;

    public Board2()
    {
        addKeyListener(new TAdapter());
        addMouseListener(this);
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        setBackground(Color.black);

        if (animator == null || !inGame) {
            animator = new Thread(this);
            animator.start();
        }


        setDoubleBuffered(true);

        //set our brick positions
        int brickX = 20;
        int brickY = 20;
        int count = 1;

        for(int i=0; i<bricks.length; i++){
            bricks[i] = new Brick(brickX, brickY, 50, 10);
            showBrick[i] = true;
            brickX += 55;
            if(count % 8 == 0) {
                brickX = 20;
                brickY += 20;
            }
            count++;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(bx, by, ballW, ballW);
    }

    public boolean collision() {
        return getBounds().intersects(getBounds());
    }

    public void gameOver() {
        message = "GAME OVER";
        gameOver = true;
    }


    // move the ball
    public void bounceBall(){

        if( bx > (BOARD_WIDTH - 30) || bx < 0 ) {
            xSpeed *= -1;
        }

        if( by < 0 ) {
            ySpeed *= -1;
        }

        if( by > BOARD_HEIGHT ){
            //make them lose a life?
            bx = 20; // we can make this random
            by = 200;
        }

        if( ((bx + ballW) > paddleX) && (bx < (paddleX + paddleW)) && ((by + ballW) > paddleY) && (by < (paddleY + paddleH))){
            ySpeed *= -1;
        }

        for( int i=0; i < bricks.length; i++ ) {
            if ( (showBrick[i] && (bx + ballW) > bricks[i].x) && (bx < (bricks[i].x + bricks[i].w)) && ((by + ballW) > bricks[i].y) && (by < (bricks[i].y + bricks[i].h)) ) {
                ySpeed *= -1;
                showBrick[i] = false;
            }
        }

        if( (by + ySpeed) > (BOARD_HEIGHT - ballW)) {
            gameOver();
        }

        bx += xSpeed;
        by += ySpeed;


    }

    public void paint(Graphics g)
    {
        super.paint(g);

        g.setColor(Color.white);
        g.fillRect(0, 0, d.width, d.height);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        g.setColor(Color.blue);
        g.setFont(small);
        g.drawString(message, 10, d.height-60);
        g.setColor(Color.black);

        // paint bricks
        for(int i=0; i< bricks.length; i++){
            if(showBrick[i]){
                g.fillRect(bricks[i].x, bricks[i].y, bricks[i].w, bricks[i].h);
            }
        }

        // paint paddle
        g.fillRect(paddleX, paddleY, paddleW, paddleH);

        // paint ball
        g.setColor(Color.blue);
        g.fillOval(bx, by, ballW, ballW);
        g.setColor(Color.black);

        if (inGame) {

            bounceBall();

            if(padRight){
                paddleX += 5;
            }

            if(padLeft){
                paddleX -= 5;
            }

        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    // input class
    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            //int key = e.getKeyCode(); // uncomment for testing purpose
            padRight = false;
            padLeft = false;
        }

        public void keyPressed(KeyEvent e) {
//            // uncomment to test key code
//               System.out.println( e.getKeyCode());
//               message = "Key Pressed: " + e.getKeyCode();
//            //

            int key = e.getKeyCode();
            if( key == 39 || key == 68 ){ // right arrow or 'd'
                padRight = true;
            }

            if( key == 37 || key == 65 ) { // left arrow or 'a'
                padLeft = true;
            }



        }

    }

    public void mousePressed(MouseEvent e) {
        /* uncomment to test where your mouse is
            int mouseX = e.getX();
            int mouseY = e.getY();

         */
        System.out.println("CLICKED");
        message = "Game Started";
        inGame = true;
    }

    public void mouseReleased(MouseEvent e) {
        // do nothing
    }

    public void mouseEntered(MouseEvent e) {
        // do nothing
    }

    public void mouseExited(MouseEvent e) {
        // do nothing
    }

    public void mouseClicked(MouseEvent e) {
        // do nothing
    }

    public void run() {

        int animationDelay = 20;
        long time = System.currentTimeMillis();
        while (true) {//infinite loop
            if(!gameOver) {
                repaint();
                try {
                    time += animationDelay;
                    Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
                } catch (InterruptedException e) {
                    System.out.println(e);
                }//end catch
            } else {
                repaint();
                break;
            }
        }//end while loop

    }//end of run

}//end of class