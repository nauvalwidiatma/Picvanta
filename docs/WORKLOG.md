# Worklog

Internal engineering notes for each Picvanta iteration. Keep this more practical than the changelog: what changed, what was tested, and what remains risky.

## 2026-07-03 - Merged Brush Refine Tab

Goal:
- Merge Add and Cut into one clearer editing menu named Rapikan.

Changed:
- Replaced separate Add/Cut tabs with one `Rapikan` tab.
- Added `Tambah` and `Kurangi` mode chips inside Rapikan.
- Routed brush gestures through the active brush mode instead of the tab name.
- Kept automation compatibility: `set_tab add` and `set_tab cut` still work as aliases.
- Updated automation contract/state to v0.6 with `tab.refine`, `brush_mode.add`, `brush_mode.cut`, and runtime `brush_mode`.

Notes:
- No commit made for this iteration yet; commit only on explicit user request.

## 2026-07-03 - Original Size Export

Goal:
- Let Sticker Editor export both a styled sticker PNG and an original-size cutout PNG, while Remove BG stays focused on original-size cutout only.

Changed:
- Added `Simpan ukuran asli` export action.
- Sticker Editor export tab now shows `Simpan sticker` and `Simpan ukuran asli`.
- Remove BG export tab only shows `Simpan ukuran asli`.
- Added automation command `save_original_png` / `export_original_png`.
- Updated automation contract/state to v0.5.

Notes:
- Original-size export uses the source image dimensions and current mask as a transparent cutout.
- No commit made for this iteration yet; commit only on explicit user request.

## 2026-07-03 - Home Layout Polish

Goal:
- Make Home feel more like a polished app launcher: 30% brand area and 70% tool grid area.

Changed:
- Centered Picvanta brand and tagline in the top portion.
- Kept the lower area for the tool grid.
- Enlarged tool icons and centered card text.
- Differentiated Sticker Editor and Remove BG icons.

Notes:
- No stable ID changes.
- No commit made for this iteration yet; commit only on explicit user request.

## 2026-07-03 - Home Grid and Remove BG Mode

Goal:
- Make the Home screen a square app-tool grid and split the current editor into Sticker Editor plus Remove BG.

Changed:
- Replaced the single wide Home card with two square cards.
- Added `Sticker Editor` entry for the existing full editor with text/font/style controls.
- Added `Remove BG` entry using the same selection/brush/export engine without Text and Style tabs.
- Remove BG mode composes/export transparent cutout output instead of the styled sticker canvas.
- Added automation commands `open_sticker_editor` and `open_remove_bg_editor`.
- Updated automation contract/state to v0.4 with `editor_mode`.

Notes:
- Package and broadcast action remain `com.gassticker` for compatibility.
- `open_editor` still routes to Sticker Editor as the default legacy behavior.

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
