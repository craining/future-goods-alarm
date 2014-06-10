package com.zgy.goldmonitor.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.text.TextUtils;

import com.zgy.goldmonitor.Data;
import com.zgy.goldmonitor.Debug;
import com.zgy.goldmonitor.activity_fragment.MainActivity;
import com.zgy.goldmonitor.bean.Info;
import com.zgy.goldmonitor.bean.InfoList;

public class PraseUtil {

	public static InfoList prase( boolean inner_outer, String response) {
		InfoList list = new InfoList();
		list.infos = new HashMap<String, Info>();
		String[] valuesStr = response.split(";");

		for (String s : valuesStr) {
			Info i = new Info();
			s = s.substring(s.indexOf("=") + 2, s.length() - 1);
			Debug.e("", "s=" + s);
			String[] valuesOne = s.split(",");
			if(inner_outer) {
				i.names = Info.names_inner;
				i.values = valuesOne;
				list.infos.put(valuesOne[16], i);
			} else {
				i.names = Info.names_outer;
				i.values = valuesOne;
				list.infos.put(valuesOne[13], i);
			}
		}

		return list;
	}
}
