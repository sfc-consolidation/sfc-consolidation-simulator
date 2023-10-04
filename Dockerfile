FROM openjdk:17-jdk-oracle

WORKDIR /app
COPY app/build/libs/SFC_Consolidation_Simulator-0.0.1.jar /app/SFC_Consolidation_Simulator-0.0.1.jar

ENTRYPOINT ["java", "-jar", "SFC_Consolidation_Simulator-0.0.1.jar"]