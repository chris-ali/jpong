package com.chrisali.pingpong.applet;

import java.util.EventListener;

public interface StartGameListener extends EventListener {
	public void startGame(boolean isSinglePlayer, boolean isMutatorMode);
}
