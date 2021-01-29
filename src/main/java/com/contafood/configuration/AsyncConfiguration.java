package com.contafood.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean(name = "threadPoolTaskExecutorReport")
    public Executor threadPoolTaskExecutorReport() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(10);
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setThreadNamePrefix("Thread_CF_R_");
        threadPoolTaskExecutor.setAwaitTerminationSeconds(600);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
