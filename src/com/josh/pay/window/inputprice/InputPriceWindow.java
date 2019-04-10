package com.josh.pay.window.inputprice;

import org.josh.pay.item.ImageItem;
import org.josh.pay.item.NumericInputItem;
import org.josh.pay.item.PowerImageItem;
import org.josh.pay.item.SignImageItem;
import org.josh.pay.item.TextItem;
import org.joshvm.async.AsyncTask;
import org.joshvm.async.AsyncTaskBody;
import org.joshvm.j2me.directUI.Keypad;
import org.json.me.util.DeviceUtil;

import com.josh.pay.AEImage;
import com.josh.pay.JoshApplication;
import com.josh.pay.constant.Constant;
import com.josh.pay.net.manager.NetManager;
import com.josh.pay.service.AeMoneyService;
import com.josh.pay.service.AliPayService;
import com.josh.pay.service.WeChatPayService;
import com.josh.pay.window.ContentWindow;
import com.josh.pay.window.Event;
import com.josh.pay.window.home.MenuWindow;
import com.josh.pay.window.scanprep.QRCodeScanPrepWindow;
import com.josh.pay.window.show.QRCodeDispWindow;
import com.josh.pay.window.show.WIShowInterface;
import com.josh.pay.window.show.model.AeShowModel;
import com.josh.pay.window.show.model.AliShowModel;
import com.josh.pay.window.show.model.WeShowModel;

/**
 * 支付demo输入金额界面
 * 
 * @author Administrator
 *
 */
public class InputPriceWindow extends ContentWindow implements MIIputpriceInterface, AsyncTaskBody {

	private WIIputPriceInterface windowInterface;

	private ImageItem imgItemTitleOne;
	private NumericInputItem numTextItem;
	private ImageItem imgItem;
	private TextItem textItem;

	private ImageItem imgMoneyItemBefore;
	private ImageItem imgMoneyItemAfter;

	private PowerImageItem imgPower;
	private ImageItem imgCharging;
	private SignImageItem imgSign;

	private long exitTime = 0;
	private String strInputNumber;
	private AsyncTask asyncTask;
	private boolean isRefresh = true;

	public InputPriceWindow(int id) {
		super(id);

		imgPower = new PowerImageItem(Constant.ID_INPUT_PRICE_ITEM, new AEImage("power_one.jpg").getImage());
		imgPower.setPosition(210, 5);
		imgCharging = new ImageItem(Constant.ID_INPUT_PRICE_ITEM, new AEImage("charging.jpg").getImage());
		imgCharging.setPosition(203, 5);
		imgSign = new SignImageItem(Constant.ID_INPUT_PRICE_ITEM, new AEImage("sign.jpg").getImage());
		imgSign.setPosition(182, 5);

		numTextItem = new NumericInputItem(Constant.ID_INPUT_PRICE_ITEM, "");
		numTextItem.setPosition(35, 150);
		imgItem = new ImageItem(Constant.ID_INPUT_PRICE_ITEM, new AEImage("input.jpg").getImage());
		imgItem.setPosition(0, 70);
		textItem = new TextItem(Constant.ID_INPUT_PRICE_ITEM, "", false);
		textItem.setPosition(10, 280);

		imgMoneyItemBefore = new ImageItem(Constant.ID_INPUT_PRICE_ITEM, new AEImage("money.jpg").getImage());
		imgMoneyItemBefore.setPosition(10, 150);
		imgMoneyItemAfter = new ImageItem(Constant.ID_INPUT_PRICE_ITEM, new AEImage("money_after.jpg").getImage());
		imgMoneyItemAfter.setPosition(205, 150);

		imgItemTitleOne = new ImageItem(Constant.ID_INPUT_PRICE_ITEM, new AEImage("daqian.jpg").getImage());
		imgItemTitleOne.setPosition(0, 20);
	}

	public void init() {

		addItem(numTextItem);
		addItem(imgItem);
		addItem(textItem);
		addItem(imgMoneyItemBefore);
		addItem(imgMoneyItemAfter);
		setFocus(numTextItem);
		numTextItem.setAcceptDot(true);

		addItem(imgPower);
		addItem(imgCharging);
		addItem(imgSign);
		addItem(imgItemTitleOne);

		asyncTask = new AsyncTask();
		asyncTask.setCallback(this);

	}

	public void setMsg(String message) {
		textItem.setText(message);
	}

	public void show() {
		super.show();

		isRefresh = true;
		asyncTask.trig(null);
	}

	public void registerModel(WIIputPriceInterface windowInterface) {

		this.windowInterface = windowInterface;
		this.windowInterface.initView(imgItemTitleOne);
	}

	public void deinit() {

	}

