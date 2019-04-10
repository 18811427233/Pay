package com.josh.pay.window.show;

import org.josh.pay.item.ImageItem;
import org.josh.pay.item.QRCodeImageItem;
import org.josh.pay.item.TextItem;
import org.joshvm.async.AsyncTask;
import org.joshvm.async.AsyncTaskBody;
import org.joshvm.j2me.directUI.Keypad;

import com.josh.pay.AEImage;
import com.josh.pay.constant.Constant;
import com.josh.pay.window.ContentWindow;
import com.josh.pay.window.Event;
import com.josh.pay.window.home.MenuWindow;
import com.josh.pay.window.receipt.ReceiptWindow;

public class QRCodeDispWindow extends ContentWindow implements AsyncTaskBody, MIShowInterface {
	private WIShowInterface wiShowInterface;
	QRCodeImageItem qrcodeItem;
	private TextItem textItem;

	private ImageItem imgMoneyItemBefore;
	private ImageItem imgMoneyItemAfter;

	private AsyncTask asyncTask;
	private boolean queryResult = true;
	private int checkNumber = 7;

	private ReceiptWindow windowReceipt;
	private boolean canBack = false;

	public QRCodeDispWindow(int id) {
		super(id);

		qrcodeItem = new QRCodeImageItem(Constant.ID_QR_SHOW_ITEM);

		textItem = new TextItem(Constant.ID_QR_SHOW_ITEM, "", true);
		textItem.setPosition(35, 250);

		imgMoneyItemBefore = new ImageItem(Constant.ID_QR_SHOW_ITEM, new AEImage("money.jpg").getImage());
		imgMoneyItemBefore.setPosition(10, 250);
		imgMoneyItemAfter = new ImageItem(Constant.ID_QR_SHOW_ITEM, new AEImage("money_after.jpg").getImage());
		imgMoneyItemAfter.setPosition(205, 250);
	}

	public void init() {

		addItem(qrcodeItem);
		addItem(textItem);
		addItem(imgMoneyItemBefore);
		addItem(imgMoneyItemAfter);

		asyncTask = new AsyncTask();
		asyncTask.setCallback(this);
	}

	public void deinit() {

	}

	public void show() {

		if (MenuWindow.type == MenuWindow.DAQIAN) {
			qrcodeItem.setPosition(45, 50);
		} else if (MenuWindow.type == MenuWindow.WEIXIN) {
			qrcodeItem.setPosition(62, 60);
		} else if (MenuWindow.type == MenuWindow.ZHIFUBAO) {
			qrcodeItem.setPosition(52, 55);
		}

		super.show();
		queryResult = true;
		checkNumber = 7;
		asyncTask.trig(null);
		windowReceipt = (ReceiptWindow) getWindowManager().getWindow(Constant.WIN_ID_RECEIPT);
		canBack = true;
	}

	public void registerModel(WIShowInterface wiShowInterface) {

		this.wiShowInterface = wiShowInterface;
	}

	public void setQRCode(String str, String money) {
		qrcodeItem.setQRCode(str);
		textItem.setText(money);
		canBack = false;
	}

	public boolean event(Event evt) {

		if ((evt.type == Event.KEY_PRESSED) && ((evt.value == Keypad.KEY_FUNC1))) {
			if (canBack) {
				toPrice();

			}

			return true;
		}
		return super.event(evt);
	}

	public void onAsyncTaskStart(Object param) {

		while (queryResult) {
			checkNumber--;
			getDisplay().turnOnBacklight();
			wiShowInterface.quaryOrderResult();
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
	 * @param receiptWindow
	 */
	private void toReceipt(int type) {
		windowReceipt.setResult(type);

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

	public void onFail(String message) {

	}

}
