package com.josh.pay.window.inputprice.model;

import org.josh.pay.item.ImageItem;
import org.joshvm.j2me.directUI.Image;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.josh.pay.constant.Constant;
import com.josh.pay.net.adapter.PayNetAdapter;
import com.josh.pay.service.AliPayService;
import com.josh.pay.window.inputprice.MIIputpriceInterface;
import com.josh.pay.window.inputprice.WIIputPriceInterface;

public class AliInputPriceModel implements WIIputPriceInterface {

	private MIIputpriceInterface modelInterface;
	private Image image;

	public AliInputPriceModel(MIIputpriceInterface modelInterface,Image image) {
		this.modelInterface = modelInterface;
		this.image = image;
		
	}

	public void initView(ImageItem imageItem) {
		imageItem.setImg(image);
	}

	public void getQR(String price) {
		
		AliPayService.requestQr(price, new PayNetAdapter() {

			public void onSuccess(JSONObject json) {
				try {
					String codeUrl = json.getString("qrCode");
					String orderNumber = json.getString("orderNumber");
					AliPayService.setOrderNo(orderNumber);
					modelInterface.showQr(codeUrl);
					
				} catch (JSONException e) {
					e.printStackTrace();
					modelInterface.errorMessage(Constant.NETERROR_MESSAGE);
				}

			}

			public void onFail(String message) {
				modelInterface.errorMessage(message);

			}
		});
	}

}
