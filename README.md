# Arogya Nidhi 🏥

Arogya Nidhi is an Android application built with Kotlin and Jetpack Compose that helps citizens discover government healthcare schemes they are eligible for. By answering a few simple questions (like income, BPL status, and occupation), the app filters and displays the best healthcare schemes and supported hospitals securely pulled from a Firebase Firestore backend.

## ✨ Features

* **Interactive Eligibility Quiz:** A step-by-step UI to collect user details (Income, BPL status, Occupation) built with Jetpack Compose.
* **Real-time Eligibility Logic:** Instantly processes user input against database constraints to find matching healthcare schemes.
* **Cloud Database Integration:** Fetches up-to-date schemes and hospital data directly from Firebase Firestore.
* **Modern Android Architecture:** Built using MVVM (Model-View-ViewModel) pattern and Kotlin Coroutines/Flows for asynchronous data handling.
* **Clean UI:** Responsive and modern user interface utilizing Jetpack Compose Material Design.

## 🛠️ Tech Stack

* **Language:** [Kotlin](https://kotlinlang.org/)
* **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
* **Architecture:** MVVM (ViewModel, StateFlow)
* **Backend / Database:** [Firebase Cloud Firestore](https://firebase.google.com/docs/firestore)
* **IDE:** Android Studio (Ladybug / Koala or newer)

## 🗄️ Database Structure (Firestore)

The app relies on two main collections in Firebase Firestore:

### 1. `schemes` Collection
Stores the rules and details for each healthcare scheme.
* `id` (String - Auto-generated)
* `name` (String)
* `description` (String)
* `maxIncome` (Number)
* `bplRequired` (Boolean)
* `occupations` (Array of Strings)

### 2. `hospitals` Collection
Stores hospital details and the schemes they accept.
* `id` (String - Auto-generated)
* `name` (String)
* `district` (String)
* `type` (String - e.g., "Public", "Private")
* `supportedSchemes` (Array of Strings)

## 🚀 Getting Started

Follow these steps to run the project locally on your machine:

### 1. Prerequisites
* **Android Studio** installed on your computer.
* A **Google Account** to access the Firebase Console.

### 2. Clone/Open the Project
1. Open Android Studio.
2. Select **File > Open** and locate the `ArogyaNidhi` project folder.

### 3. Set up Firebase
To connect the app to your own database, you need to configure Firebase:
1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Create a new project (e.g., "Arogya Nidhi Database").
3. Click on the **Android icon** to add an Android app to your project.
4. Enter your app's package name (e.g., `com.example.arogyanidhi`).
5. Download the `google-services.json` file.
6. In Android Studio, switch the Project view on the left from "Android" to "Project".
7. Drag and drop the downloaded `google-services.json` file into your `app` folder (`ArogyaNidhi/app/`).

### 4. Enable Firestore
1. In the Firebase Console, go to **Build > Firestore Database**.
2. Click **Create Database**.
3. Select **Start in test mode** (allows read/write during development).
4. Select a location close to you (e.g., `asia-south1`) and click **Enable**.
5. Add some sample data to the `schemes` and `hospitals` collections matching the structure mentioned above.

### 5. Run the App
1. Sync your project with Gradle files (Click the elephant icon with the sync arrow in Android Studio).
2. Connect a physical Android device via USB (with USB Debugging enabled) or start an Android Emulator.
3. Click the green **Run** button (Shift+F10) to build and launch the app.

## 🤝 Contributing

This is a beginner-friendly project created for educational purposes. Feel free to fork the repository, make improvements, and submit pull requests!

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).
