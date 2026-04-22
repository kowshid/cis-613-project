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

### Run JPF Test
Java Path Finder is not available for Java 17, so run all the command in the terminal


- First switch to Java 11. Check with following:
    ```terminaloutput
    java -version
    ```
- Compile the test file
    ```terminaloutput
    javac AtomicIntegerJpfTest.java
    javac AtomicBooleanJpfTest.java
    ```
- Update `check_atomic.jpf` to target class of `AtomicIntegerJpfTest` or `AtomicBooleanJpfTest`. One class is checked at a time
    ```terminaloutput
    target = AtomicBooleanJpfTest
    classpath = target/classes
    search.multiple_errors = false
    ```
- Run jpf java process
    ```terminaloutput
    java -jar /path/to/jpf-core/build/RunJPF.jar check_atomic.jpf
    ```
