# Getting Started
### Option 1 : docker
```
docker compose -f /Users/komaljayswal/Documents/Temporal/src/main/resources/docker-compose.yml up -d
```
### Option 2 : install and run local

Install
```
brew install temporal
temporal server start-dev
```
Run in local
```
http://localhost:8233/namespaces/default/workflows
```
### Curl command
```
curl --location 'http://localhost:8085/order' \
--header 'Content-Type: application/json' \
--data '{
    "orderId": "1",
    "paymentStatus": "STARTED"
}'
```

### Reference Documentation

https://www.youtube.com/watch?v=yTPBEBbyWKc

