package com.josh.pay.window.scanresult.model;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.josh.pay.constant.Constant;
import com.josh.pay.net.adapter.PayNetAdapter;
import com.josh.pay.service.AliPayService;
import com.josh.pay.window.scanresult.MIScanResultInterface;
import com.josh.pay.window.scanresult.WIScanResultInterface;

public class AliScanResultModel implements WIScanResultInterface {

	private MIScanResultInterface modelInterface;

	public AliScanResultModel(MIScanResultInterface modelInterface) {
		super();
		this.modelInterface = modelInterface;
	}

	public void scan(String code) {

		AliPayService.requestScan(code, new PayNetAdapter() {

			public void onSuccess(JSONObject json) {

				try {
					int state = json.getInt("state");
					String orderNumber = json.getString("orderNumber");
					AliPayService.setOrderNo(orderNumber);

					if (state == 2) {
						modelInterface.quaryOrderResult(Constant.PAY_SUCCESS);
					} else if (state == -1) {
						modelInterface.quaryOrderResult(Constant.PAY_FAIL);
					} else if (state == 1) {
						modelInterface.scanQr();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					modelInterface.errorMessage(Constant.NETERROR_MESSAGE);
				}

			}

			public void onFail(String message) {
				modelInterface.errorMessage(message);

			}
		});
	}

	public void quaryOrderResult() {

		AliPayService.queryOrder(new PayNetAdapter() {

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