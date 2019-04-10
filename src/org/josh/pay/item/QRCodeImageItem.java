package org.josh.pay.item;

import org.joshvm.j2me.directUI.ImageBuffer;

import com.swetake.util.Qrcode;

public class QRCodeImageItem extends RegularItem {

	private static final int DEFAULT_QRCODE_W = 200;
	private static final int DEFAULT_QRCODE_H = 200;
	private static final int DEFAULT_QRCODE_X = 45;
	private static final int DEFAULT_QRCODE_Y = 50;

	private boolean[][] qrcode;

	public QRCodeImageItem(int id) {
		super(id, DEFAULT_QRCODE_W, DEFAULT_QRCODE_H);
//		setPosition(DEFAULT_QRCODE_X, DEFAULT_QRCODE_Y);
		qrcode = null;
	}

	public void setQRCode(boolean[][] QRCode) {
		qrcode = QRCode;
	}

	public void setQRCode(String str) {
		Qrcode qr = new Qrcode();
		qr.setQrcodeErrorCorrect('M');
		qr.setQrcodeEncodeMode('B');
		qrcode = qr.calQrcode(str.getBytes());
	}

	public void show() {
		// System.out.println("Showing QRCode");
		if (qrcode != null) {
			ImageBuffer img = getQRCodeImage(qrcode);
			getContainerWindow().getWindowManager().getDisplay().showImageBuffer(getX(), getY(), img);
		} else {
			System.out.println("qrcode is null, can't display");
		}
	}

	private ImageBuffer getQRCodeImage(boolean[][] matrix) {
		// System.out.println("displayQRCode...");
		int w = matrix.length;
		int h = matrix.length;
		byte[] buffer = new byte[2 * w * h * 4 * 4];
		int k = 0;
		int x = 0;
		int y = 0;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (matrix[j][i]) {
					int t = k;
					for (int m = 0; m < 4; m++) {
						buffer[t++] = (byte) 0x00;
						buffer[t++] = (byte) 0x00;
						buffer[t++] = (byte) 0x00;
						buffer[t++] = (byte) 0x00;
						buffer[t++] = (byte) 0x00;
						buffer[t++] = (byte) 0x00;
						buffer[t++] = (byte) 0x00;
						buffer[t++] = (byte) 0x00;
						t += (w * 4 * 2 - 8);
					}
					k += 2 * 4;
				} else {
					int t = k;
					for (int n = 0; n < 4; n++) {
						buffer[t++] = (byte) 0xff;
						buffer[t++] = (byte) 0xff;
						buffer[t++] = (byte) 0xff;
						buffer[t++] = (byte) 0xff;
						buffer[t++] = (byte) 0xff;
						buffer[t++] = (byte) 0xff;
						buffer[t++] = (byte) 0xff;
						buffer[t++] = (byte) 0xff;
						t += (w * 4 * 2 - 8);
					}
					k += 2 * 4;
				}
			}
			k += w * 2 * 4 * 3;
		}
		return new ImageBuffer(w * 4, h * 4, ImageBuffer.TYPE_RGB565, buffer);
	}

}
