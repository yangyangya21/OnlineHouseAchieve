package com.yjq.programmer.repositoy;

import com.yjq.programmer.OnlineHouseAchieveTest;
import com.yjq.programmer.pojo.common.House;
import com.yjq.programmer.repository.HouseRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-02 11:42
 */
public class HouseRepositoryTest extends OnlineHouseAchieveTest {

    @Autowired
    private HouseRepository houseRepository;

    private Logger logger = LoggerFactory.getLogger(HouseRepositoryTest.class);

    @Test
    public void testSaveOrUpdate(){
        House house = new House();
        house.setId(1L);
        house.setUpdateTime(new Date());
        house.setInfo("中装一室一厅，楼层好，采光足，稀缺房源");
        house.setDetails("半径200米内有：工商银行，建设银行，农业银行，邮电局，中国电信，民航总医院，朝阳区第二医院，小庄医院，易出莲花，华堂商场，京客隆，法宝超市，苏宁电器，国美电器，郭林家常菜，肯德基，麦当劳，必胜客等多家餐厅，北京纺织技术学校，甘露园小学。");
        houseRepository.save(house);
    }
}
