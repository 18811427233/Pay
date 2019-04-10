package com.josh.pay.window.receipt;

import org.josh.pay.item.ImageItem;
import org.josh.pay.item.Item;
import org.joshvm.j2me.directUI.Keypad;

import com.josh.pay.AEImage;
import com.josh.pay.constant.Constant;
import com.josh.pay.window.ContentWindow;
import com.josh.pay.window.Event;
import com.josh.pay.window.inputprice.InputPriceWindow;

public class ReceiptWindow extends ContentWindow {

	private Item ok;
	private Item fail;
	private Item netError;
	private Item quaryOuttime;

	public ReceiptWindow(int id) {
		super(id);
		ok = new ImageItem(Constant.ID_RECEIPT_ITEM, new AEImage("ok.jpg").getImage());
		fail = new ImageItem(Constant.ID_RECEIPT_ITEM, new AEImage("fail.jpg").getImage());
		netError = new ImageItem(Constant.ID_RECEIPT_ITEM, new AEImage("net_error.jpg").getImage());
		quaryOuttime = new ImageItem(Constant.ID_RECEIPT_ITEM, new AEImage("quary_outtime.jpg").getImage());
	}

	public void init() {
		addItem(ok);
		addItem(fail);
		addItem(netError);
		addItem(quaryOuttime);
	}

	public void setResult(int result) {

		switch (result) {
		case Constant.PAY_SUCCESS:
			ok.setHidden(false);
			fail.setHidden(true);
			quaryOuttime.setHidden(true);
			netError.setHidden(true);
			break;
		case Constant.PAY_FAIL:
			ok.setHidden(true);
			fail.setHidden(false);
			quaryOuttime.setHidden(true);
			netError.setHidden(true);
			break;
		case Constant.QUARY_OUTTIME:
			ok.setHidden(true);
			fail.setHidden(true);
			quaryOuttime.setHidden(false);
			netError.setHidden(true);
			break;
		case Constant.NET_ERROR:
			ok.setHidden(true);
			fail.setHidden(true);
			quaryOuttime.setHidden(true);
			netError.setHidden(false);
			break;

		default:
			break;
		}
	}

	public boolean event(Event evt) {

		if ((evt.type == Event.KEY_PRESSED) && (evt.value != Keypad.KEY_POWER)) {
			InputPriceWindow window = (InputPriceWindow) window_manager.getWindow(Constant.WIN_ID_INPUTPRICE);
			window.setMsg("");
			getWindowManager().setCurrent(window);
			return true;
		} else {
			return super.event(evt);
		}

	}

	public void deinit() {
		// TODO Auto-generated method stub

	}

}
