package com.josh.pay.service;

import org.json.me.util.DeviceUtil;
import org.json.me.util.IllegalServiceStateException;

import com.josh.pay.net.adapter.AeMoneyNetAdapter;
import com.josh.pay.net.manager.AeMoneyNetManager;
import com.josh.pay.net.manager.NetManager;

public class AeMoneyService {

	public static String sessionId = null;
	private static String orderNo = null;
	private static String payment_url = null;
	private static String payamount = null;
	private static AeMoneyNetManager aeMoneyNetManager;

	public static void initNetManager() {

		if (aeMoneyNetManager == null) {
			aeMoneyNetManager = new AeMoneyNetManager(DeviceUtil.getDeviceInfo());
		}
	}

	public static void register() {

		NetManager.register(aeMoneyNetManager);
	}

	public static void startNetWork() {

		NetManager.startNetWork();

	}

	public static void requestQRShowPayment(String price, final AeMoneyNetAdapter adapter) {

		orderNo = null;
		payment_url = null;

		NetManager.reqQRCode(price, orderNo, adapter);

	}

	public static void requestCreateOrder(AeMoneyNetAdapter adapter) {

		aeMoneyNetManager.createOrder(payamount, adapter);

	}

	public static void requestQRCodeScanPayment(String payCode, final AeMoneyNetAdapter adapter) {
		String payType = getPayType(payCode);
		if (payType == null) {
			adapter.onFail("二维码错误"); // Illegal QRCode;
			return;
		}
		aeMoneyNetManager.setPayType(payType);
		NetManager.reqScanQR(payamount, payCode, orderNo, adapter);

	}

	private static String getPayType(String payCode) {
		String payType = null;

		if (payCode.startsWith("4201")) {
			payType = "aemoney";
		} else if (payCode.startsWith("28")) {
			payType = "alipay";
		} else if (payCode.startsWith("13")) {
			payType = "wechat";
		}
		return payType;
	}

	public static void requestEmployeeLogin(String logName, String logPwd, final AeMoneyNetAdapter adapter) {

		aeMoneyNetManager.Login(logName, logPwd, adapter);

	}

	public static void setSessionId(String sessionId) {
		AeMoneyService.sessionId = sessionId;
		aeMoneyNetManager.setSessionId(sessionId);
	}

	/**
	 * 查询二维码状态
	 * 
	 * @return
	 */
	public static void queryCodeInfo(AeMoneyNetAdapter adapter) {

		String code = "";
		int start = payment_url.indexOf("gatherCode=");
		if (start > 0) {

			code = payment_url.substring(start + 11, payment_url.length());
		}

		aeMoneyNetManager.queryCodeInfo(code, adapter);

	}

	/**
	 * 查询订单信息
	 * 
	 * @return
	 */
	public static void queryBpOrderDetail(AeMoneyNetAdapter adapter) {

		NetManager.reqQueryOrder(orderNo, adapter);

	}

	public static synchronized void setAmount(String amount) {
		payamount = amount;
	}

	public static void setPayment_url(String payment_url) {
		orderNo = null;
		AeMoneyService.payment_url = payment_url;
	}

	public static String getOrderNo() {
		return orderNo;
	}

	public static void setOrderNo(String orderNo) {
		AeMoneyService.orderNo = orderNo;
	}

}
