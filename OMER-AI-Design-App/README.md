# OMER AI Design App — AI Studio

Standalone Android project folder on the `omer-ai-design-app` branch.

- App name: AI Studio
- Package: `com.omer.aistudio`
- Theme: Black + Purple Premium
- Platform: Android APK
- Policy goal: free access, no subscription, no export watermark

## Current starter scope
- Compose-based dark/purple dashboard
- Navigation for Home, Photo, Video, Script and Projects
- Android photo picker and image preview
- Offline editable video-script template

## Not yet implemented
Advanced AI image generation, object removal, background removal, cloud script generation, video rendering, voice generation and export pipelines need additional model/backend and media-processing work. This starter does not claim those features are live.

## Build
GitHub Actions workflow: `.github/workflows/omer-ai-studio-apk.yml`
The workflow builds a debug APK and uploads it as `OMER-AI-Studio-debug-APK`.
