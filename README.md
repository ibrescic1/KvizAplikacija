# KvizAplikacija (Android)

An Android application for taking quizzes, developed as a university project for the *Mobile Application Development* course (RMA) at the Faculty of Electrical Engineering, University of Sarajevo.

## Overview

The app allows students to browse subjects and groups, enroll in a group, view available quizzes, take a quiz, and see their results. Data is fetched from a REST backend rather than static/local data.

## Tech Stack

- **Kotlin**
- **Jetpack Compose** — UI toolkit
- **MVVM architecture** — ViewModel + Repository pattern
- **Retrofit + OkHttp** — REST API communication
- **Kotlin Coroutines & StateFlow** — asynchronous operations and state management
- **Jetpack Navigation** — screen navigation

## Architecture

The project follows an MVVM structure with a clear separation of concerns:

- `data.models` — data classes (Predmet, Grupa, Kviz, Pitanje, KvizTaken, Odgovor)
- `data.repositories` — repository objects responsible for network communication (AccountRepository, KvizRepository, PredmetIGrupaRepository, PitanjeKvizRepository, TakeKvizRepository, OdgovorRepository)
- `viewmodel` — screen state and business logic
- `ui.screens` / `ui.components` — Compose UI
- `navigation` — navigation graph

All repository methods are implemented as `suspend` functions, and all network calls are dispatched from coroutines — Composable functions never call the backend directly.

## Features

- Browse subjects, groups, and available quizzes with filtering and sorting
- Enroll in a group
- Start a quiz attempt, view its questions, and submit answers
- View the running score after each submitted answer
- Animated feedback on correct/incorrect answers
- Average score display
- Dark mode support

## Backend

The app communicates with a lightweight Node.js backend (provided separately for the course). For the Android emulator, the default base URL is: http://10.0.2.2:3000/

The base URL can be reconfigured via `ApiConfig.postaviBaseURL()` if the backend runs elsewhere (e.g. via ngrok).

## Running the project

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle
4. Make sure the backend service is running locally (see backend setup instructions, if included)
5. Run the app on an emulator or physical device

## Development History

The project was built succesively across several development cycles ("spirals"):

- **Spiral 1–2:** Static UI, navigation, and filtering logic
- **Spiral 3:** Replaced static data with real backend integration via repositories and coroutines
- **Spiral 4:** Usability and code-quality improvements — including dark mode, animated feedback, average score display, and refactored domain logic (e.g. `KvizFilterHelper`)

