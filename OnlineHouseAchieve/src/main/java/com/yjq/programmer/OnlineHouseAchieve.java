package com.yjq.programmer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * 项目启动类
 */

@SpringBootApplication
@MapperScan("com.yjq.programmer.dao")
@EnableElasticsearchRepositories(basePackages = "com.yjq.programmer.repository")
public class OnlineHouseAchieve
{
    public static void main( String[] args )
    {
        SpringApplication.run(OnlineHouseAchieve.class, args);
    }
}
