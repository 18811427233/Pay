package org.josh.pay.item;

import com.josh.pay.window.Event;
import com.josh.pay.window.Window;

public abstract class RegularItem implements Item {
	private int item_id;
	private boolean isEnable;
	private boolean hidden;
	private int pos_x;
	private int pos_y;
	private int width;
	private int height;
	private Window container;
	protected boolean isFocused;

	public RegularItem(int id, int width, int height) {
		item_id = id;
		isEnable = true;
		hidden = false;
		pos_x = pos_y = 0;
		this.width = width;
		this.height = height;
		isFocused = false;
	}

	public int id() {
		return item_id;
	}

	public void enable() {
		isEnable = true;
		if (!hidden) {
			show();
		}
	}

	public void disable() {
		isEnable = false;
		if (!hidden) {
			show();
		}
	}

	public boolean isEnable() {
		return isEnable;
	}

	public void setHidden(boolean hide) {
		hidden = hide;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setPosition(int x, int y) {
		pos_x = x;
		pos_y = y;
	}

	public int getX() {
		return pos_x;
	}

	public int getY() {
		return pos_y;
	}

	public int getW() {
		return width;
	}

	public int getH() {
		return height;
	}

	public void setContainerWindow(Window window) {
		container = window;
	}

	public Window getContainerWindow() {
		return container;
	}

	public boolean setFocus(boolean status) {
		return false;
	}

	public boolean hasFocus() {
		return isFocused;
	}

	public boolean onKeyInputEvent(Event evt) {
		return false;
	}

}
