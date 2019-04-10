package org.josh.pay.item;

import org.joshvm.j2me.directUI.Image;
import org.joshvm.j2me.directUI.Text;

import com.josh.pay.AEImage;

public class TextItem extends RegularItem {
	private String textstr;
	private Text text;
	private Image image;
	private Image imageNumber;
	private boolean isInput;

	public TextItem(int id, String displayText, boolean TextType) {
		super(id, 0, 0);
		textstr = displayText;
		isInput = TextType;
		if (isInput) {
			imageNumber = new AEImage("text_clear_number.jpg").getImage();
		} else {
			image = new AEImage("text_clear.jpg").getImage();
		}

		if (displayText == null) {
			text = new Text();
		} else {
			text = new Text(displayText);
		}
	}

	public void setText(String displayText) {
		textstr = displayText;
		text.setString(displayText);
	}

	public String getText() {
		return textstr;
	}

	public void show() {

		if (textstr != null) {
			if (isInput) {
				getContainerWindow().getDisplay().showImage(getX(), getY(), imageNumber);
			} else {
				getContainerWindow().getDisplay().showImage(getX(), getY(), image);
			}

			getContainerWindow().getDisplay().showText(getX(), getY(), text);
		}
	}

}
