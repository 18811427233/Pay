package com.josh.pay.window.login;

import org.josh.pay.item.ImageItem;
import org.josh.pay.item.NumericInputItem;
import org.josh.pay.item.PowerImageItem;
import org.josh.pay.item.SignImageItem;
import org.josh.pay.item.TextItem;
import org.joshvm.async.AsyncTask;
import org.joshvm.async.AsyncTaskBody;
import org.joshvm.j2me.directUI.Keypad;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.json.me.util.DeviceUtil;

import com.josh.pay.AEImage;
import com.josh.pay.JoshApplication;
import com.josh.pay.constant.Constant;
import com.josh.pay.net.adapter.AeMoneyNetAdapter;
import com.josh.pay.net.manager.NetManager;
import com.josh.pay.service.AeMoneyService;
import com.josh.pay.window.ContentWindow;
import com.josh.pay.window.Event;

public class LoginWindow extends ContentWindow implements AsyncTaskBody {

	private PowerImageItem imgPower;
	private ImageItem imgCharging;
	private SignImageItem imgSign;

	private ImageItem imgAeMoneyLogo;

	private NumericInputItem employeeID;
	private NumericInputItem password;
	private ImageItem imgEmployeeID;
	private ImageItem imgPassword;
	private ImageItem imgArrowEmployeeId;
	private ImageItem imgArrowPassword;
	private TextItem textItem;

	private long exitTime = 0;
	private AsyncTask asyncTask;
	private boolean isRefresh = true;

	public LoginWindow(int id) {
		super(id);

		imgPower = new PowerImageItem(Constant.ID_LOGING_ITEM, new AEImage("power_one.jpg").getImage());
		imgPower.setPosition(210, 5);
		imgCharging = new ImageItem(Constant.ID_LOGING_ITEM, new AEImage("charging.jpg").getImage());
		imgCharging.setPosition(203, 5);
		imgSign = new SignImageItem(Constant.ID_LOGING_ITEM, new AEImage("sign.jpg").getImage());
		imgSign.setPosition(182, 5);

		imgAeMoneyLogo = new ImageItem(Constant.ID_LOGING_ITEM, new AEImage("daqian.jpg").getImage());
		imgAeMoneyLogo.setPosition(0, 20);

		imgEmployeeID = new ImageItem(Constant.ID_LOGING_ITEM, new AEImage("empid.jpg").getImage());
		imgEmployeeID.setPosition(0, 70);

		employeeID = new NumericInputItem(Constant.ID_LOGING_ITEM, "");
		employeeID.setPosition(30, 120);
		employeeID.setAcceptDot(false);

		imgPassword = new ImageItem(Constant.ID_LOGING_ITEM, new AEImage("passwd.jpg").getImage());
		imgPassword.setPosition(0, 170);

		password = new NumericInputItem(Constant.ID_LOGING_ITEM, "");
		password.setPosition(30, 220);
		password.setAcceptDot(false);

		imgArrowEmployeeId = new ImageItem(Constant.ID_LOGING_ITEM, new AEImage("arrow.jpg").getImage());
		imgArrowEmployeeId.setPosition(5, 120);

		imgArrowPassword = new ImageItem(Constant.ID_LOGING_ITEM, new AEImage("arrow_clear.jpg").getImage());
		imgArrowPassword.setPosition(5, 220);

		textItem = new TextItem(Constant.ID_LOGING_ITEM, "", false);
		textItem.setPosition(10, 280);
	}

	public void init() {
		addItem(imgPower);
		addItem(imgCharging);
		addItem(imgSign);
		addItem(imgAeMoneyLogo);
		addItem(employeeID);
		addItem(imgEmployeeID);
		addItem(password);
		addItem(imgPassword);
		addItem(imgArrowEmployeeId);
		addItem(imgArrowPassword);
		addItem(textItem);
		setFocus(employeeID);

		asyncTask = new AsyncTask();
		asyncTask.setCallback(this);

	}

	public void setMsg(String string) {
		textItem.setText(string);
	}

