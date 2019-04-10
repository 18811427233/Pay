package org.josh.pay.item;

import org.joshvm.j2me.directUI.Image;

import com.josh.pay.AEImage;

public class PowerImageItem extends ImageItem {

	private Image imageClear;

	private int number;

	public PowerImageItem(int id, Image image) {
		super(id, image);
		number = 0;
		imageClear = new AEImage("power_clear.jpg").getImage();
		// TODO Auto-generated constructor stub
	}

	public void show() {
		getContainerWindow().getDisplay().showImage(getX(), getY(), imageClear);
		super.show();
	}

	public void showPower(int power) {

		number = power;

		if (power < 2) {
			setImg(new AEImage("power_one.jpg").getImage());
		} else if (power == 2) {
			setImg(new AEImage("power_two.jpg").getImage());
		} else if (power == 3) {
			setImg(new AEImage("power_three.jpg").getImage());
		} else if (power > 3 && power < 6) {
			setImg(new AEImage("power_four.jpg").getImage());
		} else if (power == 6) {
			setImg(new AEImage("power_five.jpg").getImage());
		}

	}

	public int getNumber() {
		return number;
	}

}
