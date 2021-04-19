package com.yjq.programmer.controller.home;

import com.yjq.programmer.form.HouseSearchForm;
import com.yjq.programmer.pojo.common.House;
import com.yjq.programmer.service.common.IHouseService;
import com.yjq.programmer.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-31 22:43
 */

/**
 * 前台房屋控制器
 */
@Controller
@RequestMapping("/home/house")
public class HomeHouseController {


    @Autowired
    private IHouseService houseService;


    /**
     * 前台房屋出租列表页面
     * @return
     */
    @GetMapping("/achieve_list")
    public String achieveList(){
        return "home/house/achieve_house_list";
    }

    /**
     * 前台房屋详情页面
     * @return
     */
    @GetMapping("/info")
    public String info(){
        return "home/house/info";
    }

    /**
     * 前台获取房屋详情数据
     * @param house
     * @return
     */
    @PostMapping("/get_info_data")
    @ResponseBody
    public ResponseVo<House> getInfoData(@RequestBody House house){
        return houseService.getInfoData(house);
    }


    /**
     * 前台获取房屋信息
     * @return
     */
    @PostMapping("/get_data")
    @ResponseBody
    public ResponseVo<List<House>> getData(@RequestBody HouseSearchForm houseSearchForm) throws IOException {
        return houseService.getESData(houseSearchForm);
    }

    /**
     * 和中介聊天操作处理
     * @param house
     * @return
     */
    @PostMapping("/chat_with_agent")
    @ResponseBody
    public ResponseVo<Boolean> chatWithAgent(@RequestBody House house, HttpServletRequest request){
        return houseService.chatWithAgent(house, request);
    }

    /**
     * 前台获取新上房源的房屋信息
     * @return
     * @throws IOException
     */
    @PostMapping("/get_new_house")
    @ResponseBody
    public ResponseVo<List<House>> getDataByCreateTime() throws IOException {
        return houseService.getDataByCreateTime();
    }

    @PostMapping("/get_renting_house")
    @ResponseBody
    public ResponseVo<List<House>> getRentingHouseList() throws IOException {
        return houseService.getRentingHouseList();
    }


    @PostMapping("/get_purchase_house")
    @ResponseBody
    public ResponseVo<List<House>> getPurchaseHouseList() throws IOException {
        return houseService.getPurchaseHouseList();
    }

}
