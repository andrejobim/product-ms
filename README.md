# product-ms
Microserviço para gerenciamento de Catálogo de Produtos

# Banco de Dados (Não precisa alterar nada)
- spring.datasource.url=jdbc:h2:mem:file:/db-desafio-java
- spring.datasource.driverClassName=org.h2.Driver
- spring.datasource.username=sa
- spring.datasource.password=password

OBS: As informações inseridas no banco ficaram presentes enquanto o projeto estiver rodando.

# Swagger
Foi colocado Swagger para documentação dos endpoints.

# Build
mvn clean install (Vai rodar todos os testes)

# Jar
java -jar product-ms-0.0.1-SNAPSHOT.jar

# Browser
http://localhost:9999/swagger-ui.html
