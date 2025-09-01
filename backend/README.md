# Hospital Management System - Backend

This is the backend implementation of the Hospital Management System (HMS) built with Spring Boot microservices architecture.

## Architecture Overview

The backend consists of the following microservices:

1. **API Gateway** (Port: 8080) - Central entry point for all client requests
2. **Patient Service** (Port: 8081) - Manages patient information and records
3. **Doctor Service** (Port: 8082) - Manages doctor information and schedules
4. **Appointment Service** (Port: 8083) - Handles appointment scheduling and management
5. **Billing Service** (Port: 8084) - Manages billing and payment processing
6. **Notification Service** (Port: 8085) - Handles email and SMS notifications
7. **Audit Service** (Port: 8086) - Manages audit logs and system monitoring

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Java Version**: 17
- **Database**: MySQL 8.0 (Primary), MongoDB 7.0 (Audit logs)
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Messaging**: Apache Kafka
- **Monitoring**: Prometheus, Grafana
- **Tracing**: Zipkin
- **Build Tool**: Maven

## Prerequisites

Before running the backend services, ensure you have the following installed:

1. **Java 17** or higher
2. **Maven 3.6** or higher
3. **MySQL 8.0**
4. **MongoDB 7.0**
5. **Apache Kafka 3.0** or higher
6. **Docker** (optional, for containerized deployment)

## Database Setup

### MySQL Database

1. Create the MySQL database:
```sql
CREATE DATABASE hms_db;
```

2. Run the schema script:
```bash
mysql -u root -p hms_db < database/mysql/schema.sql
```

### MongoDB Database

1. Start MongoDB service
2. Run the collections setup script:
```bash
mongosh < database/mongodb/collections.js
```

## Service Configuration

Each service has its own configuration file (`application.yml`) with the following common settings:

- **Database connection** (MySQL)
- **Eureka client configuration**
- **Kafka producer/consumer settings**
- **Actuator endpoints**
- **Logging configuration**

### Environment Variables

Set the following environment variables for production:

```bash
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=hms_db
DB_USERNAME=root
DB_PASSWORD=your_password

# MongoDB
MONGO_HOST=localhost
MONGO_PORT=27017
MONGO_DATABASE=hms_audit

# Kafka
KAFKA_BOOTSTRAP_SERVERS=localhost:9092

# Eureka
EUREKA_SERVER_URL=http://localhost:8761/eureka/

# Email (for notification service)
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
```

## Building and Running Services

### 1. Build All Services

```bash
# From the backend directory
mvn clean install -DskipTests
```

### 2. Start Services in Order

1. **Start Eureka Server** (if using external Eureka):
```bash
cd api-gateway
mvn spring-boot:run
```

2. **Start Core Services**:
```bash
# Patient Service
cd patient-service
mvn spring-boot:run

# Doctor Service
cd doctor-service
mvn spring-boot:run

# Appointment Service
cd appointment-service
mvn spring-boot:run

# Billing Service
cd billing-service
mvn spring-boot:run
```

3. **Start Supporting Services**:
```bash
# Notification Service
cd notification-service
mvn spring-boot:run

# Audit Service
cd audit-service
mvn spring-boot:run
```

### 3. Using Docker Compose (Recommended)

```bash
# From the project root
docker-compose up -d
```

## API Documentation

### Base URLs

- **API Gateway**: http://localhost:8080
- **Patient Service**: http://localhost:8081
- **Doctor Service**: http://localhost:8082
- **Appointment Service**: http://localhost:8083
- **Billing Service**: http://localhost:8084
- **Notification Service**: http://localhost:8085
- **Audit Service**: http://localhost:8086

### Service Endpoints

#### Patient Service (`/api/v1/patients`)
- `GET /` - Get all patients (paginated)
- `GET /{id}` - Get patient by ID
- `GET /user/{userId}` - Get patient by user ID
- `POST /` - Create new patient
- `PUT /{id}` - Update patient
- `DELETE /{id}` - Delete patient
- `GET /search?name={name}` - Search patients by name
- `GET /statistics` - Get patient statistics

#### Doctor Service (`/api/v1/doctors`)
- `GET /` - Get all doctors (paginated)
- `GET /{id}` - Get doctor by ID
- `GET /user/{userId}` - Get doctor by user ID
- `POST /` - Create new doctor
- `PUT /{id}` - Update doctor
- `DELETE /{id}` - Delete doctor
- `GET /specialization/{specialization}` - Get doctors by specialization
- `GET /department/{departmentId}` - Get doctors by department
- `GET /available` - Get available doctors

#### Appointment Service (`/api/v1/appointments`)
- `GET /` - Get all appointments (paginated)
- `GET /{id}` - Get appointment by ID
- `GET /patient/{patientId}` - Get appointments by patient
- `GET /doctor/{doctorId}` - Get appointments by doctor
- `POST /` - Create new appointment
- `PUT /{id}` - Update appointment
- `DELETE /{id}` - Cancel appointment
- `PUT /{id}/status` - Update appointment status

#### Billing Service (`/api/v1/bills`)
- `GET /` - Get all bills (paginated)
- `GET /{id}` - Get bill by ID
- `GET /patient/{patientId}` - Get bills by patient
- `POST /` - Create new bill
- `PUT /{id}` - Update bill
- `POST /{id}/pay` - Process payment
- `GET /statistics` - Get billing statistics

## Monitoring and Observability

### Health Checks

Each service exposes health check endpoints:
- `GET /actuator/health` - Service health status
- `GET /actuator/info` - Service information
- `GET /actuator/metrics` - Service metrics

### Prometheus Metrics

All services expose Prometheus metrics at:
- `GET /actuator/prometheus`

### Distributed Tracing

Zipkin is used for distributed tracing:
- **Zipkin UI**: http://localhost:9411

## Testing

### Unit Tests

```bash
# Run unit tests for all services
mvn test

# Run tests for specific service
cd patient-service
mvn test
```

### Integration Tests

```bash
# Run integration tests
mvn verify
```

### API Testing

Use the provided Postman collection or curl commands to test the APIs:

```bash
# Example: Get all patients
curl -X GET http://localhost:8080/api/v1/patients

# Example: Create a patient
curl -X POST http://localhost:8080/api/v1/patients \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "firstName": "John",
    "lastName": "Doe",
    "dateOfBirth": "1990-01-01",
    "gender": "MALE",
    "phone": "+1234567890"
  }'
```

## Security

The system implements the following security measures:

1. **JWT Authentication** - Token-based authentication
2. **Role-based Access Control** - Different roles for patients, doctors, and admins
3. **Input Validation** - Comprehensive validation for all inputs
4. **Audit Logging** - All actions are logged for compliance
5. **CORS Configuration** - Cross-origin resource sharing setup

## Deployment

### Docker Deployment

```bash
# Build Docker images
docker-compose build

# Deploy services
docker-compose up -d
```

### Kubernetes Deployment

Kubernetes manifests are provided in the `k8s/` directory:

```bash
# Apply Kubernetes manifests
kubectl apply -f k8s/
```

## Troubleshooting

### Common Issues

1. **Database Connection Issues**
   - Verify MySQL/MongoDB is running
   - Check database credentials
   - Ensure database exists

2. **Service Discovery Issues**
   - Verify Eureka server is running
   - Check service registration
   - Review network connectivity

3. **Kafka Connection Issues**
   - Verify Kafka is running
   - Check topic creation
   - Review consumer group settings

### Logs

Check service logs for debugging:

```bash
# View logs for specific service
docker logs hms-patient-service

# Follow logs
docker logs -f hms-patient-service
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
