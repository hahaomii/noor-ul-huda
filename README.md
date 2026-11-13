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
