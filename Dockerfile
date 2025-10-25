# --- ESTÁGIO 1: O "Builder" ---
# Usamos uma imagem completa do JDK para compilar o nosso projeto.
# 'AS builder' dá um nome a este estágio.
FROM eclipse-temurin:22-jdk-jammy AS builder

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia os ficheiros do Maven para aproveitar o cache de dependências do Docker
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Baixa todas as dependências do projeto. Se o pom.xml não mudar, esta camada fica em cache.
RUN ./mvnw dependency:go-offline

# Copia o resto do código-fonte da sua aplicação
COPY src ./src

# Compila a aplicação e gera o ficheiro .jar, saltando os testes.
RUN ./mvnw package -DskipTests


# --- ESTÁGIO 2: O "Runner" ---
# Usamos uma imagem muito mais leve, apenas com o Java Runtime (JRE), para rodar a aplicação.
FROM eclipse-temurin:22-jre-jammy

# Define o diretório de trabalho
WORKDIR /app

# Copia APENAS o ficheiro .jar compilado do estágio 'builder' para a nossa imagem final.
# Isto resulta numa imagem final muito mais pequena e segura.
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta que a sua aplicação usa (você mencionou 8082)
EXPOSE 8082

# O comando que será executado quando o container iniciar.
ENTRYPOINT ["java", "-jar", "app.jar"]