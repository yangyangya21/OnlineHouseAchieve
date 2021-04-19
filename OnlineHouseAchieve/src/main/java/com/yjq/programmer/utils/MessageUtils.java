package com.yjq.programmer.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjq.programmer.pojo.home.ResultMessage;


/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-02-22 0:01
 */

/**
 * 用来封装消息的工具类
 */
public class MessageUtils {

    public static String getMessage(boolean isSystemMessage, String fromName, Object message){
        try{
            ResultMessage result = new ResultMessage();
            result.setSystem(isSystemMessage);
            result.setMessage(message);
            if(fromName != null){
                result.setFromName(fromName);
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(result);
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
