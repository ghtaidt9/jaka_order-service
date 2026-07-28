# Order Service

REST API for order management with Redis caching, built with Spring Boot 4.1.0, Java 17, and Gradle 9.5.1.

## Running Locally

### Prerequisites
- Java 17
- Gradle 9.5.1
- PostgreSQL 16
- Redis 7

### Build
```bash
gradle clean build
```

### Run

```bash
gradle bootRun
```
The service will start on port `8080`.

## Docker

### Build Image
```bash
docker build -t order-service:latest .
```

### Run Container
```bash
# Create a custom network and run postgres + redis containers
docker network create springboot-microservices-code

docker run -d --name postgres-db --network springboot-microservices-code \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_INITDB_ARGS="--encoding=UTF8 --locale=en_US.UTF-8" \
  -v "${PWD}/../init-databases.sql:/docker-entrypoint-initdb.d/init.sql" \
  postgres:16-alpine

docker run -d --name redis-cache --network springboot-microservices-code \
  redis:7-alpine redis-server --appendonly yes

# Run order service on the same network
docker run --rm --name order-service --network springboot-microservices-code \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/order_db \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  -e SPRING_DATA_REDIS_HOST=redis-cache \
  -e SPRING_DATA_REDIS_PORT=6379 \
  order-service:latest
```

## API Endpoints

### Create Order
```
POST /api/orders
Content-Type: application/json

{
  "customerId": "customer123",
  "productSku": "PROD-001",
  "quantity": 2,
  "totalAmount": 99.99
}
```

### Get Order by ID
```
GET /api/orders/{id}
```

### Get Orders by Customer
```
GET /api/orders?customerId=customer123
```

### Update Order Status
```
PATCH /api/orders/{id}/status?status=PAID
```

## Database Schema

```sql
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    customer_id VARCHAR(255) NOT NULL,
    product_sku VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    total_amount DECIMAL(19,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
```

## Health Check
```
GET /actuator/health
```

## Metrics
```
GET /actuator/metrics
```

## Environment Variables

- `SPRING_DATASOURCE_URL`: PostgreSQL connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SPRING_DATA_REDIS_HOST`: Redis host
- `SPRING_DATA_REDIS_PORT`: Redis port

## Technologies

- Spring Boot 4.1.0
- Spring Data JPA
- Spring Cache (Redis)
- PostgreSQL Driver
- Lombok
- Gradle 9.5.1
- Java 17
