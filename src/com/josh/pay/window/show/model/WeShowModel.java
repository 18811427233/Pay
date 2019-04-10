package com.josh.pay.window.show.model;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.josh.pay.constant.Constant;
import com.josh.pay.net.adapter.PayNetAdapter;
import com.josh.pay.service.WeChatPayService;
import com.josh.pay.window.show.MIShowInterface;
import com.josh.pay.window.show.WIShowInterface;

public class WeShowModel implements WIShowInterface {

	private MIShowInterface modelInterface;

	public WeShowModel(MIShowInterface modelInterface) {
		super();
		this.modelInterface = modelInterface;
	}

	public void quaryOrderResult() {

		WeChatPayService.queryOrder(new PayNetAdapter() {

			public void onSuccess(JSONObject json) {
				try {
					int state = json.getInt("state");
					if (state == 2) {
						modelInterface.quaryOrderResult(Constant.PAY_SUCCESS);
					} else if (state == -1) {
						modelInterface.quaryOrderResult(Constant.PAY_FAIL);
					} else if (state == 1) {
						modelInterface.quaryOrderResult(Constant.QUARY_OUTTIME);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					modelInterface.quaryOrderResult(Constant.NET_ERROR);
				}

			}

			public void onFail(String message) {
				modelInterface.quaryOrderResult(Constant.NET_ERROR);
			}
		});
	}

}
