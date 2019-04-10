package com.josh.pay.window.waiting;

import org.josh.pay.item.ImageItem;

import com.josh.pay.AEImage;
import com.josh.pay.constant.Constant;
import com.josh.pay.window.ContentWindow;
import com.josh.pay.window.Window;

public class WaitingWindow extends ContentWindow {

	private ImageItem image;
	private Window nextWindowFail;
	private Window nextWindowOK;

	private static ImageItem defaultImage = new ImageItem(Constant.ID_WAITING_ITEM,
			new AEImage("waiting_def.jpg").getImage());
	public static int EVENT_CODE_OK = 0;
	public static int EVENT_CODE_FAIL = 1;
	public static int EVENT_CODE_USER = 100;

	public WaitingWindow(int id, ImageItem img) {
		super(id);
		if (img != null) {
			image = img;
		} else {
			image = defaultImage;
		}
		nextWindowFail = nextWindowOK = null;
	}

	public void init() {
		addItem(image);
	}

	public void deinit() {
		// TODO Auto-generated method stub

	}

	public void notifyWaitingEventReceived(int code) {
		if (code == EVENT_CODE_OK) {
			onOK();
		} else if (code == EVENT_CODE_FAIL) {
			onFail();
		}
	}

	public void setNextWindowFail(Window nextWindowFail) {
		this.nextWindowFail = nextWindowFail;
	}

	public void setNextWindowOK(Window nextWindowOK) {
		this.nextWindowOK = nextWindowOK;
	}

	protected void onFail() {
		if (nextWindowFail != null) {
			getWindowManager().setCurrent(nextWindowFail);
		}
	}

	protected void onOK() {
		if (nextWindowOK != null) {
			getWindowManager().setCurrent(nextWindowOK);
		}
	}

}
