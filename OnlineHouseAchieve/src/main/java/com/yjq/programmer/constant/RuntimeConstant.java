package com.yjq.programmer.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 系统运行时的常量
 * @author 杨杨吖
 *
 */
public class RuntimeConstant {

	//前台用户访问需要拦截但无需验证的url      Arrays.asList：字符串数组转化为List
	public static List<String> userNotNeedConfirmUrl = Arrays.asList(
			"/home/system/index",
			"/home/user/login",
			"/home/user/register",
			"/home/house/renting_list",
			"/home/house/get_data",
			"/home/house/info",
			"/home/house/get_info_data",
			"/home/system/about",
			"/home/house/get_new_house",
			"/home/house/get_renting_house",
			"/home/house/get_purchase_house"
	);

}
