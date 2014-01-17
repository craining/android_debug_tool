package com.zgy.debugtool.main;

import android.app.Application;
import android.view.WindowManager;

public class MainApplication extends Application {
	private static WindowManager.LayoutParams wmParams;
	private static MainApplication instance;


	@Override
	public void onCreate() {
		super.onCreate();
		if(wmParams == null) {
			wmParams = new WindowManager.LayoutParams();
		}
		
		if(instance == null) {
			instance = this;
		}
		
	}
	
	public static MainApplication getInstance() {
		return instance;
	}
	
	public static WindowManager.LayoutParams getMywmParams() {
		if(wmParams == null) {
			wmParams = new WindowManager.LayoutParams();
		}
		return wmParams;
	}
}
