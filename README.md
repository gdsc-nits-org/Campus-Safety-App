# Campus Safety App

A mobile application designed to enhance campus safety by allowing users to report bugs and safety concerns directly from their Android devices. Built with Kotlin and modern Android development practices.

## 📱 Overview

The Campus Safety App is an Android application that streamlines the bug reporting process for campus communities. Users can easily describe issues, attach screenshots, and send reports directly to the development team via email integration.

## ✨ Features

- **Bug Reporting**: Simple and intuitive interface to report bugs and issues
- **Screenshot Attachment**: Attach images/screenshots to bug reports for better documentation
- **Email Integration**: Send reports directly to the development team using Gmail
- **File Management**: Browse and select files from device storage
- **Confirmation Screen**: Dedicated "Done" activity confirming successful report submission
- **Material Design**: Modern UI following Material Design guidelines
- **Edge-to-Edge UI**: Full-screen modern Android experience with system insets handling

## 🛠️ Tech Stack

- **Language**: Kotlin
- **Min SDK**: Android 7.0 (API 24)
- **Target SDK**: Android 14 (API 34)
- **Compile SDK**: Android 15 (API 35)
- **Build System**: Gradle with Kotlin DSL

### Dependencies

- **AndroidX**:
  - `androidx.core:core-ktx` - Kotlin extensions for Android core library
  - `androidx.appcompat:appcompat` - Backward compatibility for AppCompat features
  - `androidx.activity` - Activity component
  - `androidx.constraintlayout` - Constraint layout for flexible UI design

- **Material Design**:
  - `material:material` - Material Design components

- **Testing**:
  - JUnit - Unit testing framework
  - AndroidX Test - Instrumented testing

## 📂 Project Structure

```
Campus-Safety-App/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/campussafetyapp/
│   │   │   │   ├── MainActivity.kt          # Main activity entry point
│   │   │   │   ├── BugActivity.kt           # Bug reporting screen
│   │   │   │   └── DoneActivity.kt          # Confirmation screen
│   │   │   ├── res/
│   │   │   │   ├── layout/                  # XML layout files
│   │   │   │   ├── drawable/                # Drawable resources
│   │   │   │   └── xml/                     # Backup and extraction rules
│   │   │   └── AndroidManifest.xml          # App configuration
│   │   ├── androidTest/                     # Instrumented tests
│   │   └── test/                            # Unit tests
│   ├── build.gradle.kts                     # App-level build configuration
│   ├── google-services.json                 # Firebase configuration (if applicable)
│   └── proguard-rules.pro                   # ProGuard configuration
├── gradle/
│   └── libs.versions.toml                   # Centralized dependency versions
├── build.gradle.kts                         # Project-level build configuration
├── settings.gradle.kts                      # Gradle settings
├── gradle.properties                        # Gradle properties
└── README.md                                # This file
```

## 🎯 Main Components

### MainActivity
Entry point of the application that initializes the main UI with edge-to-edge display support.

**Features**:
- Enables edge-to-edge display
- Handles system insets (status bar, navigation bar)
- Sets up the main layout

### BugActivity
The core activity for reporting bugs with attachment capabilities.

**Features**:
- Bug description text input
- Image attachment picker
- File attachment display with filename
- Email intent integration with Gmail
- Toast notifications for user feedback
- File URI permission handling

**Key Methods**:
- `onCreate()`: Initializes UI bindings and sets up click listeners
- `pickImage()`: Handles image selection from device storage
- `sendBugBtn`: Validates input and launches email intent
- `onResume()`: Handles post-email navigation to DoneActivity

### DoneActivity
Confirmation screen displayed after successfully sending a bug report.

**Features**:
- Confirmation message display
- Back button navigation
- System back button handling
- Returns user to BugActivity

## 🔧 Installation & Setup

### Prerequisites
- Android Studio (latest version recommended)
- JDK 1.8 or higher
- Android SDK 24 (API 24) or higher
- Gmail app installed on testing device (for email functionality)

### Clone the Repository

```bash
git clone https://github.com/gdsc-nits-org/Campus-Safety-App.git
cd Campus-Safety-App
```

### Build the Project

1. **Using Android Studio**:
   - Open the project in Android Studio
   - Android Studio will automatically download required dependencies
   - Click "Build" > "Make Project"

