package com.josh.pay.window.inputprice.model;

import org.josh.pay.item.ImageItem;
import org.joshvm.j2me.directUI.Image;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.josh.pay.AEImage;
import com.josh.pay.constant.Constant;
import com.josh.pay.net.adapter.AeMoneyNetAdapter;
import com.josh.pay.service.AeMoneyService;
import com.josh.pay.window.inputprice.MIIputpriceInterface;
import com.josh.pay.window.inputprice.WIIputPriceInterface;

public class AeInputPriceModel implements WIIputPriceInterface {

	private MIIputpriceInterface modelInterface;
	private Image image;

	public AeInputPriceModel(MIIputpriceInterface modelInterface, Image image) {
		this.modelInterface = modelInterface;
		this.image = image;
	}

	public void initView(ImageItem imageItem) {

		imageItem.setImg(image);
	}

	public void getQR(String price) {

		AeMoneyService.requestQRShowPayment(price, new AeMoneyNetAdapter() {

			public void onSuccess(JSONObject json) {
				try {
					String url = (String) json.get("url");
					AeMoneyService.setPayment_url(url);
					modelInterface.showQr(url);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
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
