package org.json.me.util;

import java.io.IOException;

import org.joshvm.j2me.cellular.CellularDeviceInfo;

public class DeviceUtil {
	private static String deviceInfo = null;

	public static String getDeviceInfo() {

		if (deviceInfo == null) {

			CellularDeviceInfo[] devices = CellularDeviceInfo.listCellularDevices();
			if (devices != null && devices.length > 0) {

				try {
					deviceInfo = devices[0].getIMEI();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return deviceInfo;
	}
	
	public static boolean getDeviceImsi() {

		CellularDeviceInfo[] devices = CellularDeviceInfo.listCellularDevices();

		if (devices != null && devices.length > 0) {

			try {
				return devices[0].getIMSI().length() > 1;

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public static int getDeviceSign() {

		int sign = 0;

		CellularDeviceInfo[] devices = CellularDeviceInfo.listCellularDevices();
		if (devices != null && devices.length > 0) {

			try {
				sign = devices[0].getNetworkSignalLevel();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sign;
	}
}
