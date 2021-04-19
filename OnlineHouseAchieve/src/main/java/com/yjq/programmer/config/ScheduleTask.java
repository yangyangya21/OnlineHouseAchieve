package com.yjq.programmer.config;

import com.yjq.programmer.pojo.common.House;
import com.yjq.programmer.repository.HouseRepository;
import com.yjq.programmer.service.common.IHouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-02 21:20
 */

/**
 * 定时任务
 */
@Configuration
@EnableScheduling  //开启定时任务
public class ScheduleTask {

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private IHouseService houseService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //redis键名模板
    private final static String HOT_WORD_REDIS_KEY_TEMPLATE = "hot_word";


    private Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    @Scheduled(cron = "0 */10 * * * ?") //10分钟执行一次
    //@Scheduled(cron = "*/5 * * * * ?") //5s执行一次
    private void configureESTasks() {
        logger.info("开始更新ES中数据....");
        //先清空ElasticSearch中的数据
        Iterable<House> houseIterable = houseRepository.findAll();
        for (House house : houseIterable) {
            houseRepository.delete(house);
        }
        //从数据库中查询出数据添加到ES中
        List<House> houseList = houseService.getData().getData();
        for(House house : houseList){
            houseRepository.save(house);
        }
        logger.info("更新ES中数据完成....");
    }

    @Scheduled(cron = "0 0 */1 * * ?") //1小时执行一次
    //@Scheduled(cron = "*/5 * * * * ?") //5s执行一次
    private void configureRedisTasks() {
        //定期清除redis中的热词数据 避免存储过多数据影响性能
        logger.info("开始清除Redis中热词数据....");
        stringRedisTemplate.delete(HOT_WORD_REDIS_KEY_TEMPLATE);
        logger.info("清除Redis中热词数据完成....");
    }
}
