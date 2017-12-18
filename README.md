# spring-zuul

Requests:
- curl -d '{ "firstname": "pippo", "lastname": "pluto" }' -H "Content-Type: application/json" -X POST http://localhost:8080/service-one/api/customer
- curl -d '{ "firstname": "pippo", "lastname": "pluto" }' -H "Content-Type: application/json" -X POST http://localhost:8080/service-two/api/customer
