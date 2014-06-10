package com.zgy.goldmonitor.activity_fragment;

import java.util.Date;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zgy.goldmonitor.Data;
import com.zgy.goldmonitor.Debug;
import com.zgy.goldmonitor.Preference;
import com.zgy.goldmonitor.R;
import com.zgy.goldmonitor.R.id;
import com.zgy.goldmonitor.R.layout;
import com.zgy.goldmonitor.util.ImageLoaderUtil;
import com.zgy.goldmonitor.views.PicShowDlg;
import com.zgy.goldmonitor.views.ToastView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class FragmentChartWeek extends Fragment {
	private View mMainView;
	private ImageView mImgChart;
	public FragmentChartWeek() {
		super();
	}

	public static final String url = "http://image.sinajs.cn/newchart/v5/futures/weekly/";
	public static final String url_global = "http://image.sinajs.cn/newchart/v5/futures/global/weekly/";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.fragment_chartshow, container, false);
		mImgChart = (ImageView) mMainView.findViewById(R.id.img_chart_show);
		refreshPicChart();
		return mMainView;
	}

	public void refreshPicChart() {
		
		if(Preference.getInstance().is3GNoPicOn()) {
			mImgChart.setImageResource(R.drawable.bg_onlineimg_noshow);
			mImgChart.setEnabled(false);
			return;
		}
		mImgChart.setEnabled(true);
		final int groupId = MainActivity.selectedGroupId;
		final int itemid = MainActivity.selectedItemId;
		
		String nameId = Data.getItemNameEn(groupId, itemid);
//		final String urlImg = Data.isInner(groupId) ? url + nameId + ".gif" : url_global + nameId.substring(3, nameId.length()) + ".gif";
		final String urlImg = (Data.isInner(groupId) ? url + nameId + ".gif" : url_global + nameId.substring(3, nameId.length()) + ".gif") + "&rn=" + (new Date()).getTime() + (Math.random() + "").toString().replace("0.", "");
		Debug.e("", "img url=" + urlImg);
		ImageLoader.getInstance().displayImage(urlImg, mImgChart, ImageLoaderUtil.getInstance().getNormalPicOpinions(), null);

		mImgChart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
					PicShowDlg.showPic(getActivity(), Data.getItemNameCn(groupId, itemid), urlImg);
				 
			}
		});
	}
}
