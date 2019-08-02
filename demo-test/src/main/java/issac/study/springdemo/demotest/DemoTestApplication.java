package issac.study.springdemo.demotest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages ={"issac.study.springdemo.core","issac.study.springdemo.demotest"} )
public class DemoTestApplication {

    public static void main(String[] args) {

       SpringApplication.run(DemoTestApplication.class, args);

    }

}
