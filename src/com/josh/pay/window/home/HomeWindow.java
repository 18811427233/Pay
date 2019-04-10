package com.josh.pay.window.home;

import org.josh.pay.item.ImageItem;
import org.josh.pay.item.Item;
import org.joshvm.j2me.directUI.Image;
import org.joshvm.j2me.directUI.Keypad;

import com.josh.pay.AEImage;
import com.josh.pay.constant.Constant;
import com.josh.pay.window.ContentWindow;
import com.josh.pay.window.Event;
import com.josh.pay.window.inputprice.InputPriceWindow;
import com.josh.pay.window.inputprice.WIIputPriceInterface;
import com.josh.pay.window.inputprice.model.AeInputPriceModel;

public class HomeWindow extends ContentWindow {
	private Item homescreen;
	private Image image;

	public HomeWindow(int id) {
		super(id);
		homescreen = new ImageItem(Constant.ID_HOME_ITEM, new AEImage("home.jpg").getImage());
	}

	public void init() {
		addItem(homescreen);
		image = new AEImage("daqian.jpg").getImage();
	}

	public boolean event(Event evt) {

		if ((evt.type == Event.KEY_PRESSED) && (evt.value != Keypad.KEY_POWER)) {
			InputPriceWindow window = (InputPriceWindow) window_manager.getWindow(Constant.WIN_ID_INPUTPRICE);
			WIIputPriceInterface windowInterface = new AeInputPriceModel(window, image);
			window.registerModel(windowInterface);
			getWindowManager().setCurrent(window);
			return true;
		} else {
			return super.event(evt);
		}
	}

	public void deinit() {

	}

}
