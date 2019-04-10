package com.josh.pay.net.adapter;

import java.io.UnsupportedEncodingException;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.josh.pay.constant.Constant;
import com.josh.pay.net.NetCallback;
import com.josh.pay.service.AeMoneyService;
import com.josh.pay.window.WindowManager;
import com.josh.pay.window.login.LoginWindow;

public abstract class AeMoneyNetAdapter implements NetCallback {

	public abstract void onSuccess(JSONObject json);

	public abstract void onFail(String message);

	public void onProcess(byte[] data) {

		try {
			JSONObject json = new JSONObject(new String(data, "utf-8"));
			String retCode = (String) json.get("retCode");
			String retMsg = (String) json.get("retMsg");

			if (retCode != null && retCode.equals("10000")) {
				onSuccess(json);
			} else if (retCode != null && retCode.equals("96200")) {

				AeMoneyService.setSessionId(null);
				LoginWindow w = (LoginWindow) WindowManager.createWindowManager().getWindow(Constant.WIN_ID_EMPLOYEEID);
				w.setMsg("登录异常请重新登录");
				WindowManager.createWindowManager().setCurrent(Constant.WIN_ID_EMPLOYEEID);

			} else {

				onFail(retMsg);
			}

		} catch (JSONException e) {
			onFail(Constant.NETERROR_MESSAGE);
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			onFail(Constant.NETERROR_MESSAGE);
			e.printStackTrace();
		}

	}

	public void onError() {

		onFail(Constant.NETERROR_MESSAGE);
	}

}
