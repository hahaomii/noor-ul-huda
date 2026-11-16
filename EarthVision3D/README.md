# Earth Vision 3D

Separate Android app starter project. This is stored on branch `earth-vision-3d` and does not change the existing Quran app on `main`.

## Build APK
1. Open the `EarthVision3D` folder in Android Studio.
2. Allow Gradle sync and install requested Android SDK components.
3. Select **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
4. The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

## Data notes
- Satellite layer: Esri World Imagery.
- Place search: OpenStreetMap Nominatim.
- Satellite imagery is not live. Coverage, resolution and image dates vary by area and provider.
- Package ID: `com.earthvision3d.app`.
