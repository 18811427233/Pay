package com.josh.pay.service;

import org.json.me.util.DeviceUtil;

import com.josh.pay.net.adapter.PayNetAdapter;
import com.josh.pay.net.manager.NetManager;
import com.josh.pay.net.manager.WeChatNetManager;

public class WeChatPayService {

	private static final String SUBJECT = "JOSH WeChat Pay";
	private static final String KEY = "312314694b084b43873dd1285024aec5";
	private static String money = null;
	private static String orderNo = null;

	private static WeChatNetManager weChatNetManager;

	public static void initNetManager() {

		if (weChatNetManager == null) {

			weChatNetManager = new WeChatNetManager(SUBJECT, KEY, DeviceUtil.getDeviceInfo());
		}
	}

	public static void register() {

		NetManager.register(weChatNetManager);
	}

	public static void startNetWork() {

		NetManager.startNetWork();

	}

	/**
	 * 微信二维码
	 * 
	 * @param money
	 * @return
	 */
	public static void requestQr(PayNetAdapter payNetAdapter) {
		NetManager.reqQRCode(money, orderNo, payNetAdapter);

	}

	/**
	 * 微信订单查询
	 * 
	 * @return
	 */
	public static void queryOrder(PayNetAdapter payNetAdapter) {

		NetManager.reqQueryOrder(orderNo, payNetAdapter);

	}

	/**
	 * 微信扫码
	 * 
	 * @param money
	 * @param cod
	 * @return
	 */
	public static void requestScan(String code, PayNetAdapter payNetAdapter) {

		if (code.length() != 18 || (!code.startsWith("10") && !code.startsWith("11") && !code.startsWith("12")
				&& !code.startsWith("13") && !code.startsWith("14") && !code.startsWith("15"))) {
			payNetAdapter.onFail("二维码错误");
			return;
		}

		NetManager.reqScanQR(money, code, orderNo, payNetAdapter);

	}

	public static void setAmount(String money) {
		WeChatPayService.money = money;
	}

	public static void setOrderNo(String orderNo) {
		WeChatPayService.orderNo = orderNo;
	}

}
