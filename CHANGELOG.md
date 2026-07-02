# Changelog

All notable user-facing changes to Picvanta are tracked here.

## Unreleased

- Changed Home to a two-item square grid: Sticker Editor and Remove BG.
- Added Remove BG editor mode without text/font/style controls.
- Updated automation contract/state to v0.4 with editor mode routing.
- Rebranded the app from GasSticker to Picvanta while keeping the existing package and stable automation IDs compatible.
- Added full ALTANOVA automation contract API v0.3 for editor iteration.
- Added stable broadcast commands for image loading, sample image loading, magic selection, brush strokes, undo/redo, text styling, border styling, compose, and PNG export.
- Added runtime state fields for `last_saved_uri` and `last_error`.
- Renamed public stable IDs from legacy `outline.*` naming to `border.*` and `control.border_width`.
- Verified debug APK on whyred `162aca6e` with App Lab v2 and broadcast smoke tests.
