# Multithread-client-experiment
## Overview
This project demonstrates how to perform simple GET and POST requests to a server using multiple threads in Java. It uses the Apache HttpClient library for HTTP requests and Java's concurrency utilities for multithreading.

## Prerequisites
- Java Development Kit (JDK) 8 or higher
- Apache Maven
- Internet connection to download dependencies

## Setup
1. **Clone the repository:**
   ```sh
   git clone https://github.com/yourusername/multithread-client-experiment.git
   cd multithread-client-experiment
   ```
2. **Build the project using Maven:**  
   ```sh
   mvn clean install
    ```
3. **Run the project:**  
   ```sh
   cd src/main/java
   java -cp multithread-client-experiment-1.0-SNAPSHOT.jar com.example.ClientAPI
   ```

## Configuration
- Thread Group Size: The number of threads in each group. Configurable in ClientAPI.java.
- Number of Thread Groups: The number of thread groups. Configurable in ClientAPI.java.
- Delay: The delay between the start of each thread group in milliseconds. Configurable in ClientAPI.java.
- Server URLs: The URLs for the GET and POST requests. Configurable in ClientAPI.java.

## Logging
The application logs the following information for each request:  
- Timestamp 
- Thread Group 
- Number of active threads 
- Number of completed tasks 
- Queue size 
- Task count