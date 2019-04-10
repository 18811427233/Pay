package com.josh.pay.window.scanresult;

import org.josh.pay.item.ImageItem;
import org.joshvm.async.AsyncTask;
import org.joshvm.async.AsyncTaskBody;
import org.joshvm.j2me.directUI.Keypad;

import com.josh.pay.AEImage;
import com.josh.pay.constant.Constant;
import com.josh.pay.window.ContentWindow;
import com.josh.pay.window.Event;
import com.josh.pay.window.inputprice.InputPriceWindow;
import com.josh.pay.window.receipt.ReceiptWindow;

public class QRCodeResultWindow extends ContentWindow implements AsyncTaskBody, MIScanResultInterface {
	private WIScanResultInterface wiScanResultInterface;
	private String qrcode_str;
	private ImageItem image;

	private AsyncTask asyncTask;
	private boolean queryResult = false;

	private int checkNumber = 7;

	private ReceiptWindow windowReceipt;

	public QRCodeResultWindow(int id) {
		super(id);
		qrcode_str = null;
		image = new ImageItem(Constant.ID_QR_RESULT_ITEM, new AEImage("waiting_def.jpg").getImage());
	}

	public void registerModel(WIScanResultInterface wiScanResultInterface) {

		this.wiScanResultInterface = wiScanResultInterface;
	}

	public void init() {
		addItem(image);

		asyncTask = new AsyncTask();
		asyncTask.setCallback(this);
	}

	public void deinit() {

	}

	public void show() {
		super.show();

		windowReceipt = (ReceiptWindow) getWindowManager().getWindow(Constant.WIN_ID_RECEIPT);

		wiScanResultInterface.scan(qrcode_str);

		queryResult = true;

	}

	public void setQRCode(String text) {
		qrcode_str = text;
	}

	public boolean event(Event evt) {
		if ((evt.type == Event.KEY_PRESSED) && ((evt.value == Keypad.KEY_FUNC1))) {

			toPrice();

			return true;
		}
		return super.event(evt);
	}

	public void onAsyncTaskStart(Object param) {

		while (queryResult) {
			checkNumber--;

			getDisplay().turnOnBacklight();
			wiScanResultInterface.quaryOrderResult();

			try {
				Thread.sleep(5 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 停止轮训跳转到收款结果界面的操作
	 * 
	 * @param isSuccess
	 * @param receiptWindow
	 */
	private void toReceipt(int result) {
		windowReceipt.setResult(result);

		if (queryResult) {
			queryResult = false;
			checkNumber = 7;
			getWindowManager().setCurrent(windowReceipt);
		}
	}

	private void toPrice() {
		queryResult = false;
		checkNumber = 7;
		window_manager.setCurrent(Constant.WIN_ID_INPUTPRICE);
	}

	public void errorMessage(String message) {

		InputPriceWindow w = (InputPriceWindow) window_manager.getWindow(Constant.WIN_ID_INPUTPRICE);
		w.setMsg(message);
		toPrice();
	}

	public void scanQr() {

		asyncTask.trig(null);

	}

	public void quaryOrderResult(int result) {
		if (result == Constant.PAY_SUCCESS) {
			toReceipt(result);
		} else if (result == Constant.PAY_FAIL) {
			toReceipt(result);
		} else if (result == Constant.NET_ERROR) {
			if (checkNumber == 1) {

				toReceipt(result);
			}
		} else if (result == Constant.QUARY_OUTTIME) {
			if (checkNumber == 1) {

				toReceipt(result);
			}
		}

	}

}
