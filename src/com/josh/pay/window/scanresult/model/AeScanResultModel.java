package com.josh.pay.window.scanresult.model;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.josh.pay.constant.Constant;
import com.josh.pay.net.adapter.AeMoneyNetAdapter;
import com.josh.pay.service.AeMoneyService;
import com.josh.pay.window.scanresult.MIScanResultInterface;
import com.josh.pay.window.scanresult.WIScanResultInterface;

public class AeScanResultModel implements WIScanResultInterface {

	private MIScanResultInterface modelInterface;

	public AeScanResultModel(MIScanResultInterface modelInterface) {
		super();
		this.modelInterface = modelInterface;
	}

	public void scan(final String code) {

		AeMoneyService.requestCreateOrder(new AeMoneyNetAdapter() {

			public void onSuccess(JSONObject json) {
				// TODO Auto-generated method stub
				String orderNo = null;
				try {
					orderNo = (String) json.get("orderNo");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					modelInterface.errorMessage(Constant.NETERROR_MESSAGE);
				}
				if (orderNo == null) {
					modelInterface.errorMessage("订单号异常");
					return;

				}
				AeMoneyService.setOrderNo(orderNo);

				new Thread(new Runnable() {
					public void run() {
						AeMoneyService.requestQRCodeScanPayment(code, new AeMoneyNetAdapter() {

							public void onSuccess(JSONObject json) {
								// TODO Auto-generated method stub
								modelInterface.scanQr();
							}

							public void onFail(String message) {
								modelInterface.errorMessage(message);

							}
						});

					}
				}).start();

			}

			public void onFail(String message) {
				modelInterface.errorMessage(message);

			}
		});

	}

	public void quaryOrderResult() {

		AeMoneyService.queryBpOrderDetail(new AeMoneyNetAdapter() {

			public void onSuccess(JSONObject json) {

				try {
					String string = json.getString("tradeState");

					if (string.equals("trade_success")) {
						modelInterface.quaryOrderResult(Constant.PAY_SUCCESS);
					} else if (string.equals("trade_failed") || string.equals("trade_closed")) {
						modelInterface.quaryOrderResult(Constant.PAY_FAIL);
					} else {
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
