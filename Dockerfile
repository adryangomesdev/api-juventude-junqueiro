# Estágio 1: Build da aplicação com Maven
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copia os arquivos de configuração do Maven e baixa as dependências primeiro (aproveita cache)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copia o código-fonte e gera o arquivo .jar ignorando os testes (já validados localmente)
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Estágio 2: Imagem final enxuta para execução
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Limita a memória da JVM para caber com folga nos 512 MB gratuitos do Render
ENV JAVA_TOOL_OPTIONS="-Xmx320m -Xms192m -XX:MaxMetaspaceSize=110m"

EXPOSE 8080

# Usa a variável PORT fornecida automaticamente pelo Render (padrão 8080 localmente)
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]