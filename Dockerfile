FROM bellsoft/liberica-openjdk-alpine:21.0.3

WORKDIR /app

COPY target/mcsv-ventas.jar /app/mcsv-ventas.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "mcsv-ventas.jar"]
