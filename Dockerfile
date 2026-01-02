# ===============================
# 1️⃣ STAGE: BUILD
# ===============================
FROM maven:3.9.9-eclipse-temurin-17 AS build

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos solo los archivos necesarios para descargar dependencias
COPY pom.xml .

# Descargamos dependencias (capa cacheable)
RUN mvn dependency:go-offline

# Copiamos el código fuente
COPY src ./src

# Construimos el JAR (sin tests para acelerar)
RUN mvn clean package -DskipTests


# ===============================
# 2️⃣ STAGE: RUNTIME
# ===============================
FROM eclipse-temurin:17-jre-alpine

# Seguridad: usuario no root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Directorio de ejecución
WORKDIR /app

# Copiamos SOLO el jar desde el stage build
COPY --from=build /app/target/*.jar app.jar

# Puerto que expone Spring Boot
EXPOSE 8080

# Variables JVM optimizadas para contenedores
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

# Comando de arranque
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
