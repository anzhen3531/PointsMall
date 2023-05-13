package xyz.ziang.pointsmallservice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@MapperScan("xyz.ziang.pointsmallservice.mapper")
@Configuration
public class MyBatisPlusConfig {}
