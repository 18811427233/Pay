package org.josh.pay.item;

import com.josh.pay.window.Window;

public interface Item {
	public int id();

	public void show();

	public void setHidden(boolean hidden);

	public boolean isHidden();

	public void enable();

	public void disable();

	public boolean isEnable();

	public void setPosition(int x, int y);

	public void setContainerWindow(Window window);

	public Window getContainerWindow();

	public int getX();

	public int getY();

	public int getW();

	public int getH();
}
