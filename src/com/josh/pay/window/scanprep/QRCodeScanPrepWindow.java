package com.josh.pay.window.scanprep;

import org.josh.pay.item.ImageItem;
import org.josh.pay.item.TextItem;

import com.josh.pay.AEImage;
import com.josh.pay.constant.Constant;
import com.josh.pay.window.ContentWindow;
import com.josh.pay.window.Event;
import com.josh.pay.window.Window;

public class QRCodeScanPrepWindow extends ContentWindow implements Window {

	private ImageItem item;
	private TextItem textItem;
	private ImageItem imgMoneyItemBefore;
	private ImageItem imgMoneyItemAfter;

	public QRCodeScanPrepWindow(int id) {
		super(id);
		item = new ImageItem(Constant.ID_QR_SCAN_PREP_ITEM, new AEImage("scanprep.jpg").getImage());
		textItem = new TextItem(Constant.ID_QR_SCAN_PREP_ITEM, "", true);
		textItem.setPosition(35, 280);

		imgMoneyItemBefore = new ImageItem(Constant.ID_QR_SCAN_PREP_ITEM, new AEImage("money.jpg").getImage());
		imgMoneyItemBefore.setPosition(10, 280);
		imgMoneyItemAfter = new ImageItem(Constant.ID_QR_SCAN_PREP_ITEM, new AEImage("money_after.jpg").getImage());
		imgMoneyItemAfter.setPosition(205, 280);
	}

	public void init() {
		addItem(item);
		addItem(textItem);
		addItem(imgMoneyItemBefore);
		addItem(imgMoneyItemAfter);
	}

	public void deinit() {
		// TODO Auto-generated method stub

	}

	public void setMoney(String money) {
		textItem.setText(money);
	}

	public boolean event(Event evt) {
		return super.event(evt);
	}

	public void show() {
		super.show();

		getWindowManager().setCurrent(Constant.WIN_ID_QRCODESCAN);
	}

}
