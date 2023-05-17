package xyz.ziang.pointsmallauthorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients({"xyz.ziang.pointsmallauthorization.client"})
@SpringBootApplication
public class PointsMallAuthorizationApplication {
    public static void main(String[] args) {
        SpringApplication.run(PointsMallAuthorizationApplication.class, args);
    }
}
