package com.chrisali.pingpong.applet;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;

public class StartPanel extends JPanel {

	private static final long serialVersionUID = -4683962900749795913L;
	
	private StartGameListener startGameListener;

	/**
	 * Create the frame.
	 */
	public StartPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
		setLayout(gridBagLayout);
		
		JLabel lblTestSwingApplet = new JLabel("Ping Pong Applet");
		lblTestSwingApplet.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc_lblTestSwingApplet = new GridBagConstraints();
		gbc_lblTestSwingApplet.gridwidth = 2;
		gbc_lblTestSwingApplet.weightx = 1.0;
		gbc_lblTestSwingApplet.weighty = 0.8;
		gbc_lblTestSwingApplet.anchor = GridBagConstraints.SOUTH;
		gbc_lblTestSwingApplet.insets = new Insets(0, 0, 5, 0);
		gbc_lblTestSwingApplet.gridx = 0;
		gbc_lblTestSwingApplet.gridy = 0;
		add(lblTestSwingApplet, gbc_lblTestSwingApplet);
		
		JRadioButton rdbtnSinglePlayer = new JRadioButton("Single Player");
		rdbtnSinglePlayer.setToolTipText("Player 2 (Blue) will be controlled by a computer if selected");
		rdbtnSinglePlayer.setSelected(true);
		GridBagConstraints gbc_rdbtnSinglePlayer = new GridBagConstraints();
		gbc_rdbtnSinglePlayer.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnSinglePlayer.anchor = GridBagConstraints.LAST_LINE_START;
		gbc_rdbtnSinglePlayer.weighty = 0.1;
		gbc_rdbtnSinglePlayer.weightx = 0.5;
		gbc_rdbtnSinglePlayer.gridx = 1;
		gbc_rdbtnSinglePlayer.gridy = 1;
		add(rdbtnSinglePlayer, gbc_rdbtnSinglePlayer);
		
		JRadioButton rdbtnMultiPlayer = new JRadioButton("Multi Player");
		GridBagConstraints gbc_rdbtnMultiPlayer = new GridBagConstraints();
		gbc_rdbtnMultiPlayer.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnMultiPlayer.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc_rdbtnMultiPlayer.weighty = 0.1;
		gbc_rdbtnMultiPlayer.gridx = 1;
		gbc_rdbtnMultiPlayer.gridy = 2;
		add(rdbtnMultiPlayer, gbc_rdbtnMultiPlayer);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rdbtnSinglePlayer);
		buttonGroup.add(rdbtnMultiPlayer);
		
		JCheckBox chckbxMutatorMode = new JCheckBox("Mutator Mode");
		chckbxMutatorMode.setToolTipText("When selected, scoring against an opponent causes the scorer's bat to shrink and vice versa");
		GridBagConstraints gbc_chckbxMutatorMode = new GridBagConstraints();
		gbc_chckbxMutatorMode.weighty = 0.8;
		gbc_chckbxMutatorMode.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc_chckbxMutatorMode.gridx = 1;
		gbc_chckbxMutatorMode.gridy = 3;
		add(chckbxMutatorMode, gbc_chckbxMutatorMode);
		
		JButton btnStartGame = new JButton("Start Game");
		btnStartGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(startGameListener != null)
					startGameListener.startGame(rdbtnSinglePlayer.isSelected(), chckbxMutatorMode.isSelected());
			}
		});
		GridBagConstraints gbc_btnStartGame = new GridBagConstraints();
		gbc_btnStartGame.anchor = GridBagConstraints.NORTHEAST;
		gbc_btnStartGame.gridheight = 3;
		gbc_btnStartGame.weightx = 0.5;
		gbc_btnStartGame.weighty = 0.8;
		gbc_btnStartGame.insets = new Insets(15, 0, 5, 20);
		gbc_btnStartGame.gridx = 0;
		gbc_btnStartGame.gridy = 1;
		add(btnStartGame, gbc_btnStartGame);
	}
	
	public void setStartGameListener(StartGameListener startGameListener) {
		this.startGameListener = startGameListener;
	}

}
