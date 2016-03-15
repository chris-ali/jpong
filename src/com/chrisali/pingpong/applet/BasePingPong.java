package com.chrisali.pingpong.applet;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JApplet;
import javax.swing.JComponent;

public class BasePingPong extends JComponent {

	private static final long serialVersionUID = -8395759457708163217L;
	
	private BufferedImage image;
	private ScoreEventListener scoreEventListener;
	
	// Scoring
	private int score;
	
	// Ball
	private Ellipse2D.Double ball;
	private double ballSpeed;
	private int ballXDirection;
	private int ballYDirection;
	private boolean alreadyIntersected = true;
	
	// Bat
	private RoundRectangle2D.Double bat;
	private double batSpeed;
	
	public BasePingPong(JApplet parent) {
		
		score = 0;
		
		ball = new Ellipse2D.Double(parent.getWidth()/2, parent.getHeight()/2, 15, 15);
		ballSpeed = 10.0;
		ballYDirection = new Random().nextInt() > 0 ? -1 : 1;
		ballXDirection = new Random().nextInt() > 0 ? -1 : 1;
		
		bat = new RoundRectangle2D.Double(parent.getWidth()/2, 3*parent.getHeight()/4, 100, 10, 20, 20);
		batSpeed = 10.0;
		
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				bat.x = e.getX()-(bat.getWidth()/2);
				bat.y = e.getY()-(bat.getHeight()/2);
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {}
		});
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					bat.y -= batSpeed;
					break;
				case KeyEvent.VK_DOWN:
					bat.y += batSpeed;
					break;
				case KeyEvent.VK_LEFT:
					bat.x -= batSpeed;
					break;
				case KeyEvent.VK_RIGHT:
					bat.x += batSpeed;
					break;
				default:
					break;
				}
				return false;
			}
		});
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {image = null;}
		});
		
		Cursor hiddenCursor = getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(1, 1), "");
		setCursor(hiddenCursor);
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		if (image == null)
			image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
					
		Graphics2D g2 = (Graphics2D)image.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Background
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		// Ball
		g2.setColor(Color.RED);
		g2.fill(ball);
		
		// Bat
		g2.setColor(Color.BLUE);
		g2.fill(bat);
		
		g2.setColor(Color.WHITE);
		g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		g2.drawString(String.valueOf(score), getHeight()/10, getWidth()/10);
		
		g.drawImage(image, 0, 0, null);
	}
		
	public void update() {
		ball.x += ballSpeed*ballXDirection;
		ball.y += ballSpeed*ballYDirection;
		
		// Ball X Bounding
		if (ball.x < 0) {
			ball.x = 0;
			ballXDirection *= -1;
		} else if (ball.x > (getWidth() - ball.getBounds().getWidth())) {
			ball.x = getWidth() - ball.getBounds().getWidth();
			ballXDirection *= -1;
		}
		// Ball Y Bounding
		if (ball.y < 0) {
			ball.y = 0;
			ballYDirection *= -1;
		} else if (ball.y > (getHeight() - ball.getBounds().getHeight())) {
			resetState();
			scoreGoal();
		}
		
		// Bat X Bounding
		if (bat.x < 0) {
			bat.x = 0;
		} else if (bat.x > (getWidth() - bat.getBounds().getWidth())) {
			bat.x = getWidth() - bat.getBounds().getWidth();
		}
		// Bat Y Bounding
		if (bat.y < 0) {
			bat.y = 0;
		} else if (bat.y > (getHeight() - bat.getBounds().getHeight())) {
			bat.y = getHeight() - bat.getBounds().getHeight();
		}
		
		if (ball.intersects(bat.getBounds2D()) && alreadyIntersected) {
			ballYDirection *= -1;
			alreadyIntersected = false;
		} else {alreadyIntersected = true;}
		
		repaint();
	}
	
	public void scoreGoal() {
		score--;
		
		if (scoreEventListener != null)
			scoreEventListener.scoreEventOccurred();
	}
	
	public void resetState() {
		ball.x = getWidth()/2;
		ball.y = getHeight()/2;

		ballYDirection = new Random().nextInt() > 0 ? -1 : 1;
		ballXDirection = new Random().nextInt() > 0 ? -1 : 1;
	}
	
	public void setScoreEventListener (ScoreEventListener scoreEventListener) {
		this.scoreEventListener = scoreEventListener;
	}
	
}
