package com.josh.pay.window.home;

import org.josh.pay.item.ImageItem;
import org.josh.pay.item.PowerImageItem;
import org.josh.pay.item.SignImageItem;
import org.joshvm.async.AsyncTask;
import org.joshvm.async.AsyncTaskBody;
import org.joshvm.j2me.directUI.Image;
import org.joshvm.j2me.directUI.Keypad;
import org.json.me.util.DeviceUtil;

import com.josh.pay.AEImage;
import com.josh.pay.constant.Constant;
import com.josh.pay.service.AliPayService;
import com.josh.pay.service.WeChatPayService;
import com.josh.pay.window.ContentWindow;
import com.josh.pay.window.Event;
import com.josh.pay.window.inputprice.InputPriceWindow;
import com.josh.pay.window.inputprice.WIIputPriceInterface;
import com.josh.pay.window.inputprice.model.AliInputPriceModel;
import com.josh.pay.window.inputprice.model.WeInputPriceModel;

public class MenuWindow extends ContentWindow implements AsyncTaskBody {

	private ImageItem imageItem;
	private PowerImageItem imgPower;
	private ImageItem imgCharging;
	private SignImageItem imgSign;

	public static final int DAQIAN = 1;
	public static final int WEIXIN = 2;
	public static final int ZHIFUBAO = 3;

	public static int type = 0;
	private AsyncTask asyncTask;
	private boolean isRefresh = true;

	private Image imageWe;
	private Image imageAli;
	private boolean canBack = false;

	public MenuWindow(int id) {
		super(id);

		imageItem = new ImageItem(Constant.ID_MENU_ITEM, new AEImage("pay_home.jpg").getImage());
		imageItem.setPosition(0, 20);
		imgPower = new PowerImageItem(Constant.ID_MENU_ITEM, new AEImage("power_one.jpg").getImage());
		imgPower.setPosition(210, 5);
		imgCharging = new ImageItem(Constant.ID_MENU_ITEM, new AEImage("charging.jpg").getImage());
		imgCharging.setPosition(203, 5);
		imgSign = new SignImageItem(Constant.ID_MENU_ITEM, new AEImage("sign.jpg").getImage());
		imgSign.setPosition(182, 5);
		imageWe = new AEImage("weixin.jpg").getImage();
		imageAli = new AEImage("zhifubao.jpg").getImage();
	}

	public void init() {
		addItem(imageItem);
		addItem(imgPower);
		addItem(imgCharging);
		addItem(imgSign);

		asyncTask = new AsyncTask();
		asyncTask.setCallback(this);

	}

	public void show() {
		super.show();

		isRefresh = true;
		asyncTask.trig(null);
		WeChatPayService.initNetManager();
		AliPayService.initNetManager();

		canBack = true;
	}

	public void deinit() {

	}

	public boolean event(Event evt) {

		if (evt.type == Event.KEY_PRESSED) {
			if (canBack) {

				InputPriceWindow w = (InputPriceWindow) window_manager.getWindow(Constant.WIN_ID_INPUTPRICE);

					switch (evt.value) {
					case Keypad.KEY_NUM1:
						type = DAQIAN;
						isRefresh = false;
						getWindowManager().setCurrent(Constant.WIN_ID_EMPLOYEEID);
						return false;

					case Keypad.KEY_NUM2:
						type = WEIXIN;
						isRefresh = false;
						WeChatPayService.register();
						WeChatPayService.startNetWork();
						WIIputPriceInterface windowInterfaceWe = new WeInputPriceModel(w, imageWe);
						w.registerModel(windowInterfaceWe);
						getWindowManager().setCurrent(w);
						return false;

					case Keypad.KEY_NUM3:
						type = ZHIFUBAO;
						isRefresh = false;
						AliPayService.register();
						AliPayService.startNetWork();
						WIIputPriceInterface windowInterfaceAli = new AliInputPriceModel(w, imageAli);
						w.registerModel(windowInterfaceAli);
						getWindowManager().setCurrent(w);
						return false;
					default:
						return super.event(evt);
					}
			} else {
				return super.event(evt);
			}
		} else {
			return super.event(evt);
		}

	}

	public void onAsyncTaskStart(Object param) {

		int number = 0;

		String s = "";

		while (isRefresh) {

			if (isRefresh) {

				if ((number == 1) || (number % 5 == 0)) {

					boolean hasSim = DeviceUtil.getDeviceImsi();

					if (hasSim) {
						if ((number == 1) || (number % 30 == 0)) {
							int sign = DeviceUtil.getDeviceSign();
							if (isRefresh) {
								if (imgSign.isChange(sign)) {
									imgSign.showSign(sign);
									if (isRefresh) {
										imgSign.show();
									}
								}
							}
						}

					} else {
						if (isRefresh) {
							imgSign.showSign(-1);

							if (isRefresh) {
								imgSign.show();
							}
						}

					}
				}

				if ((number == 0) || (number % 3 == 0)) {
					String isCharging = System.getProperty("battery.ischarging");
					if (isRefresh) {
						if (!s.equals(isCharging)) {
							s = isCharging;
							if (isCharging.equals("1")) {
								imgCharging.setImg(new AEImage("charging.jpg").getImage());
							} else {
								imgCharging.setImg(new AEImage("charging_clear.jpg").getImage());
							}

							if (isRefresh) {
								imgCharging.show();
							}
						}
					}
				}

				if (number == 0) {
					int currentPower = Integer.parseInt(System.getProperty("battery.level"));
					if (isRefresh) {
						if (currentPower != imgPower.getNumber()) {
							imgPower.showPower(currentPower);
							if (isRefresh) {
								imgPower.show();
							}
						}
					}
				}
			}

			number++;
			if (number > 60) {
				number = 0;
			}

			try {
				Thread.sleep(1 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
