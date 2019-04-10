package com.josh.pay;

import java.io.IOException;
import java.io.InputStream;

import org.joshvm.j2me.directUI.Image;

public class AEImage {
	private static Image image;
	private final String filename;
	
	public AEImage(String imageFilename) {
		filename = imageFilename;
		image = null;
	}
	
	public Image getImage() {
		if (image != null) {
			return image;
		}
		
		byte[] imageData;
		try {
			imageData = getImageFile(this.getClass(), filename);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		int type;
		if (filename.endsWith(".jpg")) {
			type = Image.IMAGE_TYPE_JPG;
		} else if (filename.endsWith(".png")) {
			type = Image.IMAGE_TYPE_PNG;
		} else if (filename.endsWith(".bmp")) {
			type = Image.IMAGE_TYPE_BMP;
		} else {
			return null;
		} 
		
		image = new Image(type);
		image.setImageData(imageData);
		return image;
	}
	
	 private byte[] getImageFile(Class resourceClass, String filename) throws IOException {
        byte[] ret = null;
        
        InputStream is = resourceClass.getResourceAsStream(filename);
        byte[] buf = new byte[4096];
        int len = 0;
        int l = -1;

        while ((l = is.read(buf, len, buf.length - len)) != -1) {
            len += l;
            if (len == buf.length) {
                byte[] b = new byte[buf.length + 4096];
                System.arraycopy(buf, 0, b, 0, len);
                buf = b;
            }
        }

        ret = new byte[len];
        System.arraycopy(buf, 0, ret, 0, len);
        
        return ret;
	 }
}
