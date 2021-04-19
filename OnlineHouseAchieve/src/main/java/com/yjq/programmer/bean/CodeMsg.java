package com.yjq.programmer.bean;

import java.io.Serializable;

/**
 * 错误码统一处理类，所有的错误码统一定义在这里
 * @author 杨杨吖
 *
 */
public class CodeMsg implements Serializable {

	private Integer code;//错误码
	
	private String msg;//错误信息
	
	/**
	 * 构造函数私有化即单例模式
	 * 该类负责创建自己的对象，同时确保只有单个对象被创建。这个类提供了一种访问其唯一的对象的方式，可以直接访问，不需要实例化该类的对象。
	 * @param code
	 * @param msg
	 */
	private CodeMsg(Integer code, String msg){
		this.code = code;
		this.msg = msg;
	}
	
	public CodeMsg() {
		
	}
	
	public Integer getCode() {
		return code;
	}



	public void setCode(Integer code) {
		this.code = code;
	}



	public String getMsg() {
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	//通用错误码定义
	//处理成功消息码
	public static CodeMsg SUCCESS = new CodeMsg(0, "success");
	//通用数据错误码
	public static CodeMsg DATA_ERROR = new CodeMsg(-1, "非法数据！");
	public static CodeMsg VALIDATE_ENTITY_ERROR = new CodeMsg(-2, "");
	public static CodeMsg CPACHA_EMPTY = new CodeMsg(-3, "验证码不能为空!");
	public static CodeMsg SESSION_EXPIRED = new CodeMsg(-4, "会话已失效，请刷新页面重试！");
	public static CodeMsg CPACHA_ERROR = new CodeMsg(-5, "验证码错误！");
	public static CodeMsg USER_SESSION_EXPIRED = new CodeMsg(-6, "还未登录或会话失效，请重新登录！");
	public static CodeMsg UPLOAD_PHOTO_SUFFIX_ERROR = new CodeMsg(-7, "图片格式不正确！");
	public static CodeMsg UPLOAD_PHOTO_ERROR = new CodeMsg(-8, "图片上传失败！");
	public static CodeMsg UPLOAD_ATTACHMENT_ERROR = new CodeMsg(-9, "附件上传失败！");
	public static CodeMsg DOWNLOAD_FILE_ERROR = new CodeMsg(-10, "文件下载失败！");
	public static CodeMsg CPACHA_EXPIRE = new CodeMsg(-11, "验证码已过期，请刷新验证码！");
	public static CodeMsg SYSTEM_ERROR = new CodeMsg(-12, "系统出现了错误，请联系管理员！");
	public static CodeMsg NO_AUTHORITY = new CodeMsg(-13, "不好意思，您没有权限操作哦！");
	public static CodeMsg COMMON_ERROR = new CodeMsg(-14, "");


	//系统管理错误码
	public static CodeMsg USERNAME_OR_PASSWORD_ERROR = new CodeMsg(-5000, "用户昵称或者密码错误！");
	public static CodeMsg USER_STATE_ERROR = new CodeMsg(-5001, "该用户已被冻结！无法登录！");
	public static CodeMsg USER_AUTHORITY_ERROR = new CodeMsg(-5002, "该用户没有任何权限！无法登录！");
	public static CodeMsg PERSON_INFO_SAVE_ERROR = new CodeMsg(-5003, "个人信息保存失败！请联系管理员！");
	

	//用户管理错误码
	public static CodeMsg USER_REPASSWORD_EMPTY  = new CodeMsg(-7000, "确认密码不能为空！");
	public static CodeMsg USER_REPASSWORD_ERROR  = new CodeMsg(-7001, "两次密码输入不一致！");
	public static CodeMsg USER_ADD_ERROR  = new CodeMsg(-7002, "用户信息添加失败，请联系管理员！");
	public static CodeMsg USER_USERNAME_ALREADY_EXIST  = new CodeMsg(-7003, "用户昵称已经存在，请换一个！");
	public static CodeMsg USER_USERNAME_EMPTY  = new CodeMsg(-7004, "用户昵称不能为空！");
	public static CodeMsg USER_PASSWORD_EMPTY  = new CodeMsg(-7005, "用户密码不能为空！");
	public static CodeMsg USER_NOT_EXIST  = new CodeMsg(-7006, "该用户不存在！");
	public static CodeMsg USER_INFO_EDIT_ERROR  = new CodeMsg(-7007, "用户个人信息修改失败，请联系管理员！");
	public static CodeMsg USER_PREPASSWORD_EMPTY  = new CodeMsg(-7008, "原密码不能为空！");
	public static CodeMsg USER_NEWPASSWORD_EMPTY  = new CodeMsg(-7009, "新密码不能为空！");
	public static CodeMsg USER_RENEWPASSWORD_EMPTY  = new CodeMsg(-7010, "确认新密码不能为空！");
	public static CodeMsg USER_PREPASSWORD_ERROR  = new CodeMsg(-7011, "原密码错误！");
	public static CodeMsg USER_RENEWPASSWORD_ERROR  = new CodeMsg(-7012, "新密码和确认新密码输入不一致！");
	public static CodeMsg USER_PASSWORD_EDIT_ERROR  = new CodeMsg(-7013, "用户密码修改失败，请联系管理员！");
	public static CodeMsg USER_DELETE_ERROR  = new CodeMsg(-7014, "用户删除失败，请联系管理员！");
	public static CodeMsg USER_EMAIL_ALREADY_EXIST  = new CodeMsg(-7015, "用户电子邮箱已经被注册，请换一个！");
	public static CodeMsg USER_PHONE_ALREADY_EXIST  = new CodeMsg(-7016, "用户手机号码已经被注册，请换一个！");
	public static CodeMsg REGISTER_ERROR = new CodeMsg(-7017, "用户注册失败，请联系管理员！");
	public static CodeMsg USER_FREEZE = new CodeMsg(-7018, "用户已被冻结，无法登录！");
	public static CodeMsg ADD_APPLY_AGENT_ERROR = new CodeMsg(-7019, "添加申请成为中介数据失败，请联系管理员！");
	public static CodeMsg APPLY_AGENT_ALREADY = new CodeMsg(-7020, "你已提交过申请了，请勿重复提交！");

	//权限管理类错误码
	public static CodeMsg AUTHORITY_NAME_EXIST = new CodeMsg(-8000, "该角色已有该权限，请换一个！");
	public static CodeMsg AUTHORITY_ADD_ERROR = new CodeMsg(-8001, "添加权限信息失败，请联系管理员！");
	public static CodeMsg AUTHORITY_EDIT_ERROR = new CodeMsg(-8002, "修改权限信息失败，请联系管理员！");
	public static CodeMsg AUTHORITY_DELETE_ERROR = new CodeMsg(-8003, "删除权限信息失败，请联系管理员！");

	//聊天管理错误码
	public static CodeMsg AGENT_NOT_ONLINE  = new CodeMsg(-9000, "中介目前不在线哦，请稍后再试！");
	public static CodeMsg CHAT_WITH_MYSELF  = new CodeMsg(-9001, "不能和自己聊天哦！");
	public static CodeMsg AGENT_CHAT_WITH_AGENT  = new CodeMsg(-9002, "中介不能和其他中介聊天哦！");

	//房屋管理错误码
	public static CodeMsg HOUSE_ADD_ERROR  = new CodeMsg(-10000, "房屋添加失败，请联系管理员！");
	public static CodeMsg HOUSE_DELETE_ERROR  = new CodeMsg(-10001, "房屋删除失败，请联系管理员！");
	public static CodeMsg HOUSE_EDIT_ERROR  = new CodeMsg(-10002, "房屋修改失败，请联系管理员！");
	public static CodeMsg HOUSE_USER_EMPTY  = new CodeMsg(-10003, "房屋所属用户不能为空！");
	public static CodeMsg HOUSE_NOT_EXIST  = new CodeMsg(-10004, "该房屋不存在！");

	//预约时间管理错误码
	public static CodeMsg TIME_NOT_LEGAL  = new CodeMsg(-11000, "预约看房时间不能小于当前时间！");
	public static CodeMsg ADD_ORDER_TIME_ERROR  = new CodeMsg(-11001, "添加预约看房时间失败，请联系管理员！");
	public static CodeMsg ORDER_TIME_NOT_EXIST  = new CodeMsg(-11002, "该预约看房信息已不存在！");


}
