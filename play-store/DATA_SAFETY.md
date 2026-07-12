# Google Play Data Safety Answers - Picvanta 1.0.0

This file is a release record for the exact codebase audited on July 12, 2026. Re-check it after every dependency or feature change.

## App behavior verified

- No `INTERNET` permission.
- No account, sign-in, backend, advertising, analytics, crash reporting, or cloud-storage SDK.
- No device location, contacts, camera, microphone, or broad media permission in the manifest.
- The Android system picker grants access only to a user-selected image.
- Image segmentation and editing run locally with a bundled MediaPipe model.
- PNG exports are written locally to `Pictures/Picvanta` through MediaStore.
- The release manifest excludes the local automation broadcast receiver.

## Play Console selections

### Does your app collect or share any of the required user data types?

Select: **No**.

### Is all user data encrypted in transit?

Not applicable. The app does not transmit user data.

### Do you provide a way for users to request deletion of their data?

Not applicable. The app does not collect or retain user data. Users can delete PNG files they export from their own device gallery or file manager.

### Privacy policy URL

`https://<GITHUB-USERNAME>.github.io/<REPOSITORY>/privacy-policy/`

## Important boundary

Do not use these answers if a future version adds network access, analytics, crash reporting, ads, cloud sync, user accounts, remote AI processing, or another SDK that collects or shares data. Update the privacy policy and Data Safety form first.
