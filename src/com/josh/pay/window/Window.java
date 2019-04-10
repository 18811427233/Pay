package com.josh.pay.window;

import org.joshvm.j2me.directUI.Display;

public interface Window {
	public void init();

	public void deinit();

	public void setWindowManager(WindowManager wm);

	public WindowManager getWindowManager();

	public void show();

	public void refresh(boolean repaint);

	public boolean event(Event evt);

	public int id();

	public Display getDisplay();
}
