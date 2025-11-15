# Jasper

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Version](https://img.shields.io/badge/version-0.1.0-blue)
![License](https://img.shields.io/badge/license-Unlicensed-lightgrey)

Jasper is a lightweight and modular Java utility framework designed to simplify common application development tasks.

It provides a collection of loosely-coupled modules for command-line argument parsing, command dispatching, configuration management, and an annotation-based event system. It is designed to be easy to integrate and extend.

## Key Features

*   **Command Dispatcher:** A powerful command system with a tree-like structure for registering and parsing complex commands.
*   **Argument Parser:** A simple, type-safe utility for parsing command-line arguments (`--key value`) and flags (`--verbose`).
*   **Annotation-Based Event System:** A flexible event bus that allows for easy communication between different parts of an application using the `@HandleEvent` annotation.
*   **Reflective Configuration:** An abstract `ConfigHandler` that automatically manages loading and saving configuration properties to a JSON file using the `@Property` annotation.
*   **Text Utilities:** Includes tools for creating and rendering styled text in ASCII environments.

## Tech Stack

*   **Language:** Java 23
*   **Build Tool:** Gradle
*   **Dependencies:**
    *   Gson: For JSON serialization/deserialization in the config module.
    *   Log4j: For logging.
    *   JUnit 5: For testing.

## Installation and Setup

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/businessdirt/Jasper.git
    cd Jasper
    ```

2.  **Build the project:**
    Use the included Gradle wrapper to build the project and download dependencies.
    ```sh
    ./gradlew build
    ```

## Running Tests

To run the complete test suite, use the following Gradle command:

```sh
./gradlew test
```

## Contributing

Contributions are welcome! If you'd like to help improve Jasper, please follow these steps:

1.  Fork the repository.
2.  Create a new branch for your feature or bug fix (`git checkout -b feature/my-new-feature`).
3.  Make your changes and commit them (`git commit -m 'Add some feature'`).
4.  Push to your branch (`git push origin feature/my-new-feature`).
5.  Open a pull request.

## License

This project is currently not licensed.
