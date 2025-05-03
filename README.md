# Ktor Hello

Starter template for my Kotlin + Ktor projects. A simple backend server with common features I use. Ready for deployment.

## What's Inside

- Kotlin 2.0.0 + Ktor 2.3.3
- MongoDB for data
- Static file serving
- Health checks
- Gradle setup ready for deployment:
    - Ktlint for code style
    - Dokka for docs
    - Fat JAR packaging
    - Deployment config

## Quick Start

1. Clone it:
   ```sh
   git clone https://github.com/onelenyk/ktor-hello.git
   cd ktor-hello
   ```

2. Setup environment:
   Edit .env with your settings:
   ```sh
   DB_CONNECTION="@localhost/?retryWrites=true&w=majority"
   PORT=8080
   DB_USERNAME=luianderson
   DB_PASSWORD=123123
   ```

3. Run it:
   ```sh
   ./gradlew run
   ```

Server starts at `http://localhost:8080`

## Development

- `./gradlew ktlintCheck` - check code style
- `./gradlew dokkaHtml` - generate docs

## Deploy to Heroku

```sh
heroku create your-app-name
git push heroku main
heroku ps:scale web=1
```

## License

Apache 2.0 - see [LICENSE](LICENSE)