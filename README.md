# Family Tree API (Java/Spring Boot)

A RESTful API for managing family trees, built with Spring Boot and JPA.

## Technologies

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **REST Assured** (for API testing instead of Newman)
- **Docker**

## Features

- Create and manage family trees
- Add family members with detailed information
- Define relationships between family members
- JPA/Hibernate for database operations
- RESTful API endpoints
- Docker containerization
- Jenkins CI/CD pipeline with RestAssured tests

## API Endpoints

- `POST /api/trees` - Create a new family tree
- `GET /api/trees` - Get all family trees
- `GET /api/trees/{treeId}` - Get a specific tree
- `DELETE /api/trees/{treeId}` - Delete a tree
- `POST /api/trees/{treeId}/members` - Add a member to a tree
- `GET /api/trees/{treeId}/members` - Get all members in a tree
- `GET /api/trees/{treeId}/members/{memberId}` - Get a specific member
- `POST /api/trees/{treeId}/members/{memberId}/partner` - Add a partner relationship
- `POST /api/trees/{treeId}/members/{memberId}/children` - Add a child relationship
- `GET /api/trees/{treeId}/members/{memberId}/relationships` - Get member relationships
- `GET /health` - Health check endpoint

## Running Locally

```bash
# Build the application
mvn clean package

# Run with Maven
mvn spring-boot:run

# Or run the JAR
java -jar target/family-tree-api-java-1.0.0.jar
```

## Docker

```bash
# Build image
docker build -t family-tree-api-java .

# Run container
docker run -p 3000:3000 \
  -e DB_HOST=localhost \
  -e DB_PORT=5432 \
  -e DB_NAME=familytree \
  -e DB_USER=postgres \
  -e DB_PASSWORD=postgres \
  family-tree-api-java
```

## Environment Variables

- `PORT` - Server port (default: 3000)
- `DB_HOST` - Database host (default: localhost)
- `DB_PORT` - Database port (default: 5432)
- `DB_NAME` - Database name (default: familytree)
- `DB_USER` - Database user (default: postgres)
- `DB_PASSWORD` - Database password (default: postgres)

## Testing

Uses **REST Assured** instead of Newman for API testing:

```bash
mvn test
```

## CI/CD

Jenkins pipeline includes:
- Maven build
- Docker image creation
- Database deployment
- Health checks
- REST Assured API tests
