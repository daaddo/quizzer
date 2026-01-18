# --- STAGE 1: Compilazione (Build) ---
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# 1. Copia il file pom.xml e scarica le dipendenze (ottimizzazione cache Docker)
COPY pom.xml .
RUN mvn dependency:go-offline

# 2. Copia i sorgenti e compila il progetto
COPY src ./src
RUN mvn clean package -DskipTests

# --- STAGE 2: Esecuzione (Runtime) ---
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copia il file JAR generato nello stage precedente (usiamo l'asterisco per flessibilità)
COPY --from=build /app/target/quizzer-0.0.1-SNAPSHOT.jar app.jar

# Espone la porta e avvia l'app
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]