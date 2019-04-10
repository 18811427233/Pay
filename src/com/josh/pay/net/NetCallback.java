package com.josh.pay.net;

public interface NetCallback {
	
	void onError();
	void onProcess(byte[] data);
}
