# Ktor Hello

A modern Kotlin-based web application built with Ktor framework. This project demonstrates a clean architecture approach to building web services with Kotlin and Ktor.

## Features

- **Kotlin & Ktor**: Built with Kotlin 2.0.0 and Ktor 2.3.3
- **MongoDB Integration**: Uses MongoDB for data persistence
- **Dependency Injection**: Uses Koin for dependency management
- **API Documentation**: Integrated with Dokka for comprehensive documentation
- **Code Quality**: Integrated with ktlint for consistent code style
- **JWT Authentication**: Secure endpoints with JWT-based authentication
- **Content Negotiation**: JSON serialization support
- **Health Checks**: Built-in health check endpoints
- **Static Resources**: Serves static content

## Project Structure

```
src/main/kotlin/dev/onelenyk/ktorhello/
├── app/
│   ├── Application.kt        # Main application entry point
│   ├── Server.kt            # Server configuration
│   ├── di/                  # Dependency injection
│   └── routing/             # API routes
├── data/
│   └── db/                  # Database configuration
└── utils/                   # Utility classes
```

## API Endpoints

- `/routes` - Lists all available routes
- `/live` - Health check endpoint
- `/hello` - Sample endpoint returning "Hello, Ktor!"
- `/` - Serves static documentation

## Prerequisites

- JDK 17 or higher
- Gradle 8.x
- MongoDB (if using database features)

## Getting Started

1. Clone the repository:
   ```sh
   git clone https://github.com/onelenyk/ktor-hello.git
   ```

2. Navigate to project directory:
   ```sh
   cd ktor-hello
   ```

3. Build the project:
   ```sh
   ./gradlew build
   ```

4. Run the application:
   ```sh
   ./gradlew run
   ```

The server will start on `http://localhost:8080`

## Development

- Use `./gradlew ktlintCheck` to check code style
- Use `./gradlew dokkaHtml` to generate documentation

## Deployment

### Deploying to Heroku

1. Install the Heroku CLI and login:
   ```sh
   brew install heroku
   heroku login
   ```

2. Create a new Heroku app:
   ```sh
   heroku create your-app-name
   ```

3. Deploy to Heroku:
   ```sh
   git push heroku main
   ```

4. Ensure at least one instance is running:
   ```sh
   heroku ps:scale web=1
   ```

5. Open the deployed application:
   ```sh
   heroku open
   ```

### Environment Variables

The following environment variables can be configured in Heroku:

- `PORT` - Automatically set by Heroku
- Add any other environment variables your application needs using:
  ```sh
  heroku config:set VARIABLE_NAME=value
  ```

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
