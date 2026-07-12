# Runtime Dependency License Inventory

Audit date: 2026-07-12

This inventory is for Picvanta version 1.0.0, resolved from the `releaseRuntimeClasspath`. It is a release record, not a substitute for reviewing a changed dependency graph.

## Apache License 2.0

The following runtime families are covered by `Apache-2.0.txt` in this directory:

- AndroidX: Activity, Annotation, Arch Core, Collection, Compose, Concurrent, Core, Emoji2, Interpolator, Lifecycle, ProfileInstaller, SavedState, Startup, Tracing, VersionedParcelable, and related AndroidX transitive modules.
- Kotlin and KotlinX: `kotlin-stdlib`, JetBrains annotations, and `kotlinx-coroutines`.
- MediaPipe: `com.google.mediapipe:tasks-vision:0.10.35` and `com.google.mediapipe:tasks-core:0.10.35`.
- Google libraries: Guava, Flogger, DataTransport, Firebase Encoders, Error Prone annotations, J2ObjC annotations, ListenableFuture, and JSR-305.
- `javax.inject:javax.inject:1`, Checker Framework compatibility annotations, JSpecify annotations, and Animal Sniffer annotations.

## BSD 3-Clause

- `com.google.protobuf:protobuf-javalite:4.26.1` (Protocol Buffers Lite). The applicable text is `BSD-3-Clause.txt` in this directory.

## Font licenses

- SIL Open Font License 1.1: Anton, Bangers, Bebas Neue, Black Ops One, Codystar, Creepster, Ewert, Fascinate Inline, Fredericka the Great, Frijole, Grenze Gotisch, Metal Mania, Modak, Monoton, Pirata One, Press Start 2P, Racing Sans One, Rampart One, and Rubik Glitch.
- Apache License 2.0: Permanent Marker.

The exact bundled font filenames are listed in `THIRD_PARTY_NOTICES.md`.

## Verification rule

Before every production upload, run:

```powershell
./gradlew :app:dependencies --configuration releaseRuntimeClasspath --console=plain
```

Compare the result with this inventory. Any new runtime dependency must have its license and required attribution added before the AAB is released.
