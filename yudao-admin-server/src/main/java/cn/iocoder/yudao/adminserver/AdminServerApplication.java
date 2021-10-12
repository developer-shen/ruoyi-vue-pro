package cn.iocoder.yudao.adminserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SuppressWarnings("SpringComponentScan") // 忽略 IDEA 无法识别 ${yudao.info.base-package} 和 ${yudao.core-service.base-package}
@SpringBootApplication(scanBasePackages = {"${yudao.info.base-package}", "${yudao.core-service.base-package}"})
public class AdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminServerApplication.class, args);
    }

}
