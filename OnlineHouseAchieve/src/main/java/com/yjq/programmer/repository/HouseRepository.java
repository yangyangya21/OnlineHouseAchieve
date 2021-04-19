package com.yjq.programmer.repository;

import com.yjq.programmer.pojo.common.House;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-02 11:34
 */

/**
 * ElasticSearch房屋Repository接口
 */

public interface HouseRepository extends ElasticsearchRepository<House,String> {

}
