package com.zgy.debugtool.processinfo;

import java.util.Comparator;

/**
 * 用于排序的类
 * @Author zhuanggy
 * @Date:2014-1-17
 * @version 
 * @since
 */
public class CompareClassInfo implements Comparator<Object> {

	 
	@Override
	public int compare(Object lhs, Object rhs) {

		ClassInfo cla1 = (ClassInfo) lhs;
		ClassInfo cla2 = (ClassInfo) rhs;
		 
		return cla1.packageName.compareTo(cla2.packageName);
	}

	 
}
