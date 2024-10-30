# Project NT531

## Introduction
Project NT531 is a microservices-based application designed to manage user authentication, API gateway routing, and user management. It leverages a distributed architecture with Kafka for messaging, Redis for caching, and PostgreSQL for persistence.

## Technologies
- **Helm**: Version 3
- **Kubernetes**
- **Kafka**: Version 3.8.0
- **Redis**: Version 7.4.1
- **PostgreSQL**: Version 16.0.6

## Setup Instructions
1. **Install Prerequisites**:
   - Helm 3
   - Kubernetes (Minikube or a cloud-based cluster)
   - kubectl

2. **Configure kubectl** to connect to your Kubernetes cluster.

3. **Start Infrastructure Components**:
   ```shell
   helm install yamp-infra ./sub-charts/yamp-infra
   ```

## Usage
1. **Install the Authentication Service**:
   ```shell
   helm install auth-svc ./sub-charts/auth-svc
   ```

2. **Install the API Gateway Service**:
   ```shell
   helm install gateway-svc ./sub-charts/gateway-svc
   ```

3. **Install the User Service**:
   ```shell
   helm install user-svc ./sub-charts/user-svc
   ```

## Testing
- **Dependencies**: Busybox for testing connections.
- **Test Execution**: Tests are executed as Helm hooks.

## Deployment
- The application is deployed to a Kubernetes cluster using Helm.

## Notes
- OpenTelemetry is used for distributed tracing.
- The project includes basic connection tests for each service.
