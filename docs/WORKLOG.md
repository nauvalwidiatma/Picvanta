# Worklog

Internal engineering notes for each Picvanta iteration. Keep this more practical than the changelog: what changed, what was tested, and what remains risky.

## 2026-07-02 - Picvanta Rebrand

Goal:
- Rebrand the app from GasSticker to Picvanta as a broader photo editor, not only a sticker tool.

Changed:
- Updated Android app label to `Picvanta`.
- Updated Home and editor screen copy to Picvanta / Cutout & Photo Editor positioning.
- Updated exported PNG naming and save folder to `Pictures/Picvanta`.
- Updated automation contract app name to `Picvanta`.
- Kept package `com.gassticker`, broadcast action `com.gassticker.AUTOMATION`, and stable IDs unchanged for automation compatibility.

Notes:
- Package migration to a future `com.picvanta` namespace should be handled as a separate release because it changes install identity and automation receiver names.

## 2026-07-02 - Automation Contract v0.3

Goal:
- Make Picvanta fast to iterate through ALTANOVA whyred automation with stable IDs and non-interactive broadcast commands.

Changed:
- Added full runtime automation API through `AutomationBridge` and `AutomationReceiver`.
- Added commands for `open_editor`, `navigate_home`, `open_gallery`, `load_sample_image`, `load_image_path`, `load_image_uri`, `magic_select`, `select_full_image`, `clear_selection`, `brush_stroke`, `undo`, `redo`, `set_text`, `move_text`, `reset_text_position`, `set_font`, `set_text_style`, `set_font_color`, `set_text_style_color`, `set_border_color`, `set_border_width`, `set_brush_size`, `compose_sticker`, and `save_png`.
- Updated `automation-contract.json` and app asset contract to `altanova.automation_contract.v0.3`.
- Updated `automation-state.json` and app asset state to `altanova.automation_state.v0.3`.
- Standardized public stable IDs around `border.*` and `control.border_width`.

Verified:
- Built debug APK successfully.
- Installed debug APK to whyred `162aca6e`.
- Confirmed `dump_contract` from installed APK returns v0.3.
- Confirmed sample image, selection, brush, undo/redo, text/font/color/border styling, and PNG save through broadcast API.
- Confirmed `magic_select` async flow: `is_processing=true` while running, then `has_selection=true`.
- Ran App Lab v2 with no failed steps.
- Confirmed kernel injection status remained `disabled`.

Notes:
- For shell commands, prefer `brush_stroke` with `from_x/from_y/to_x/to_y` or `points_json`; raw semicolon-separated `points` can be interpreted by the Android shell if not escaped.
