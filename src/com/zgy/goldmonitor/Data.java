package com.zgy.goldmonitor;

/**
 * 此数据是从sina平台获得
 * @Author zhuanggy
 * @Date:2014-6-10
 * @Copyright (c) 2014, 方正电子 All Rights Reserved.
 * @version 
 * @since
 */
public class Data {

	
	public static final int GROUPID_INNER_0_ZHENGZHOU = 0;
	public static final int GROUPID_INNER_1_DALIAN = 1;
	public static final int GROUPID_INNER_2_SHANGHAI = 2;
	public static final int GROUPID_INNER_3_BOHAI = 3;
	public static final int GROUPID_INNER_4_FANYA = 4;
	public static final int GROUPID_OUTER_5_OTHER = 5;

	public static final String[] Group_name = { "郑州商品交易所", "大连商品交易所", "上海期货交易所", "渤海商品交易所", "泛亚有色金属交易所", "外盘商品期货" };// 中国金融期货交易所

	public static final String[] Group_0_name_en = { "TA0", "OI0", "RS0", "RM0", "TC0", "WH0", "JR0", "SR0", "CF0", "RI0", "ME0", "FG0", };
	public static final String[] Group_0_name_cn = { "甲酸", "菜油", "菜籽", "菜粕", "动力煤", "强麦", "粳稻", "白糖", "棉花", "籼稻", "甲醇", "玻璃", };

	public static final String[] Group_1_name_en = { "V0", "P0", "B0", "M0", "I0", "JD0", "L0", "PP0", "FB0", "BB0", "Y0", "C0", "A0", "J0", "JM0" };
	public static final String[] Group_1_name_cn = { "PVC", "棕油", "豆二", "豆粕", "铁矿石", "鸡蛋", "乙烯", "聚丙烯", "纤维板", "胶合板", "豆油", "玉米", "豆一", "焦炭", "焦煤" };

	public static final String[] Group_2_name_en = { "FU0", "AL0", "RU0", "ZN0", "CU0", "AU0", "AG0", "RB0", "PB0", "WR0", "BU0", "HC0" };
	public static final String[] Group_2_name_cn = { "燃油", "沪铝", "橡胶", "沪锌", "沪铜", "黄金", "白银", "螺钢", "沪铅", "线材", "沥青", "热轧卷板" };

	public static final String[] Group_3_name_en = { "BWSS", "BWGS", "BSC", "BRCM", "BRC", "BRBW", "BPTA", "BPET", "BCO", "BCK" };
	public static final String[] Group_3_name_cn = { "绵白糖", "白砂糖", "动力煤", "热卷板中原", "热卷板", "螺纹钢西部", "PTA", "聚酯切片", "原油", "焦炭" };

	public static final String[] Group_4_name_en = { "IN", "GE", "GA", "CO", "BI", "APT" };
	public static final String[] Group_4_name_cn = { "铟", "锗", "金属镓", "电积钴", "铋", "仲钨酸铵" };

	public static final String[] Group_5_name_en = { "hf_GC", "hf_SI", "hf_HG",//
			"hf_NID", "hf_PBD", "hf_SND", "hf_ZSD", "hf_AHD", "hf_CAD",//
			"hf_S", "hf_W", "hf_C", "hf_BO", "hf_SM",//
			"hf_NG", "hf_CL", "hf_LHC",//
			"hf_XAU", "hf_XAG", "hf_XPT", "hf_XPD", //
			"hf_OIL", "hf_TRB" };

	public static final String[] Group_5_name_cn = { "COMEX黄金", "COMEX白银", "COMEX铜",//
			"LME镍", "LME铅", "LME锡", "LME锌", "LME铝", "LME铜",//
			"CBOT黄豆", "CBOT小麦", "CBOT玉米", "CBOT黄豆油", "CBOT黄豆粉",//
			"NYMEX天然气", "NYMEX原油", "CME瘦猪肉",//
			"伦敦金", "伦敦银", "伦敦铂金", "伦敦钯金",//
			"布伦特原油", "日本橡胶" };

	public static class Inner {
		public static final int CHECK_ID_NEW = 8;
		public static final int CHECK_ID_IN = 6;
		public static final int CHECK_ID_OUT = 7;

		public static String getIdName(int id) {
			switch (id) {
			case CHECK_ID_NEW:
				return "最新价";
			case CHECK_ID_IN:
				return "买入价";
			case CHECK_ID_OUT:
				return "卖出价";
			default:
				break;
			}

			return "";
		}
	}

	public static class Outer {
		public static final int CHECK_ID_NEW = 0;
		public static final int CHECK_ID_IN = 2;
		public static final int CHECK_ID_OUT = 3;
		public static final int CHECK_ID_RATE = 1;

		public static String getIdName(int id) {
			switch (id) {
			case CHECK_ID_NEW:
				return "最新价";
			case CHECK_ID_IN:
				return "买入价";
			case CHECK_ID_OUT:
				return "卖出价";
			case CHECK_ID_RATE:
				return "涨跌幅";
			default:
				break;
			}

			return "";
		}
	}

	public static boolean isInner(int groupId) {
		if (groupId < 5) {
			return true;
		}
		return false;
	}

