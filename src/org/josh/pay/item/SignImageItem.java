package org.josh.pay.item;

import org.joshvm.j2me.directUI.Image;

import com.josh.pay.AEImage;

public class SignImageItem extends ImageItem {
	private Image imageClear;
	private int number = 0;

	public SignImageItem(int id, Image image) {
		super(id, image);
		// TODO Auto-generated constructor stub
		imageClear = new AEImage("sign_clear.jpg").getImage();
	}

	public void show() {
		// TODO Auto-generated method stub
		getContainerWindow().getDisplay().showImage(getX(), getY(), imageClear);
		super.show();
	}

	public void showSign(int sign) {
		number = sign;

		if (sign == 0) {
			setImg(new AEImage("sign.jpg").getImage());
		} else if (sign > 0 && sign <= 6) {
			setImg(new AEImage("sign_one.jpg").getImage());
		} else if (sign > 6 && sign <= 12) {
			setImg(new AEImage("sign_two.jpg").getImage());
		} else if (sign > 12 && sign <= 18) {
			setImg(new AEImage("sign_three.jpg").getImage());
		} else if (sign > 18 && sign <= 24) {
			setImg(new AEImage("sign_four.jpg").getImage());
		} else if (sign > 24 && sign <= 31) {
			setImg(new AEImage("sign_five.jpg").getImage());
		} else {
			setImg(new AEImage("no_sign.jpg").getImage());
		}

	}

	public int getNumber() {
		return number;
	}

	public boolean isChange(int sign) {

		if (sign == 0) {

			if (sign != number) {
				return true;
			}
		}
		if (sign > 0 && sign <= 6) {

			if (number > 6 || number == 0) {
				return true;
			}
		}
		if (sign > 6 && sign <= 12) {

			if (number <= 6 || number > 12) {
				return true;
			}
		}
		if (sign > 12 && sign <= 18) {

			if (number <= 12 || number > 18) {
				return true;
			}
		}
		if (sign > 18 && sign <= 24) {

			if (number <= 18 || number > 24) {
				return true;
			}
		}
		if (sign > 24 && sign <= 31) {

			if (number <= 24) {
				return true;
			}
		}

		return false;

	}

}

