package com.zgy.debugtool.allappsinfo;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.zgy.debugtool.main.R;

public class AllAppsList extends Activity{

	
	
	private ListView mListViewApps;
	private AllAppsListAdapter mAdapter;
	
	private ProgressBar mProgressBar;
	
	
	
	
	private List<PackageInfo> mPackagesList;
	private BlockingQueue<String> mBlockPackageStep = new PriorityBlockingQueue<String>();
	
	
	private PackageManager mPackageM;
	
	private boolean mSingleLine;
	
	private static final int MSG_REFRESHLIST_AFTER_GET_APPS = 0x100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allapps);
		mPackageM = getApplicationContext().getPackageManager();
		
		mListViewApps = (ListView) findViewById(R.id.lv_apps);
		mProgressBar = (ProgressBar) findViewById(R.id.progress_appslist);
		
		new Thread(mRunnableGetPackageList).start();
		putGetAppsStep();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	
	
	
	private Runnable mRunnableGetPackageList = new Runnable() {
		
		@Override
		public void run() {
			mPackagesList = mPackageM.getInstalledPackages(PackageManager.GET_PERMISSIONS | PackageManager.GET_SERVICES | PackageManager.GET_GIDS | PackageManager.GET_ACTIVITIES | PackageManager.GET_PROVIDERS | PackageManager.GET_RECEIVERS | PackageManager.GET_CONFIGURATIONS | PackageManager.GET_SIGNATURES);
			mHandler.sendEmptyMessage(MSG_REFRESHLIST_AFTER_GET_APPS);
		}
	};
	
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case MSG_REFRESHLIST_AFTER_GET_APPS:
				if(mProgressBar.getVisibility() == View.VISIBLE) {
					mProgressBar.setVisibility(View.GONE);
				}
				if(mAdapter == null) {
					mAdapter = new AllAppsListAdapter(AllAppsList.this, mPackagesList, mSingleLine, mPackageM);
					mListViewApps.setAdapter(mAdapter);
				} else {
					mAdapter.notifyDataChanged(mPackagesList, mSingleLine);
				}
				break;

			default:
				break;
			}
		}
		
	};
	
	/**
	 * 触发线程走出阻塞，读取cpu和内存
	 * 
	 * @param
	 * @author zhuanggy
	 * @date 2014-1-15
	 */
	private void putGetAppsStep() {
		try {
			mBlockPackageStep.put("test");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
