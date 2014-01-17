package com.zgy.debugtool.processinfo;

import java.util.List;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.pm.PackageInfo;
import android.os.Debug.MemoryInfo;

public class Process {

	public RunningAppProcessInfo processInfo;
	public MemoryInfo memoryInfo;
	public boolean isLocked;
	public PackageInfo[] packageInfos;
	
	
	public void getPackageInfosAfterSetRunningAppProcessInfo(List<PackageInfo> systemPackageInfoList) {
		String[] pkgs = processInfo.pkgList;
		if(pkgs != null) {
			packageInfos = new PackageInfo[pkgs.length];
			for(int i=0; i<pkgs.length; i++) {
				for(PackageInfo packageInfo : systemPackageInfoList) {
					if(packageInfo.packageName.equals(pkgs[i])) {
						packageInfos[i] = packageInfo;
						break;
					}
				}
			}
		}
	}
	
}
