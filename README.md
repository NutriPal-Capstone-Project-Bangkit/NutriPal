# Mobile Development - Nutripal 

![image alt](https://github.com/NutriPal-Capstone-Project-Bangkit/NutriPal/blob/main/screenshot.png?raw=true)

## Overview

Nutripal is a mobile application designed to track users nutrition and provide personalized recommendations. It includes features such as history scan, daily tracking with charts, and scan-based recommendations.  After scanning with OCR, users get recommendations that can be added to their daily intake, all supported by advanced APIs and local database storage.

## Features 

- **User Login and Registration**: Users can log in using their Gmail or Google account. If they choose Gmail, they must first verify their email. There is also a "Remember Me" feature for email login.

- **Personal Details**: Users can fill in their personal details such as name, gender, age, height, weight, and activity level. A profile picture can be uploaded (optional). This information is used to personalize recommendations after scanning nutrition labels.

- **News Feed**: A feature on the home screen that displays health-related news.

- **Chatbot**: Users can consult with a health chatbot powered by Gemini API for health-related queries.

- **Nutrition Label Scan**: Users can scan food labels. The app uses a bounding box that follows the object and provides a confidence score for recognition.

- **Result Processing**: After scanning and taking a picture, users can crop the photo, which will then direct them to the result page. The app processes the nutrition data through OCR (using ML Kit Text Recognition), displaying macronutrient information and personalized recommendations based on user details and the recognized data.

- **Add to Daily Nutrition**: After receiving the scan result, users can add the product (food, drink, or snack) to their daily nutrition. They can edit the nutrition data if it doesn’t match the OCR output and refresh recommendations to get updated results.

- **Daily Nutrition Tracking**: Users can scroll through dates to view products they have scanned and added, with nutritional information displayed for each day.

- **History**: On the profile page, users can view their scan history. Clicking on an item will direct them to the result page.

- **Edit Profile**: Users can edit and save their personal details (name, age, height, etc.) in the profile section.

- **Change Email and Password**: Users have the option to change their email address and password.

- **About the App**: A section providing information about the app.

- **Logout**: Users can log out of the app from the profile page.

## Installation

To install and use the NutriPal app, follow these steps:

1. **Download the APK**
   - You can download the latest APK from the [releases page](https://github.com/your-repo/nutripal/releases) (release app).

2. **Enable Unknown Sources (if needed)**
   - On your Android device, go to **Settings** > **Security** or **Apps & notifications**, and enable **Install from Unknown Sources** to allow installing APKs outside of the Play Store.

3. **Install the APK**
   - Open the downloaded APK file and tap **Install** to install NutriPal on your device.

4. **Launch the App**
   - After installation, open the app from your app drawer.
  
## Usage

Once installed, you can log in using your Gmail or Google account, fill in your personal details, and start scanning nutrition labels to track your daily intake.

## Technologies Used

- **Jetpack Compose**: For building modern UI with declarative components.
- **Firebase Authentication**: For user login via email and Google account authentication.
- **ML Kit Text Recognition**: For OCR functionality to recognize text from scanned images.
- **Gemini API**: For chatbot interactions related to health queries.
- **Room Database**: For local storage of scanned nutrition data and daily nutrition tracking.
- **CameraX**: For camera integration and handling photo captures.
- **Retrofit**: For API calls to retrieve health-related data and news.
- **TensorFlow Lite**: For integrating machine learning models for nutrition analysis.
- **Hilt**: For dependency injection across the app.
- **Coil**: For image loading in Jetpack Compose.
- **Accompanist**: For system UI controls like status bar appearance.
