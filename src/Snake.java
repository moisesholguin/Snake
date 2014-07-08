import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Snake implements Runnable {
	static int multiplier, currentScore;
	static int snakeSize;
	static int snakeX[], snakeY[];
	static int xDirection, yDirection;
	static String scoreString;
	
	public Snake() {
		multiplier = 1;
		currentScore = 0;
		snakeSize=3;
		scoreString = "000000000";
		snakeX = new int [525];
		snakeY = new int [525];
		createSnake();
	}
	
	public void createSnake() {
		for(int i=0; i<snakeSize; i++) {
			snakeX[i] = 100 - i*10;
			snakeY[i] = 100;
		}
	}
	
	public void draw(Graphics g) {
		for(int i=0; i<snakeSize; i++) {
			g.setColor(Color.WHITE);
			g.fillRect(snakeX[i], snakeY[i], 10, 10);
			g.setColor(Color.BLACK);
			g.drawRect(snakeX[i], snakeY[i], 10, 10);
		}
		g.setColor(Color.GREEN);
		g.drawString("Score: "+scoreString.substring(0,10-(currentScore+"").length())+currentScore, 5, 37);
		g.drawString("____________________________________", 0, 40);
	}

	public void keyPressed(KeyEvent e) {
		if(!Main.GAMESTARTED && e.getKeyCode()==e.VK_RIGHT) {
			Main.SNAKE.start();
			Main.GAMESTARTED = true;
			setXDirection(10);
			setYDirection(0);
		}
		if(e.getKeyCode()==e.VK_RIGHT) {
			setXDirection(10);
			setYDirection(0);
		}
		if(e.getKeyCode()==e.VK_LEFT) {
			setXDirection(-10);
			setYDirection(0);
		}
		if(e.getKeyCode()==e.VK_DOWN) {
			setYDirection(10);
			setXDirection(0);
		}
		if(e.getKeyCode()==e.VK_UP) {
			setYDirection(-10);
			setXDirection(0);
		}
		if(e.getKeyCode()==e.VK_Y && Main.LOST) {
			Main.PLAYAGAIN = true;
		}
		if(e.getKeyCode()==e.VK_N && Main.LOST) {
			Main.CANSTILLPLAY = false;
		}
	}

	private void setXDirection(int i) {
		xDirection = i;
	}
	
	private void setYDirection(int i) {
		yDirection=i;
	}
	
	private void move() {
		for(int i=snakeSize; i>0; i--) {
			snakeX[i] = snakeX[i-1];
			snakeY[i] = snakeY[i-1];
		}
		snakeX[0]+=xDirection;
		snakeY[0]+=yDirection;
	}
	
	private void checkApple() {
		if(snakeX[0]==Main.dotX && snakeY[0]==Main.dotY) {
			snakeSize++;
			currentScore+=multiplier*10;
			multiplier++;
			Main.ATE = true;
		}
	}
	
	private void checkCollision() {
		for(int i=snakeSize; i>0; i--)
			if(snakeX[0]==snakeX[i] && snakeY[0]==snakeY[i])
				Main.LOST = true;
		if(snakeX[0]<0 || snakeX[0]>=250 || snakeY[0]<=30 || snakeY[0]>=250)
			Main.LOST = true;
	}
	
	public void run() {
		try {
			while(!Main.LOST) {
				checkCollision();
				checkApple();
				move();
				Thread.sleep(150);
			}
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
