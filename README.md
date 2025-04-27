# Crudfather

Crudfather is a Kotlin-based project utilizing Ktor for server-side development. It focuses on
creating dynamic CRUD endpoints for dynamically generated models. This project aims to simplify the
process of managing different data models by allowing new model definitions to be created at runtime
without changing the server code.

## Features

- **Kotlin CLI**: Built with Kotlin, ensuring modern language features and robust performance.
- **Dynamic Models**: Allows creation of new data models at runtime.
- **CRUD Operations**: Supports create, read, update, and delete operations for dynamic models.
- **MongoDB Integration**: Uses MongoDB for data storage.
- **Koin Dependency Injection**: Ensures a modular and maintainable codebase.
- **Comprehensive Documentation**: Generates detailed documentation using Dokka.

## Installation

### Prerequisites

- JDK 8 or higher
- Gradle 8.2 or higher
- MongoDB

### Setup

1. Clone the repository:
   ```sh
   git clone https://github.com/onelenyk/crudfather.git
   ```
2. Navigate to the project directory:
   ```sh
   cd crudfather
   ```
3. Build the project:
   ```sh
   ./gradlew build
   ```

## Usage

### Running the Server

1. Start the MongoDB server.
2. Run the application:
   ```sh
   ./gradlew run
   ```

## Contributions

Contributions are welcome! Please submit a pull request or open an issue for any improvements or bug
fixes.

## Acknowledgements

- **onelenyk** - Initial work and maintenance.
- **ChatGPT by OpenAI** - Assisted in development by providing code insights, optimization
  strategies, and documentation support.

## License

This project is licensed under the [Apache License 2.0](LICENSE).
