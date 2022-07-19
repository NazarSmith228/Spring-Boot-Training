package org.spring.training.boot.infrastructure.internal;

import org.spring.training.boot.infrastructure.internal.beans.TalkingService;
import org.spring.training.boot.infrastructure.external.annotation.EnableStringTrimming;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableStringTrimming
public class SpringBootInfrastructureApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication
                .run(SpringBootInfrastructureApplication.class, args);

        TalkingService talkingService = context.getBean(TalkingService.class);
        String result = talkingService.talk(" Some amazing speech", "Me   and folks  ", 5);
        System.out.println(result);
    }
}
