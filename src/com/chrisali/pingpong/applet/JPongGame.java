package com.chrisali.pingpong.applet;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
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
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JApplet;
import javax.swing.JComponent;

public class JPongGame extends JComponent {

	private static final long serialVersionUID = -8395759457708163217L;
	
	private BufferedImage image;
	private boolean isSinglePlayer;
	private boolean isMutatorMode;
	
	private ScoreEventListener scoreEventListener;
	private StopGameListener stopGameListener;
	
	// Scoring
	private int score1;
	private int score2;
	
	// Ball
	private Ellipse2D.Double ball;
	private double ballSpeed;
	private int ballXDirection;
	private int ballYDirection;
	private boolean alreadyIntersected;
	
	// Bat 1
	private RoundRectangle2D.Double bat1;
	private double bat1Speed;
	
	// Bat 2
	private RoundRectangle2D.Double bat2;
	private double bat2Speed;
	
	public JPongGame(JApplet parent) {
		
		Dimension windowSize = new Dimension(parent.getWidth(), parent.getHeight());
		
		isSinglePlayer = false;
		isMutatorMode = false;
		
		// Scoring
		score1 = 0;
		score2 = 0;
		
		// Ball
		ball = new Ellipse2D.Double(windowSize.getWidth()/2, windowSize.getHeight()/2, windowSize.getHeight()/50, windowSize.getHeight()/50);
		alreadyIntersected = true;
		ballSpeed = windowSize.getWidth()/360;
		ballYDirection = new Random().nextInt() > 0 ? -1 : 1;
		ballXDirection = new Random().nextInt() > 0 ? -1 : 1;
		
		// Bat 1
		bat1 = new RoundRectangle2D.Double(5*windowSize.getWidth()/100, windowSize.getHeight()/2, windowSize.getWidth()/90, windowSize.getHeight()/5, 20, 20);
		bat1Speed = windowSize.getHeight()/40;
		
		// Bat 2
		bat2 = new RoundRectangle2D.Double(95*windowSize.getWidth()/100, windowSize.getHeight()/2, windowSize.getWidth()/90, windowSize.getHeight()/5, 20, 20);
		bat2Speed = windowSize.getHeight()/40;
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (!isSinglePlayer) {
					switch (e.getKeyCode()) {					
					// Bat 1 Controls (or AI)
					case KeyEvent.VK_W:
						bat1.y -= bat1Speed;
						break;
					case KeyEvent.VK_S:
						bat1.y += bat1Speed;
						break;
					}
				}
				switch (e.getKeyCode()) {
					// Bat 2 Controls
					case KeyEvent.VK_UP:
						bat2.y -= bat2Speed;
						break;
					case KeyEvent.VK_DOWN:
						bat2.y += bat2Speed;
						break;
					// Stop game
					case KeyEvent.VK_ESCAPE:
						if (stopGameListener != null) {
							stopGame();
							stopGameListener.gameStopped();
						}
						break;
					default:
						break;
				}
				return false;
			}
		});
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				image = null;
				
				bat1.setRoundRect(5*getWidth()/100, bat1.getY(), getWidth()/90, getHeight()/5, 20, 20);
				bat1Speed = windowSize.getHeight()/40;
				bat2.setRoundRect(95*getWidth()/100, bat2.getY(), getWidth()/90, getHeight()/5, 20, 20);
				bat2Speed = windowSize.getHeight()/40;
				
				ball.setFrame(ball.getX(), ball.getY(), getHeight()/50, getHeight()/50);
				ballSpeed = getWidth()/360;
			}
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
		
		// Draw Background
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		// Draw Ball
		g2.setColor(Color.WHITE);
		g2.fill(ball);
		
		// Draw Line
		g2.setColor(Color.LIGHT_GRAY);
		g2.drawLine(getWidth()/2, 0, getWidth()/2, getHeight());
		
		// Draw Bat 1
		g2.setColor(Color.BLUE);
		g2.fill(bat1);
		
		// Draw Bat 2
		g2.setColor(Color.RED);
		g2.fill(bat2);
		
		// Draw Score
		g2.setColor(Color.WHITE);
		g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		g2.drawString(String.valueOf(score1), 5*getWidth()/100, getHeight()/10);
		g2.drawString(String.valueOf(score2), 95*getWidth()/100, getHeight()/10);
		
		g.drawImage(image, 0, 0, null);
	}
		
	protected void update() {
		// Ball Translation
		ball.x += ballSpeed*ballXDirection;
		ball.y += ballSpeed*ballYDirection;
		
		// Ball X Bounding (Score events)
		if (ball.x < 0) {
			resetState();
			player2ScoreGoal();
		} else if (ball.x > (getWidth() - ball.getBounds().getWidth())) {
			resetState();
			player1ScoreGoal();
		}
		// Ball Y Bounding
		if (ball.y < 0) {
			ball.y = 0;
			ballYDirection *= -1;
		} else if (ball.y > (getHeight() - ball.getBounds().getHeight())) {
			ball.y = getHeight() - ball.getBounds().getHeight();
			ballYDirection *= -1;
		}
		
		// Bat 1 X Bounding
		if (bat1.x < 0) {
			bat1.x = 0;
		} else if (bat1.x > (getWidth() - bat1.getBounds().getWidth())) {
			bat1.x = getWidth() - bat1.getBounds().getWidth();
		}
		// Bat 1 Y Bounding
		if (bat1.y < 0) {
			bat1.y = 0;
		} else if (bat1.y > (getHeight() - bat1.getBounds().getHeight())) {
			bat1.y = getHeight() - bat1.getBounds().getHeight();
		}
		
		// Bat 2 X Bounding
		if (bat2.x < 0) {
			bat2.x = 0;
		} else if (bat2.x > (getWidth() - bat2.getBounds().getWidth())) {
			bat2.x = getWidth() - bat2.getBounds().getWidth();
		}
		// Bat 2 Y Bounding
		if (bat2.y < 0) {
			bat2.y = 0;
		} else if (bat2.y > (getHeight() - bat2.getBounds().getHeight())) {
			bat2.y = getHeight() - bat2.getBounds().getHeight();
		}
		
		// Simple Bat 1 AI
		if ((bat1.y != ball.y && ballXDirection == -1) && isSinglePlayer)
			bat1.y += ballYDirection*bat1Speed/4;
		
		if ((ball.intersects(bat1.getBounds2D()) || ball.intersects(bat2.getBounds2D())) && alreadyIntersected) {
			ballXDirection *= -1;
			alreadyIntersected = false;
		} else {alreadyIntersected = true;}
		
		repaint();
	}
	
	private void player1ScoreGoal() {
		score1++;
		
		if(isMutatorMode) {
			batMutator(2, true);
			batMutator(1, false);
		}
				
		if (scoreEventListener != null)
			scoreEventListener.scoreEventOccurred();
	}

	private void player2ScoreGoal() {
		score2++;
		
		if(isMutatorMode) {
			batMutator(1, true);
			batMutator(2, false);
		}
		
		if (scoreEventListener != null)
			scoreEventListener.scoreEventOccurred();
	}
	
	private void resetState() {
		ball.x = getWidth()/2;
		ball.y = getHeight()/2;

		ballYDirection = new Random().nextInt() > 0 ? -1 : 1;
		ballXDirection = new Random().nextInt() > 0 ? -1 : 1;
	}
	
	private void stopGame() {
		resetState();
		score1 = 0;
		score2 = 0;
	}
	
	private void batMutator(int player, boolean isGrowth) {
		int mutator = isGrowth ? 10 : -10;
		
		switch (player) {
		case 1:
			if (bat1.getHeight() > 30)
				bat1.setRoundRect(bat1.getX(), bat1.getY(), bat1.getWidth(), bat1.getHeight()+mutator, bat1.getArcWidth(), bat1.getArcHeight());
			break;
		case 2:
			if (bat2.getHeight() > 30)
				bat2.setRoundRect(bat2.getX(), bat2.getY(), bat2.getWidth(), bat2.getHeight()+mutator, bat2.getArcWidth(), bat2.getArcHeight());
			break;
		default:
			break;
		}
	}
	
	public void setSinglePlayer(boolean isSinglePlayer) {
		this.isSinglePlayer = isSinglePlayer;
	}
	
	public void setMutatorMode(boolean isMutatorMode) {
		this.isMutatorMode = isMutatorMode;
	}
	
	public void setScoreEventListener(ScoreEventListener scoreEventListener) {
		this.scoreEventListener = scoreEventListener;
	}
	
	public void setStopGameListener(StopGameListener stopGameListener) {
		this.stopGameListener = stopGameListener;
	}
}
