package com.example.sm9m2step1;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jnidriver.*;

public class MainActivity extends Activity {
	
	ReceiveThread mSegThread;
	ReceiveThread2 mSegThread2;
	FNDThread mFNDThread;
	TempThread mTempThread;
	boolean mStart, mThreadRun = true;
	
	JNIDriver mDriver = new JNIDriver();
	
	int direction, fnd = 0;
	
	byte[] data = {0,0,0,0,0,0,0,0};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btn1 = (Button)findViewById(R.id.button1);
		Button btn2 = (Button)findViewById(R.id.button2);
		Button btn3 = (Button)findViewById(R.id.button3);
		Button btn4 = (Button)findViewById(R.id.button4);
		Button btn5 = (Button)findViewById(R.id.button5);
		
		mDriver.writeLED(data);
		mFNDThread = new FNDThread();
		mFNDThread.start();
		
		mTempThread = new TempThread();
		mTempThread.start();
		
		mSegThread2 = new ReceiveThread2();
		mSegThread2.start();
		
		btn1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				direction = 1;
				if (mSegThread == null) {
					mSegThread = new ReceiveThread();
					mSegThread.start();
				}
				
				fnd = 3;
				data[1] = 0;
				data[2] = 0;
				data[3] = 1;
				
				mDriver.writeLED(data);
			}
		});
		
		btn2.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				direction = 2;
				if (mSegThread == null) {
					mSegThread = new ReceiveThread();
					mSegThread.start();
				}
				
				fnd = 4;
				data[1] = 0;
				data[2] = 1;
				data[3] = 0;
				
				mDriver.writeLED(data);
			}
		});
		
		btn3.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				direction = 3;
				if (mSegThread == null) {
					mSegThread = new ReceiveThread();
					mSegThread.start();
				}
				
				fnd = 2;
				data[1] = 1;
				data[2] = 0;
				data[3] = 0;
				
				mDriver.writeLED(data);
			}
		});
		
		btn4.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				direction = 4;
				if (mSegThread == null) {
					mSegThread = new ReceiveThread();
					mSegThread.start();
				}
				
				fnd = 1;
				data[1] = 1;
				data[2] = 0;
				data[3] = 0;
				
				mDriver.writeLED(data);
			}
		});
		
		btn5.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				direction = 0;
				if (mSegThread != null) {
					mSegThread = null;
				}
				
				fnd = 000000;
				data[1] = 0;
				data[2] = 0;
				data[3] = 0;
				
				mDriver.writeLED(data);
				mDriver.setMotor(0);
			}
		});
	}
	
	private class ReceiveThread extends Thread {
		@Override
		public void run() {
			super.run();
			while(mThreadRun) {
				mDriver.setMotor(direction);
			}
		}
	}
	
	private class ReceiveThread2 extends Thread {
		@Override
		public void run() {
			super.run();
			while(mThreadRun) {
				Message text = Message.obtain();
				handler.sendMessage(text);
				try {
					Thread.sleep(1000);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class TempThread extends Thread {
		@Override
		public void run() {
			super.run();
			while(mThreadRun) {
				if(mDriver.getTemp() >= 27) {
					if (mSegThread == null) {
						mSegThread = new ReceiveThread();
						mSegThread.start();
					}
					
					direction = 2;
					
					data[1] = 0;
					data[2] = 1;
					data[3] = 0;
					
					fnd = 4;
					mDriver.writeLED(data);
				}
			}
		}
	}
	
	private class FNDThread extends Thread {
		@Override
		public void run() {
			super.run();
			while (mThreadRun) {
				byte[] n= {0,0,0,0,0,0,0};
				
				if(mStart==false) { mDriver.writeFND(n); }
				else {
					n[0] = (byte)(fnd % 1000000 / 100000);
					n[1] = (byte)(fnd % 100000 / 10000);
					n[2] = (byte)(fnd % 10000 / 1000);
					n[3] = (byte)(fnd % 1000 / 100);
					n[4] = (byte)(fnd % 100 / 10);
					n[5] = (byte)(fnd % 10);
					mDriver.writeFND(n);
				}
			}
		}
	}
	
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			TextView tv;
			
			tv = (TextView) findViewById(R.id.textView1);
			tv.setText("¿Âµµ: " + mDriver.getTemp());
			
		}
	};
	
	@Override
	protected void onPause() {
		mDriver.close();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		if(mDriver.open("/dev/sm9s5422_step") < 0 || mDriver.openLED("/dev/sm9s5422_led") < 0 || mDriver.openFND("/dev/sm9s5422_segment") < 0 || mDriver.openTemp("/dev/sm9s5422_sht20") < 0 ) {
			Toast.makeText(MainActivity.this, "Driver Open Failed", Toast.LENGTH_SHORT).show();
		}
		
		super.onResume();
	}
}
