## project dependency environment

| Field           | Version  |
|-----------------|----------|
| JDK version     | 17       |
| Maven version   | 3.9.9    |


## Run project command
| Command Type    | Command                |
|-----------------|------------------------|
| Build           | mvn compile            |
| Package         | mvn package            |
| Run Command     | mvn spring-boot:run    |
| Test Command    | mvn test               |


## Application Configuration
- `logging.file.name`: `logs/incident.log`
- `server.address`: `192.168.0.106`
- `server.port`: `9090`

## API Design Document,same as frontend
[API Design Document](./Document/API-Design-document.pdf)


## Current shortcomings and areas that need to be strengthened and supplemented in the future.
1. Complete all test cases and provide product code coverage. Due to time constraints, many test cases were not written and need to be added. Supplement test cases for each API's exception scenarios.
2. Before committing code to the repository, an automated pipeline process should be followed, such as compilation, running test cases, and testing code coverage. Once the above steps are successful, the code will enter the review phase. The code will only be merged if the review has no issues.
3. The accidentId used in the project is the ID card number, and sensitive fields need to be encrypted and decrypted.
4. For each designed API, data communication should use HTTPS with mutual authentication.
5. In the future, containerization with Docker needs to be implemented, and the system should be deployable to a Kubernetes (K8S) environment for validation. It should support automatic scaling of the product.
6. Frontend and backend integration testing should be done together, and relevant test cases should be written to ensure coverage.
