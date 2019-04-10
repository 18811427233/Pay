package org.josh.pay.item;

import org.joshvm.j2me.directUI.Image;

public class ImageItem extends RegularItem {

	private Image img;

	public ImageItem(int id, Image image) {
		super(id, image.getWidth(), image.getHeight());
		img = image;
	}

	public void show() {
		getContainerWindow().getDisplay().showImage(getX(), getY(), img);
	}

	public void setImg(Image img) {
		this.img = img;
	}
}
