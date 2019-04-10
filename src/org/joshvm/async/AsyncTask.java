package org.joshvm.async;

public class AsyncTask implements Runnable {

	private AsyncTaskBody listener;
	private boolean stop;
	private Thread theThread;
	private Object callbackParam;
	
	public AsyncTask() {
		listener = null;
		stop = true;
		theThread = new Thread(this);
	}

	public synchronized void setCallback(AsyncTaskBody listener) {
		if (stop && (listener != null) && (this.listener == null)) {
			System.out.println("AsyncEvent callback thread start");
			stop = false;
			theThread.start();
		}
		
		if ((listener == null) && (this.listener != null)) {
			stop = true;
			notifyAll();
		}
		
		this.listener = listener;		
	}

	public void run() {
		while (!stop) {
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (!stop) {
				listener.onAsyncTaskStart(callbackParam);
			}
		}		
	}

	public synchronized void trig(Object param) {
		callbackParam = param;
		notifyAll();
	}
	
	public synchronized void destroy() {
		setCallback(null);
		stop = true;
		theThread = null;
	}

}
