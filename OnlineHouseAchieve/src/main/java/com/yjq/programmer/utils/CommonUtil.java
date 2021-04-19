package com.yjq.programmer.utils;

import com.yjq.programmer.bean.CodeMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-01-28 14:30
 */

/**
 * 通用工具类
 */
public class CommonUtil {

    private Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * 验证字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if(str == null || "".equals(str)) {
            return true; //为空
        }else {
            return false; //不为空
        }
    }


    /**
     * 判断请求是否是ajax
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request){
        String header = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(header))return true; //是ajax请求
        return false; //不是ajax请求
    }

    /**
     * 判断请求是否是Axios
     * @param request
     * @return
     */
    public static boolean isAxios(HttpServletRequest request){
        String accept = request.getHeader("accept");
        if (accept == null || accept.contains("application/json")) {
            return true; //是axios请求
        }else{
            return false; //不是axios请求
        }
    }


    /**
     * 验证手机号是否符合规范格式
     * @param phone
     * @return
     */
    public static CodeMsg validatePhone(String phone){
        CodeMsg codeMsg = new CodeMsg();
        if(isEmpty(phone)){
            codeMsg.setCode(-2);
            codeMsg.setMsg("手机号码不能为空！");
            return codeMsg;
        }
        if(phone.trim().length() != 11){
            codeMsg.setCode(-2);
            codeMsg.setMsg("请输入正确格式的手机号码！");
            return codeMsg;
        }else{
            try{
                long long_phone = Long.parseLong(phone);
                if(long_phone <= 0){
                    codeMsg.setCode(-2);
                    codeMsg.setMsg("请输入正确格式的手机号码！");
                    return codeMsg;
                }
            }catch(Exception e){
                e.printStackTrace();
                codeMsg.setCode(-2);
                codeMsg.setMsg("请输入正确格式的手机号码！");
                return codeMsg;
            }
        }
        return CodeMsg.SUCCESS;
    }

    /**
     * 对象转化为字节码
     * @param obj
     * @return
     * @throws Exception
     */
    public static byte[] getBytesFromObject(Serializable obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(obj);
        return bo.toByteArray();
    }

    /**
     * 字节码转化为对象
     * @param objBytes
     * @return
     * @throws Exception
     */
    public static Object getObjectFromBytes(byte[] objBytes) throws Exception {
        if (objBytes == null || objBytes.length == 0) {
            return null;
        }
        ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);
        ObjectInputStream oi = new ObjectInputStream(bi);
        return oi.readObject();
    }


    /**
     * 判断后缀是否是图片文件的后缀
     * @param suffix
     * @return
     */
    public static boolean isPhoto(String suffix){
        if("jpg".toUpperCase().equals(suffix.toUpperCase())){
            return true;
        }else if("png".toUpperCase().equals(suffix.toUpperCase())){
            return true;
        }else if("gif".toUpperCase().equals(suffix.toUpperCase())){
            return true;
        }else if("jpeg".toUpperCase().equals(suffix.toUpperCase())){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 返回指定格式的日期
     * @param str_date
     * @param formatter
     * @return
     */
    public static Date getFormatterDate(String str_date, String formatter){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatter);
        try{
            Date date = simpleDateFormat.parse(str_date);
            return date;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 截取文件路径中的日期部分
     * @param filePath
     * @return
     */
    public static String getFileDate(String filePath){
        filePath = filePath.substring(0,filePath.lastIndexOf("/")); //把路径去掉文件名部分
        String fileDate = filePath.substring(filePath.lastIndexOf("/") + 1);
        return fileDate;
    }


    /**
     * 返回指定格式的日期字符串
     * @param date
     * @param formatter
     * @return
     */
    public static String getFormatterDate(Date date, String formatter){
        SimpleDateFormat sdf = new SimpleDateFormat(formatter);
        return sdf.format(date);
    }

    /**
     * 获取时间戳
     * @param date
     * @return
     */
    public static long getTimeStamp(Date date) {
        long times = date.getTime();
        return times;
    }
}
