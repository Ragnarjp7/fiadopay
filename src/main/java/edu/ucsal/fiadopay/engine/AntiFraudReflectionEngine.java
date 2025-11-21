package edu.ucsal.fiadopay.engine;

import edu.ucsal.fiadopay.annotation.AntiFraud;
import edu.ucsal.fiadopay.domain.Payment;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Component
public class AntiFraudReflectionEngine {

    private static class Rule {
        final Object bean;
        final Method method;
        final String name;
        final double threshold;

        Rule(Object bean, Method method, String name, double threshold) {
            this.bean = bean;
            this.method = method;
            this.name = name;
            this.threshold = threshold;
        }
    }

    private final List<Rule> rules = new ArrayList<>();

    private final java.util.List<Object> fraudBeans;

    public AntiFraudReflectionEngine(java.util.List<Object> fraudBeans) {
        this.fraudBeans = fraudBeans;
    }

    @PostConstruct
    public void init() {
        for (Object bean : fraudBeans) {
            Method[] methods = bean.getClass().getMethods();

            for (Method m : methods) {
                AntiFraud af = m.getAnnotation(AntiFraud.class);
                if (af != null) {

                    boolean valid =
                        m.getParameterCount() == 1 &&
                        m.getParameterTypes()[0].equals(Payment.class) &&
                        (m.getReturnType().equals(boolean.class) ||
                         m.getReturnType().equals(Boolean.class));

                    if (valid) {
                        rules.add(new Rule(bean, m, af.name(), af.threshold()));
                        System.out.println("[AntiFraudReflectionEngine] Registered rule: " +
                            af.name() + " on " + bean.getClass().getSimpleName() + "#" + m.getName());
                    } else {
                        System.out.println("[AntiFraudReflectionEngine] Invalid rule signature on " +
                            bean.getClass().getSimpleName() + "#" + m.getName());
                    }
                }
            }
        }
    }

    public boolean runChecks(Payment payment) {
        for (Rule r : rules) {
            try {
                Boolean result = (Boolean) r.method.invoke(r.bean, payment);

                if (Boolean.TRUE.equals(result)) {
                    System.out.println("[AntiFraud] Payment " + payment.getId() +
                        " flagged by " + r.name);
                    return true;
                }

            } catch (Exception e) {
                System.err.println("[AntiFraud] ERROR executing rule " + r.name);
                return true;
            }
        }
        return false;
    }
}
