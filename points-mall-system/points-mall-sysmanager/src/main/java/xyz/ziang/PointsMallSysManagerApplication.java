package xyz.ziang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients("xyz.ziang.client")
public class PointsMallSysManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PointsMallSysManagerApplication.class, args);
    }

}
