# Picvanta

Picvanta is an on-device Android sticker and background-removal editor. It uses a local MediaPipe segmentation model, keeps selected photos on the device, and exports PNG files to `Pictures/Picvanta`.

## Release materials

- [English Google Play listing](play-store/STORE_LISTING_EN.md)
- [Indonesian Google Play listing](play-store/STORE_LISTING_ID.md)
- [Data Safety declaration record](play-store/DATA_SAFETY.md)
- [Release checklist](play-store/RELEASE_CHECKLIST.md)
- [Public privacy policy source](docs/privacy-policy/index.html)
- [Third-party notices](THIRD_PARTY_NOTICES.md)

## Build

Use Android Studio or Gradle with JDK 17.

```powershell
./gradlew assembleDebug
```

For Play Store release, create a private `keystore.properties` from `keystore.properties.example`, then run:

```powershell
./gradlew verifyPlayRelease bundleRelease
```

Do not commit the upload keystore or `keystore.properties`.
