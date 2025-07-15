# MaPi 🗺️ 

> **AI-Powered Restaurant Discovery from Your Google Maps Saved Places**

Do you constantly add new restaurants and cafés to your Google Maps saved places without really trying them out? Or you can't find the correct place from your saved places when you're craving something? **MaPi** is the perfect solution! Just describe your current craving, and our AI will find the best match from your saved places.

## ✨ Features

- 🔐 **Secure OAuth Authentication** - Login with your Google account
- 📍 **Google Maps Integration** - Sync your saved places automatically  
- 🤖 **AI-Powered Recommendations** - Gemini AI analyzes your cravings and suggests perfect matches
- 🎨 **Modern UI** - Beautiful, intuitive interface built with Jetpack Compose
- 💾 **Offline Support** - Local caching of your places for faster access
- 🔄 **Real-time Sync** - Keep your places up-to-date with Google Maps

## 📱 Screenshots

| **Welcome Screen** | **AI Recommendations** |
| -------------- | ----------- |
| ![mapi_initial](https://github.com/user-attachments/assets/1567897c-2e9a-466c-b082-0d1e21abbd0f) | ![mapi_result](https://github.com/user-attachments/assets/2e89efa4-c57c-480e-909b-e668020dfbc3) |

## 🏗️ Architecture

MaPi follows **Clean Architecture** principles with clear separation of concerns:

```
├── 🎨 UI Layer (Compose)
│   ├── MainActivity
│   ├── MainViewModel  
│   └── Compose Screens
├── 💼 Business Layer
│   ├── AuthenticationService
│   ├── GetPlacesIdsApplicationService
│   └── TakeoutSavedCollectionsService
├── 🗄️ Data Layer
│   ├── 🌐 Remote (Google APIs)
│   │   ├── MapsApiService
│   │   └── GeminiService
│   ├── 💾 Local (Room Database)
│   │   ├── LocalPlace entities
│   │   └── PlacesDao
│   └── 🔄 Repository Pattern
└── 🏛️ Domain Layer
    └── PlacesRepository
```

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: Clean Architecture + MVVM
- **Dependency Injection**: Dagger Hilt
- **Database**: Room
- **Networking**: Ktor Client
- **AI Integration**: Google Gemini API
- **Maps Integration**: Google Maps API & Data Portability API
- **Authentication**: OAuth 2.0
- **Build System**: Gradle with Kotlin DSL

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog | 2023.1.1 or newer
- Android SDK API 31+
- JDK 17+
- Google Cloud Console account
- Domain for OAuth redirect URI (for production)

### 📋 Setup Instructions

#### 1. **Clone the Repository**
```bash
git clone https://github.com/mahyami/mapi.git
cd mapi
```

#### 2. **Create Google Cloud Project & APIs**

Go to [Google Cloud Console](https://console.cloud.google.com/):

1. **Create a new project** or select existing one
2. **Enable required APIs**:
   - Google Maps API
   - Google Places API  
   - Data Portability API
   - Generative AI API (for Gemini)

3. **Create API credentials**:
   
   ![Google Console Setup](https://github.com/user-attachments/assets/f4e16c02-89c5-4677-aa27-e13e21b838dd)

#### 3. **Setup API Keys**

Create `apikey.properties` file in the **root directory**:

```properties
GOOGLE_API_KEY="your_google_maps_api_key"
GOOGLE_GEN_AI_KEY="your_gemini_api_key" 
OAUTH_CLIENT_ID="your_oauth_client_id"
OAUTH_CLIENT_SECRET="your_oauth_client_secret"
```

#### 4. **Configure OAuth**

**Important**: OAuth setup is required for accessing user's saved places. See detailed instructions in [oauth.md](oauth.md).

**Key steps**:
- Set up OAuth credentials in Google Cloud Console
- Configure verified redirect URI
- Update `AuthenticationService.kt` with your redirect URI
- Add deep link intent filter in `AndroidManifest.xml`

#### 5. **Build & Run**

```bash
./gradlew assembleDebug
# Or open in Android Studio and run
```

## 🔧 Development

### Project Structure

```
mapi/
├── androidApp/                    # Main Android application
│   ├── src/main/java/com/google/mapi/
│   │   ├── business/             # Business logic services  
│   │   ├── data/                 # Data layer (local & remote)
│   │   ├── domain/               # Domain interfaces
│   │   ├── ui/                   # UI components & ViewModels
│   │   ├── converters/           # Data mapping utilities
│   │   └── MapiApplication.kt    # Application class
│   └── src/main/res/             # Android resources
├── gradle/                       # Gradle configuration
└── build.gradle.kts             # Root build script
```

### 🧪 Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests  
./gradlew connectedAndroidTest
```

### 📦 Building Release

```bash
# Build release APK
./gradlew assembleRelease

# Build App Bundle (recommended for Play Store)
./gradlew bundleRelease
```

## 🔒 Security & Privacy

- **OAuth 2.0**: Secure authentication with Google
- **Local Storage**: User data cached locally using Room encryption
- **API Keys**: Stored securely in build configuration
- **Data Portability**: Only accesses user's own saved places with explicit consent

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add KDoc comments for public APIs
- Run `./gradlew ktlintCheck` before committing

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Google Maps API for location services
- Google Gemini AI for intelligent recommendations  
- Jetpack Compose team for the modern UI toolkit
- The Android developer community

## 📞 Support

- 🐛 **Issues**: [GitHub Issues](https://github.com/mahyami/mapi/issues)
- 💬 **Discussions**: [GitHub Discussions](https://github.com/mahyami/mapi/discussions)
- 📧 **Email**: [Create an issue](https://github.com/mahyami/mapi/issues/new) for support

---

**Made with ❤️ for food lovers who can't decide where to eat from their endless saved places list!**

