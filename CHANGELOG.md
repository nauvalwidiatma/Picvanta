# Changelog

All notable user-facing changes to Picvanta are tracked here.

## Unreleased

- Completed release license materials: full Apache-2.0, BSD-3-Clause, and OFL-1.1 texts; a per-font inventory; and a SHA-256-verified MagicTouch model provenance record packaged with the app.
- Added production-release preparation: an offline release manifest, signing-key guard, bilingual Google Play listings, Data Safety record, third-party notices, and a GitHub Pages privacy policy.
- Replaced the app launcher/splash logo with the new Picvanta P wordmark supplied by the user.
- Added a branded launch background/splash to avoid the long blank white startup screen.
- Merged separate Add and Cut brush tabs into one `Rapikan` tab with add/cut mode chips.
- Updated automation contract/state to v0.6 with `tab.refine`, `brush_mode.add`, `brush_mode.cut`, and live `brush_mode`.
- Added original-size PNG export. Sticker Editor now has sticker export plus original-size export; Remove BG only exposes original-size export.
- Updated automation contract/state to v0.5 with `save_original_png`.
- Refined Home layout with centered brand area and larger centered tool icons/text.
- Changed Home to a two-item square grid: Sticker Editor and Remove BG.
- Added Remove BG editor mode without text/font/style controls.
- Updated automation contract/state to v0.4 with editor mode routing.
- Rebranded the app from GasSticker to Picvanta while keeping the existing package and stable automation IDs compatible.
- Added full ALTANOVA automation contract API v0.3 for editor iteration.
- Added stable broadcast commands for image loading, sample image loading, magic selection, brush strokes, undo/redo, text styling, border styling, compose, and PNG export.
- Added runtime state fields for `last_saved_uri` and `last_error`.
- Renamed public stable IDs from legacy `outline.*` naming to `border.*` and `control.border_width`.
- Verified debug APK on whyred `162aca6e` with App Lab v2 and broadcast smoke tests.
