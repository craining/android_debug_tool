package com.zgy.debugtool.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.pm.ActivityInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.util.Log;

import com.zgy.debugtool.processinfo.ClassInfo;
import com.zgy.debugtool.processinfo.CompareClassInfo;

public class PraseClassUtil {

	private static void addClass(List<ClassInfo> classList, String packageName, String className) {
		boolean contains = false;
		for (ClassInfo clas : classList) {
			if (clas.packageName.equals(packageName)) {
				contains = true;
				if (clas.classes == null) {
					clas.classes = new ArrayList<String>();
				}
				clas.classes.add(className);
				break;
			}
		}
		if (!contains) {
			ClassInfo cls = new ClassInfo();
			cls.packageName = packageName;
			cls.classes = new ArrayList<String>();
			cls.classes.add(className);
			classList.add(cls);
		}
	}

	private static String sortClasses(List<ClassInfo> classList) {
		StringBuffer sb = new StringBuffer();
		// 排序按包名排序，每个包按类名排序
		Collections.sort(classList, new CompareClassInfo());
		for (ClassInfo cls : classList) {
			sb.append(cls.packageName + ":\r\n");
			Collections.sort(cls.classes);
			for (String clasname : cls.classes) {
				sb.append("\t").append(clasname).append("\r\n");
			}
		}
		if (sb.length() > 0) {
			return sb.toString();
		}
		return "null";
	}

	public static String praseActivity(ActivityInfo[] acs) {
		if (acs == null) {
			return "null";
		}
		int count = 0;
		List<ClassInfo> classes = new ArrayList<ClassInfo>();
		for (ActivityInfo ac : acs) {
			String className = "";
			Log.i("", "ac.name=" + ac.name   + "ac.packageName=" + ac.packageName);
			if (ac.name.contains(".")) {
				String[] names = ac.name.split("\\.");
				className = names[names.length - 1];
				count ++;
				addClass(classes, ac.name.replace("." + className, ""), "." + className);
			} else {
				count ++;
				addClass(classes, ac.name, "." + className);
			}
			
		}
		return count + " 个\r\n"+ sortClasses(classes);
	}
	
	public static String praseService(ServiceInfo[] sers) {
		if (sers == null) {
			return "null";
		}
		int count =0;
		List<ClassInfo> classes = new ArrayList<ClassInfo>();
		for (ServiceInfo ac : sers) {
			String className = "";
			if (ac.name.contains(".")) {
				String[] names = ac.name.split("\\.");
				className = names[names.length - 1];
				count ++;
				addClass(classes, ac.name.replace("." + className, ""), "." + className);
			} else {
				count ++;
				addClass(classes, ac.name, "." + className);
			}
			
		}
		return count + " 个\r\n"+ sortClasses(classes);
	}
	
	public static String praseProvider(ProviderInfo[] acs) {
		if (acs == null) {
			return "null";
		}
		int count = 0;
		List<ClassInfo> classes = new ArrayList<ClassInfo>();
		for (ProviderInfo ac : acs) {
			String className = "";
			if (ac.name.contains(".")) {
				String[] names = ac.name.split("\\.");
				className = names[names.length - 1];
				count ++;
				addClass(classes, ac.name.replace("." + className, ""), "." + className);
			} else {
				count ++;
				addClass(classes, ac.name, "." + className);
			}
			
		}
		return count + " 个\r\n"+ sortClasses(classes);
	}
	
	public static String praseReceiver(ActivityInfo[] acs) {
		if (acs == null) {
			return "null";
		}
		int count = 0;
		List<ClassInfo> classes = new ArrayList<ClassInfo>();
		for (ActivityInfo ac : acs) {
			String className = "";
			if (ac.name.contains(".")) {
				String[] names = ac.name.split("\\.");
				className = names[names.length - 1];
				count ++;
				addClass(classes, ac.name.replace("." + className, ""), "." + className);
			} else {
				count ++;
				addClass(classes, ac.name, "." + className);
			}
			
		}
		return count + " 个\r\n"+ sortClasses(classes);
	}
}
