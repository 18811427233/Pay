package com.josh.pay.window;

import java.util.Enumeration;
import java.util.Vector;

import org.josh.pay.item.Item;
import org.josh.pay.item.RegularItem;
import org.joshvm.j2me.directUI.Display;
import org.joshvm.j2me.directUI.Keypad;

import com.josh.pay.constant.Constant;

public abstract class ContentWindow implements Window {
	private int window_id;
	private Vector items = new Vector();
	protected WindowManager window_manager;
	private RegularItem currentFocusItem;

	public ContentWindow(int id) {
		window_id = id;
		currentFocusItem = null;
	}

	public void show() {

		getWindowManager().getDisplay().clear();
		Enumeration e = getItems();
		while (e.hasMoreElements()) {
			Item item = (Item) e.nextElement();
			if (!item.isHidden()) {
				item.show();
			}
		}
	}

	public void showWaiting() {
		getWindowManager().setCurrent(Constant.WIN_ID_DEFWAITING);
	}

	public void refresh(boolean repaint) {
		if (repaint) {
			// TODO: Invoke display function to repaint background
			getWindowManager().getDisplay().clear();
		}

		Enumeration e = getItems();
		while (e.hasMoreElements()) {
			Item item = (Item) e.nextElement();
			if (!item.isHidden()) {
				item.show();
			}
		}
	}

	public void addItem(Item item) {
		item.setContainerWindow(this);
		items.addElement(item);
	}

	public void removeItem(Item item) {
		if (items.contains(item)) {
			items.removeElement(item);
			item.setContainerWindow(null);
		}
	}

	public Enumeration getItems() {
		return items.elements();
	}

	public int id() {
		return window_id;
	}

	public void setWindowManager(WindowManager wm) {
		window_manager = wm;
	}

	public Display getDisplay() {
		return window_manager.getDisplay();
	}

	public WindowManager getWindowManager() {
		return window_manager;
	}

	public boolean event(Event evt) {
		boolean processed = false;

		if (evt.type == Event.KEY_PRESSED && evt.value == Keypad.KEY_USER1) {
			window_manager.getDisplay().turnOffBacklight();

			return true;
		}

		// System.out.println("ContentWindow.event: type=" + evt.type);
		if (currentFocusItem != null) {
			try {
				processed = currentFocusItem.onKeyInputEvent(evt);
			} catch (Exception e) {
				// Catch all Exceptions!
				e.printStackTrace();
			}
		}
		// System.out.println("processed=" + processed);
		return processed;
	}

	public RegularItem setFocus(RegularItem item) {
		RegularItem prevFocusItem = currentFocusItem;

		if (!items.contains(item)) {
			throw new IllegalArgumentException("Item not found");
		}

		if (item.setFocus(true)) {
			if (currentFocusItem != null) {
				currentFocusItem.setFocus(false);
			}
			currentFocusItem = item;
		}

		return prevFocusItem;
	}

	public RegularItem getFocus() {
		return currentFocusItem;
	}

}
