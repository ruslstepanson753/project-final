package com.javarush.jira;

import com.javarush.jira.bugtracking.task_tag.TaskTag;
import com.javarush.jira.bugtracking.task_tag.TaskTagRepository;
import com.javarush.jira.common.internal.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@EnableCaching
public class JiraRushApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(JiraRushApplication.class, args);
//        TaskTagRepository taskTagRepository = context.getBean(TaskTagRepository.class);
//        TaskTag taskTag= new TaskTag(1, "qqqqqqqqqqqq");
//        taskTagRepository.save(taskTag);
//        TaskTag taskTag1 = taskTagRepository.getExisted(1L);
//        System.out.println(taskTag1);

    }

}
