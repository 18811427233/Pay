package com.josh.pay;

import org.joshvm.j2me.directUI.Display;

import com.josh.pay.constant.Constant;
import com.josh.pay.net.manager.NetManager;
import com.josh.pay.window.WindowManager;
import com.josh.pay.window.home.HomeWindow;
import com.josh.pay.window.home.MenuWindow;
import com.josh.pay.window.inputprice.InputPriceWindow;
import com.josh.pay.window.inputprice.WIIputPriceInterface;
import com.josh.pay.window.inputprice.model.AeInputPriceModel;
import com.josh.pay.window.login.LoginWindow;
import com.josh.pay.window.receipt.ReceiptWindow;
import com.josh.pay.window.scan.QRCodeScanWindow;
import com.josh.pay.window.scanprep.QRCodeScanPrepWindow;
import com.josh.pay.window.scanresult.QRCodeResultWindow;
import com.josh.pay.window.scanresult.WIScanResultInterface;
import com.josh.pay.window.scanresult.model.AeScanResultModel;
import com.josh.pay.window.show.QRCodeDispWindow;
import com.josh.pay.window.show.WIShowInterface;
import com.josh.pay.window.show.model.AeShowModel;
import com.josh.pay.window.waiting.WaitingWindow;

public class AeMoneyApplication {

	public static void main(String[] args) {

		System.out.println("Version:V0.1");
		JoshApplication.AppId = JoshApplication.AeMoney;
		MenuWindow.type = MenuWindow.DAQIAN;
		NetManager.initSSL(new AeMoneyApplication().getClass(), "_main.ks");

		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// Get the instance of WindowManager
			WindowManager wm = WindowManager.createWindowManager();
			try {
				wm.setDisplay(Display.getDisplay());

				// Add Home Screen Window
				wm.addWindow(new LoginWindow(Constant.WIN_ID_EMPLOYEEID));
				// Add Home Screen Window
				wm.addWindow(new HomeWindow(Constant.WIN_ID_HOME));
				// Add Receipt Window
				wm.addWindow(new ReceiptWindow(Constant.WIN_ID_RECEIPT));
				// Add QRCode Display Window
				wm.addWindow(new QRCodeDispWindow(Constant.WIN_ID_QRCODEDISP));
				// Add default waiting window
				wm.addWindow(new WaitingWindow(Constant.WIN_ID_DEFWAITING, null));
				// Add QRCode scan window
				wm.addWindow(new QRCodeScanWindow(Constant.WIN_ID_QRCODESCAN));
				// Add QRCode scan ready window
				wm.addWindow(new QRCodeScanPrepWindow(Constant.WIN_ID_QRCODESCANPREP));
				// Add QRCode result Window
				wm.addWindow(new QRCodeResultWindow(Constant.WIN_ID_QRCODESCANRESULT));
				// Add Input price Window
				wm.addWindow(new InputPriceWindow(Constant.WIN_ID_INPUTPRICE));
				// Set current window to Home Screen, it will show on screen
				wm.setCurrent(Constant.WIN_ID_EMPLOYEEID);
				// Start WindowManager
				wm.start(true);

				// Should NOT reach here
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
