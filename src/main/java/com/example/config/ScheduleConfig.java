package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.example.compoment.MessageTask;

/**定时任务配置*/
@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {
    
    @Autowired
    private MessageTask messageTask;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addFixedRateTask(messageTask, 5000);
        
    }
}
