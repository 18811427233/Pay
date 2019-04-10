package com.josh.pay.window.inputprice.model;

import org.josh.pay.item.ImageItem;
import org.joshvm.j2me.directUI.Image;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.josh.pay.constant.Constant;
import com.josh.pay.net.adapter.PayNetAdapter;
import com.josh.pay.service.WeChatPayService;
import com.josh.pay.window.inputprice.MIIputpriceInterface;
import com.josh.pay.window.inputprice.WIIputPriceInterface;

public class WeInputPriceModel implements WIIputPriceInterface {

	private MIIputpriceInterface modelInterface;
	private Image image;

	public WeInputPriceModel(MIIputpriceInterface modelInterface,Image image) {
		this.modelInterface = modelInterface;
		this.image = image;
	}

	public void initView(ImageItem imageItem) {
		imageItem.setImg(image);
	}

	public void getQR(String price) {
		
		WeChatPayService.requestQr(new PayNetAdapter() {

			public void onSuccess(JSONObject json) {
				try {
					String codeUrl = json.getString("qrCode");
					String orderNumber = json.getString("orderNumber");
					WeChatPayService.setOrderNo(orderNumber);
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
