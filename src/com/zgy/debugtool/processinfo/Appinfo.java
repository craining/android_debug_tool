package com.zgy.debugtool.processinfo;

import android.graphics.drawable.Drawable;

/**
 * 为方便展示“程序信息”弹窗里的信息列表而写的一个bean，展示的信息尚未完善 
 * @Author zhuanggy
 * @Date:2014-1-17
 * @version 
 * @since
 */
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
