package com.colak.springtutorial.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@Slf4j
public class TaskExecutorConfig {

    private static final int AWAIT_TERMINATION_SECONDS = 90;

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        // Core number of threads
        threadPoolTaskExecutor.setCorePoolSize(1);
        // Maximum number of threads
        threadPoolTaskExecutor.setMaxPoolSize(5);
        // Capacity for queued tasks
        threadPoolTaskExecutor.setQueueCapacity(500);
        threadPoolTaskExecutor.setThreadNamePrefix("MyAsyncThread-");
        threadPoolTaskExecutor.setRejectedExecutionHandler(this::logRejection);

        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setAwaitTerminationSeconds(AWAIT_TERMINATION_SECONDS);

        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;
    }

    // If you want to use SecurityContextHolder passed with @Async everytime, you change your configuration and
    // use DelegatingSecurityContextAsyncTaskExecutor.

    // See https://medium.com/@office.yeon/graceful-shutdown-in-spring-boot-with-sync-and-async-tasks-a8f8d89ee252
    // It’s crucial to note that the shutdown method is invoked from the destroy method, which is overridden from DisposableBean.
    // This implies that you must define ThreadPoolTaskExecutor as its own bean when employing DelegatingSecurityContextExecutor.
    // Failure to do so, will result in the shutdown method not being properly called during the Spring bean's lifecycle,
    // potentially causing the executor not to shut down as expected.
    @Bean
    public Executor taskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        // return new DelegatingSecurityContextExecutor(threadPoolTaskExecutor);
        return new DelegatingSecurityContextAsyncTaskExecutor(threadPoolTaskExecutor);
    }

    private void logRejection(Runnable runnable, ThreadPoolExecutor taskExecutor) {
        log.warn("Task rejected, thread pool is full and queue is also full");
    }
}
