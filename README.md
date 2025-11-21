# FiadoPay Simulator (Spring Boot + H2)

Gateway de pagamento **FiadoPay** para a AVI/POOA.
Substitui PSPs reais com um backend em memória (H2).

##Rodar

./mvnw spring-boot:run
ou
mvn spring-boot:run


# H2 Console:
http://localhost:8080/h2

(JDBC: jdbc:h2:mem:fiadopay)

# Swagger UI:
http://localhost:8080/swagger-ui.html

# Fluxo
# 1. Cadastrar merchant
curl -X POST http://localhost:8080/fiadopay/admin/merchants \
  -H "Content-Type: application/json" \
  -d '{"name":"MinhaLoja ADS","webhookUrl":"http://localhost:8081/webhooks/payments"}'

# 2. Obter token
curl -X POST http://localhost:8080/fiadopay/auth/token \
  -H "Content-Type: application/json" \
  -d '{"client_id":"<clientId>","client_secret":"<clientSecret>"}'

# 3. Criar pagamento
curl -X POST http://localhost:8080/fiadopay/gateway/payments \
  -H "Authorization: Bearer FAKE-<merchantId>" \
  -H "Idempotency-Key: 550e8400-e29b-41d4-a716-446655440000" \
  -H "Content-Type: application/json" \
  -d '{"method":"CARD","currency":"BRL","amount":250.50,"installments":12,"metadataOrderId":"ORD-123"}'

# 4. Consultar pagamento
curl http://localhost:8080/fiadopay/gateway/payments/<paymentId>

# Observações

Regras de antifraude são carregadas via reflexão em @AntiFraud.

Pagamentos são processados em background com ExecutorService.

Webhooks possuem assinatura HMAC e tentativas automáticas.

Banco em memória reinicia a cada execução.


# Video Apresentação
https://www.youtube.com/watch?v=oRxVzOKaCqw

# PRINTS

# Print - Inicialização
<img width="1621" height="806" alt="Captura de tela 2025-11-21 114838" src="https://github.com/user-attachments/assets/b36e0289-5e32-4c1b-93fc-1b7719d7d58e" />


# Print - Antifraud rodando
<img width="640" height="38" alt="image" src="https://github.com/user-attachments/assets/b38bd3d0-8edf-4197-a93d-d239684b26f3" />


# Print - Merchant
<img width="720" height="404" alt="Captura de tela 2025-11-21 115549" src="https://github.com/user-attachments/assets/871f7a61-d659-4c63-bb44-fd583ae99c6c" />


# Print - H2
<img width="1915" height="345" alt="image" src="https://github.com/user-attachments/assets/7f1a8c52-bdb5-4387-94fe-70098e091fec" />


# Print - Token
<img width="718" height="359" alt="Captura de tela 2025-11-21 115556" src="https://github.com/user-attachments/assets/9a1f763c-9129-4238-b8a6-90020a1d5199" />


# Print - Pagamento
<img width="724" height="417" alt="Captura de tela 2025-11-21 115610" src="https://github.com/user-attachments/assets/21130e92-612a-459d-8344-02d192b683d7" />


# Print - Pagamento Recusado
<img width="723" height="399" alt="Captura de tela 2025-11-21 115634" src="https://github.com/user-attachments/assets/cc935bb0-6ad0-46bf-a912-184cfd4c812b" />
