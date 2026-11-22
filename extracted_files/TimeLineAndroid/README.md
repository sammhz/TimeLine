# TimeLine — Android prototype source

A minimal native Android timer prototype with a Home Screen widget. It supports:
- User-defined duration in hours/minutes
- Countdown and elapsed-time display modes
- Horizontal and vertical widget layouts
- Persisted start/end timestamps (survives app closure and reboot)
- Periodic widget refresh

## Build

Open this folder in Android Studio (Jellyfish or newer), allow Gradle sync, then choose **Build > Build Bundle(s) / APK(s) > Build APK(s)**.

The build environment used to prepare this source archive did not include Android SDK/Gradle, so an APK could not be compiled or device-tested here.

## Add the Home Screen widget

Long-press the Home Screen > Widgets > TimeLine > add the widget.

## Try Lock Screen placement on Samsung

1. Install/update Good Lock and its LockStar module from Galaxy Store if available for your region/device.
2. Open LockStar and edit the Lock Screen.
3. Choose Add Widget and look for TimeLine. If listed, add and position it near the Samsung clock, then save.
4. Wake the phone to test. LockStar and widget availability can vary by device/firmware/region; this is not guaranteed by the app itself.

## Prototype limitations

- Widget text refresh is scheduled approximately once per minute; Android may defer background updates to save battery.
- The Android widget's vertical mode is a rotated progress bar and may need visual adjustment on the actual device.
- This is a starter prototype, not a signed/release APK. Package name: `com.example.timeline`.

## Build APK without a computer (GitHub Actions)

This project includes `.github/workflows/build-apk.yml` for a cloud build.
1. Create a GitHub repository and upload the **contents** of this project folder (not the ZIP file itself).
2. In the repository, open **Actions** > **Build TimeLine APK** > **Run workflow**.
3. When the run finishes, open that run and download the `TimeLine-debug-apk` artifact.
4. Extract the downloaded artifact ZIP on the phone; install `app-debug.apk`. Android may ask you to allow installs from the browser or file manager.

The cloud build compiles a debug APK. It is suitable for local testing, not Play Store distribution. The Lock Screen / LockStar behavior still needs to be tested on the target Samsung phone.
