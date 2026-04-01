# Campus Safety App

An Android application built for NIT Silchar students to enhance campus safety by providing an easy way to report issues and bugs directly to the development team.

---

## Features

- **Bug / Issue Reporting** – Users can describe a problem and optionally attach a screenshot, which is sent directly via Gmail to the development team.
- **File Attachment** – Pick any image from the device gallery to attach alongside the bug report.
- **Confirmation Screen** – A dedicated "Done" screen confirms successful submission.
- **Edge-to-Edge UI** – Supports Android's edge-to-edge display for a modern look on all screen sizes.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | XML Layouts + View Binding |
| Backend / Auth | Firebase (google-services.json) |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 34 (Android 14) |
| Build System | Gradle (Kotlin DSL) |

---

## Project Structure

```
app/src/main/
├── java/com/example/campussafetyapp/
│   ├── BugActivity.kt      # Report a bug (launcher activity)
│   ├── MainActivity.kt     # Main campus safety screen
│   └── DoneActivity.kt     # Confirmation screen after submission
└── res/
    ├── layout/             # XML layouts for each activity
    ├── drawable/           # App icons and backgrounds
    └── values/             # Colors, strings, themes
```

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- Android SDK 35
- A Google account / Gmail app on the test device (required for bug reporting)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/gdsc-nits-org/Campus-Safety-App.git
   cd Campus-Safety-App
   ```

2. **Open in Android Studio**
   - Open Android Studio → *Open an existing project* → select the cloned folder.

3. **Add `google-services.json`**
   - Place your `google-services.json` (from Firebase Console) inside the `app/` directory.

4. **Build and run**
   - Connect a physical device or start an emulator (API 24+).
   - Click **Run ▶** or use `Shift + F10`.

---

## How It Works

1. The app launches directly into **BugActivity**.
2. The user types a description of the issue in the text field.
3. Optionally, the user taps **Choose a file** to attach a screenshot.
4. Tapping **Submit** opens Gmail (if installed) pre-filled with the report details sent to the development team.
5. After returning from Gmail, the app automatically navigates to **DoneActivity**, confirming the report was sent.

---

## Contributing

1. Fork the repository and create a feature branch.
2. Commit your changes with clear, descriptive messages.
3. Open a Pull Request against the `master` branch.

---

## License

This project is maintained by [GDSC NIT Silchar](https://github.com/gdsc-nits-org). All rights reserved.