2. **Using Command Line**:
   ```bash
   ./gradlew clean build
   ```

### Run the Application

1. **Using Android Studio**:
   - Connect an Android device or launch an emulator
   - Click "Run" > "Run 'app'"

2. **Using Command Line**:
   ```bash
   ./gradlew installDebug
   ```

## 🚀 Usage

### Reporting a Bug

1. **Launch the App**: Open the Campus Safety App from your device
2. **Describe the Issue**: Type your bug description in the text field
3. **Attach Evidence** (Optional): 
   - Tap the "Choose File" button
   - Select an image/screenshot from your device
4. **Send Report**: 
   - Tap the "Send" button
   - Gmail will open with the bug report pre-filled
   - Review and send the email
5. **Confirmation**: The app shows a confirmation screen after sending

### Email Configuration

Reports are sent to: `kkunaljit@gmail.com`

To change the recipient email, modify the `EXTRA_EMAIL` parameter in `BugActivity.kt`:

```kotlin
putExtra(Intent.EXTRA_EMAIL, arrayOf("your-email@example.com"))
```

## 🔐 Permissions

The app requires the following permissions (declared in `AndroidManifest.xml`):

- `android.permission.INTERNET` - For email functionality

Additional implicit permissions:
- Reading external storage (for file selection)
- Gmail app package access (for email client integration)

## 🎨 UI/UX

### Design Features
- Material Design 3 compliance
- Edge-to-edge display with inset handling
- Responsive layouts using ConstraintLayout
- Custom drawables for visual elements
- Smooth activity transitions

### Layout Files
- `activity_main.xml` - Main activity layout
- `activity_bug.xml` - Bug reporting form layout
- `activity_done.xml` - Confirmation screen layout

## 📦 Build Configuration

### Gradle Configuration
- **Build Tools Version**: 35
- **Compile SDK**: 35
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

### Version Information
- **Current Version**: 1.0
- **Version Code**: 1

## 🧪 Testing

The project includes test configurations:

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

Test files are located in:
- `app/src/test/java/` - Unit tests
- `app/src/androidTest/java/` - Instrumented tests

## 🐛 Troubleshooting

### Gmail App Not Found
- Ensure Gmail is installed on your testing device
- The app requires Gmail for email functionality
- Without Gmail, users will see a toast message: "Gmail app not found!"

### File Selection Issues
- Grant file access permissions when prompted
- Ensure the selected image file is valid and accessible
- Check content resolver for proper URI handling

### Activity Navigation Issues
- The app uses Intent flags to properly manage back stack
- Use the provided back buttons for consistent navigation

## 🔄 Project Status

- **Status**: Active Development
- **Latest Version**: 1.0
- **Last Updated**: 2026
- **Organization**: GDSC NITS (Google Developer Student Clubs - NIT Silchar)

## 📝 Development Guidelines

### Code Style
- Kotlin best practices
- ViewBinding for type-safe view access
- Activity-based architecture
- Material Design principles

### Adding New Features
1. Create new Activity classes extending `AppCompatActivity`
2. Use ViewBinding for layout references
3. Implement edge-to-edge display support
4. Handle system insets properly
5. Add appropriate intent-filters in `AndroidManifest.xml`

## 🤝 Contributing

Contributions are welcome! To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📋 Future Enhancements

Potential features for future releases:
- Multiple recipient emails for bug reports
- Category selection for bug types
- Priority levels for bug severity
- Database integration for offline storage
- Analytics integration for crash reporting
- User authentication and account management
- Push notifications for bug report status
- Camera integration for real-time screenshots
- Attachment file size validation

## 📄 License

This project is maintained by GDSC NITS. For license information, please contact the organization.

## 📞 Support & Contact

- **Organization**: Google Developer Student Clubs - NIT Silchar
- **Repository**: [gdsc-nits-org/Campus-Safety-App](https://github.com/gdsc-nits-org/Campus-Safety-App)
- **Bug Reports**: Use the app itself to report issues!

## 🙏 Acknowledgments

- **Developer**: Kunal Jit (contact: kkunaljit@gmail.com)
- **Organization**: GDSC NITS
- **Frameworks**: Google's Material Design, AndroidX libraries

---

**Made with ❤️ by GDSC NITS for Campus Safety**
