package com.josh.pay.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 * 提供网络连接会话
 * 
 * @author Administrator
 *
 */
public class NetSession {

	// Content-Type
	private String contentType;
	// 是否重连
	private boolean retry;
	private boolean pause;

	private Object pauseLock = new Object();

	// 地址：ip:port
	private String host;
	// 回调
	private NetCallback netCallback;

	// 流
	private StreamConnection streamConnection;
	private OutputStream outputStream;
	private InputStream inputStream;

	private boolean isPause = false;
	private boolean isDissConnect = false;

	private boolean waitSend = false;

	public NetSession(String contentType) {

		retry = true;
		pause = true;
		this.contentType = contentType;
	}

	/**
	 * 建立连接
	 * 
	 * @param host
	 * @param method
	 * @param callback
	 */
	public void connect(final String host) {

		this.host = host;

		new Thread(new Runnable() {

			public void run() {

				while (retry) {

					try {

						isDissConnect = false;

						synchronized (pauseLock) {

							if (pause) {
								isPause = true;
								pauseLock.wait();
							}
						}

						isPause = false;

						System.out.println(" net connect == " + host + " start ");

						streamConnection = (StreamConnection) Connector.open("ssl://" + host);
						outputStream = streamConnection.openOutputStream();
						inputStream = streamConnection.openInputStream();

						if (pause) {

							disConnect();
						}

						byte[] readBuffer;
						int responseLen = 0;
						String code = null;

						while ((readBuffer = readLine(inputStream)) != null) {
							String lenMessage = new String(readBuffer);
							// System.out.println("len -- " + lenMessage);
							if (lenMessage.indexOf("HTTP/1") > -1) {

								int start = lenMessage.indexOf(" ");
								int end = lenMessage.indexOf(" ", start + 1);
								code = lenMessage.substring(start + 1, end);

							} else if (lenMessage.indexOf("Content-Length") > -1) {

								responseLen = Integer.parseInt(lenMessage.substring(16, lenMessage.length()));
								// System.out.println("rrrrrrr " +
								// responseLen);

							} else if (lenMessage.equals("")) {

								if (code.equals("200")) {

									byte[] response = new byte[responseLen];
									inputStream.read(response);

									System.out
											.println("net response == " + host + " message == " + new String(response));

									if (netCallback != null) {

										netCallback.onProcess(response);
										netCallback = null;
										waitSend = false;
									}

								} else {

									if (netCallback != null) {

										netCallback.onError();
										netCallback = null;
										waitSend = false;
									}
								}
							}
						}

					} catch (IOException e) {

						e.printStackTrace();

					} catch (InterruptedException e) {

						e.printStackTrace();

					} finally {

						System.out.println("net with == " + host + " finish ");

						if (netCallback != null) {

							if (!isDissConnect) {

								netCallback.onError();
							}

							netCallback = null;

						}

						if (!isDissConnect) {

							try {
								disConnect();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						streamConnection = null;
						inputStream = null;
						outputStream = null;

						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						waitSend = false;
					}
				}
			}

		}).start();
	}

	/**
	 * 实现ReadLine
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private byte[] readLine(InputStream in) throws IOException {

		int bufferLen = 128;
		byte[] valueBuffer = new byte[bufferLen];
		byte[] temBuffer = valueBuffer;

		int offset = 0;
		int readValue;
		boolean isR = false;
		boolean isRN = false;

		while ((readValue = in.read()) > -1) {

			switch (readValue) {

			case '\r':

				isR = true;

				break;

			case '\n':

				if (isR) {

					isRN = true;
				}

				break;

			default:

				if (isR) {

					isR = false;
				}

				break;
			}

			temBuffer[offset] = (byte) readValue;
			offset++;

			if (offset == bufferLen) {

				bufferLen += bufferLen;
				valueBuffer = new byte[bufferLen];
				System.arraycopy(temBuffer, 0, valueBuffer, 0, offset);
				temBuffer = valueBuffer;
			}

			if (isRN) {

				offset -= 2;
				valueBuffer = new byte[offset];

				System.arraycopy(temBuffer, 0, valueBuffer, 0, offset);

				return valueBuffer;
			}
		}

		return null;
	}

	/**
	 * 发送数据
	 * 
	 * @param message
	 * @param disconnect
	 * @param netCallback
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void send(String method, String message, boolean disconnect, NetCallback netCallback)
			throws IOException, InterruptedException {

		while (waitSend) {

			Thread.sleep(100);
		}
		waitSend = true;

		this.netCallback = netCallback;

		StringBuffer request = new StringBuffer();

		request.append("POST " + method + " HTTP/1.1\r\n");
		request.append("Host: " + host + "\r\n");
		request.append("Content-Type: " + contentType + "\r\n");
		request.append("Content-Length: " + message.length() + "\r\n");

		// if (disconnect) {
		//
		// request.append("Connection: close\r\n");
		//
		// } else {
		//
		// request.append("Connection: keepAlive\r\n");
		// }

		request.append("\r\n");

		request.append(message + "\r\n");
		request.append("\r\n");

		// if (outputStream == null) {
		//
		// connectLock.wait();
		// }

		System.out.println("net request == " + host + method + " message == " + message);

		int num = 60;
		while (outputStream == null && num > 0) {
			num--;
			Thread.sleep(500);

			if (this.netCallback == null) {
				return;
			}
		}

		if (outputStream != null) {

			outputStream.write(request.toString().getBytes());

		} else if (this.netCallback != null) {
			this.netCallback.onError();
			this.netCallback = null;
		}

	}

	/**
	 * 断开连接
	 * 
	 * @throws IOException
	 */
	public void disConnect() throws IOException {

		isDissConnect = true;
		waitSend = false;
		if (inputStream != null) {

			inputStream.close();
		}

		if (outputStream != null) {

			outputStream.close();
		}

		if (streamConnection != null) {

			streamConnection.close();
		}
	}

	public void pause() throws IOException {

		pause = true;
		disConnect();
	}

	public void start() {

		synchronized (pauseLock) {

			pause = false;
			pauseLock.notifyAll();
		}
	}

	/**
	 * 销毁
	 * 
	 * @throws IOException
	 */
	public void destory() throws IOException {

		retry = false;
		disConnect();
	}

	public boolean isPause() {
		return isPause;
	}
}
