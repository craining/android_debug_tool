package com.zgy.debugtool.processinfo;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zgy.debugtool.main.R;
import com.zgy.debugtool.util.JumpUtil;
import com.zgy.debugtool.view.MarqueeTextView;

/**
 * 
 * @Description:自定义中间是list的Dialog
 * @author:hanlx
 * @see:
 * @since:
 * @Date:2013-1-14
 */
public class ProcessAppsinfoListDialog extends Dialog {
	private Context con;
	private PackageInfo[] apps;
	private View.OnClickListener cancelListener;
	private AppinfoListAdapter mAdapter;
	private TextView titleView;
	private ListView listViews;
	private ImageView cancelButton;
	private String title;
	private PackageManager pm;

	private static final String TAG_NEW_APP = "new app";

	public ProcessAppsinfoListDialog(Context context, int theme, PackageManager pm) {
		super(context, theme);
		this.con = context;
		this.pm = pm;
	}

	public void setCancelListener(View.OnClickListener clickListener) {
		this.cancelListener = clickListener;
	}

	public void setApps(PackageInfo[] apps) {
		this.apps = apps;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_dialog_apps);
		initView();
	}

	public void initView() {
		titleView = (TextView) findViewById(R.id.title_list_dialog_apps);
		if (title != null && title.length() > 0) {
			titleView.setText(title);
		}

		listViews = (ListView) findViewById(R.id.list_dialog_apps_lists);

		List<Appinfo> appinfos = new ArrayList<Appinfo>();

		for (PackageInfo app : apps) {
			Appinfo newapp = new Appinfo(TAG_NEW_APP, app.packageName);
			newapp.icon = app.applicationInfo.loadIcon(pm);
			newapp.name = (String) pm.getApplicationLabel(app.applicationInfo);
			appinfos.add(newapp);
			appinfos.add(new Appinfo("processName", app.applicationInfo.processName));
			// appinfos.add(new Appinfo("className", app.applicationInfo.className));
			appinfos.add(new Appinfo("dataDir", app.applicationInfo.dataDir));
			appinfos.add(new Appinfo("sourceDir", app.applicationInfo.sourceDir));
			// appinfos.add(new Appinfo("publicSourceDir", app.applicationInfo.publicSourceDir));
			// appinfos.add(new Appinfo("uid", app.applicationInfo.uid + ""));
			// appinfos.add(new Appinfo("description", app.applicationInfo.descriptionRes == 0 ? "" : con.getString(app.applicationInfo.descriptionRes)));
			appinfos.add(new Appinfo("targetSdkVersion", app.applicationInfo.targetSdkVersion + ""));

			// if(app.requestedPermissions != null) {
			// int permissionCount = app.requestedPermissions.length;
			// StringBuffer sb = new StringBuffer();
			// for (int i=0; i< permissionCount; i++) {
			// if(i < permissionCount-1) {
			// sb.append(app.requestedPermissions[i]+ "\r\n");
			// } else {
			// sb.append(app.requestedPermissions[i]);
			// }
			// }
			// appinfos.add(new Appinfo("permission", sb.toString()));
			// } else {
			// appinfos.add(new Appinfo("permission", "没有任何权限"));
			// }

			// if(app.permissions != null) {
			// int permissionCount = app.permissions.length;
			// StringBuffer sb = new StringBuffer();
			// for (int i=0; i< permissionCount; i++) {
			// if(i < permissionCount-1) {
			// sb.append(app.permissions[i].name+ "\r\n");
			// } else {
			// sb.append(app.permissions[i].name);
			// }
			// }
			// appinfos.add(new Appinfo("permission", sb.toString()));
			// } else {
			// appinfos.add(new Appinfo("permission", "没有任何权限"));
			// }

		}

		listViews.setAdapter(new AppinfoListAdapter(appinfos));

		// listView = (ListView) findViewById(R.id.value_list_list_dialog_apps);
		// listView.setAdapter(mAdapter);

		cancelButton = (ImageView) findViewById(R.id.img_list_dialog_apps_close);
		if (cancelListener != null) {
			cancelButton.setOnClickListener(cancelListener);
		} else {
			cancelButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ProcessAppsinfoListDialog.this.cancel();
				}

			});
		}
	}

	/**
	 * 
	 * @Description:dialog的ListView 的Adapter
	 * @author:hanlx
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-1-14
	 */
	class AppinfoListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		private List<Appinfo> mAppInfos;

		public AppinfoListAdapter(List<Appinfo> appInfos) {
			this.mAppInfos = appInfos;
			mInflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return mAppInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return mAppInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.list_dialog_apps_item, null);
				holder = new ViewHolder();
				holder.textLabel = (TextView) convertView.findViewById(R.id.text_listitem_appinfo_label);
				holder.textValue = (MarqueeTextView) convertView.findViewById(R.id.text_listitem_appinfo_value);
				holder.layoutTag = (RelativeLayout) convertView.findViewById(R.id.layout_listitem_appinfo_tag);
				holder.layoutContent = (LinearLayout) convertView.findViewById(R.id.layout__listitem_appinfo_content);
				holder.textAppname = (MarqueeTextView) convertView.findViewById(R.id.text_listitem_appinfo_name);
				holder.imgAppIcon = (ImageView) convertView.findViewById(R.id.img_listitem_appinfo_icon);
				holder.btnMore = (Button) convertView.findViewById(R.id.btn_listitem_appinfo_more);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (mAppInfos.get(position).label.equals(TAG_NEW_APP)) {
				holder.layoutContent.setVisibility(View.GONE);
				holder.layoutTag.setVisibility(View.VISIBLE);
				if (mAppInfos.get(position).icon != null) {
					holder.imgAppIcon.setVisibility(View.VISIBLE);
					holder.imgAppIcon.setImageDrawable(mAppInfos.get(position).icon);
				} else {
					holder.imgAppIcon.setVisibility(View.GONE);
				}
				holder.textAppname.setText(mAppInfos.get(position).name + "");
				holder.textAppname.setMarquee(true);
				holder.btnMore.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						JumpUtil.showInstalledAppDetails(con, mAppInfos.get(position).value);
					}
				});
			} else {
				holder.layoutContent.setVisibility(View.VISIBLE);
				holder.layoutTag.setVisibility(View.GONE);
				holder.textLabel.setText(mAppInfos.get(position).label);
				holder.textValue.setText(mAppInfos.get(position).value);
			}

			return convertView;
		}

		public class ViewHolder {
			TextView textLabel;
			MarqueeTextView textValue;
			RelativeLayout layoutTag;
			LinearLayout layoutContent;
			MarqueeTextView textAppname;
			ImageView imgAppIcon;
			Button btnMore;
		}

	}

}
