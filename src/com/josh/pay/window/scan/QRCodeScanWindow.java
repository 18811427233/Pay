package com.josh.pay.window.scan;

import java.io.IOException;

import org.joshvm.async.AsyncTask;
import org.joshvm.async.AsyncTaskBody;
import org.joshvm.j2me.directUI.Display;
import org.joshvm.j2me.directUI.Keypad;
import org.joshvm.j2me.qrcode.QrcodeDecoder;

import com.josh.pay.constant.Constant;
import com.josh.pay.window.Event;
import com.josh.pay.window.Window;
import com.josh.pay.window.WindowManager;
import com.josh.pay.window.home.MenuWindow;
import com.josh.pay.window.scanresult.QRCodeResultWindow;
import com.josh.pay.window.scanresult.WIScanResultInterface;
import com.josh.pay.window.scanresult.model.AeScanResultModel;
import com.josh.pay.window.scanresult.model.AliScanResultModel;
import com.josh.pay.window.scanresult.model.WeScanResultModel;

public class QRCodeScanWindow implements Window, AsyncTaskBody {
	private AsyncTask event;
	private WindowManager windowManager;
	private int winID;
	private boolean cancelled;

	private AsyncTask eventTask;
	private boolean canEvent = false;

	public QRCodeScanWindow(int id) {
		winID = id;
		cancelled = false;
	}

	public void init() {

		event = new AsyncTask();
		event.setCallback(this);

		eventTask = new AsyncTask();
		eventTask.setCallback(new AsyncTaskBody() {

			public void onAsyncTaskStart(Object param) {

				try {
					Thread.sleep(1000);
					canEvent = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void deinit() {
		event.destroy();
		eventTask.destroy();
	}

	public void setWindowManager(WindowManager wm) {
		windowManager = wm;
	}

	public WindowManager getWindowManager() {
		return windowManager;
	}

	public void show() {

		canEvent = false;
		event.trig(null);
	}

	public void refresh(boolean repaint) {

	}

	public boolean event(Event evt) {

		if (!canEvent) {

			return false;
		}

		if (evt.type == Event.KEY_PRESSED) {

			if (evt.value == Keypad.KEY_FUNC1) {
				System.out.println("Cancel decoder");
				cancelled = true;
				QrcodeDecoder.cancel();
				return true;
			}

		}
		return false;
	}

	public int id() {
		return winID;
	}

	public Display getDisplay() {
		return windowManager.getDisplay();
	}

	public void onAsyncTaskStart(Object param) {
		System.out.println("onAsyncEvent");
		QRCodeResultWindow resultWin = (QRCodeResultWindow) windowManager.getWindow(Constant.WIN_ID_QRCODESCANRESULT);
		String result;
		try {
			cancelled = false;

			resultWin.setQRCode(null);

			eventTask.trig(null);

			result = QrcodeDecoder.getQrCode();
			windowManager.getDisplay().playBuzzer();
			resultWin.setQRCode(result);
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (cancelled) {
				getWindowManager().setCurrent(Constant.WIN_ID_INPUTPRICE);
			} else {

				WIScanResultInterface wiScanResultInterface = null;

				switch (MenuWindow.type) {
				case MenuWindow.DAQIAN:
					wiScanResultInterface = new AeScanResultModel(resultWin);
					break;
				case MenuWindow.WEIXIN:
					wiScanResultInterface = new WeScanResultModel(resultWin);
					break;
				case MenuWindow.ZHIFUBAO:
					wiScanResultInterface = new AliScanResultModel(resultWin);
					break;

				default:
					break;
				}

				resultWin.registerModel(wiScanResultInterface);
				windowManager.setCurrent(resultWin);

			}

		}
	}
}
