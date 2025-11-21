package edu.ucsal.fiadopay.config;

import edu.ucsal.fiadopay.rules.FraudRules;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AntiFraudConfig {

    @Bean
    public List<Object> fraudBeans(FraudRules rules) {
        return List.of(rules);
    }
}