	public boolean event(Event evt) {

		if (evt.type == Event.KEY_PRESSED) {
			strInputNumber = numTextItem.getText().trim();
			switch (evt.value) {
			case Keypad.KEY_FUNC2:
				if (strInputNumber.length() == 0) {

					textItem.setText("请输入金额");
					textItem.show();
					return false;

				}

				if (strInputNumber.equals(".") || strInputNumber.startsWith(".") || strInputNumber.endsWith(".")
						|| Double.parseDouble(strInputNumber) <= 0 || Double.parseDouble(strInputNumber) > 9999999) {

					textItem.setText("金额输入错误");
					textItem.show();
					return false;
				}

				if (strInputNumber.substring(0, 1).equals("0") && !strInputNumber.substring(1, 2).equals(".")) {

					textItem.setText("金额输入错误");
					textItem.show();
					return false;

				}

				numTextItem.clear();
				textItem.setText("");
				isRefresh = false;
				showWaiting();

				switch (MenuWindow.type) {
				case MenuWindow.DAQIAN:
					AeMoneyService.setAmount(strInputNumber);
					break;
				case MenuWindow.WEIXIN:
					WeChatPayService.setAmount(strInputNumber);
					break;
				case MenuWindow.ZHIFUBAO:
					AliPayService.setAmount(strInputNumber);
					break;

				default:
					break;
				}
				windowInterface.getQR(strInputNumber);

				break;

			case Keypad.KEY_FUNC3:

				if (strInputNumber.length() == 0) {

					textItem.setText("请输入金额");
					textItem.show();
					return false;

				}

				if (strInputNumber.equals(".") || strInputNumber.startsWith(".") || strInputNumber.endsWith(".")
						|| Double.parseDouble(strInputNumber) <= 0 || Double.parseDouble(strInputNumber) > 9999999) {

					textItem.setText("金额输入错误");
					textItem.show();
					return false;
				}

				if (strInputNumber.substring(0, 1).equals("0") && !strInputNumber.substring(1, 2).equals(".")) {

					textItem.setText("金额输入错误");
					textItem.show();
					return false;

				}

				switch (MenuWindow.type) {
				case MenuWindow.DAQIAN:
					AeMoneyService.setAmount(strInputNumber);
					break;
				case MenuWindow.WEIXIN:
					WeChatPayService.setAmount(strInputNumber);
					break;
				case MenuWindow.ZHIFUBAO:
					AliPayService.setAmount(strInputNumber);
					break;

				default:
					break;
				}

				QRCodeScanPrepWindow w = (QRCodeScanPrepWindow) window_manager
						.getWindow(Constant.WIN_ID_QRCODESCANPREP);
				w.setMoney(strInputNumber);
				numTextItem.clear();
				isRefresh = false;
				window_manager.setCurrent(Constant.WIN_ID_QRCODESCANPREP);
				textItem.setText("");

				break;

			case Keypad.KEY_USER2:
				numTextItem.clear();
				textItem.setText("");
				numTextItem.show();
				textItem.show();
				break;
			case Keypad.KEY_FUNC1:

				if (JoshApplication.AppId == JoshApplication.AeMoney) {
					numTextItem.clear();
					textItem.setText("");
					numTextItem.show();
					textItem.show();
					return true;
				}

				if ((System.currentTimeMillis() - exitTime) > 2000) {
					exitTime = System.currentTimeMillis();
					textItem.setText("再次点击返回首页");
					textItem.show();

				} else {
					textItem.setText("");
					numTextItem.clear();
					exitTime = 0;
					isRefresh = false;
					NetManager.unRegister();
					getWindowManager().setCurrent(Constant.WIN_ID_MENU);
				}

				break;

			default:
				break;
			}

		}
		return super.event(evt);

	}

	public void errorMessage(String message) {

		textItem.setText(message);
		textItem.show();
		window_manager.setCurrent(Constant.WIN_ID_INPUTPRICE);
	}

	public void showQr(String qrUrl) {

		QRCodeDispWindow w = (QRCodeDispWindow) window_manager.getWindow(Constant.WIN_ID_QRCODEDISP);
		WIShowInterface wiShowInterface = null;

		switch (MenuWindow.type) {
		case MenuWindow.DAQIAN:
			wiShowInterface = new AeShowModel(w);
			break;
		case MenuWindow.WEIXIN:
			wiShowInterface = new WeShowModel(w);
			break;
		case MenuWindow.ZHIFUBAO:
			wiShowInterface = new AliShowModel(w);
			break;

		default:
			break;
		}

		w.registerModel(wiShowInterface);
		w.setQRCode(qrUrl, strInputNumber);
		window_manager.setCurrent(w);

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
