# Picvanta Play Store Release Checklist

## One-time publisher setup

- [ ] Use the final package name deliberately: `com.gassticker` is permanent after first Play upload. Change it now if a Picvanta-specific ID is preferred.
- [ ] Create a real, monitored support email. Google Play requires it and the privacy policy refers users to it.
- [ ] Create and securely back up the upload keystore. Never commit its passwords or `.jks` file.
- [ ] Create a GitHub repository and push this project. Enable GitHub Pages with **GitHub Actions** as the source. The workflow publishes `docs/` automatically after a push to `main`.
- [ ] Replace `<GITHUB-USERNAME>` and `<REPOSITORY>` in the two store-listing files after Pages is live.

## Build the production bundle

- [ ] Copy `keystore.properties.example` to `keystore.properties` and enter the real upload key details.
- [ ] Run `./gradlew verifyPlayRelease bundleRelease` from the project root.
- [ ] Upload only `app/build/outputs/bundle/release/app-release.aab`, never the debug APK.
- [ ] Enroll in Play App Signing during the first upload and store the upload key in a secure offline/password-managed location.
- [ ] Verify the release manifest has no automation receiver and no `INTERNET` permission.

## Store listing

- [ ] Add the English content from `STORE_LISTING_EN.md` as the default listing.
- [ ] Add `STORE_LISTING_ID.md` as the Indonesian translation.
- [ ] Add a 512 x 512 PNG app icon without transparent padding.
- [ ] Add a 1024 x 500 feature graphic with the Picvanta mark and real app UI.
- [ ] Add at least two phone screenshots for each language. Use real Sticker Editor and Remove BG screens; do not put marketing copy over screenshots.
- [ ] Add the live public privacy policy URL. It must be an active, non-editable web page, not a PDF or a repository file view.
- [ ] Add the monitored support email and optional website.

## App content declarations

- [ ] Complete Data Safety using `DATA_SAFETY.md` only after confirming the uploaded AAB matches this code.
- [ ] Set ads declaration to **No**.
- [ ] Set app access to **No special access required**.
- [ ] Complete the content rating questionnaire truthfully using the submitted build.
- [ ] Choose the target age groups truthfully. Do not opt into the Families program unless the product is intentionally designed and compliant for children.
- [ ] Complete the privacy policy, app category, content, and export compliance declarations in Play Console.

## Final QA

- [ ] Test the signed release on a real phone and tablet, from first launch through export.
- [ ] Test selected JPG and PNG images, an object near an image edge, zoom, brush add/cut, undo/redo, text, and both export paths.
- [ ] Confirm saved PNGs appear in the gallery and remain transparent where expected.
- [ ] Check all text in English and Indonesian listing assets for truncation and spelling.
- [ ] Start with an internal test track, then closed testing, before a production rollout.

## Policy maintenance trigger

Before adding any account, online feature, remote model, analytics, ads, crash reporting, payment, or cloud storage: update the Data Safety declaration, privacy policy, store listing, and release checklist before shipping the change.
