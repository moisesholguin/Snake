import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
/**
 * Snake User Manual:
 * 
 *      Start = RIGHT Arrow Key
 *      Pause = P
 *      Resume = SPACE
 *      Move = Up, Down, Left, and Right Arrow Keys
 * 
 * The methods suspend() and resume() have been deprecated but they
 * will suffice for this simple game. If you want to play again please
 * exit window and re-open .jar file (or re-run code).
 * 
 * To change the speed of the snake alter the Thread.sleep() method by
 * increasing the number to make snake faster or vice-versa.
 * 
 * Thank you and enjoy!
 * 
 * @author Moises Holguin
 * @version Friday, July 27, 2012, 1:28AM
 */
public class Main extends JFrame {
	static boolean GAMESTARTED, PAUSED, LOST, ATE, PLAYAGAIN, CANSTILLPLAY;
	static Rectangle dot;
	static int dotX, dotY;
	static Map<Integer,Integer> randomXPoints;
	static Map<Integer,Integer> randomYPoints;
	Image dbImage;
	Graphics dbg;
	
	static Snake snake;
	static Thread SNAKE;
	
	public Main() {
		GAMESTARTED = PAUSED = LOST = ATE = PLAYAGAIN = false;
		CANSTILLPLAY = true;
		setTitle("Snake by MoisesHolguin");
		setBackground(Color.BLACK);
		setSize(250,250);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(new AL());
		randomXPoints = new TreeMap<Integer, Integer>();
		randomYPoints = new TreeMap<Integer, Integer>();
		createRandomPositions();
		randomPosition();
		dot = new Rectangle (dotX, dotY, 10, 10);
	}
	
	public static void main (String args[]) {
		new Main();
		snake = new Snake();
		SNAKE = new Thread(snake);
		while(CANSTILLPLAY) {
			while(!LOST);
			if(PLAYAGAIN) {
				new Main();
				snake = new Snake();
				SNAKE = new Thread(snake);
			}
		}
	}
	
	public static void createRandomPositions() {
		int Xcount = 0;
		int Ycount = 40;
		int index = 0;
		while(Xcount<=240) {
			randomXPoints.put(index,Xcount);
			randomYPoints.put(index++, Ycount);
			Xcount+=10;
			Ycount+=10;
		}
	}
	
	public static void randomPosition() {
		Random r = new Random();
		int tempX = randomXPoints.get(r.nextInt(25));
		int tempY = randomYPoints.get(r.nextInt(21));
		dotX = tempX;
		dotY = tempY;
	}
	
	public void paint(Graphics g) {
		dbImage = createImage(getWidth(), getHeight());
		dbg = dbImage.getGraphics();
		draw(dbg);
		g.drawImage(dbImage,0,0,this);
	}
	
	public void draw(Graphics g) {
		if(!GAMESTARTED) {
			g.setColor(Color.YELLOW);
			g.drawString("Press RIGHT Arrow Key To Begin", 25, 140);
			g.drawString("Press P to Pause game", 25, 155);
			g.drawString("Press SPACEBAR to Resume game", 25, 170);
			g.setColor(Color.RED);
			g.drawString("DON'T CROSS GREEN LINE!!!", 25, 185);
			g.setColor(Color.YELLOW);
			g.drawString("ENJOY", 25, 200);
			snake.draw(g);
		}
		else	{
			g.setColor(Color.RED);
			g.fillRect(dotX, dotY, 10, 10);
			g.setColor(Color.BLACK);
			g.drawRect(dotX, dotY, 10, 10);
			snake.draw(g);
			if(ATE) {
				randomPosition();
				ATE = false;
			}
			if(PAUSED) {
				g.setColor(Color.YELLOW);
				g.drawString("PAUSED", 100, 130);
			}
			if(LOST) {
				g.setColor(Color.YELLOW);
				g.drawString("GAME OVER", 90, 130);
				g.drawString("Play Again:\tY/N", 90, 145);
			}
		}
		repaint();
	}
	
	public class AL extends KeyAdapter {
		@SuppressWarnings("deprecation")
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()==e.VK_P && GAMESTARTED && !LOST) {
				PAUSED = true;
				SNAKE.suspend();
			}
			else if(e.getKeyCode()==e.VK_SPACE && GAMESTARTED && PAUSED && !LOST) {
				PAUSED = false;
				SNAKE.resume();
			}
			else
				snake.keyPressed(e);
		}
	}
}