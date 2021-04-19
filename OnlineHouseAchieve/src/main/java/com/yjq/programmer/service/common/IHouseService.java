package com.yjq.programmer.service.common;

import com.yjq.programmer.form.HouseSearchForm;
import com.yjq.programmer.pojo.common.House;
import com.yjq.programmer.vo.ResponseVo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-30 18:05
 */

/**
 * 用户Service接口
 */
public interface IHouseService {

    //后台获取房屋列表信息
    Map<String, Object> getHouseList(Integer page, Integer rows, String info, Integer state, HttpServletRequest request);

    //后台添加房屋信息操作处理
    ResponseVo<Boolean> addHouse(House house, HttpServletRequest request);

    //后台修改房屋信息操作处理
    ResponseVo<Boolean> editHouse(House house, HttpServletRequest request);

    //后台删除房屋信息操作处理
    ResponseVo<Boolean> deleteHouse(String ids);

    //后台管理员修改房屋状态操作处理
    ResponseVo<Boolean> editStateByAdmin(Long id, Integer state);

    //后台中介修改房屋状态操作处理
    ResponseVo<Boolean> editStateByAgent(Long id, Integer state);

    //定时任务中获取全部房屋信息数据
    ResponseVo<List<House>> getData();

    //前台获取ES中的所有房屋信息数据
    ResponseVo<List<House>> getESData(HouseSearchForm houseSearchForm) throws IOException;

    //前台获取房屋详情数据
    ResponseVo<House> getInfoData(House house);

    //前台和中介聊天操作处理
    ResponseVo<Boolean> chatWithAgent(House house, HttpServletRequest request);

    //前台获取新上房源的信息
    ResponseVo<List<House>> getDataByCreateTime() throws IOException;

    //前台获取租房信息数据
    ResponseVo<List<House>> getRentingHouseList() throws IOException;

    //前台获取购房信息数据
    ResponseVo<List<House>> getPurchaseHouseList() throws IOException;

}
