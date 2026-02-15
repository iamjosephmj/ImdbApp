# IMDB App

Android movie browsing app built with Jetpack Compose, Clean Architecture, and TMDB API.

## Setup

### Prerequisites
- Android Studio (Hedgehog or later)
- JDK 11+
- TMDB API Key: [Get one here](https://www.themoviedb.org/settings/api)

### Installation

1. Clone the repository
2. **Add API Key**: Create `local.properties` in the project root:
   ```properties
   TMDB_API_KEY=your_api_key_here
   ```
   (Replace `your_api_key_here` with your actual TMDB API key)

3. Open in Android Studio and sync Gradle
4. Run on device/emulator (API 24+)

## Running Tests

```bash
./gradlew testDebugUnitTest
```

## Download APK from CI

The CI workflow automatically builds a debug APK on every push and pull request. To download it:

1. Go to the **Actions** tab in your GitHub repository
2. Click on the latest workflow run (e.g., "CI")
3. Scroll down to the **Artifacts** section
4. Click on **debug-apk** to download the APK file
5. The APK will be available for 7 days after the workflow run

**Note**: The APK is located at `app/build/outputs/apk/debug/app-debug.apk` in the artifact.

## Architecture

- **Clean Architecture**: Data → Domain → UI layers
- **MVVM** with Jetpack Compose
- **Dagger** for dependency injection
- **Paging 3** for infinite scroll
- **Retrofit + OkHttp** for networking