	public static int getItemIdByNameEn(int groupId, String nameEn) {
		int id = 0;
		String[] namesEn = null;
		switch (groupId) {
		case GROUPID_INNER_0_ZHENGZHOU:
			namesEn = Group_0_name_en;
			break;
		case GROUPID_INNER_1_DALIAN:
			namesEn = Group_1_name_en;
			break;
		case GROUPID_INNER_2_SHANGHAI:
			namesEn = Group_2_name_en;
			break;
		case GROUPID_INNER_3_BOHAI:
			namesEn = Group_3_name_en;
			break;
		case GROUPID_INNER_4_FANYA:
			namesEn = Group_4_name_en;
			break;
		case GROUPID_OUTER_5_OTHER:
			namesEn = Group_5_name_en;
			break;
		default:
			break;
		}

		if (namesEn != null) {
			for (int i = 0; i < namesEn.length; i++) {
				if (namesEn[i].equals(nameEn)) {
					return i;
				}
			}
		}

		return id;
	}

	public static String[] getItemNameCn(int groupId) {
		switch (groupId) {
		case GROUPID_INNER_0_ZHENGZHOU:
			return Group_0_name_cn;
		case GROUPID_INNER_1_DALIAN:
			return Group_1_name_cn;
		case GROUPID_INNER_2_SHANGHAI:
			return Group_2_name_cn;
		case GROUPID_INNER_3_BOHAI:
			return Group_3_name_cn;
		case GROUPID_INNER_4_FANYA:
			return Group_4_name_cn;
		case GROUPID_OUTER_5_OTHER:
			return Group_5_name_cn;
		default:
			break;
		}
		return null;
	}

	public static String getItemNameCn(int groupId, int itemId) {
		switch (groupId) {
		case GROUPID_INNER_0_ZHENGZHOU:
			return Group_0_name_cn[itemId];
		case GROUPID_INNER_1_DALIAN:
			return Group_1_name_cn[itemId];
		case GROUPID_INNER_2_SHANGHAI:
			return Group_2_name_cn[itemId];
		case GROUPID_INNER_3_BOHAI:
			return Group_3_name_cn[itemId];
		case GROUPID_INNER_4_FANYA:
			return Group_4_name_cn[itemId];
		case GROUPID_OUTER_5_OTHER:
			return Group_5_name_cn[itemId];
		default:
			break;
		}
		return "";
	}

	public static String getItemNameEn(int groupId, int itemId) {
		switch (groupId) {
		case GROUPID_INNER_0_ZHENGZHOU:
			return Group_0_name_en[itemId];
		case GROUPID_INNER_1_DALIAN:
			return Group_1_name_en[itemId];
		case GROUPID_INNER_2_SHANGHAI:
			return Group_2_name_en[itemId];
		case GROUPID_INNER_3_BOHAI:
			return Group_3_name_en[itemId];
		case GROUPID_INNER_4_FANYA:
			return Group_4_name_en[itemId];

		case GROUPID_OUTER_5_OTHER:
			return Group_5_name_en[itemId];
		default:
			break;
		}
		return "";
	}
}

// AU0 黄金
// AG0 白银
// FU0 燃油
// RB0 螺纹钢
// CU0 沪铜
// AL0 沪铝
// ZN0 沪锌
// PB0 沪铅
// RU0 橡胶
// WR0 线材
// A0 大豆
// M0 豆粕
// Y0 豆油
// J0 焦炭
// C0 玉米
// L0 乙烯
// P0 棕油
// V0 PVC
// RS0 菜籽
// RM0 菜粕
// FG0 玻璃
// CF0 棉花
// WS0 强麦
// ER0 籼稻
// ME0 甲醇
// RO0 菜油
// TA0 甲酸

// var hq_str_hf_CL="104.09,  -0.0192,    104.09,104.10,104.29,104.05,13:01:38,104.11,104.14,642,0,0,2014-05-28,NYMEX原油";
// var hq_str_hf_GC="1262.7,  -0.2213,    1262.7,1262.8,1265.5,1260.8,13:01:40,1265.5,1264.4,2239,0,0,2014-05-28,COMEX黄金";
// var hq_str_hf_SI="19.080,  0.0524,     19.080,19.080,19.130,19.040,13:01:37,19.070,19.070,283,0,0,2014-05-28,COMEX白银";
// var hq_str_hf_CAD="6925.50, -0.1226,   6924.00,6927.00,6942.75,6911.00,13:01:23,6934.00,6933.75,434,0,0,2014-05-28,LME铜";
// var hq_str_hf_ZSD="2084.00, -0.1677,   2084.00,2084.25,2089.75,2083.00,13:01:31,2087.50,2084.00,26,0,0,2014-05-28,LME锌";
// var hq_str_hf_S="1499.00,   0.6885,    1498.75,1499.00,1500.75,1491.25,13:00:06,1488.75,1492.00,663,0,0,2014-05-28,CBOT黄豆";
// var hq_str_hf_C="468.75,    -0.2129,   468.75,469.00,469.50,468.50,13:00:06,469.75,468.75,70,0,0,2014-05-28,CBOT玉米";
// var hq_str_hf_W="637.75,    -0.5070,   638.00,638.25,639.50,637.50,12:59:56,641.00,638.50,74,0,0,2014-05-28,CBOT小麦";
