package com.chrisali.pingpong.applet;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.Timer;

public class Main extends JApplet {

	private static final long serialVersionUID = 3206847208968227199L;

	private StartPanel startPanel;
	private CardLayout cardLayout;
	private Timer timer;
	private JPongGame game;
	
	@Override
	public void init() {
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		setSize(new Dimension(900, 500));
				
		startPanel = new StartPanel();
		startPanel.setStartGameListener(new StartGameListener() {
			@Override
			public void startGame(boolean isSinglePlayer, boolean isMutatorMode) {
				game.setSinglePlayer(isSinglePlayer);
				game.setMutatorMode(isMutatorMode);
				cardLayout.show(Main.this.getContentPane(), "game");
			}
		});
		add(startPanel, "startPanel");
		
		game = new JPongGame(this);
		game.setStopGameListener(new StopGameListener() {
			@Override
			public void gameStopped() {
				cardLayout.show(Main.this.getContentPane(), "startPanel");
			}
		});
		add(game, "game");
		
		timer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {game.update();}
		});
	}

	@Override
	public void start() {timer.start();}

	@Override
	public void stop() {timer.stop();}
	
	@Override
	public void destroy() {}
}
