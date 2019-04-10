package com.josh.pay;

import org.joshvm.j2me.directUI.Display;

import com.josh.pay.constant.Constant;
import com.josh.pay.net.manager.NetManager;
import com.josh.pay.window.WindowManager;
import com.josh.pay.window.home.StartWindow;

public class JoshApplication {

	public static int AppId = 0;
	public static final int AeMoney = 0;
	public static final int Josh = 1;

	public static void main(String[] args) {

		System.out.println("Version:V0.5");
		AppId = Josh;
		NetManager.initSSL(new AeMoneyApplication().getClass(), "_main.ks");

		while (true) {

			// Get the instance of WindowManager
			WindowManager wm = WindowManager.createWindowManager();
			try {
				wm.setDisplay(Display.getDisplay());

				// Add start window
				wm.addWindow(new StartWindow(Constant.WIN_ID_START));
				// Set current window to Home Screen, it will show on screen
				wm.setCurrent(Constant.WIN_ID_START);
				// Start WindowManager
				wm.start(true);

				// Should NOT reach here
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
