# 📱 My First Mobile App: Android Jetpack Compose Showcase

Welcome to your first modern Android application! This app was built using **Kotlin**, **Jetpack Compose**, and **Material 3 Design**.

---

## 🌟 What's in this App?

This **Interactive Showcase App** demonstrates fundamental Android and modern reactive UI principles:

1. **🎨 Material 3 Hero Banner & Dynamic Theme Toggling**:
   - Seamlessly toggle between **Light Mode** and **Dark Mode** via the top-bar action button.
   - Built on Material 3's dynamic color schemes and typography.

2. **⚡ Interactive State Tracker (Counter & Milestone Engine)**:
   - Learn how Jetpack Compose handles state changes cleanly without boilerplate.
   - Includes `+1`, `+5`, `-1`, and `Reset` operations.
   - **Dynamic Badge & Progress**: Unlocks levels as you tap (`🌱 Getting Started` ➔ `⚡ Rising Star` ➔ `🔥 Pro Builder` ➔ `🏆 Master Level`).

3. **💬 Real-Time Reactive Input Playground**:
   - Type your name or a message in the `OutlinedTextField`.
   - Experience instantaneous UI recomposition as Compose updates the greeting preview card with every single keystroke.
   - Interactive topic chips (`Compose`, `Kotlin`, `Material 3`, `Coroutines`, `Architecture`).

4. **⭐ Interactive Favorites / Bookmark Card**:
   - Demonstrates interactive UI state toggles with visual feedback.

5. **🏛️ Core Concept Cards**:
   - Clean explanation cards for **Jetpack Compose**, **Material 3**, **Reactive State & Flows**, and **Clean Architecture**.

---

## 🏗️ Project Architecture

```
app/src/main/kotlin/com/example/showcaseapp/
├── MainActivity.kt               # Main entry point & Theme host
├── Navigation.kt                 # Navigation host & Screen routing
├── NavigationKeys.kt             # Type-safe navigation keys
├── data/
│   └── DataRepository.kt         # Data layer (Flow-based data stream)
├── ui/
│   ├── main/
│   │   ├── MainScreen.kt         # Main Composable screen & Material 3 UI
│   │   └── MainScreenViewModel.kt # State management & Unidirectional Data Flow (UDF)
│   └── theme/
│       ├── Color.kt              # Material 3 light & dark palette definitions
│       ├── Theme.kt              # App theme provider
│       └── Type.kt               # Material 3 typography definitions
```

---

## 🚀 How to Run and Test the App

### 1. In Android Studio (Already Opened & Ready)
The project is currently loaded in **Android Studio**:
- Select your target device (physical device or emulator) from the device dropdown at the top.
- Click the green **Run** button (`Shift + F10`) or click the **Split / Preview** tab in `MainScreen.kt` to view the interactive Compose preview.

### 2. Run on a Physical Android Device
1. Enable **Developer Options** and **USB Debugging** on your Android phone.
2. Connect your device via USB (or wireless ADB).
3. Verify connection by running:
   ```powershell
   adb devices
   ```
4. Deploy the app:
   ```powershell
   android run
   ```

### 3. Build APK from Command Line
```powershell
.\gradlew.bat assembleDebug
```
The output debug APK will be created at:
`app/build/outputs/apk/debug/app-debug.apk`
