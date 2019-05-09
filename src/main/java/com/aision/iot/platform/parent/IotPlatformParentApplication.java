package com.aision.iot.platform.parent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author yim
 * @description 启动类
 * @date 2019/4/26
 */
@SpringBootApplication
@EnableJpaAuditing
public class IotPlatformParentApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotPlatformParentApplication.class, args);
    }

}
