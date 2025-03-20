# Java with Weka - Movie Trend Analysis & Predicting
> This is Spring Boot application with Weka to predict movie ratings for COIL project.

## Prerequisites

Before proceeding, ensure you have the following installed:

-IntelliJ IDEA (Community or Ultimate Edition)

-JDK (Java Development Kit) 8 or higher

-Weka library JAR file (or Maven dependency)

## Steps to Set Up 

### 1. **Clone project**
```agsl
git clone https://github.com/coil-team-2/data-mining.git
```

### 2. **Open project on IntelliJ**

### 3. Run locally on main application at file: 
```agsl
Main.java
```
> Or running on terminals with:
```agsl
- Build the application: mvn clean package
- Run the app: java -jar target/modelEvaluation-1.0-SNAPSHOT-jar-with-dependencies.jar
```
> Or running with Docker:
```agsl

For run : docker-compose up -d
For stop: docker-compose down
```
- For checking logs:
```agsl
docker logs movie-prediction-service
```

### API documents:
> **Link**: coming soon

---

## Project Structure:
```
src/main/java/org/example/  
├── config/ # Application configuration 
├── controller/ # REST API controllers 
├── model/ # Data models 
├── service/ # Business logic 
├── util/ # Utility classes 
└── weka/ # Weka integration
```
