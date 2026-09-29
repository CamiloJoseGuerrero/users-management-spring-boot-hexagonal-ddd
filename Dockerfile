# =============================================================
# Etapa 1: compilacion con Maven (no requiere Maven instalado en la PC)
# =============================================================
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Descarga dependencias primero para aprovechar la cache de capas
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q clean package -DskipTests

# =============================================================
# Etapa 2: imagen liviana solo con el JRE
# =============================================================
FROM eclipse-temurin:17-jre
WORKDIR /app

RUN useradd --system --create-home spring
USER spring

COPY --from=build /app/target/*.jar app.jar

# Render define PORT; localmente se usa 8080
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -XX:MaxRAMPercentage=75 -jar /app/app.jar"]
