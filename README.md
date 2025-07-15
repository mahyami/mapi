
# MaPi - Maps + AI Restaurant Finder

<p align="center">
  <img src="https://github.com/user-attachments/assets/1567897c-2e9a-466c-b082-0d1e21abbd0f" alt="MaPi Logo" width="200"/>
</p>

<p align="center">
  <strong>🍕 Never struggle to pick a restaurant from your saved places again!</strong>
</p>

## 📋 Overview

Do you constantly add new restaurants and cafés to your Google Maps saved places without really trying them out?  
Or you can't find the correct place from your saved places when you're craving something?  
If that's the case then we have the perfect solution for you!  

**MaPi** combines the power of Google Maps with AI to intelligently recommend restaurants from your saved places based on your current cravings. Simply login to your Google account, tell us what you're in the mood for, and let our AI find the perfect match from your saved locations!

## ✨ Features

- 🔐 **Secure Google OAuth Authentication** - Login with your Google account
- 📍 **Automatic Saved Places Sync** - Imports your Google Maps saved places
- 🤖 **AI-Powered Recommendations** - Uses Google Gemini AI to understand your cravings
- 🗺️ **Direct Maps Integration** - One-tap navigation to recommended places
- 💾 **Local Storage** - Caches your places for faster access
- 🎨 **Modern UI** - Beautiful Jetpack Compose interface
- ⚡ **Real-time Suggestions** - Get instant recommendations based on your mood

## 🏗️ Architecture

MaPi follows **Clean Architecture** principles with clear separation of concerns:

```
📁 com.google.mapi/
├── 🎨 ui/                     # Presentation Layer
│   ├── compose/               # Jetpack Compose UI components
│   ├── MainActivity.kt        # Main activity
│   ├── MainViewModel.kt       # ViewModel for state management
│   └── theme/                 # App theming
├── 🏢 business/               # Application Services
│   ├── AuthenticationService.kt
│   ├── GetPlacesIdsApplicationService.kt
│   ├── ParseCSVApplicationService.kt
│   └── TakeoutSavedCollectionsService.kt
├── 🌐 data/                   # Data Layer
│   ├── local/                 # Room database
│   ├── remote/                # API clients
│   └── gemini/                # AI service
├── 🎯 domain/                 # Domain Layer
│   └── PlacesRepository.kt    # Repository pattern
└── 🔧 converters/             # Data converters
```

## 🛠️ Tech Stack

| Component | Technology |
|-----------|------------|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose |
| **Architecture** | MVVM + Clean Architecture |
| **Dependency Injection** | Dagger Hilt |
| **Database** | Room |
| **HTTP Client** | Ktor |
| **AI Engine** | Google Gemini 1.5 Pro |
| **Maps API** | Google Maps Places API |
| **Authentication** | Google OAuth 2.0 |
| **Build System** | Gradle (Kotlin DSL) |

## 📱 Screenshots

| **Welcome Screen** | **User Prompt** | **AI Recommendations** |
| ------------------ | --------------- | ---------------------- |
| ![Welcome](https://github.com/user-attachments/assets/1567897c-2e9a-466c-b082-0d1e21abbd0f) | ![Prompt](https://github.com/user-attachments/assets/2e09efa4-c57c-480e-909b-e668020dfbc3) | *Coming soon* |

## 🚀 Getting Started

### Prerequisites

- **Android Studio** Arctic Fox or later
- **JDK 17** or later
- **Android SDK** with minimum API level 31
- **Google Cloud Platform** account with billing enabled

### Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/mahyami/mapi.git
   cd mapi
   ```

2. **Set Up Google Cloud APIs**
   
   Go to the [Google Cloud Console](https://console.cloud.google.com/apis/credentials) and enable:
   - **Maps API** (for place details)
   - **Gemini API** (for AI recommendations)
   - **Data Portability API** (for accessing saved places)

   <img width="1502" alt="Google Console Setup" src="https://github.com/user-attachments/assets/f4e16c02-89c5-4677-aa27-e13e21b838dd">

3. **Configure API Keys**
   
   Create an `apikey.properties` file in the root directory:
   ```properties
   GOOGLE_API_KEY="your_maps_api_key"
   GOOGLE_GEN_AI_KEY="your_gemini_api_key"
   OAUTH_CLIENT_ID="your_oauth_client_id"
   OAUTH_CLIENT_SECRET="your_oauth_client_secret"
   ```

4. **Set Up OAuth**
   
   Follow the detailed [OAuth setup guide](oauth.md) to configure:
   - OAuth 2.0 credentials
   - App verification for deep linking
   - Redirect URI configuration

5. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```
   Or open the project in Android Studio and run it.

## 🔧 Configuration

### Required API Keys

| Key | Purpose | How to Get |
|-----|---------|------------|
| `GOOGLE_API_KEY` | Google Maps Places API | [Google Cloud Console](https://console.cloud.google.com/apis/credentials) |
| `GOOGLE_GEN_AI_KEY` | Gemini AI API | [Google AI Studio](https://makersuite.google.com/app/apikey) |
| `OAUTH_CLIENT_ID` | Google OAuth | [Google Cloud Console - OAuth](https://console.cloud.google.com/apis/credentials) |
| `OAUTH_CLIENT_SECRET` | Google OAuth | Same as above |

### Deep Link Configuration

The app uses a custom redirect URI: `https://ipiyush.com/mapi/`

**Important:** You'll need to:
1. Own a verified domain for the redirect URI
2. Update `AuthenticationService.kt` with your domain
3. Update `AndroidManifest.xml` intent filters accordingly

See [oauth.md](oauth.md) for detailed instructions.

## 📖 Usage

1. **Launch the App** - Open MaPi on your Android device
2. **Sync Your Places** - Tap the sync button to connect your Google account
3. **Authentication** - Complete Google OAuth flow in your browser
4. **Wait for Sync** - The app will import and process your saved places
5. **Ask for Recommendations** - Type what you're craving (e.g., "Italian food near downtown")
6. **Get Results** - View AI-powered recommendations from your saved places
7. **Navigate** - Tap any result to open it directly in Google Maps

## 🤝 Contributing

We welcome contributions! Here's how you can help:

1. **Fork the repository**
2. **Create a feature branch** (`git checkout -b feature/amazing-feature`)
3. **Commit your changes** (`git commit -m 'Add some amazing feature'`)
4. **Push to the branch** (`git push origin feature/amazing-feature`)
5. **Open a Pull Request**

### Development Guidelines

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful commit messages
- Add tests for new features
- Update documentation as needed

## 🐛 Troubleshooting

### Common Issues

**Authentication Fails**
- Verify your OAuth credentials are correct
- Ensure your redirect URI is properly configured
- Check that your domain is verified for app links

**No Places Found**
- Make sure you have saved places in Google Maps
- Verify the Data Portability API is enabled
- Check network connectivity

**Build Errors**
- Ensure `apikey.properties` file exists with all required keys
- Verify Android SDK and build tools are up to date
- Clean and rebuild the project

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Google** for providing the Maps API, Gemini AI, and OAuth services
- **Jetpack Compose** team for the modern UI toolkit
- **Dagger Hilt** for excellent dependency injection
- **Ktor** for the reliable HTTP client

## 📞 Support

- 🐞 **Bug Reports**: [GitHub Issues](https://github.com/mahyami/mapi/issues)
- 💡 **Feature Requests**: [GitHub Discussions](https://github.com/mahyami/mapi/discussions)
- 📧 **Contact**: [Project Maintainer](mailto:your-email@example.com)

---

<p align="center">
  <strong>Made with ❤️ for food lovers who can't decide where to eat!</strong>
</p>

