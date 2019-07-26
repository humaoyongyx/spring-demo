package issac.study.springdemo.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author  issac.hu
 */
@SpringBootApplication(scanBasePackages ={"issac.study.springdemo.core","issac.study.springdemo.service"} )
public class DemoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoServiceApplication.class, args);
    }

}
