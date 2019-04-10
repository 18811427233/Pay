package com.josh.pay.window;

import java.io.IOException;
import java.util.Hashtable;

import org.joshvm.j2me.directUI.Display;
import org.joshvm.j2me.directUI.Keypad;
import org.joshvm.j2me.directUI.KeypadEventListener;

public final class WindowManager {
	private Hashtable windows;
	private Window current;
	private Display display;
	private static WindowManager wm = null;
	private static Event evt;

	private WindowManager() {
		windows = new Hashtable();
		current = null;
		evt = new Event();
	}

	public synchronized static WindowManager createWindowManager() {
		if (wm != null) {
			return wm;
		}

		wm = new WindowManager();
		return wm;
	}

	public void start(boolean ifListeningEvent) throws IOException {
		// TODO Start event listening thread
		if (ifListeningEvent) {
			startListeningEvent();
		}

		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void destroy() {
		// Stop event listening thread
		synchronized (this) {
			notifyAll();
		}
	}

	public void addWindow(Window w) {
		if (windows.contains(w)) {
			return;
		}

		w.init();
		w.setWindowManager(this);

		Window prev = (Window) windows.put(new Integer(w.id()), w);
		if (prev != null) {
			prev.deinit();
		}
	}

	public void removeWindow(int id) {
		Window w = (Window) windows.remove(new Integer(id));
		if (w != null) {
			w.deinit();
		}
	}

	public Window getWindow(int id) {
		return (Window) windows.get(new Integer(id));
	}

	public void setCurrent(Window w) {
		current = w;
		w.show();
	}

	public void refresh(boolean repaint) {
		current.refresh(repaint);
	}

	public void setCurrent(int id) {
		setCurrent(getWindow(id));
	}

	public Window getCurrent() {
		return current;
	}

	public Display getDisplay() {
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

	private void startListeningEvent() throws IOException {
		Keypad pad = Keypad.getKeypad();
		pad.setKeyListener(new KeypadEventListener() {
			public void keyPressed(char keyValue) {
				// System.out.println("WM key down event:"+keyValue);
				evt.type = Event.KEY_PRESSED;
				evt.value = keyValue;
				current.event(evt);
			}

			public void keyReleased(char keyValue) {
				// System.out.println("WM key up event:"+keyValue);
				evt.type = Event.KEY_RELEASED;
				evt.value = keyValue;
				current.event(evt);
			}
		});
	}
}
