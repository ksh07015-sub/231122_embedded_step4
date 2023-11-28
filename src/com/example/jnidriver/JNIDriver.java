package com.example.jnidriver;

public class JNIDriver {
	private boolean mConnectFlag;
	private boolean mConnectFlagLED;
	private boolean mConnectFlagFND;
	private boolean mConnectFlagTemp;
	
	static {
		System.loadLibrary("JNIDriver");
	}
	
	private native static int openDriver(String path);
	private native static int openDriverLED(String path);
	private native static int openDriverFND(String path);
	private native static int openDriverTemp(String path);
	private native static void closeDriver();
	private native void setMotor(char led);
	private native static void writeDriverLED(byte[] data, int length);
	private native static void writeDriverFND(byte[] data2, int length2);
	private native float readTemp();
	
	public JNIDriver() {
		mConnectFlag = false;
	}
	
	public int open(String driver) {
		if(mConnectFlag) return -1;
		
		if(openDriver(driver) > 0) {
			mConnectFlag = true;
			return 1;
		} else {
			return -1;
		}
	}
	
	public int openLED(String driver) {
		if(mConnectFlagLED) return -1;
		
		if(openDriverLED(driver) > 0) {
			mConnectFlagLED = true;
			return 1;
		} else {
			return -1;
		}
	}
	
	public int openFND(String driver2) {
		if(mConnectFlagFND) return -1;
		
		if(openDriverFND(driver2) > 0) {
			mConnectFlagFND = true;
			return 1;
		} else {
			return -1;
		}
	}
	
	public int openTemp(String driver) {
		if(mConnectFlagTemp) return -1;
		
		if(openDriverTemp(driver) > 0) {
			mConnectFlagTemp = true;
			return 1;
		} else {
			return -1;
		}
	}
	
	public void close() {
		if(!mConnectFlag) return;
		mConnectFlag = false;
		if(!mConnectFlagLED) return;
		mConnectFlagLED = false;
		if(!mConnectFlagFND) return;
		mConnectFlagFND = false;
		if(!mConnectFlagTemp) return;
		mConnectFlagTemp = false;
		closeDriver();
	}
	
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}
	
	public void setMotor(int val) {
		if(!mConnectFlag) return;
		
		setMotor((char)val);
	}
	
	public void writeLED(byte[] data) {
		if (!mConnectFlagLED) return;
		writeDriverLED(data, data.length);
	}
	
	public void writeFND(byte[] data2) {
		if (!mConnectFlagFND) return;
		writeDriverFND(data2, data2.length);
	}
	
	public float getTemp() {
		return readTemp();
	}
	
}
