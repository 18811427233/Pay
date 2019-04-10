package com.josh.pay.window.home;

import java.io.IOException;

import org.josh.pay.item.ImageItem;
import org.josh.pay.item.Item;
import org.joshvm.j2me.directUI.Display;

import com.josh.pay.AEImage;
import com.josh.pay.constant.Constant;
import com.josh.pay.window.ContentWindow;
import com.josh.pay.window.WindowManager;
import com.josh.pay.window.inputprice.InputPriceWindow;
import com.josh.pay.window.login.LoginWindow;
import com.josh.pay.window.receipt.ReceiptWindow;
import com.josh.pay.window.scan.QRCodeScanWindow;
import com.josh.pay.window.scanprep.QRCodeScanPrepWindow;
import com.josh.pay.window.scanresult.QRCodeResultWindow;
import com.josh.pay.window.show.QRCodeDispWindow;
import com.josh.pay.window.waiting.WaitingWindow;

public class StartWindow extends ContentWindow {

	private Item homescreen;

	public StartWindow(int id) {
		super(id);
		homescreen = new ImageItem(Constant.ID_START_ITEM, new AEImage("loadding.jpg").getImage());
	}

	public void init() {
		addItem(homescreen);
	}

	public void deinit() {
		// TODO Auto-generated method stub

	}

	public void show() {
		// TODO Auto-generated method stub
		super.show();

		new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				// Get the instance of WindowManager
				WindowManager wm = WindowManager.createWindowManager();
				try {
					wm.setDisplay(Display.getDisplay());

					// Add Home Screen Window
					wm.addWindow(new HomeWindow(Constant.WIN_ID_HOME));
					// Add Receipt Window
					wm.addWindow(new ReceiptWindow(Constant.WIN_ID_RECEIPT));
					// Add QRCode Display Window
					wm.addWindow(new QRCodeDispWindow(Constant.WIN_ID_QRCODEDISP));
					// Add QRCode scan window
					wm.addWindow(new QRCodeScanWindow(Constant.WIN_ID_QRCODESCAN));
					// Add QRCode scan ready window
					wm.addWindow(new QRCodeScanPrepWindow(Constant.WIN_ID_QRCODESCANPREP));
					// Add QRCode result Window
					wm.addWindow(new QRCodeResultWindow(Constant.WIN_ID_QRCODESCANRESULT));
					// Add Input price window
					wm.addWindow(new InputPriceWindow(Constant.WIN_ID_INPUTPRICE));
					// Add Input employee ID window
					wm.addWindow(new LoginWindow(Constant.WIN_ID_EMPLOYEEID));
					// Add default waiting window
					wm.addWindow(new WaitingWindow(Constant.WIN_ID_DEFWAITING, null));
					// Add pay home window
					wm.addWindow(new MenuWindow(Constant.WIN_ID_MENU));
					// Start WindowManager
					// Set current window to Home Screen, it will show on screen
					wm.setCurrent(Constant.WIN_ID_MENU);
					wm.start(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

}
