# 🧠 Prometheus-Grafana Demo Application

## About

The goal of this Mini Project to build a Spring Boot application with a **Spring Batch** job that:
- Reads a list of users from a CSV file,
- Processes them (e.g., converts names to uppercase),
- Writes them to an in-memory database (H2).

While the job runs:
 -  We’ll collect metrics (job duration, step counts, success/failure rates) via **Micrometer + OpenTelemetry**.

After that, **Prometheus** will scrape those metrics, 
and **Grafana** will visualize them in real time.

## How to run the application
The application can be started with the following command:
- **docker-compose up --build.**

Once all the related services have started, these are the
final working URLs:
- **Plaintext metrics** (Spring Boot) - http://localhost:8080/actuator/prometheus
- **UI and queries** (Prometheus) - http://localhost:9090
- **Dashboards** (Grafana) - http://localhost:3000

Once those are up-and-running, the batch jobs can be started
with the following API call:
- http://localhost:8080/run-batch