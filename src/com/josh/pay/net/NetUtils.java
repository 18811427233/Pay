package com.josh.pay.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

import javax.xml.stream.XMLStreamException;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.signers.RSADigestSigner;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.util.encoders.Base64;

public class NetUtils {

	public static String bcMD5(String content) throws UnsupportedEncodingException {

		Digest digest = new MD5Digest();
		digest.update(content.getBytes("utf-8"), 0, content.getBytes("utf-8").length);
		byte[] md5Byte = new byte[digest.getDigestSize()];
		digest.doFinal(md5Byte, 0);
		return org.bouncycastle.util.encoders.Hex.toHexString(md5Byte);
	}

	/**
	 * 转换为XML格式的字符串
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws XMLStreamException
	 * @throws IOException
	 * @throws Exception
	 */
	public static String toXml(String[] key, String value[]) {

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
		stringBuffer.append("<xml>");
		for (int i = 0; i < key.length; i++) {

			stringBuffer.append("<" + key[i] + ">" + value[i] + "</" + key[i] + ">");
		}

		stringBuffer.append("</xml>");

		return stringBuffer.toString();
	}

	/**
	 * 获取系统时间
	 * 
	 * @return
	 */
	public static String getTimeStamp() {

		Calendar calendar = Calendar.getInstance();
		Date date = new Date();

		date.setTime(System.currentTimeMillis());
		calendar.setTime(date);

		String year = calendar.get(Calendar.YEAR) + "";
		String month = (calendar.get(Calendar.MONTH) + 1) > 9 ? (calendar.get(Calendar.MONTH) + 1) + ""
				: "0" + (calendar.get(Calendar.MONTH) + 1);
		String day = calendar.get(Calendar.DAY_OF_MONTH) > 9 ? calendar.get(Calendar.DAY_OF_MONTH) + ""
				: "0" + calendar.get(Calendar.DAY_OF_MONTH);
		String hour = calendar.get(Calendar.HOUR_OF_DAY) > 9 ? calendar.get(Calendar.HOUR_OF_DAY) + ""
				: "0" + calendar.get(Calendar.HOUR_OF_DAY);
		String minute = calendar.get(Calendar.MINUTE) > 9 ? calendar.get(Calendar.MINUTE) + ""
				: "0" + calendar.get(Calendar.MINUTE);
		String second = calendar.get(Calendar.SECOND) > 9 ? calendar.get(Calendar.SECOND) + ""
				: "0" + calendar.get(Calendar.SECOND);

		return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
	}

	public static String signatureAli(String content, String key) throws Exception {
		
		System.out.println("==============11111111111111===========" + System.currentTimeMillis());
		byte[] keyBytes = Base64.decode(key.getBytes("utf-8"));
		AsymmetricKeyParameter signingKey = PrivateKeyFactory.createKey(keyBytes);
		RSADigestSigner signer = new RSADigestSigner(new SHA256Digest());
		signer.init(true, signingKey);
		signer.update(content.getBytes(), 0, content.getBytes().length);
		byte[] signature = signer.generateSignature();
		System.out.println("==============22222222222222===========" + System.currentTimeMillis());
		return new String(Base64.encode(signature));
		
//		byte[] keyBytes = Base64.decode(key.getBytes("utf-8"));
//		Signature sig = Signature.getInstance("SHA256WITHRSA");
//		RSAKeyParameters k = (RSAKeyParameters) PrivateKeyFactory.createKey(keyBytes);
//
//		sig.initSign(new BouncyCastleRSAPrivateKey(k).getRSAPrivateKey());
//		sig.update(content.getBytes(), 0, content.getBytes().length);
//		byte[] outBuf = new byte[sig.getLength()];
//		sig.sign(outBuf);
//		return new String(Base64.encode(outBuf));
	}

	// get the encoded value of a single symbol, each return value is 3
	// characters long
	static String hex(int sym) {
		return (hex.substring(sym * 3, sym * 3 + 3));
	}

	// Hex constants concatenated into a string, messy but efficient
	final static String hex = "%00%01%02%03%04%05%06%07%08%09%0a%0b%0c%0d%0e%0f%10%11%12%13%14%15%16%17%18%19%1a%1b%1c%1d%1e%1f"
			+ "%20%21%22%23%24%25%26%27%28%29%2a%2b%2c%2d%2e%2f%30%31%32%33%34%35%36%37%38%39%3a%3b%3c%3d%3e%3f"
			+ "%40%41%42%43%44%45%46%47%48%49%4a%4b%4c%4d%4e%4f%50%51%52%53%54%55%56%57%58%59%5a%5b%5c%5d%5e%5f"
			+ "%60%61%62%63%64%65%66%67%68%69%6a%6b%6c%6d%6e%6f%70%71%72%73%74%75%76%77%78%79%7a%7b%7c%7d%7e%7f"
			+ "%80%81%82%83%84%85%86%87%88%89%8a%8b%8c%8d%8e%8f%90%91%92%93%94%95%96%97%98%99%9a%9b%9c%9d%9e%9f"
			+ "%a0%a1%a2%a3%a4%a5%a6%a7%a8%a9%aa%ab%ac%ad%ae%af%b0%b1%b2%b3%b4%b5%b6%b7%b8%b9%ba%bb%bc%bd%be%bf"
			+ "%c0%c1%c2%c3%c4%c5%c6%c7%c8%c9%ca%cb%cc%cd%ce%cf%d0%d1%d2%d3%d4%d5%d6%d7%d8%d9%da%db%dc%dd%de%df"
			+ "%e0%e1%e2%e3%e4%e5%e6%e7%e8%e9%ea%eb%ec%ed%ee%ef%f0%f1%f2%f3%f4%f5%f6%f7%f8%f9%fa%fb%fc%fd%fe%ff";

	public static String urlEncode(String s) {
		StringBuffer sbuf = new StringBuffer();
		int len = s.length();
		for (int i = 0; i < len; i++) {
			int ch = s.charAt(i);
			if ('A' <= ch && ch <= 'Z') { // 'A'..'Z'
				sbuf.append((char) ch);
			} else if ('a' <= ch && ch <= 'z') { // 'a'..'z'
				sbuf.append((char) ch);
			} else if ('0' <= ch && ch <= '9') { // '0'..'9'
				sbuf.append((char) ch);
			} else if (ch == ' ') { // space
				sbuf.append('+');
			} else if (ch == '-' || ch == '_' // these characters don't need
												// encoding
					|| ch == '.' || ch == '*') {
				sbuf.append((char) ch);
			} else if (ch <= 0x007f) { // other ASCII
				sbuf.append(hex(ch));
			} else if (ch <= 0x07FF) { // non-ASCII <= 0x7FF
				sbuf.append(hex(0xc0 | (ch >> 6)));
				sbuf.append(hex(0x80 | (ch & 0x3F)));
			} else { // 0x7FF < ch <= 0xFFFF
				sbuf.append(hex(0xe0 | (ch >> 12)));
				sbuf.append(hex(0x80 | ((ch >> 6) & 0x3F)));
				sbuf.append(hex(0x80 | (ch & 0x3F)));
			}
		}
		return sbuf.toString();
	}

}
