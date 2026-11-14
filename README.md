# Noor-ul-Huda — Android starter app

An Urdu-first Islamic reading app starter project using Kotlin + Android WebView.

## Included
- Quran reading modes: Arabic-only and Arabic with Urdu Jalandhri translation
- Surah-by-surah audio recitation by Mishary Rashid Alafasy, with ayah controls (internet required)
- Readable system Arabic font (no Al Qalam Quran Majeed font)
- Sahih al-Bukhari Arabic + Urdu edition loader using fawazahmed0/hadith-api
- Search/filter and locally saved ayah/hadith items
- Emerald/gold Islamic visual theme
- GitHub Actions workflow to build a debug APK without Android Studio

## Build on a computer
Open this folder in Android Studio (Hedgehog or newer), allow Gradle sync, then choose **Build > Build Bundle(s) / APK(s) > Build APK(s)**.

## Build using only a phone
Follow `BUILD_FROM_MOBILE_UR.md`. Upload the contents of this folder to a GitHub repository (including `.github/workflows/build-apk.yml`), then run the workflow under the repository's Actions tab and download the APK artifact.

## Data sources
- Quran: `https://api.alquran.cloud/` (Arabic Quran + `ur.jalandhry` Urdu translation)
- Hadith: `https://github.com/fawazahmed0/hadith-api` (Arabic and Urdu Bukhari editions)

## Important limitations
- Internet is required for Quran and Hadith data.
- Hadith Arabic/Urdu records are paired by dataset order. Before public release, validate record counts, numbering, chapter metadata, and translation alignment against a trusted printed edition. This is not a scholarly verification claim.
- The Hadith screen currently renders the first 150 records initially; pagination and chapter/book navigation remain to be added.
- Offline Quran/audio downloads, background playback controls, prayer-time notifications, tafsir, and other hadith collections are not included yet.
- No APK has yet been compiled or tested on a physical device.


## Islamic Edition (branch: `islamic-edition`)

This branch is the separate audio-focused edition. The Quran screen opens Audio Quran only; written Quran Arabic and translation reading screens are not exposed in this edition pending independent scholarly verification. The original `main` branch is unchanged.

### Build APK from GitHub Actions
A workflow is included at `.github/workflows/build-apk.yml`. It installs Java 17 and Gradle 8.9, runs `gradle assembleDebug`, and uploads `app-debug.apk` as the `Noor-ul-Huda-Islamic-Edition-APK` artifact. Open the repository's Actions tab, select the latest run for `islamic-edition`, and download its artifact after the run succeeds.

Audio streaming and gallery downloads require an internet connection. The in-app mini-player remains available while navigating between app pages. Background playback after the Android app is force-closed has not yet been verified on a physical device.
