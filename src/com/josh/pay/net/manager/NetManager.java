package com.josh.pay.net.manager;

import org.joshvm.crypto.keystore.KeyStore;

import com.josh.pay.net.NetCallback;

/**
 * 提供对外开放的网络管理对象
 * 
 * @author Administrator
 *
 */
public abstract class NetManager {

	public static final String CONTENT_TYPE_JSON = "application/json";
	public static final String CONTENT_TYPE_XML = "application/xml";
	public static final String CONTENT_TYPE_URLENCODED = "application/x-www-form-urlencoded;charset=utf-8";

	private static NetManager netManager;

	/**
	 * 初始化SSL
	 * <p>
	 * 启动后只调用一次
	 * 
	 * @param resClass
	 * @param resRoot
	 */
	public static void initSSL(Class resClass, String resRoot) {

		KeyStore.initWebPublicKeystoreLocation(resClass, resRoot);
	}

	/**
	 * 注册实际业务逻辑NetManager
	 * 
	 * @param netManager
	 */
	public static void register(NetManager netManager) {

		NetManager.netManager = netManager;
	}

	/**
	 * 启动网络服务
	 */
	public static void startNetWork() {

		NetManager.netManager.startNetwork();
	}

	/**
	 * 获取二维码
	 * 
	 * @param price
	 * @param orderNumber
	 * @param netCallback
	 */
	public static void reqQRCode(String price, String orderNumber, NetCallback netCallback) {

		netManager.getQRCode(price, orderNumber, netCallback);
	}

	/**
	 * 扫码收款
	 * 
	 * @param price
	 * @param qrCode
	 * @param orderNumber
	 * @param netCallback
	 */
	public static void reqScanQR(String price, String qrCode, String orderNumber, NetCallback netCallback) {

		netManager.scanQR(price, qrCode, orderNumber, netCallback);
	}

	/**
	 * 查询订单详情
	 * 
	 * @param orderNumber
	 * @param netCallback
	 */
	public static void reqQueryOrder(String orderNumber, NetCallback netCallback) {

		netManager.queryOrder(orderNumber, netCallback);
	}

	/**
	 * 停止查询订单
	 */
	public static void stopReqQueryOrder() {

		netManager.stopQueryOrder();
	}

	/**
	 * 检查网络连接是否暂停
	 * 
	 * @return
	 */
	public static boolean checkNetWorkPause() {

		return netManager.isNetWorkPause();
	}

	/**
	 * 注销
	 */
	public static void unRegister() {

		NetManager.netManager.pauseNetwork();
	}

	/**
	 * 获取二维码
	 * 
	 * @param price
	 * @param orderNumber
	 * @param netCallback
	 */
	protected abstract void getQRCode(String price, String orderNumber, NetCallback netCallback);

	/**
	 * 扫码二维码
	 * 
	 * @param price
	 * @param qrCode
	 * @param orderNumber
	 * @param netCallback
	 */
	protected abstract void scanQR(String price, String qrCode, String orderNumber, NetCallback netCallback);

	/**
	 * 查询订单
	 * 
	 * @param orderNumber
	 * @param netCallback
	 */
	protected abstract void queryOrder(String orderNumber, NetCallback netCallback);

	/**
	 * 停止查询订单
	 */
	protected abstract void stopQueryOrder();

	/**
	 * 销毁链接
	 */
	protected abstract void destoryNetwork();

	/**
	 * 启动
	 */
	protected abstract void startNetwork();

	/**
	 * 暂停
	 */
	protected abstract void pauseNetwork();

	/**
	 * 检查网络连接是否暂停
	 * 
	 * @return
	 */
	protected abstract boolean isNetWorkPause();

}
