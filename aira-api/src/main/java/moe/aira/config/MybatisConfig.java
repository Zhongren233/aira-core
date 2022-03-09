package moe.aira.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@Slf4j
public class MybatisConfig {
    @Value(
            "${aira.core.async.dao-poolSize}"
    )
    private Integer threadPoolSize;


    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean("daoAsyncExecutor")
    public Executor daoAsyncExecutor() {
        return Executors.newFixedThreadPool(threadPoolSize, new ThreadFactory() {
            private final AtomicInteger atomicInteger = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                int andAdd = atomicInteger.getAndAdd(1);
                String name = "dao-async" + andAdd;
                log.info("daoAsyncExecutor:create {}", name);
                return new Thread(r, name);
            }
        });
    }
}
