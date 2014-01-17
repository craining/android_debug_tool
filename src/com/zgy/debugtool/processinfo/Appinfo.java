package com.zgy.debugtool.processinfo;

import android.graphics.drawable.Drawable;

public class Appinfo {

	public String label;
	public String value;

	public String name;
	public Drawable icon;

	public Appinfo(String label, String value) {
		this.label = label;
		this.value = value;
	}
}
