package com.josh.pay.net.manager;

import java.io.IOException;

import org.json.me.JSONObject;

import com.josh.pay.net.NetCallback;
import com.josh.pay.net.NetSession;
import com.josh.pay.net.NetUtils;

public class AeMoneyNetManager extends NetManager {

	private String sessionId = "";
	private String payType = "";
	private String deviceInfo = "";

	private NetSession netSession;

	private static final String AE_MONEY_HOST = "t.aemoney.com:443";

	public AeMoneyNetManager(String deviceInfo) {
		super();
		this.deviceInfo = deviceInfo;

		netSession = new NetSession(NetManager.CONTENT_TYPE_JSON);
		netSession.connect(AE_MONEY_HOST);
	}

	/**
	 * 登录
	 * 
	 * @param userName
	 * @param password
	 * @param netCallback
	 */
	public void Login(String userName, String password, NetCallback netCallback) {

		try {

			JSONObject req = new JSONObject();
			req.put("timestamp", NetUtils.getTimeStamp());
			req.put("deviceInfo", deviceInfo);
			req.put("logName", userName);
			req.put("logPwd", password);

			netSession.send("/aemoney_beta/merchant/empLogin.htm", req.toString(), true, netCallback);

		} catch (Exception e) {

			e.printStackTrace();
			netCallback.onError();
		}
	}

	protected void getQRCode(String price, String orderNumber, NetCallback netCallback) {

		try {

			JSONObject req = new JSONObject();
			req.put("timestamp", NetUtils.getTimeStamp());
			req.put("deviceInfo", deviceInfo);
			req.put("sessionId", sessionId);
			req.put("currency", "CNY");
			req.put("amount", price);

			netSession.send("/aemoney_beta/pay/passiveGathering.htm", req.toString(), true, netCallback);

		} catch (Exception e) {

			e.printStackTrace();
			netCallback.onError();
		}
	}

	/**
	 * 创建订单（用于扫码付款）
	 * 
	 * @param price
	 * @param netCallback
	 */
	public void createOrder(String price, NetCallback netCallback) {

		try {

			JSONObject req = new JSONObject();
			req.put("timestamp", NetUtils.getTimeStamp());
			req.put("deviceInfo", deviceInfo);
			req.put("sessionId", sessionId);
			req.put("currency", "CNY");
			req.put("amount", price);

			netSession.send("/aemoney_beta/order/merchantCreateOrder.htm", req.toString(), true, netCallback);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	protected void scanQR(String price, String qrCode, String orderNumber, NetCallback netCallback) {
		try {

			JSONObject req = new JSONObject();
			req = new JSONObject();
			req.put("timestamp", NetUtils.getTimeStamp());
			req.put("deviceInfo", deviceInfo);
			req.put("sessionId", sessionId);
			req.put("orderNo", orderNumber);
			req.put("payCode", qrCode);
			req.put("payType", payType);

			netSession.send("/aemoney_beta/pay/activeGathering.htm", req.toString(), true, netCallback);

		} catch (Exception e) {

			e.printStackTrace();
			netCallback.onError();
		}
	}

	/**
	 * 获取二维码的状态（用于获取二维码是否绑定订单）
	 * 
	 * @param code
	 * @param netCallback
	 */
	public void queryCodeInfo(String code, NetCallback netCallback) {

		try {

			JSONObject req = new JSONObject();
			req.put("timestamp", NetUtils.getTimeStamp());
			req.put("deviceInfo", deviceInfo);
			req.put("sessionId", sessionId);
			req.put("code", code);
			req.put("codeType", "gatherCode");

			netSession.send("/aemoney_beta/pay/queryCodeInfo.htm", req.toString(), false, netCallback);

		} catch (Exception e) {

			e.printStackTrace();
			netCallback.onError();
		}
	}

	protected void queryOrder(String orderNumber, NetCallback netCallback) {

		try {

			JSONObject req = new JSONObject();
			req.put("timestamp", NetUtils.getTimeStamp());
			req.put("deviceInfo", deviceInfo);
			req.put("orderNo", orderNumber);

			netSession.send("/aemoney_beta/order/queryBpOrderDetail.htm", req.toString(), false, netCallback);

		} catch (Exception e) {

			e.printStackTrace();
			netCallback.onError();
		}
	}

	protected void destoryNetwork() {

		try {

			netSession.destory();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void startNetwork() {

		netSession.start();
	}

	protected void pauseNetwork() {

		try {

			netSession.pause();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	protected boolean isNetWorkPause() {

		return netSession.isPause();
	}

	protected void stopQueryOrder() {

	}

}
