package com.zgy.debugtool.allappsinfo;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.zgy.debugtool.main.R;
import com.zgy.debugtool.processinfo.ProcessAppsinfoListDialog;
import com.zgy.debugtool.processinfo.ProcessListActivity;

public class AllAppsList extends Activity implements OnClickListener, OnItemClickListener{

	private ListView mListViewApps;
	private AllAppsListAdapter mAdapter;
	private ProgressBar mProgressBar;
	
	private Button mBtnRefresh;
	private ImageView mImgBack;
	
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
		mBtnRefresh = (Button) findViewById(R.id.btn_apps_refresh);
		mImgBack = (ImageView) findViewById(R.id.img_apps_back);
		
		mBtnRefresh.setOnClickListener(this);
		mImgBack.setOnClickListener(this);
		mListViewApps.setOnItemClickListener(this);
		
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
			while (true) {
				try {
					mBlockPackageStep.peek();
					mBlockPackageStep.take();
					mPackagesList = mPackageM.getInstalledPackages(PackageManager.GET_SERVICES | PackageManager.GET_GIDS | PackageManager.GET_ACTIVITIES | PackageManager.GET_PROVIDERS | PackageManager.GET_RECEIVERS | PackageManager.GET_SIGNATURES);//| PackageManager.GET_CONFIGURATIONS
					mHandler.sendEmptyMessage(MSG_REFRESHLIST_AFTER_GET_APPS);
				} catch (Exception e) {
					e.printStackTrace();
					try {
						Thread.sleep(1000);
					} catch (Exception e2) {
						e.printStackTrace();
					}
				}
			}
		}
	};
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case MSG_REFRESHLIST_AFTER_GET_APPS:
				if(isFinishing()) {
					Log.e("", "activity is finished!");
					break;
				}
				if(mProgressBar.getVisibility() == View.VISIBLE) {
					mProgressBar.setVisibility(View.GONE);
				}
				mListViewApps.setVisibility(View.VISIBLE);
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
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		showAppInfosDlg(mAdapter.getShowingAppsList().get(position));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_apps_refresh:
			//刷新
			mListViewApps.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
			putGetAppsStep();
			
			break;
		case R.id.img_apps_back:
			//返回
			finish();
			break;
		default:
			break;
		}
	}
	
	private void showAppInfosDlg(PackageInfo packagesInfo) {
		PackageInfo[] info = new PackageInfo[1];
		info[0] = packagesInfo;
		final ProcessAppsinfoListDialog dialogMode = new ProcessAppsinfoListDialog(this, R.style.dialog, mPackageM);
		dialogMode.setApps(info);
		dialogMode.show();

		// 全屏
		WindowManager.LayoutParams params = dialogMode.getWindow().getAttributes();
		DisplayMetrics dm = new DisplayMetrics();
		AllAppsList.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		params.width = dm.widthPixels;
		params.height = dm.heightPixels;
		dialogMode.getWindow().setAttributes(params);
		dialogMode.getWindow().setGravity(Gravity.CENTER);
	}
}
