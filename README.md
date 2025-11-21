# FiadoPay Simulator (Spring Boot + H2)

Gateway de pagamento **FiadoPay** para a AVI/POOA.
Substitui PSPs reais com um backend em memória (H2).

Rodar
./mvnw spring-boot:run
# ou
mvn spring-boot:run


H2 Console:
http://localhost:8080/h2

(JDBC: jdbc:h2:mem:fiadopay)

Swagger UI:
http://localhost:8080/swagger-ui.html

Fluxo
1. Cadastrar merchant
curl -X POST http://localhost:8080/fiadopay/admin/merchants \
  -H "Content-Type: application/json" \
  -d '{"name":"MinhaLoja ADS","webhookUrl":"http://localhost:8081/webhooks/payments"}'

2. Obter token
curl -X POST http://localhost:8080/fiadopay/auth/token \
  -H "Content-Type: application/json" \
  -d '{"client_id":"<clientId>","client_secret":"<clientSecret>"}'

3. Criar pagamento
curl -X POST http://localhost:8080/fiadopay/gateway/payments \
  -H "Authorization: Bearer FAKE-<merchantId>" \
  -H "Idempotency-Key: 550e8400-e29b-41d4-a716-446655440000" \
  -H "Content-Type: application/json" \
  -d '{"method":"CARD","currency":"BRL","amount":250.50,"installments":12,"metadataOrderId":"ORD-123"}'

4. Consultar pagamento
curl http://localhost:8080/fiadopay/gateway/payments/<paymentId>

Observações

Regras de antifraude são carregadas via reflexão em @AntiFraud.

Pagamentos são processados em background com ExecutorService.

Webhooks possuem assinatura HMAC e tentativas automáticas.

Banco em memória reinicia a cada execução.
