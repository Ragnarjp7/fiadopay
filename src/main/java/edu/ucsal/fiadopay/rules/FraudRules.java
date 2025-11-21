package edu.ucsal.fiadopay.rules;

import edu.ucsal.fiadopay.annotation.AntiFraud;
import edu.ucsal.fiadopay.domain.Payment;
import org.springframework.stereotype.Component;

@Component
public class FraudRules {

    @AntiFraud(name = "HighAmount", threshold = 1000.0)
    public boolean ruleHighAmount(Payment p){
        if (p.getTotalWithInterest() == null) return false;
        try {
            return p.getTotalWithInterest().doubleValue() > 1000.0;
        } catch (Exception e) {
            return true;
        }
    }

    @AntiFraud(name = "BlockBoleto", threshold = 0.0)
    public boolean ruleBlockBoleto(Payment p){
        return "BOLETO".equalsIgnoreCase(p.getMethod())
               && p.getAmount() != null
               && p.getAmount().doubleValue() > 5000.0;
    }
}
