package com.josh.pay.net.manager;

import java.io.IOException;

import org.json.me.JSONObject;

import com.josh.pay.net.NetCallback;
import com.josh.pay.net.NetSession;

public class AliNetManager extends NetManager {

	private static final String NET_HOST = "47.93.250.223:9000";

	private NetSession netSession;

	private String subject;
	private String appKey;
	private String deviceInfo;

	public AliNetManager(String subject, String appKey, String deviceInfo) {
		super();

		this.subject = subject;
		this.appKey = appKey;
		this.deviceInfo = deviceInfo;

		netSession = new NetSession(NetManager.CONTENT_TYPE_JSON);
		netSession.connect(NET_HOST);
	}

	protected void getQRCode(String price, String orderNumber, NetCallback netCallback) {

		try {

			JSONObject contentJson = new JSONObject();
			contentJson.put("appKey", appKey);
			contentJson.put("subject", subject);
			contentJson.put("price", (int) (Double.parseDouble(price) * 100));
			contentJson.put("imei", deviceInfo);
			contentJson.put("sign", "");

			netSession.send("/api/pay/order/alipay/qrcode/get", contentJson.toString(), true, netCallback);

		} catch (Exception e) {

			e.printStackTrace();
			netCallback.onError();
		}
	}

	protected void scanQR(String price, String qrCode, String orderNumber, NetCallback netCallback) {

		try {

			JSONObject contentJson = new JSONObject();
			contentJson.put("appKey", appKey);
			contentJson.put("subject", subject);
			contentJson.put("price", (int) (Double.parseDouble(price) * 100));
			contentJson.put("authCode", qrCode);
			contentJson.put("imei", deviceInfo);
			contentJson.put("sign", "");

			netSession.send("/api/pay/order/alipay/qrcode/scan", contentJson.toString(), true, netCallback);

		} catch (Exception e) {

			e.printStackTrace();
			netCallback.onError();
		}
	}

	protected void queryOrder(String orderNumber, NetCallback netCallback) {

		try {

			JSONObject contentJson = new JSONObject();
			contentJson.put("appKey", appKey);
			contentJson.put("imei", deviceInfo);
			contentJson.put("orderNumber", orderNumber);
			contentJson.put("sign", "");

			netSession.send("/api/pay/order/alipay/query", contentJson.toString(), true, netCallback);

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

	protected void stopQueryOrder() {

		try {
			netSession.disConnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected boolean isNetWorkPause() {

		return netSession.isPause();
	}

}
