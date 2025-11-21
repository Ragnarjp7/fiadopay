package edu.ucsal.fiadopay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ExecutorConfig {

    @Bean(destroyMethod = "shutdown")
    public ExecutorService paymentExecutor() {
        ThreadFactory tf = r -> {
            Thread t = new Thread(r);
            t.setName("fiadopay-worker-" + t.getId());
            t.setDaemon(false);
            return t;
        };
        return new ThreadPoolExecutor(2, 8, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(500),
                tf,
                new ThreadPoolExecutor.AbortPolicy());
    }
}
