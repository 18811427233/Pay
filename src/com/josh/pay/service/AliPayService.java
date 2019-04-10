package com.josh.pay.service;

import org.json.me.JSONObject;
import org.json.me.util.DeviceUtil;

import com.josh.pay.net.adapter.PayNetAdapter;
import com.josh.pay.net.manager.AliNetManager;
import com.josh.pay.net.manager.NetManager;

public class AliPayService {

	private static final String SUBJECT = "JOSH Ali Pay";
	private static final String KEY = "312314694b084b43873dd1285024aec5";

	private static String money = null;
	private static String orderNo = null;

	private static AliNetManager aliNetManager;

	public static void initNetManager() {

		if (aliNetManager == null) {
			aliNetManager = new AliNetManager(SUBJECT, KEY, DeviceUtil.getDeviceInfo());
		}
	}

	public static void register() {

		NetManager.register(aliNetManager);
	}

	public static void startNetWork() {

		NetManager.startNetWork();
	}

	/**
	 * 阿里支付请求二维码
	 * 
	 * @return
	 */
	public static void requestQr(String money, PayNetAdapter adapter) {

		NetManager.reqQRCode(money, orderNo, adapter);

	}

	/**
	 * 停止订单查询
	 */
	public static void stopQueryOrder() {
		NetManager.stopReqQueryOrder();
	}

	/**
	 * 阿里支付查询订单
	 * 
	 * @return
	 */
	public static void queryOrder(PayNetAdapter payNetAdapter) {

		if (orderNo == null) {
			payNetAdapter.onFail("订单号异常");
			return;
		}

		NetManager.reqQueryOrder(orderNo, payNetAdapter);

	}

	/**
	 * 阿里扫码支付
	 * 
	 * @param code
	 * @return
	 */
	public static void requestScan(String code, PayNetAdapter adapter) {

		if (code.length() < 16 || code.length() > 24
				|| (!code.startsWith("25") && !code.startsWith("26") && !code.startsWith("27") && !code.startsWith("28")
						&& !code.startsWith("29") && !code.startsWith("30"))) {
			adapter.onFail("二维码错误");
			return;

		}

		NetManager.reqScanQR(money, code, orderNo, adapter);

	}

	public static void setAmount(String money) {
		AliPayService.money = money;
	}

	public static void setOrderNo(String orderNo) {
		AliPayService.orderNo = orderNo;
	}

}