	public void deinit() {
		// TODO Auto-generated method stub

	}

	public void show() {
		// TODO Auto-generated method stub
		super.show();

		isRefresh = true;
		asyncTask.trig(null);
		AeMoneyService.initNetManager();
		AeMoneyService.register();
		AeMoneyService.startNetWork();
	}

	public boolean event(Event evt) {
		String employeeId = employeeID.getText().trim();
		String pwd = password.getText().trim();

		if ((evt.type == Event.KEY_PRESSED)) {

			switch (evt.value) {

			case Keypad.KEY_OK:
				// function for key_func4
			case Keypad.KEY_FUNC4:

				if (getFocus() == employeeID) {

					if (employeeId.length() == 0) {

						textItem.setText("请输入员工号");
						textItem.show();
						return false;
					}

					setFocus(password);
					setArrowPosition(false, true);
				} else {

					if (pwd.length() == 0) {

						textItem.setText("请输入密码");
						textItem.show();
						return false;
					}

					setArrowPosition(true, false);
					isRefresh = false;
					showWaiting();

					AeMoneyService.requestEmployeeLogin(employeeId, pwd, new AeMoneyNetAdapter() {

						public void onSuccess(JSONObject json) {

							try {
								String sessionId = (String) json.get("sessionId");
								if (sessionId != null) {
									AeMoneyService.setSessionId(sessionId);
									employeeID.clear();
									password.clear();
									setFocus(employeeID);
									textItem.setText("");
									getWindowManager().setCurrent(Constant.WIN_ID_HOME);
								}

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

						public void onFail(String message) {
							employeeID.clear();
							password.clear();
							setFocus(employeeID);
							setMsg(message);
							getWindowManager().setCurrent(Constant.WIN_ID_EMPLOYEEID);

						}
					});
				}
				return true;

			case Keypad.KEY_BACK:

				if (getFocus() == password && pwd.length() == 0) {
					setFocus(employeeID);
					setArrowPosition(true, true);

					return true;
				} else {
					return super.event(evt);
				}
			case Keypad.KEY_FUNC1:
				if (JoshApplication.AppId == JoshApplication.AeMoney) {
					textItem.setText("");
					employeeID.clear();
					password.clear();
					setFocus(employeeID);
					setArrowPosition(true, true);
					isRefresh = false;
					textItem.show();
					employeeID.show();
					password.show();
					return true;
				}

				if ((System.currentTimeMillis() - exitTime) > 2000) {
					exitTime = System.currentTimeMillis();
					textItem.setText("再次点击返回首页");
					textItem.show();

				} else {
					textItem.setText("");
					employeeID.clear();
					password.clear();
					setFocus(employeeID);
					setArrowPosition(true, false);
					exitTime = 0;
					isRefresh = false;
					NetManager.unRegister();
					getWindowManager().setCurrent(Constant.WIN_ID_MENU);
				}

				return true;
			case Keypad.KEY_USER2:

				if (getFocus() == employeeID && employeeId.length() > 0) {
					employeeID.clear();
					employeeID.show();

				} else if (getFocus() == password) {

					if (pwd.length() > 0) {
						password.clear();
						password.show();
					} else {
						setFocus(employeeID);
						setArrowPosition(true, true);
					}
				}
				textItem.setText("");
				textItem.show();

				return true;

			default:
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
								;
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

	/**
	 * 设置焦点箭头位置
	 * 
	 * @param isEmployeeId
	 */
	private void setArrowPosition(boolean isEmployeeId, boolean show) {
		if (isEmployeeId) {
			imgArrowEmployeeId.setImg(new AEImage("arrow.jpg").getImage());
			imgArrowPassword.setImg(new AEImage("arrow_clear.jpg").getImage());
		} else {
			imgArrowEmployeeId.setImg(new AEImage("arrow_clear.jpg").getImage());
			imgArrowPassword.setImg(new AEImage("arrow.jpg").getImage());
		}

		if (show) {
			imgArrowEmployeeId.show();
			imgArrowPassword.show();
		}

	}
}
