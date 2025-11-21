package edu.ucsal.fiadopay.engine;

import edu.ucsal.fiadopay.annotation.AntiFraud;
import edu.ucsal.fiadopay.domain.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    @Autowired
    private ApplicationContext ctx;

    private final List<Rule> rules = new ArrayList<>();

    @PostConstruct
    public void init() {
        String[] beanNames = ctx.getBeanDefinitionNames();
        for (String bn : beanNames) {
            Object bean = ctx.getBean(bn);
            Method[] methods = bean.getClass().getMethods();
            for (Method m : methods) {
                AntiFraud af = m.getAnnotation(AntiFraud.class);
                if (af != null) {
                    Class<?>[] params = m.getParameterTypes();
                    if (params.length == 1 && params[0].equals(Payment.class)
                        && (m.getReturnType().equals(boolean.class) || m.getReturnType().equals(Boolean.class))) {
                        rules.add(new Rule(bean, m, af.name(), af.threshold()));
                        System.out.println("[AntiFraudReflectionEngine] Registered rule '" + af.name()
                                + "' on method " + bean.getClass().getName() + "#" + m.getName()
                                + " threshold=" + af.threshold());
                    } else {
                        System.out.println("[AntiFraudReflectionEngine] Skipped invalid method signature for @AntiFraud on "
                                + bean.getClass().getName() + "#" + m.getName()
                                + " — expected boolean method(Payment)");
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
                    System.out.println("[AntiFraudReflectionEngine] Payment " + payment.getId() + " flagged by " + r.name);
                    return true;
                }
            } catch (Exception e) {
                System.err.println("[AntiFraudReflectionEngine] Error invoking rule " + r.name + ": " + e.getMessage());
                return true;
            }
        }
        return false;
    }
}
