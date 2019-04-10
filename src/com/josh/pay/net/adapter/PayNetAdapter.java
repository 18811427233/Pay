package com.josh.pay.net.adapter;

import java.io.UnsupportedEncodingException;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.josh.pay.constant.Constant;
import com.josh.pay.net.NetCallback;

public abstract class PayNetAdapter implements NetCallback {

	public PayNetAdapter() {
		super();
	}

	public abstract void onSuccess(JSONObject json);

	public abstract void onFail(String message);

	public void onProcess(byte[] data) {

		try {

			JSONObject json = new JSONObject(new String(data, "utf-8"));

			int errorNumber = json.getInt("errorNumber");
			if (errorNumber == 0) {

				JSONObject resp = json.getJSONObject("result");
				onSuccess(resp);

			} else {

				onFail("收款失败");
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
