package com.josh.pay.window.show.model;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.josh.pay.constant.Constant;
import com.josh.pay.net.adapter.AeMoneyNetAdapter;
import com.josh.pay.service.AeMoneyService;
import com.josh.pay.window.WindowManager;
import com.josh.pay.window.login.LoginWindow;
import com.josh.pay.window.show.MIShowInterface;
import com.josh.pay.window.show.WIShowInterface;

public class AeShowModel implements WIShowInterface {

	private MIShowInterface modelInterface;

	public AeShowModel(MIShowInterface modelInterface) {
		super();
		this.modelInterface = modelInterface;
	}

	public void quaryOrderResult() {

		if (AeMoneyService.getOrderNo() == null) {

			AeMoneyService.queryCodeInfo(new AeMoneyNetAdapter() {

				public void onSuccess(JSONObject json) {
					try {
						String orderNo = json.getString("orderNo");
						if (orderNo == null) {
							modelInterface.quaryOrderResult(Constant.NET_ERROR);
							return;
						}
						AeMoneyService.setOrderNo(orderNo);
						quaryOrderDetail();
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						modelInterface.quaryOrderResult(Constant.QUARY_OUTTIME);
					}

				}

				public void onFail(String message) {

					modelInterface.quaryOrderResult(Constant.NET_ERROR);

				}
			});

		} else {

			quaryOrderDetail();

		}
	}
	
	private void quaryOrderDetail(){
		new Thread(new Runnable() {

			public void run() {
				AeMoneyService.queryBpOrderDetail(new AeMoneyNetAdapter() {

					public void onSuccess(JSONObject json) {

						try {
							String string = json.getString("tradeState");

							if (string.equals("trade_success")) {
								modelInterface.quaryOrderResult(Constant.PAY_SUCCESS);
							} else if (string.equals("trade_failed") || string.equals("trade_closed")) {
								modelInterface.quaryOrderResult(Constant.PAY_FAIL);
							} else if (string.equals("pending")) {
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
		}).start();
	}

}
