This is a Java Maven project that tests custom implementations of _AtomicBoolean_ and _AtomicInteger_. It relies on Maven for dependency management, compilation, and running tests.

### Prerequisites
- Java Development Kit (JDK) 17
- Apache Maven

### Terminal Commands
- Clean and Compile the Project
    ```terminaloutput
    mvn clean compile
    ```
- Run the Unit Tests
    ```terminaloutput
    mvn clean test
    ```
- Run Mutation Testing
    ```terminaloutput
    mvn clean test pitest:mutationCoverage
    ```