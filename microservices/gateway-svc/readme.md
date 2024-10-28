# Gateway-svc
This is the gateway service built with Reactive Spring WebFlux and Spring Cloud Gateway for high performance and scalability.
## Features
- Resilience: Circuit Breaker, Rate Limiter, Retry, Timeout
- Logging traceId and spanId for each request
- Add JWT token to the request header for communication between internal services
- Collect metrics for each request and send to Prometheus
- Load balancing between internal services
### Security
- I use Session to the frontend and Oauth2  to the AuthTZ between internal microservices.
- I have built this base on this [Medium blog](https://medium.com/@a.zagarella/microservices-architecture-a-real-business-world-scenario-c77c31a957fb)
using gateway as an Oauth2 client

