package com.gassticker

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.provider.MediaStore
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private val OutlineOptions = listOf(
    "Putih" to Color.WHITE,
    "Hitam" to Color.rgb(28, 28, 28),
    "Biru" to Color.rgb(0, 122, 255),
    "Merah" to Color.rgb(255, 59, 48),
    "Teal" to Color.rgb(47, 196, 166),
    "Kuning" to Color.rgb(255, 207, 31),
    "Pink" to Color.rgb(241, 141, 184),
    "Ungu" to Color.rgb(128, 90, 213),
    "Hijau" to Color.rgb(52, 199, 89),
    "Oranye" to Color.rgb(255, 149, 0),
    "Abu" to Color.rgb(142, 142, 147),
    "Navy" to Color.rgb(10, 44, 92),
    "Lime" to Color.rgb(190, 242, 100),
)

private val ProInk = androidx.compose.ui.graphics.Color(0xFF111827)
private val ProMuted = androidx.compose.ui.graphics.Color(0xFF6B7280)
private val ProSubtle = androidx.compose.ui.graphics.Color(0xFF9CA3AF)
private val ProBackground = androidx.compose.ui.graphics.Color(0xFFF5F5F7)
private val ProSurface = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
private val ProSurfaceAlt = androidx.compose.ui.graphics.Color(0xFFF9FAFB)
private val ProBorder = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
private val ProBlue = androidx.compose.ui.graphics.Color(0xFF007AFF)
private val ProBlueSoft = androidx.compose.ui.graphics.Color(0xFFEAF3FF)
private val ProDanger = androidx.compose.ui.graphics.Color(0xFFFF3B30)

private val AppColors = lightColorScheme(
    primary = ProBlue,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    secondary = ProBlue,
    tertiary = ProMuted,
    background = ProBackground,
    surface = ProSurface,
    onBackground = ProInk,
    onSurface = ProInk,
    outline = ProBorder,
)

private data class FontChoice(
    val name: String,
    val assetPath: String,
)

private data class TextTreatmentChoice(
    val name: String,
    val treatment: StickerTextTreatment,
)

private val FontChoices = listOf(
    FontChoice("Anton", "fonts/Anton.ttf"),
    FontChoice("Bangers", "fonts/Bangers.ttf"),
    FontChoice("Bebas Neue", "fonts/BebasNeue.ttf"),
    FontChoice("Black Ops", "fonts/BlackOpsOne.ttf"),
    FontChoice("Codystar", "fonts/Codystar.ttf"),
    FontChoice("Creepster", "fonts/Creepster.ttf"),
    FontChoice("Ewert", "fonts/Ewert.ttf"),
    FontChoice("Fascinate", "fonts/FascinateInline.ttf"),
    FontChoice("Fredericka", "fonts/FrederickatheGreat.ttf"),
    FontChoice("Frijole", "fonts/Frijole.ttf"),
    FontChoice("Gotisch", "fonts/GrenzeGotisch.ttf"),
    FontChoice("Monoton", "fonts/Monoton.ttf"),
    FontChoice("Marker", "fonts/PermanentMarker.ttf"),
    FontChoice("Pirata", "fonts/PirataOne.ttf"),
    FontChoice("Pixel", "fonts/PressStart2P.ttf"),
    FontChoice("Racing", "fonts/RacingSansOne.ttf"),
    FontChoice("Rampart", "fonts/RampartOne.ttf"),
    FontChoice("Metal", "fonts/MetalMania.ttf"),
    FontChoice("Modak", "fonts/Modak.ttf"),
    FontChoice("Glitch", "fonts/RubikGlitch.ttf"),
)

private val TextTreatmentChoices = listOf(
    TextTreatmentChoice("Stroke", StickerTextTreatment.Stroke),
    TextTreatmentChoice("Shadow", StickerTextTreatment.Shadow),
    TextTreatmentChoice("Bubble", StickerTextTreatment.Bubble),
    TextTreatmentChoice("Pop", StickerTextTreatment.Pop),
    TextTreatmentChoice("Neon", StickerTextTreatment.Neon),
    TextTreatmentChoice("Candy", StickerTextTreatment.Candy),
    TextTreatmentChoice("Comic", StickerTextTreatment.Comic),
    TextTreatmentChoice("Gold", StickerTextTreatment.Gold),
    TextTreatmentChoice("Stamp", StickerTextTreatment.Stamp),
)

private fun Modifier.stableId(id: String, role: String): Modifier =
    semantics { contentDescription = "id:$id role:$role" }

private fun stableSlug(value: String): String =
    value.lowercase().replace(Regex("[^a-z0-9]+"), "_").trim('_')

private fun automationOk(command: String, block: JSONObject.() -> Unit = {}): JSONObject =
    JSONObject()
        .put("ok", true)
        .put("handled", true)
        .put("command", command)
        .apply(block)

private fun automationError(command: String, message: String): JSONObject =
    JSONObject()
        .put("ok", false)
        .put("handled", true)
        .put("command", command)
        .put("error", message)

private fun Int.toHexColor(): String =
    String.format("#%06X", 0xFFFFFF and this)

private fun localizedText(context: Context, indonesian: String, english: String): String =
    if (context.resources.configuration.locales[0]?.language == "id") indonesian else english

@Composable
private fun uiText(indonesian: String, english: String): String =
    localizedText(LocalContext.current, indonesian, english)

private fun JSONObject.optColor(name: String, fallback: Int): Int =
    if (!has(name)) fallback else runCatching {
        val raw = get(name)
        when (raw) {
            is Number -> raw.toInt()
            else -> {
                val text = raw.toString().trim()
                Color.parseColor(if (Regex("^[0-9A-Fa-f]{6}$").matches(text)) "#$text" else text)
            }
        }
    }.getOrElse { fallback }

private data class StickerResult(
    val mask: SegmentMask,
    val boundary: IntArray,
    val sticker: Bitmap,
)

private enum class EditorTab {
    Select,
    Refine,
    Text,
    Style,
    Export,
}

private enum class AppScreen {
    Home,
    Editor,
}

private enum class EditorMode {
    Sticker,
    RemoveBg,
}

private enum class ColorPickerTarget {
    Font,
    Style,
    Outline,
}

private fun BrushMode.toAutomationValue(): String =
    if (this == BrushMode.Subtract) "cut" else "add"

private fun BrushMode.toBrushStatus(): String =
    if (this == BrushMode.Subtract) "Mengurangi seleksi..." else "Menambah seleksi..."

@Composable
fun GasStickerApp() {
    MaterialTheme(colorScheme = AppColors) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            ProBackdrop {
                GasStickerRoot()
            }
        }
    }
}

@Composable
private fun ProBackdrop(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ProBackground),
    ) {
        content()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GasStickerRoot() {
    var screen by remember { mutableStateOf(AppScreen.Home) }
    var editorMode by remember { mutableStateOf(EditorMode.Sticker) }

    SideEffect {
        AutomationBridge.rootHandler = { command ->
            when (command.name) {
                "open_editor",
                "navigate_editor",
                "show_editor",
                "open_sticker_editor" -> {
                    editorMode = EditorMode.Sticker
                    screen = AppScreen.Editor
                    automationOk(command.name) {
                        put("active_screen", "screen.editor")
                        put("editor_mode", "sticker")
                    }
                }
                "open_remove_bg_editor",
                "open_remove_bg" -> {
                    editorMode = EditorMode.RemoveBg
                    screen = AppScreen.Editor
                    automationOk(command.name) {
                        put("active_screen", "screen.editor")
                        put("editor_mode", "remove_bg")
                    }
                }
                "navigate_home",
                "show_home" -> {
                    screen = AppScreen.Home
                    automationOk(command.name) { put("active_screen", "screen.home") }
                }
                else -> JSONObject().put("handled", false)
            }
        }
        AutomationBridge.rootStateProvider = {
            JSONObject()
                .put("schema_version", "altanova.automation_runtime_state.v0.1")
                .put("active_screen", if (screen == AppScreen.Home) "screen.home" else "screen.editor")
                .put("editor_mode", if (editorMode == EditorMode.Sticker) "sticker" else "remove_bg")
                .put("package", "com.gassticker")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            AutomationBridge.rootHandler = null
            AutomationBridge.rootStateProvider = null
        }
    }

    when (screen) {
        AppScreen.Home -> HomeScreen(
            onOpenSticker = {
                editorMode = EditorMode.Sticker
                screen = AppScreen.Editor
            },
            onOpenRemoveBg = {
                editorMode = EditorMode.RemoveBg
                screen = AppScreen.Editor
            },
        )
        AppScreen.Editor -> GasStickerScreen(
            editorMode = editorMode,
            onBackHome = { screen = AppScreen.Home },
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GasStickerScreen(
    editorMode: EditorMode,
    onBackHome: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var sourceBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var stickerBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var selectedMask by remember { mutableStateOf<SegmentMask?>(null) }
    var selectionBoundary by remember { mutableStateOf<IntArray?>(null) }
    var selectedPoint by remember { mutableStateOf<Offset?>(null) }
    var activeTab by remember { mutableStateOf(EditorTab.Select) }
    var activeBrushMode by remember { mutableStateOf(BrushMode.Add) }
    var brushSize by remember { mutableStateOf(0.045f) }
    var lastBrushPoint by remember { mutableStateOf<Offset?>(null) }
    var undoStack by remember { mutableStateOf<List<SegmentMask>>(emptyList()) }
    var redoStack by remember { mutableStateOf<List<SegmentMask>>(emptyList()) }
    var label by remember { mutableStateOf("") }
    var textOffset by remember { mutableStateOf(Offset.Zero) }
    var outlineColor by remember { mutableStateOf(Color.WHITE) }
    var outlineWidth by remember { mutableStateOf(12f) }
    var fontColor by remember { mutableStateOf(Color.WHITE) }
    var textStyleColor by remember { mutableStateOf(Color.rgb(28, 28, 28)) }
    var selectedFont by remember { mutableStateOf(FontChoices.first()) }
    var selectedTextTreatment by remember { mutableStateOf(StickerTextTreatment.Stroke) }
    val fontTypefaces = remember(context) {
        FontChoices.associateWith { font ->
            runCatching { Typeface.createFromAsset(context.assets, font.assetPath) }
                .getOrElse { Typeface.DEFAULT_BOLD }
        }
    }
    val stickerStyle = remember(label, textOffset, outlineColor, outlineWidth, fontColor, textStyleColor, selectedFont, selectedTextTreatment, fontTypefaces) {
        StickerStyle(
            label = label,
            outlineColor = outlineColor,
            outlineRadiusPx = outlineWidth.toInt(),
            fontColor = fontColor,
            textStyleColor = textStyleColor,
            labelOffsetX = textOffset.x,
            labelOffsetY = textOffset.y,
            typeface = fontTypefaces[selectedFont] ?: Typeface.DEFAULT_BOLD,
            textTreatment = selectedTextTreatment,
        )
    }
    var isProcessing by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf(localizedText(context, "Pilih foto, lalu tap objek yang mau jadi sticker.", "Choose a photo, then tap the object to turn into a sticker.")) }
    var lastSavedUri by remember { mutableStateOf<String?>(null) }
    var lastAutomationError by remember { mutableStateOf<String?>(null) }
    val isStickerEditor = editorMode == EditorMode.Sticker

    LaunchedEffect(editorMode) {
        if (!isStickerEditor && (activeTab == EditorTab.Text || activeTab == EditorTab.Style)) {
            activeTab = EditorTab.Select
        }
        if (!isStickerEditor && status.contains("sticker", ignoreCase = true)) {
            status = localizedText(context, "Pilih foto, lalu tap objek yang mau disimpan.", "Choose a photo, then tap the object to save.")
        }
    }

    fun tabFromName(value: String): EditorTab? {
        val slug = stableSlug(value)
        return when (slug) {
            "add",
            "cut",
            "brush",
            "rapikan" -> EditorTab.Refine
            else -> EditorTab.entries.firstOrNull {
                it.name.equals(value, ignoreCase = true) || stableSlug(it.name) == slug
            }
        }
    }

    fun statusForTab(tab: EditorTab): String =
        when (tab) {
            EditorTab.Select -> if (isStickerEditor) localizedText(context, "Tap objek untuk magic remove BG.", "Tap an object for Magic Remove BG.") else localizedText(context, "Tap objek yang mau disimpan.", "Tap the object you want to save.")
            EditorTab.Refine -> localizedText(context, "Rapikan seleksi dengan tambah atau kurangi.", "Refine the selection with add or subtract.")
            EditorTab.Text -> localizedText(context, "Atur teks sticker.", "Set the sticker text.")
            EditorTab.Style -> localizedText(context, "Atur font dan border sticker.", "Set the font and sticker border.")
            EditorTab.Export -> localizedText(context, "Preview dan simpan PNG.", "Preview and save a PNG.")
        }

    fun composeOutput(bitmap: Bitmap, mask: SegmentMask): Bitmap =
        if (isStickerEditor) {
            StickerComposer.createSticker(bitmap, mask, stickerStyle)
        } else {
            StickerComposer.createCutout(bitmap, mask)
        }

    fun saveBitmapAsync(bitmap: Bitmap, successStatus: String) {
        scope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    BitmapUtils.savePngToPictures(context, bitmap)
                }
            }.onSuccess {
                lastSavedUri = it.toString()
                lastAutomationError = null
                status = successStatus
                Toast.makeText(context, localizedText(context, "Disimpan ke Pictures/Picvanta", "Saved to Pictures/Picvanta"), Toast.LENGTH_SHORT).show()
            }.onFailure {
                lastAutomationError = it.message ?: "Gagal menyimpan PNG."
                status = lastAutomationError ?: "Gagal menyimpan PNG."
                Toast.makeText(context, it.message ?: "Gagal simpan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveOriginalAsync(): Boolean {
        val bitmap = sourceBitmap ?: return false
        val mask = selectedMask ?: return false
        scope.launch {
            runCatching {
                withContext(Dispatchers.Default) {
                    StickerComposer.createCutout(bitmap, mask)
                }
            }.onSuccess {
                saveBitmapAsync(it, localizedText(context, "PNG ukuran asli disimpan.", "Original-size PNG saved."))
            }.onFailure {
                lastAutomationError = it.message ?: "Gagal membuat ukuran asli."
                status = lastAutomationError ?: "Gagal membuat ukuran asli."
            }
        }
        return true
    }

    fun setTextOffset(next: Offset) {
        textOffset = Offset(
            x = next.x.coerceIn(-150f, 150f),
            y = next.y.coerceIn(-170f, 70f),
        )
    }

    fun findFont(value: String): FontChoice? {
        val slug = stableSlug(value)
        return FontChoices.firstOrNull {
            it.name.equals(value, ignoreCase = true) || stableSlug(it.name) == slug
        }
    }

    fun findTextTreatment(value: String): StickerTextTreatment? {
        val slug = stableSlug(value)
        return TextTreatmentChoices.firstOrNull {
            it.name.equals(value, ignoreCase = true) || stableSlug(it.name) == slug
        }?.treatment
    }

    fun resetImageState(bitmap: Bitmap, sourceName: String) {
        sourceBitmap = bitmap
        stickerBitmap = null
        selectedMask = null
        selectionBoundary = null
        selectedPoint = null
        textOffset = Offset.Zero
        activeTab = EditorTab.Select
        activeBrushMode = BrushMode.Add
        lastBrushPoint = null
        undoStack = emptyList()
        redoStack = emptyList()
        lastAutomationError = null
        status = if (isStickerEditor) {
            localizedText(context, "Gambar $sourceName siap. Tap objek untuk magic remove BG.", "Your $sourceName image is ready. Tap an object for Magic Remove BG.")
        } else {
            localizedText(context, "Gambar $sourceName siap. Tap objek yang mau disimpan.", "Your $sourceName image is ready. Tap the object you want to save.")
        }
    }

    fun updateStickerFromMask(bitmap: Bitmap, mask: SegmentMask, nextStatus: String) {
        scope.launch {
            stickerBitmap = withContext(Dispatchers.Default) {
                composeOutput(bitmap, mask)
            }
            status = nextStatus
        }
    }

    fun parseAutomationPoints(args: JSONObject): List<Offset> {
        if (args.has("points_json")) {
            val array = JSONArray(args.optString("points_json"))
            return (0 until array.length()).mapNotNull { index ->
                val point = array.optJSONObject(index) ?: return@mapNotNull null
                Offset(
                    point.optDouble("x", 0.5).toFloat().coerceIn(0f, 1f),
                    point.optDouble("y", 0.5).toFloat().coerceIn(0f, 1f),
                )
            }
        }

        val points = args.optString("points", "")
        if (points.isNotBlank()) {
            return points.split(';')
                .mapNotNull { raw ->
                    val parts = raw.split(',')
                    if (parts.size != 2) return@mapNotNull null
                    Offset(
                        parts[0].trim().toFloatOrNull()?.coerceIn(0f, 1f) ?: return@mapNotNull null,
                        parts[1].trim().toFloatOrNull()?.coerceIn(0f, 1f) ?: return@mapNotNull null,
                    )
                }
        }

        if (args.has("from_x") && args.has("from_y") && args.has("to_x") && args.has("to_y")) {
            return listOf(
                Offset(args.optDouble("from_x").toFloat().coerceIn(0f, 1f), args.optDouble("from_y").toFloat().coerceIn(0f, 1f)),
                Offset(args.optDouble("to_x").toFloat().coerceIn(0f, 1f), args.optDouble("to_y").toFloat().coerceIn(0f, 1f)),
            )
        }

        if (args.has("x") && args.has("y")) {
            return listOf(
                Offset(args.optDouble("x").toFloat().coerceIn(0f, 1f), args.optDouble("y").toFloat().coerceIn(0f, 1f)),
            )
        }

        return emptyList()
    }

    fun performMagicSelect(norm: Offset) {
        val bitmap = sourceBitmap ?: return
        selectedPoint = norm
        isProcessing = true
        lastAutomationError = null
        status = "Lagi motong objek..."
        scope.launch {
            runCatching {
                withContext(Dispatchers.Default) {
                    InteractiveSegmenterClient(context).use { client ->
                        val mask = client.segment(bitmap, norm.x, norm.y)
                        StickerResult(
                            mask = mask,
                            boundary = SelectionBoundary.from(mask),
                            sticker = composeOutput(bitmap, mask),
                        )
                    }
                }
            }.onSuccess {
                selectedMask = it.mask
                selectionBoundary = it.boundary
                stickerBitmap = it.sticker
                undoStack = emptyList()
                redoStack = emptyList()
                activeTab = EditorTab.Select
                status = if (isStickerEditor) {
                    "Sticker siap. Kalau potongannya kurang pas, tap titik lain di objek."
                } else {
                    "Cutout siap. Kalau potongannya kurang pas, tap titik lain di objek."
                }
            }.onFailure {
                lastAutomationError = it.message ?: "Gagal memproses sticker."
                status = lastAutomationError ?: "Gagal memproses sticker."
            }
            isProcessing = false
        }
    }

    fun performBrushStroke(mode: BrushMode, points: List<Offset>, radius: Float): JSONObject {
        val bitmap = sourceBitmap ?: return automationError("brush_stroke", "image_required")
        val mask = selectedMask ?: return automationError("brush_stroke", "selection_required")
        if (mode != BrushMode.Add && mode != BrushMode.Subtract) return automationError("brush_stroke", "mode_required")
        if (points.isEmpty()) return automationError("brush_stroke", "points_required")

        undoStack = undoStack + SelectionEditor.copy(mask)
        redoStack = emptyList()

        var edited = SelectionEditor.applyBrush(mask, points.first().x, points.first().y, mode, radius)
        points.zipWithNext().forEach { (from, to) ->
            edited = SelectionEditor.applyBrushLine(
                mask = edited,
                fromX = from.x,
                fromY = from.y,
                toX = to.x,
                toY = to.y,
                mode = mode,
                radiusFraction = radius,
            )
        }

        selectedMask = edited
        selectionBoundary = SelectionBoundary.from(edited)
        activeBrushMode = mode
        activeTab = EditorTab.Refine
        updateStickerFromMask(bitmap, edited, "Seleksi brush diterapkan.")
        return automationOk("brush_stroke") {
            put("mode", mode.toAutomationValue())
            put("points", points.size)
            put("brush_size", radius.toDouble())
        }
    }

    fun performUndo(): Boolean {
        val current = selectedMask
        val previous = undoStack.lastOrNull()
        val bitmap = sourceBitmap
        if (current != null && previous != null && bitmap != null && !isProcessing) {
            undoStack = undoStack.dropLast(1)
            redoStack = redoStack + SelectionEditor.copy(current)
            selectedMask = SelectionEditor.copy(previous)
            selectionBoundary = SelectionBoundary.from(previous)
            updateStickerFromMask(bitmap, previous, "Undo seleksi.")
            return true
        }
        return false
    }

    fun performRedo(): Boolean {
        val current = selectedMask
        val next = redoStack.lastOrNull()
        val bitmap = sourceBitmap
        if (current != null && next != null && bitmap != null && !isProcessing) {
            redoStack = redoStack.dropLast(1)
            undoStack = undoStack + SelectionEditor.copy(current)
            selectedMask = SelectionEditor.copy(next)
            selectionBoundary = SelectionBoundary.from(next)
            updateStickerFromMask(bitmap, next, "Redo seleksi.")
            return true
        }
        return false
    }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val uri = result.data?.data
        if (uri != null) {
            scope.launch {
                runCatching {
                    withContext(Dispatchers.IO) { BitmapUtils.loadBitmap(context, uri) }
                }.onSuccess {
                    resetImageState(it, "gallery")
                }.onFailure {
                    lastAutomationError = it.message ?: "Gagal membuka gambar."
                    status = lastAutomationError ?: "Gagal membuka gambar."
                }
            }
        }
    }

    val openImagePicker = {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
        }
        val fallbackIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        val packageManager = context.packageManager
        val intent = when {
            galleryIntent.resolveActivity(packageManager) != null -> galleryIntent
            fallbackIntent.resolveActivity(packageManager) != null -> fallbackIntent
            else -> null
        }

        if (intent == null) {
            Toast.makeText(context, "Tidak ada aplikasi Gallery yang bisa dibuka.", Toast.LENGTH_SHORT).show()
        } else {
            picker.launch(intent)
        }
    }

    SideEffect {
        AutomationBridge.editorHandler = editorHandler@ { command ->
            val args = command.args
            when (command.name) {
                "open_gallery",
                "pick_image" -> {
                    openImagePicker()
                    automationOk(command.name) { put("accepted", true) }
                }
                "load_sample_image" -> {
                    resetImageState(BitmapUtils.createSampleBitmap(), "sample")
                    automationOk(command.name) { put("has_image", true) }
                }
                "load_image_path" -> {
                    val path = args.optString("path", "")
                    if (path.isBlank()) return@editorHandler automationError(command.name, "path_required")
                    scope.launch {
                        runCatching {
                            withContext(Dispatchers.IO) { BitmapUtils.loadBitmapFromPath(path) }
                        }.onSuccess {
                            resetImageState(it, "path")
                        }.onFailure {
                            lastAutomationError = it.message ?: "Gagal membuka gambar."
                            status = lastAutomationError ?: "Gagal membuka gambar."
                        }
                    }
                    automationOk(command.name) { put("accepted", true) }
                }
                "load_image_uri" -> {
                    val uriText = args.optString("uri", "")
                    if (uriText.isBlank()) return@editorHandler automationError(command.name, "uri_required")
                    scope.launch {
                        runCatching {
                            withContext(Dispatchers.IO) { BitmapUtils.loadBitmap(context, Uri.parse(uriText)) }
                        }.onSuccess {
                            resetImageState(it, "uri")
                        }.onFailure {
                            lastAutomationError = it.message ?: "Gagal membuka gambar."
                            status = lastAutomationError ?: "Gagal membuka gambar."
                        }
                    }
                    automationOk(command.name) { put("accepted", true) }
                }
                "set_tab" -> {
                    val requestedTab = args.optString("tab", "")
                    when (stableSlug(requestedTab)) {
                        "add" -> activeBrushMode = BrushMode.Add
                        "cut",
                        "subtract",
                        "sub" -> activeBrushMode = BrushMode.Subtract
                    }
                    val tab = tabFromName(requestedTab)
                        ?: return@editorHandler automationError(command.name, "unknown_tab")
                    activeTab = tab
                    lastBrushPoint = null
                    status = statusForTab(tab)
                    automationOk(command.name) { put("active_tab", tab.name.lowercase()) }
                }
                "set_text" -> {
                    if (!isStickerEditor) return@editorHandler automationError(command.name, "unsupported_in_remove_bg_editor")
                    label = args.optString("text", label).take(18)
                    activeTab = EditorTab.Text
                    status = "Teks sticker diubah via automation."
                    automationOk(command.name) { put("label", label) }
                }
                "move_text" -> {
                    if (!isStickerEditor) return@editorHandler automationError(command.name, "unsupported_in_remove_bg_editor")
                    val direction = args.optString("direction", "").lowercase()
                    val step = args.optDouble("step", 14.0).toFloat()
                    val dx = when (direction) {
                        "left" -> -step
                        "right" -> step
                        else -> args.optDouble("dx", 0.0).toFloat()
                    }
                    val dy = when (direction) {
                        "up" -> -step
                        "down" -> step
                        else -> args.optDouble("dy", 0.0).toFloat()
                    }
                    setTextOffset(Offset(textOffset.x + dx, textOffset.y + dy))
                    activeTab = EditorTab.Text
                    automationOk(command.name) {
                        put("text_offset_x", textOffset.x.toDouble())
                        put("text_offset_y", textOffset.y.toDouble())
                    }
                }
                "reset_text_position" -> {
                    if (!isStickerEditor) return@editorHandler automationError(command.name, "unsupported_in_remove_bg_editor")
                    textOffset = Offset.Zero
                    activeTab = EditorTab.Text
                    automationOk(command.name) {
                        put("text_offset_x", 0)
                        put("text_offset_y", 0)
                    }
                }
                "set_font" -> {
                    if (!isStickerEditor) return@editorHandler automationError(command.name, "unsupported_in_remove_bg_editor")
                    val font = findFont(args.optString("font", args.optString("name", "")))
                        ?: return@editorHandler automationError(command.name, "unknown_font")
                    selectedFont = font
                    activeTab = EditorTab.Style
                    automationOk(command.name) { put("font", font.name) }
                }
                "set_text_style" -> {
                    if (!isStickerEditor) return@editorHandler automationError(command.name, "unsupported_in_remove_bg_editor")
                    val treatment = findTextTreatment(args.optString("style", args.optString("text_style", "")))
                        ?: return@editorHandler automationError(command.name, "unknown_text_style")
                    selectedTextTreatment = treatment
                    activeTab = EditorTab.Style
                    automationOk(command.name) { put("text_style", treatment.name.lowercase()) }
                }
                "set_font_color" -> {
                    if (!isStickerEditor) return@editorHandler automationError(command.name, "unsupported_in_remove_bg_editor")
                    fontColor = args.optColor("color", fontColor)
                    activeTab = EditorTab.Style
                    automationOk(command.name) { put("font_color", fontColor.toHexColor()) }
                }
                "set_style_color",
                "set_text_style_color" -> {
                    if (!isStickerEditor) return@editorHandler automationError(command.name, "unsupported_in_remove_bg_editor")
                    textStyleColor = args.optColor("color", textStyleColor)
                    activeTab = EditorTab.Style
                    automationOk(command.name) { put("text_style_color", textStyleColor.toHexColor()) }
                }
                "set_border_color",
                "set_outline_color" -> {
                    if (!isStickerEditor) return@editorHandler automationError(command.name, "unsupported_in_remove_bg_editor")
                    outlineColor = args.optColor("color", outlineColor)
                    activeTab = EditorTab.Style
                    automationOk(command.name) { put("border_color", outlineColor.toHexColor()) }
                }
                "set_border_width",
                "set_outline_width" -> {
                    if (!isStickerEditor) return@editorHandler automationError(command.name, "unsupported_in_remove_bg_editor")
                    outlineWidth = args.optDouble("width", outlineWidth.toDouble()).toFloat().coerceIn(4f, 28f)
                    activeTab = EditorTab.Style
                    automationOk(command.name) { put("border_width", outlineWidth.toDouble()) }
                }
                "set_brush_size" -> {
                    brushSize = args.optDouble("size", brushSize.toDouble()).toFloat().coerceIn(0.015f, 0.12f)
                    activeTab = EditorTab.Refine
                    automationOk(command.name) { put("brush_size", brushSize.toDouble()) }
                }
                "magic_select" -> {
                    if (sourceBitmap == null) return@editorHandler automationError(command.name, "image_required")
                    val norm = Offset(
                        args.optDouble("x", 0.5).toFloat().coerceIn(0f, 1f),
                        args.optDouble("y", 0.5).toFloat().coerceIn(0f, 1f),
                    )
                    performMagicSelect(norm)
                    automationOk(command.name) {
                        put("accepted", true)
                        put("x", norm.x.toDouble())
                        put("y", norm.y.toDouble())
                    }
                }
                "select_full_image" -> {
                    val bitmap = sourceBitmap ?: return@editorHandler automationError(command.name, "image_required")
                    val mask = SegmentMask(
                        bytes = ByteArray(bitmap.width * bitmap.height) { 0 },
                        width = bitmap.width,
                        height = bitmap.height,
                    )
                    selectedMask = mask
                    selectionBoundary = SelectionBoundary.from(mask)
                    selectedPoint = Offset(0.5f, 0.5f)
                    undoStack = emptyList()
                    redoStack = emptyList()
                    activeTab = EditorTab.Select
                    updateStickerFromMask(bitmap, mask, "Full image dipilih via automation.")
                    automationOk(command.name) { put("has_selection", true) }
                }
                "clear_selection" -> {
                    selectedMask = null
                    selectionBoundary = null
                    selectedPoint = null
                    stickerBitmap = null
                    undoStack = emptyList()
                    redoStack = emptyList()
                    activeTab = EditorTab.Select
                    status = "Seleksi dihapus via automation."
                    automationOk(command.name) { put("has_selection", false) }
                }
                "brush_stroke" -> {
                    val requestedMode = args.optString("mode", args.optString("tab", activeTab.name)).lowercase()
                    val mode = when (requestedMode) {
                        "add" -> BrushMode.Add
                        "cut", "subtract", "sub" -> BrushMode.Subtract
                        else -> if (activeTab == EditorTab.Refine) activeBrushMode else BrushMode.None
                    }
                    val radius = args.optDouble("size", brushSize.toDouble()).toFloat().coerceIn(0.015f, 0.12f)
                    brushSize = radius
                    performBrushStroke(mode, parseAutomationPoints(args), radius)
                }
                "undo" -> {
                    if (performUndo()) {
                        automationOk(command.name) { put("can_undo", undoStack.isNotEmpty()) }
                    } else {
                        automationError(command.name, "nothing_to_undo")
                    }
                }
                "redo" -> {
                    if (performRedo()) {
                        automationOk(command.name) { put("can_redo", redoStack.isNotEmpty()) }
                    } else {
                        automationError(command.name, "nothing_to_redo")
                    }
                }
                "compose_sticker" -> {
                    val bitmap = sourceBitmap ?: return@editorHandler automationError(command.name, "image_required")
                    val mask = selectedMask ?: return@editorHandler automationError(command.name, "selection_required")
                    updateStickerFromMask(bitmap, mask, "Sticker dikomposisi ulang via automation.")
                    automationOk(command.name) { put("accepted", true) }
                }
                "save_png",
                "export_png" -> {
                    val sticker = stickerBitmap ?: return@editorHandler automationError(command.name, "sticker_required")
                    if (isStickerEditor) {
                        saveBitmapAsync(sticker, "PNG sticker disimpan via automation.")
                    } else {
                        if (!saveOriginalAsync()) {
                            return@editorHandler automationError(command.name, "selection_required")
                        }
                    }
                    activeTab = EditorTab.Export
                    automationOk(command.name) { put("accepted", true) }
                }
                "save_original_png",
                "export_original_png" -> {
                    if (!saveOriginalAsync()) {
                        return@editorHandler automationError(command.name, "selection_required")
                    }
                    activeTab = EditorTab.Export
                    automationOk(command.name) { put("accepted", true) }
                }
                "reset_style" -> {
                    label = ""
                    textOffset = Offset.Zero
                    outlineColor = Color.WHITE
                    outlineWidth = 12f
                    fontColor = Color.WHITE
                    textStyleColor = Color.rgb(28, 28, 28)
                    selectedFont = FontChoices.first()
                    selectedTextTreatment = StickerTextTreatment.Stroke
                    activeTab = EditorTab.Text
                    automationOk(command.name)
                }
                else -> JSONObject().put("handled", false)
            }
        }
        AutomationBridge.editorStateProvider = {
            JSONObject()
                .put("schema_version", "altanova.automation_runtime_state.v0.1")
                .put("active_screen", "screen.editor")
                .put("package", "com.gassticker")
                .put("editor_mode", if (isStickerEditor) "sticker" else "remove_bg")
                .put("has_image", sourceBitmap != null)
                .put("has_selection", selectedMask != null)
                .put("active_tab", activeTab.name.lowercase())
                .put("brush_mode", activeBrushMode.toAutomationValue())
                .put("status", status)
                .put("label", label)
                .put("font", selectedFont.name)
                .put("text_style", selectedTextTreatment.name.lowercase())
                .put("font_color", fontColor.toHexColor())
                .put("text_style_color", textStyleColor.toHexColor())
                .put("border_color", outlineColor.toHexColor())
                .put("border_width", outlineWidth.toDouble())
                .put("brush_size", brushSize.toDouble())
                .put("text_offset_x", textOffset.x.toDouble())
                .put("text_offset_y", textOffset.y.toDouble())
                .put("can_undo", undoStack.isNotEmpty())
                .put("can_redo", redoStack.isNotEmpty())
                .put("is_processing", isProcessing)
                .put("last_saved_uri", lastSavedUri ?: JSONObject.NULL)
                .put("last_error", lastAutomationError ?: JSONObject.NULL)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            AutomationBridge.editorHandler = null
            AutomationBridge.editorStateProvider = null
        }
    }

    LaunchedEffect(sourceBitmap, selectedMask, stickerStyle, editorMode) {
        val bitmap = sourceBitmap
        val mask = selectedMask
        if (bitmap != null && mask != null) {
            stickerBitmap = withContext(Dispatchers.Default) {
                composeOutput(bitmap, mask)
            }
        }
    }

    Scaffold(containerColor = androidx.compose.ui.graphics.Color.Transparent) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .stableId("screen.editor", "screen")
                .padding(innerPadding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Header(editorMode = editorMode, onBackHome = onBackHome)

            SourcePicker(
                modifier = Modifier.weight(1f),
                bitmap = sourceBitmap,
                selectedMask = selectedMask,
                selectionBoundary = selectionBoundary,
                selectedPoint = selectedPoint,
                sticker = stickerBitmap,
                activeTab = activeTab,
                status = status,
                isProcessing = isProcessing,
                onPick = openImagePicker,
                onBrushStart = { norm ->
                    val mask = selectedMask ?: return@SourcePicker
                    lastBrushPoint = norm
                    undoStack = undoStack + SelectionEditor.copy(mask)
                    redoStack = emptyList()
                    val edited = SelectionEditor.applyBrush(mask, norm.x, norm.y, activeBrushMode, brushSize)
                    selectedMask = edited
                    selectionBoundary = SelectionBoundary.from(edited)
                    status = activeBrushMode.toBrushStatus()
                },
                onBrushMove = { norm ->
                    val mask = selectedMask ?: return@SourcePicker
                    val previous = lastBrushPoint ?: norm
                    val edited = SelectionEditor.applyBrushLine(
                        mask = mask,
                        fromX = previous.x,
                        fromY = previous.y,
                        toX = norm.x,
                        toY = norm.y,
                        mode = activeBrushMode,
                        radiusFraction = brushSize,
                    )
                    lastBrushPoint = norm
                    selectedMask = edited
                    selectionBoundary = SelectionBoundary.from(edited)
                },
                onBrushEnd = {
                    lastBrushPoint = null
                    val bitmap = sourceBitmap
                    val mask = selectedMask
                    if (bitmap != null && mask != null) {
                        scope.launch {
                            stickerBitmap = withContext(Dispatchers.Default) {
                                composeOutput(bitmap, mask)
                            }
                            status = "Seleksi brush diterapkan."
                        }
                    }
                },
                onTapObject = { norm ->
                    performMagicSelect(norm)
                },
            )

            BottomEditorPanel(
                activeTab = activeTab,
                allowTextStyleControls = isStickerEditor,
                label = label,
                textOffset = textOffset,
                outlineColor = outlineColor,
                outlineWidth = outlineWidth,
                fontColor = fontColor,
                textStyleColor = textStyleColor,
                selectedFont = selectedFont,
                selectedTextTreatment = selectedTextTreatment,
                activeBrushMode = activeBrushMode,
                brushSize = brushSize,
                sticker = stickerBitmap,
                hasImage = sourceBitmap != null,
                hasSelection = selectedMask != null,
                canUndo = undoStack.isNotEmpty(),
                canRedo = redoStack.isNotEmpty(),
                onTabChange = {
                    if (sourceBitmap == null && it != EditorTab.Select) {
                        status = "Masukin gambar dulu."
                        return@BottomEditorPanel
                    }
                    if (!isStickerEditor && (it == EditorTab.Text || it == EditorTab.Style)) {
                        status = "Remove BG hanya pakai select, brush, dan export."
                        return@BottomEditorPanel
                    }
                    activeTab = it
                    lastBrushPoint = null
                    status = when (it) {
                        EditorTab.Select -> statusForTab(EditorTab.Select)
                        EditorTab.Refine -> statusForTab(EditorTab.Refine)
                        EditorTab.Text -> statusForTab(EditorTab.Text)
                        EditorTab.Style -> statusForTab(EditorTab.Style)
                        EditorTab.Export -> statusForTab(EditorTab.Export)
                    }
                },
                onLabelChange = { label = it.take(18) },
                onTextMove = { delta ->
                    textOffset = Offset(
                        x = (textOffset.x + delta.x).coerceIn(-150f, 150f),
                        y = (textOffset.y + delta.y).coerceIn(-170f, 70f),
                    )
                },
                onFontChange = { selectedFont = it },
                onTextTreatmentChange = { selectedTextTreatment = it },
                onFontColorChange = { fontColor = it },
                onTextStyleColorChange = { textStyleColor = it },
                onOutlineColorChange = { outlineColor = it },
                onOutlineWidthChange = { outlineWidth = it },
                onBrushModeChange = { activeBrushMode = it },
                onBrushSizeChange = { brushSize = it },
                onUndo = { performUndo() },
                onRedo = { performRedo() },
                onSave = {
                    val sticker = stickerBitmap ?: return@BottomEditorPanel
                    if (isStickerEditor) {
                        saveBitmapAsync(sticker, localizedText(context, "PNG sticker disimpan.", "Sticker PNG saved."))
                    } else {
                        saveOriginalAsync()
                    }
                },
                onSaveOriginal = { saveOriginalAsync() },
            )

            Text(
                text = uiText("Gunakan foto milik sendiri atau yang sudah punya izin. Untuk wajah orang, minta izin dulu sebelum diedit atau dibagikan.", "Use photos you own or are allowed to use. For someone else's face, ask before editing or sharing."),
                color = ProDanger,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HomeScreen(
    onOpenSticker: () -> Unit,
    onOpenRemoveBg: () -> Unit,
) {
    Scaffold(containerColor = androidx.compose.ui.graphics.Color.Transparent) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .stableId("screen.home", "screen")
                .padding(innerPadding)
                .padding(horizontal = 18.dp, vertical = 12.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Picvanta",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.sp,
                )
                Text(
                    text = uiText("Edit foto cepat, privat, langsung di HP.", "Fast, private photo editing, right on your phone."),
                    modifier = Modifier.padding(top = 6.dp),
                    color = ProMuted,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f),
                verticalArrangement = Arrangement.Top,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    HomeMenuCard(
                        title = "Sticker Editor",
                        subtitle = uiText("Cutout, teks, style.", "Cutout, text, style."),
                        stableId = "menu.sticker_editor",
                        icon = Icons.Filled.StickyNote2,
                        onClick = onOpenSticker,
                        modifier = Modifier.weight(1f),
                    )
                    HomeMenuCard(
                        title = "Remove BG",
                        subtitle = uiText("PNG transparan.", "Transparent PNG."),
                        stableId = "menu.remove_bg_editor",
                        icon = Icons.Filled.AutoFixHigh,
                        onClick = onOpenRemoveBg,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeMenuCard(
    title: String,
    subtitle: String,
    stableId: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = spring(stiffness = 520f, dampingRatio = 0.82f),
        label = "home-card-scale",
    )
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 2.dp else 10.dp,
        animationSpec = spring(stiffness = 420f, dampingRatio = 0.9f),
        label = "home-card-elevation",
    )

    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .stableId(stableId, "button")
            .border(1.dp, ProBorder, RoundedCornerShape(18.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(18.dp),
        shadowElevation = elevation,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                modifier = Modifier.size(82.dp),
                color = ProBlueSoft,
                shape = RoundedCornerShape(24.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = ProBlue,
                        modifier = Modifier.size(48.dp),
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    title,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
                Text(
                    subtitle,
                    color = ProMuted,
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 16.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun Header(
    editorMode: EditorMode,
    onBackHome: () -> Unit,
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = if (editorMode == EditorMode.Sticker) "Sticker Editor" else "Remove BG",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.sp,
            )
            Text(
                text = if (editorMode == EditorMode.Sticker) {
                    localizedText(context, "Cutout, teks, style, export PNG.", "Cutout, text, style, PNG export.")
                } else {
                    localizedText(context, "Potong background dan simpan PNG transparan.", "Remove the background and save a transparent PNG.")
                },
                color = ProMuted,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        OutlinedButton(
            onClick = onBackHome,
            modifier = Modifier.stableId("command.home", "button"),
        ) {
            Text(uiText("Beranda", "Home"))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BottomEditorPanel(
    activeTab: EditorTab,
    allowTextStyleControls: Boolean,
    label: String,
    textOffset: Offset,
    outlineColor: Int,
    outlineWidth: Float,
    fontColor: Int,
    textStyleColor: Int,
    selectedFont: FontChoice,
    selectedTextTreatment: StickerTextTreatment,
    activeBrushMode: BrushMode,
    brushSize: Float,
    sticker: Bitmap?,
    hasImage: Boolean,
    hasSelection: Boolean,
    canUndo: Boolean,
    canRedo: Boolean,
    onTabChange: (EditorTab) -> Unit,
    onLabelChange: (String) -> Unit,
    onTextMove: (Offset) -> Unit,
    onFontChange: (FontChoice) -> Unit,
    onTextTreatmentChange: (StickerTextTreatment) -> Unit,
    onFontColorChange: (Int) -> Unit,
    onTextStyleColorChange: (Int) -> Unit,
    onOutlineColorChange: (Int) -> Unit,
    onOutlineWidthChange: (Float) -> Unit,
    onBrushModeChange: (BrushMode) -> Unit,
    onBrushSizeChange: (Float) -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onSave: () -> Unit,
    onSaveOriginal: () -> Unit,
) {
    var colorPickerTarget by remember { mutableStateOf<ColorPickerTarget?>(null) }
    var customFontColor by remember { mutableStateOf(Color.rgb(175, 82, 222)) }
    var customTextStyleColor by remember { mutableStateOf(Color.rgb(175, 82, 222)) }
    var customOutlineColor by remember { mutableStateOf(Color.rgb(175, 82, 222)) }
    var pickerColor by remember { mutableStateOf(customOutlineColor) }

    fun openColorPicker(target: ColorPickerTarget, currentColor: Int) {
        pickerColor = currentColor
        colorPickerTarget = target
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .stableId("panel.editor_tools", "panel"),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .border(1.dp, ProBorder, RoundedCornerShape(18.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                EditorTabButton(EditorTab.Select, activeTab, Icons.Filled.TouchApp, "Select", true, onTabChange)
                EditorTabButton(EditorTab.Refine, activeTab, Icons.Filled.AutoFixHigh, uiText("Rapikan", "Refine"), hasImage, onTabChange)
                if (allowTextStyleControls) {
                    EditorTabButton(EditorTab.Text, activeTab, Icons.Filled.TextFields, "Text", hasImage, onTabChange)
                    EditorTabButton(EditorTab.Style, activeTab, Icons.Filled.Palette, "Style", hasImage, onTabChange)
                }
                EditorTabButton(EditorTab.Export, activeTab, Icons.Filled.Download, "Export", hasImage, onTabChange)
            }

            when (activeTab) {
                EditorTab.Select -> UndoRedoRow(
                    canUndo = hasSelection && canUndo,
                    canRedo = hasSelection && canRedo,
                    onUndo = onUndo,
                    onRedo = onRedo,
                )

                EditorTab.Refine -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                    ) {
                        CompactChoiceChip(
                            text = uiText("Tambah", "Add"),
                            selected = activeBrushMode == BrushMode.Add,
                            onClick = { onBrushModeChange(BrushMode.Add) },
                            stableId = "brush_mode.add",
                        )
                        CompactChoiceChip(
                            text = uiText("Kurangi", "Subtract"),
                            selected = activeBrushMode == BrushMode.Subtract,
                            onClick = { onBrushModeChange(BrushMode.Subtract) },
                            stableId = "brush_mode.cut",
                        )
                    }
                    BrushSizeRow(
                        brushSize = brushSize,
                        onBrushSizeChange = onBrushSizeChange,
                        enabled = hasSelection,
                    )
                    UndoRedoRow(
                        canUndo = hasSelection && canUndo,
                        canRedo = hasSelection && canRedo,
                        onUndo = onUndo,
                        onRedo = onRedo,
                    )
                }

                EditorTab.Text -> Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        value = label,
                        onValueChange = onLabelChange,
                        modifier = Modifier
                            .weight(1f)
                            .stableId("input.sticker_text", "text_field"),
                        label = { Text(uiText("Tuliskan teks", "Write text")) },
                        placeholder = { Text(uiText("Tuliskan teks", "Write text")) },
                        singleLine = true,
                    )
                    TextPositionJoystick(
                        textOffset = textOffset,
                        onMove = onTextMove,
                    )
                }

                EditorTab.Style -> Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 260.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(7.dp),
                    ) {
                        Text(uiText("Gaya Teks", "Text Style"), fontWeight = FontWeight.Black)
                        InlineStyleControl(label = "Font") {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                            ) {
                                FontChoices.forEach { font ->
                                    CompactChoiceChip(
                                        text = font.name,
                                        selected = selectedFont == font,
                                        onClick = { onFontChange(font) },
                                        stableId = "font.${stableSlug(font.name)}",
                                    )
                                }
                            }
                        }
                        InlineStyleControl(label = "Color") {
                            ColorChoiceRow(
                                stablePrefix = "font_color",
                                selectedColor = fontColor,
                                customColor = customFontColor,
                                onCustomClick = { openColorPicker(ColorPickerTarget.Font, customFontColor) },
                                onColorChange = onFontColorChange,
                            )
                        }
                        InlineStyleControl(label = "Style") {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                            ) {
                                TextTreatmentChoices.forEach { choice ->
                                    CompactChoiceChip(
                                        text = choice.name,
                                        selected = selectedTextTreatment == choice.treatment,
                                        onClick = { onTextTreatmentChange(choice.treatment) },
                                        stableId = "text_style.${stableSlug(choice.name)}",
                                    )
                                }
                            }
                        }
                        InlineStyleControl(label = "Color") {
                            ColorChoiceRow(
                                stablePrefix = "text_style_color",
                                selectedColor = textStyleColor,
                                customColor = customTextStyleColor,
                                onCustomClick = { openColorPicker(ColorPickerTarget.Style, customTextStyleColor) },
                                onColorChange = onTextStyleColorChange,
                            )
                        }
                        Text(uiText("Border Sticker", "Sticker Border"), fontWeight = FontWeight.Black)
                        InlineStyleControl(label = "Width") {
                            OutlineWidthRow(
                                outlineWidth = outlineWidth,
                                onOutlineWidthChange = onOutlineWidthChange,
                            )
                        }
                        InlineStyleControl(label = "Color") {
                            ColorChoiceRow(
                                stablePrefix = "border",
                                selectedColor = outlineColor,
                                customColor = customOutlineColor,
                                onCustomClick = { openColorPicker(ColorPickerTarget.Outline, customOutlineColor) },
                                onColorChange = onOutlineColorChange,
                            )
                        }
                    }

                    if (colorPickerTarget != null) {
                        ColorPickerOverlay(
                            color = pickerColor,
                            onColorChange = { pickerColor = it },
                            onCancel = { colorPickerTarget = null },
                            onApply = {
                                when (colorPickerTarget) {
                                    ColorPickerTarget.Font -> {
                                        customFontColor = pickerColor
                                        onFontColorChange(pickerColor)
                                    }
                                    ColorPickerTarget.Style -> {
                                        customTextStyleColor = pickerColor
                                        onTextStyleColorChange(pickerColor)
                                    }
                                    ColorPickerTarget.Outline -> {
                                        customOutlineColor = pickerColor
                                        onOutlineColorChange(pickerColor)
                                    }
                                    null -> Unit
                                }
                                colorPickerTarget = null
                            },
                        )
                    }
                }

                EditorTab.Export -> PreviewAndActions(
                    sticker = sticker,
                    isStickerEditor = allowTextStyleControls,
                    onSave = onSave,
                    onSaveOriginal = onSaveOriginal,
                )
            }
        }
    }
}

@Composable
private fun InlineStyleControl(
    label: String,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label,
            modifier = Modifier.width(56.dp),
            color = ProMuted,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
        )
        Box(modifier = Modifier.weight(1f)) {
            content()
        }
    }
}

@Composable
private fun CompactChoiceChip(
    text: String,
    selected: Boolean,
    stableId: String,
    onClick: () -> Unit,
) {
    val background by animateColorAsState(
        targetValue = if (selected) ProBlueSoft else ProSurface,
        animationSpec = tween(durationMillis = 160),
        label = "compact-chip-bg",
    )
    val border by animateColorAsState(
        targetValue = if (selected) ProBlue else ProBorder,
        animationSpec = tween(durationMillis = 160),
        label = "compact-chip-border",
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) ProBlue else ProInk,
        animationSpec = tween(durationMillis = 160),
        label = "compact-chip-text",
    )

    Surface(
        modifier = Modifier
            .height(34.dp)
            .stableId(stableId, "chip")
            .border(1.dp, border, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        color = background,
        shape = RoundedCornerShape(12.dp),
    ) {
        Box(
            modifier = Modifier
                .height(34.dp)
                .padding(horizontal = 11.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                color = contentColor,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun EditorTabButton(
    tab: EditorTab,
    activeTab: EditorTab,
    icon: ImageVector,
    label: String,
    enabled: Boolean,
    onClick: (EditorTab) -> Unit,
) {
    val selected = tab == activeTab
    val background by animateColorAsState(
        targetValue = if (selected) ProInk else ProSurfaceAlt,
        animationSpec = tween(durationMillis = 180),
        label = "tab-background",
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) androidx.compose.ui.graphics.Color.White else ProMuted,
        animationSpec = tween(durationMillis = 180),
        label = "tab-content",
    )
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.03f else 1f,
        animationSpec = spring(stiffness = 520f, dampingRatio = 0.86f),
        label = "tab-scale",
    )
    val elevation by animateDpAsState(
        targetValue = if (selected) 4.dp else 0.dp,
        animationSpec = tween(durationMillis = 180),
        label = "tab-elevation",
    )

    Surface(
        modifier = Modifier
            .height(54.dp)
            .alpha(if (enabled) 1f else 0.36f)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .stableId("tab.${tab.name.lowercase()}", "tab")
            .border(1.dp, if (selected) ProInk else ProBorder, RoundedCornerShape(15.dp))
            .clickable(enabled = enabled) { onClick(tab) },
        color = background,
        shape = RoundedCornerShape(15.dp),
        shadowElevation = elevation,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(20.dp),
            )
            Text(
                label,
                color = contentColor,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun TextPositionJoystick(
    textOffset: Offset,
    onMove: (Offset) -> Unit,
) {
    val step = 14f
    Column(
        modifier = Modifier
            .width(92.dp)
            .stableId("control.text_position", "joystick"),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            JoystickSpacer()
            JoystickButton(
                icon = Icons.Filled.KeyboardArrowUp,
                stableId = "command.text_up",
                onClick = { onMove(Offset(0f, -step)) },
            )
            JoystickSpacer()
        }
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            JoystickButton(
                icon = Icons.Filled.KeyboardArrowLeft,
                stableId = "command.text_left",
                onClick = { onMove(Offset(-step, 0f)) },
            )
            JoystickCenterButton(
                enabled = textOffset != Offset.Zero,
                onClick = { onMove(Offset(-textOffset.x, -textOffset.y)) },
            )
            JoystickButton(
                icon = Icons.Filled.KeyboardArrowRight,
                stableId = "command.text_right",
                onClick = { onMove(Offset(step, 0f)) },
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            JoystickSpacer()
            JoystickButton(
                icon = Icons.Filled.KeyboardArrowDown,
                stableId = "command.text_down",
                onClick = { onMove(Offset(0f, step)) },
            )
            JoystickSpacer()
        }
    }
}

@Composable
private fun JoystickSpacer() {
    Spacer(modifier = Modifier.size(28.dp))
}

@Composable
private fun JoystickButton(
    icon: ImageVector,
    stableId: String,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .size(28.dp)
            .stableId(stableId, "button")
            .border(1.dp, ProBorder, RoundedCornerShape(9.dp))
            .clickable(onClick = onClick),
        color = ProSurfaceAlt,
        shape = RoundedCornerShape(9.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ProInk,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun JoystickCenterButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .size(28.dp)
            .stableId("command.text_reset_position", "button")
            .border(1.dp, ProBorder, RoundedCornerShape(9.dp))
            .clickable(enabled = enabled, onClick = onClick),
        color = if (enabled) ProBlueSoft else ProSurfaceAlt,
        shape = RoundedCornerShape(9.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(if (enabled) ProBlue else ProSubtle, CircleShape),
            )
        }
    }
}

@Composable
private fun BrushSizeRow(
    brushSize: Float,
    onBrushSizeChange: (Float) -> Unit,
    enabled: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(uiText("Ukuran", "Size"), style = MaterialTheme.typography.bodySmall, color = ProMuted)
        Slider(
            value = brushSize,
            onValueChange = onBrushSizeChange,
            valueRange = 0.015f..0.12f,
            modifier = Modifier
                .weight(1f)
                .stableId("control.brush_size", "slider"),
            enabled = enabled,
        )
        Text("${(brushSize * 100).toInt()}%", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun OutlineWidthRow(
    outlineWidth: Float,
    onOutlineWidthChange: (Float) -> Unit,
) {
    var trackWidth by remember { mutableStateOf(1) }
    val minWidth = 4f
    val maxWidth = 28f
    val progress = ((outlineWidth - minWidth) / (maxWidth - minWidth)).coerceIn(0f, 1f)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Canvas(
            modifier = Modifier
                .weight(1f)
                .height(32.dp)
                .stableId("control.border_width", "slider")
                .onSizeChanged { trackWidth = it.width.coerceAtLeast(1) }
                .pointerInteropFilter { event ->
                    fun update(x: Float) {
                        val nextProgress = (x / trackWidth.toFloat()).coerceIn(0f, 1f)
                        onOutlineWidthChange(minWidth + nextProgress * (maxWidth - minWidth))
                    }

                    when (event.actionMasked) {
                        MotionEvent.ACTION_DOWN,
                        MotionEvent.ACTION_MOVE -> {
                            update(event.x)
                            true
                        }
                        MotionEvent.ACTION_UP,
                        MotionEvent.ACTION_CANCEL -> true
                        else -> true
                    }
                },
        ) {
            val trackHeight = 7f
            val trackTop = (size.height - trackHeight) / 2f
            drawRoundRect(
                color = ProBorder,
                topLeft = Offset(0f, trackTop),
                size = Size(size.width, trackHeight),
                cornerRadius = CornerRadius(trackHeight, trackHeight),
            )
            drawRoundRect(
                color = ProBlue,
                topLeft = Offset(0f, trackTop),
                size = Size(size.width * progress, trackHeight),
                cornerRadius = CornerRadius(trackHeight, trackHeight),
            )
            drawCircle(
                color = androidx.compose.ui.graphics.Color.White,
                radius = 20f,
                center = Offset(size.width * progress, size.height / 2f),
            )
            drawCircle(
                color = ProBlue,
                radius = 20f,
                center = Offset(size.width * progress, size.height / 2f),
                style = Stroke(width = 3f),
            )
        }
        Text(
            "${outlineWidth.toInt()}",
            modifier = Modifier.width(24.dp),
            color = ProMuted,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun UndoRedoRow(
    canUndo: Boolean,
    canRedo: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(
            onClick = onUndo,
            enabled = canUndo,
            modifier = Modifier
                .weight(1f)
                .stableId("command.undo", "button"),
        ) {
            Text("Undo")
        }
        OutlinedButton(
            onClick = onRedo,
            enabled = canRedo,
            modifier = Modifier
                .weight(1f)
                .stableId("command.redo", "button"),
        ) {
            Text("Redo")
        }
    }
}

@Composable
private fun SourcePicker(
    modifier: Modifier = Modifier,
    bitmap: Bitmap?,
    selectedMask: SegmentMask?,
    selectionBoundary: IntArray?,
    selectedPoint: Offset?,
    sticker: Bitmap?,
    activeTab: EditorTab,
    status: String,
    isProcessing: Boolean,
    onPick: () -> Unit,
    onBrushStart: (Offset) -> Unit,
    onBrushMove: (Offset) -> Unit,
    onBrushEnd: () -> Unit,
    onTapObject: (Offset) -> Unit,
) {
    var zoomScale by remember(bitmap) { mutableStateOf(1f) }
    var panOffset by remember(bitmap) { mutableStateOf(Offset.Zero) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .stableId("panel.source_picker", "panel"),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 8.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, ProBorder, RoundedCornerShape(18.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(uiText("Editor", "Editor"), fontWeight = FontWeight.Bold)
                    Text(
                        status,
                        modifier = Modifier.stableId("status.editor", "status"),
                        color = ProMuted,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Button(
                    onClick = onPick,
                    enabled = !isProcessing,
                    modifier = Modifier.stableId("command.open_image", "button"),
                ) {
                    Text(if (bitmap == null) uiText("Pilih", "Choose") else uiText("Ganti", "Replace"))
                }
            }

            if (bitmap == null) {
                EmptyImageSlot(
                    modifier = Modifier.weight(1f),
                    onClick = onPick,
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, ProBorder, RoundedCornerShape(16.dp))
                        .stableId("canvas.image_editor", "canvas")
                        .pointerInput(bitmap, isProcessing, activeTab) {
                            fun normalized(offset: Offset): Offset =
                                inverseTransformToNormalized(
                                    offset = offset,
                                    rect = fittedImageRect(
                                        containerWidth = size.width.toFloat(),
                                        containerHeight = size.height.toFloat(),
                                        imageWidth = bitmap.width.toFloat(),
                                        imageHeight = bitmap.height.toFloat(),
                                        scale = zoomScale,
                                        pan = panOffset,
                                    ),
                                )

                            awaitEachGesture {
                                val firstDown = awaitFirstDown(requireUnconsumed = false)
                                val start = firstDown.position
                                var lastSingle = start
                                var didTransform = false
                                var didBrush = false

                                while (true) {
                                    val event = awaitPointerEvent()
                                    val pressed = event.changes.filter { it.pressed }
                                    if (pressed.isEmpty()) break

                                    if (!isProcessing && pressed.size >= 2) {
                                        didTransform = true
                                        val newScale = (zoomScale * event.calculateZoom()).coerceIn(1f, 6f)
                                        zoomScale = newScale
                                        panOffset = clampPan(
                                            pan = panOffset + event.calculatePan(),
                                            width = size.width.toFloat(),
                                            height = size.height.toFloat(),
                                            scale = newScale,
                                        )
                                        event.changes.forEach { it.consume() }
                                    } else if (!isProcessing && !didTransform) {
                                        val position = pressed.first().position
                                        when (activeTab) {
                                            EditorTab.Refine -> {
                                                if (!didBrush) {
                                                    onBrushStart(normalized(position))
                                                didBrush = true
                                                } else if (position != lastSingle) {
                                                    onBrushMove(normalized(position))
                                                }
                                                pressed.first().consume()
                                            }
                                            else -> Unit
                                        }
                                        lastSingle = position
                                    }
                                }

                                if (!isProcessing && didBrush) {
                                    onBrushEnd()
                                } else if (!isProcessing && !didTransform && activeTab == EditorTab.Select && isTap(start, lastSingle)) {
                                    onTapObject(normalized(start))
                                }
                            }
                        },
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleX = zoomScale
                                scaleY = zoomScale
                                translationX = panOffset.x
                                translationY = panOffset.y
                                transformOrigin = TransformOrigin.Center
                            },
                    ) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = uiText("Foto sumber", "Source photo"),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                        )
                        if (selectedMask != null && selectionBoundary != null) {
                            MarchingSelectionAnts(
                                mask = selectedMask,
                                boundary = selectionBoundary,
                                imageAspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat(),
                            )
                        }
                    }
                    if (isProcessing) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .stableId("overlay.processing", "overlay")
                                .background(ProInk.copy(alpha = 0.58f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                uiText("Memproses...", "Processing..."),
                                color = androidx.compose.ui.graphics.Color.White,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    if (selectedMask == null && !isProcessing) {
                        MagicSelectHint(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 12.dp),
                        )
                    }
                    sticker?.let {
                        MiniStickerPreview(
                            sticker = it,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(10.dp),
                        )
                    }
                }

            }
        }
    }
}

@Composable
private fun MagicSelectHint(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "magic-hint-motion")
    val scale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.025f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "magic-hint-scale",
    )

    Surface(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .stableId("hint.magic_select", "hint"),
        color = ProInk,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 6.dp,
        shadowElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.TouchApp,
                contentDescription = uiText("Magic select", "Magic select"),
                tint = ProBlue,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = uiText("Tap objek untuk magic remove BG", "Tap an object for Magic Remove BG"),
                color = androidx.compose.ui.graphics.Color.White,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

private fun inverseTransformToNormalized(
    offset: Offset,
    rect: Rect,
): Offset {
    val imageX = offset.x - rect.left
    val imageY = offset.y - rect.top
    return Offset(
        x = (imageX / rect.width).coerceIn(0f, 1f),
        y = (imageY / rect.height).coerceIn(0f, 1f),
    )
}

private fun fittedImageRect(
    containerWidth: Float,
    containerHeight: Float,
    imageWidth: Float,
    imageHeight: Float,
    scale: Float,
    pan: Offset,
): Rect {
    val fitScale = min(containerWidth / imageWidth, containerHeight / imageHeight)
    val baseWidth = imageWidth * fitScale
    val baseHeight = imageHeight * fitScale
    val width = baseWidth * scale
    val height = baseHeight * scale
    val left = (containerWidth - width) / 2f + pan.x
    val top = (containerHeight - height) / 2f + pan.y
    return Rect(left, top, left + width, top + height)
}

private fun clampPan(pan: Offset, width: Float, height: Float, scale: Float): Offset {
    if (scale <= 1f) return Offset.Zero
    val maxX = width * (scale - 1f) / 2f
    val maxY = height * (scale - 1f) / 2f
    return Offset(
        x = pan.x.coerceIn(-maxX, maxX),
        y = pan.y.coerceIn(-maxY, maxY),
    )
}

private fun isTap(start: Offset, end: Offset): Boolean =
    abs(start.x - end.x) < 10f && abs(start.y - end.y) < 10f

@Composable
private fun MarchingSelectionAnts(
    mask: SegmentMask,
    boundary: IntArray,
    imageAspectRatio: Float,
) {
    val transition = rememberInfiniteTransition(label = "selection-ants")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "selection-ants-phase",
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasAspectRatio = size.width / size.height
        val rect = if (canvasAspectRatio > imageAspectRatio) {
            val fittedWidth = size.height * imageAspectRatio
            Rect(
                left = (size.width - fittedWidth) / 2f,
                top = 0f,
                right = (size.width + fittedWidth) / 2f,
                bottom = size.height,
            )
        } else {
            val fittedHeight = size.width / imageAspectRatio
            Rect(
                left = 0f,
                top = (size.height - fittedHeight) / 2f,
                right = size.width,
                bottom = (size.height + fittedHeight) / 2f,
            )
        }
        val stripe = 6f
        val scaleX = rect.width / mask.width
        val scaleY = rect.height / mask.height
        val thickness = max(2.4f, min(rect.width, rect.height) / 210f)
        val pixelSize = Size(scaleX + thickness, scaleY + thickness)

        boundary.forEach { packed ->
            val x = packed and 0xFFFF
            val y = packed ushr 16
            val isWhite = (((x + y + phase) / stripe).toInt() and 1) == 0
            drawRect(
                color = if (isWhite) {
                    androidx.compose.ui.graphics.Color.White
                } else {
                    androidx.compose.ui.graphics.Color.Black
                },
                topLeft = Offset(
                    x = rect.left + x * scaleX - thickness / 2f,
                    y = rect.top + y * scaleY - thickness / 2f,
                ),
                size = pixelSize,
            )
        }
    }
}

@Composable
private fun MiniStickerPreview(
    sticker: Bitmap,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .size(132.dp)
            .stableId("preview.mini_sticker", "image"),
        color = ProSurface.copy(alpha = 0.94f),
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 8.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                bitmap = sticker.asImageBitmap(),
                contentDescription = uiText("Preview sticker kecil", "Small sticker preview"),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        }
    }
}

@Composable
private fun EmptyImageSlot(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.99f else 1f,
        animationSpec = spring(stiffness = 520f, dampingRatio = 0.85f),
        label = "empty-slot-scale",
    )
    val borderColor by animateColorAsState(
        targetValue = if (isPressed) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color.White.copy(alpha = 0.68f),
        animationSpec = tween(durationMillis = 180),
        label = "empty-slot-border",
    )
    val emptyGradient = Brush.linearGradient(
        colors = listOf(
            androidx.compose.ui.graphics.Color(0xFF00EE6E),
            ProBlue,
        ),
        start = Offset.Zero,
        end = Offset.Infinite,
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .stableId("dropzone.open_image", "button")
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        color = androidx.compose.ui.graphics.Color.Transparent,
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 0.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(emptyGradient),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Surface(
                    color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.72f),
                    shape = RoundedCornerShape(18.dp),
                    shadowElevation = 0.dp,
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = uiText("Buka gambar", "Open image"),
                        tint = androidx.compose.ui.graphics.Color(0xFF0A84FF),
                        modifier = Modifier
                            .padding(12.dp)
                            .size(34.dp),
                    )
                }
                Text(uiText("Tap untuk membuka gambar", "Tap to open image"), fontWeight = FontWeight.SemiBold, color = androidx.compose.ui.graphics.Color(0xFF172033))
                Text(
                    uiText("Gambar galeri, PNG atau JPG", "Gallery image, PNG or JPG"),
                    color = androidx.compose.ui.graphics.Color(0xFF40506A),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun ColorChoiceRow(
    stablePrefix: String,
    selectedColor: Int,
    customColor: Int,
    onCustomClick: () -> Unit,
    onColorChange: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        GradientColorSwatch(
            stableId = "$stablePrefix.custom",
            selected = selectedColor == customColor,
            onClick = onCustomClick,
        )
        OutlineOptions.forEach { (name, color) ->
            ColorSwatch(
                stablePrefix = stablePrefix,
                name = name,
                color = color,
                selected = selectedColor == color,
                onClick = { onColorChange(color) },
            )
        }
    }
}

@Composable
private fun ColorSwatch(
    stablePrefix: String,
    name: String,
    color: Int,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(999.dp)
    Box(
        modifier = Modifier
            .width(36.dp)
            .height(18.dp)
            .stableId("$stablePrefix.${stableSlug(name)}", "swatch")
            .background(androidx.compose.ui.graphics.Color(color), shape)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) ProBlue else ProBorder,
                shape = shape,
            )
            .clickable(onClick = onClick),
    ) {
    }
}

@Composable
private fun GradientColorSwatch(
    stableId: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(999.dp)
    Box(
        modifier = Modifier
            .width(36.dp)
            .height(18.dp)
            .stableId(stableId, "swatch")
            .background(
                Brush.horizontalGradient(
                    listOf(
                        androidx.compose.ui.graphics.Color(0xFFFF3B30),
                        androidx.compose.ui.graphics.Color(0xFFFFCC00),
                        androidx.compose.ui.graphics.Color(0xFF34C759),
                        androidx.compose.ui.graphics.Color(0xFF007AFF),
                        androidx.compose.ui.graphics.Color(0xFFAF52DE),
                    ),
                ),
                shape,
            )
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) ProBlue else ProBorder,
                shape = shape,
            )
            .clickable(onClick = onClick),
    )
}

@Composable
private fun ColorPickerOverlay(
    color: Int,
    onColorChange: (Int) -> Unit,
    onCancel: () -> Unit,
    onApply: () -> Unit,
) {
    val initialHsv = remember { colorToHsv(color) }
    var pickerHue by remember { mutableStateOf(initialHsv[0]) }
    var pickerSaturation by remember { mutableStateOf(initialHsv[1]) }
    var pickerValue by remember { mutableStateOf(initialHsv[2]) }
    val previewColor = hsvColor(pickerHue, pickerSaturation, pickerValue)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .stableId("overlay.color_picker", "overlay")
            .border(1.dp, ProBorder, RoundedCornerShape(18.dp)),
        color = ProSurface,
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 12.dp,
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(androidx.compose.ui.graphics.Color(previewColor), CircleShape)
                        .border(1.dp, ProBorder, CircleShape),
                )
                Text(uiText("Warna kustom", "Custom color"), fontWeight = FontWeight.Bold)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(142.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                HueSaturationField(
                    hue = pickerHue,
                    saturation = pickerSaturation,
                    value = pickerValue,
                    onColorChange = { nextHue, nextSaturation ->
                        pickerHue = nextHue
                        pickerSaturation = nextSaturation
                        onColorChange(hsvColor(nextHue, nextSaturation, pickerValue))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .stableId("control.hs_field", "canvas"),
                )
                ValueSlider(
                    hue = pickerHue,
                    saturation = pickerSaturation,
                    value = pickerValue,
                    onValueChange = { nextValue ->
                        pickerValue = nextValue
                        onColorChange(hsvColor(pickerHue, pickerSaturation, nextValue))
                    },
                    modifier = Modifier
                        .width(30.dp)
                        .fillMaxSize()
                        .stableId("control.value_slider", "slider"),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .weight(1f)
                        .stableId("command.color_picker_cancel", "button"),
                ) {
                    Text(uiText("Batal", "Cancel"))
                }
                Button(
                    onClick = onApply,
                    modifier = Modifier
                        .weight(1f)
                        .stableId("command.color_picker_apply", "button"),
                ) {
                    Text(uiText("Terapkan", "Apply"))
                }
            }
        }
    }
}

@Composable
private fun HueSaturationField(
    hue: Float,
    saturation: Float,
    value: Float,
    onColorChange: (Float, Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, ProBorder, RoundedCornerShape(14.dp)),
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(value) {
                    fun update(position: Offset) {
                        val nextHue = (position.x / size.width.toFloat().coerceAtLeast(1f))
                            .coerceIn(0f, 1f) * 360f
                        val nextSaturation = (position.y / size.height.toFloat().coerceAtLeast(1f))
                            .coerceIn(0f, 1f)
                        onColorChange(nextHue, nextSaturation)
                    }

                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        update(down.position)
                        down.consume()

                        while (true) {
                            val event = awaitPointerEvent()
                            val pressed = event.changes.firstOrNull { it.pressed } ?: break
                            update(pressed.position)
                            event.changes.forEach { it.consume() }
                        }
                    }
                },
        ) {
            val columns = 90
            val columnWidth = size.width / columns
            val neutral = androidx.compose.ui.graphics.Color(hsvColor(0f, 0f, value))
            repeat(columns) { index ->
                val columnHue = (index.toFloat() / (columns - 1).toFloat()) * 360f
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            neutral,
                            androidx.compose.ui.graphics.Color(hsvColor(columnHue, 1f, value)),
                        ),
                        startY = 0f,
                        endY = size.height,
                    ),
                    topLeft = Offset(index * columnWidth, 0f),
                    size = Size(columnWidth + 1f, size.height),
                )
            }

            val marker = Offset(
                x = (hue / 360f).coerceIn(0f, 1f) * size.width,
                y = saturation.coerceIn(0f, 1f) * size.height,
            )
            drawCircle(
                color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.36f),
                radius = 10f,
                center = marker,
            )
            drawCircle(
                color = androidx.compose.ui.graphics.Color.White,
                radius = 8f,
                center = marker,
                style = Stroke(width = 2.5f),
            )
        }
    }
}

@Composable
private fun ValueSlider(
    hue: Float,
    saturation: Float,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    var sliderHeight by remember { mutableStateOf(1) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, ProBorder, RoundedCornerShape(14.dp)),
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { sliderHeight = it.height.coerceAtLeast(1) }
                .pointerInteropFilter { event ->
                    fun update(y: Float) {
                        onValueChange(
                            (1f - (y / sliderHeight.toFloat()))
                                .coerceIn(0f, 1f),
                        )
                    }

                    when (event.actionMasked) {
                        MotionEvent.ACTION_DOWN,
                        MotionEvent.ACTION_MOVE -> {
                            update(event.y)
                            true
                        }
                        MotionEvent.ACTION_UP,
                        MotionEvent.ACTION_CANCEL -> true
                        else -> true
                    }
                },
        ) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        androidx.compose.ui.graphics.Color(hsvColor(hue, saturation, 1f)),
                        androidx.compose.ui.graphics.Color.Black,
                    ),
                    startY = 0f,
                    endY = size.height,
                ),
            )

            val markerY = (1f - value.coerceIn(0f, 1f)) * size.height
            drawLine(
                color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.45f),
                start = Offset(0f, markerY),
                end = Offset(size.width, markerY),
                strokeWidth = 6f,
            )
            drawLine(
                color = androidx.compose.ui.graphics.Color.White,
                start = Offset(0f, markerY),
                end = Offset(size.width, markerY),
                strokeWidth = 3f,
            )
        }
    }
}

private fun hsvColor(hue: Float, saturation: Float, value: Float): Int =
    Color.HSVToColor(
        floatArrayOf(
            hue.coerceIn(0f, 360f),
            saturation.coerceIn(0f, 1f),
            value.coerceIn(0f, 1f),
        ),
    )

private fun colorToHsv(color: Int): FloatArray =
    FloatArray(3).also { Color.colorToHSV(color, it) }

@Composable
private fun PreviewAndActions(
    sticker: Bitmap?,
    isStickerEditor: Boolean,
    onSave: () -> Unit,
    onSaveOriginal: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(uiText("Pratinjau", "Preview"), fontWeight = FontWeight.SemiBold)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .stableId("preview.export", "image")
                .background(ProSurfaceAlt, RoundedCornerShape(18.dp))
                .border(1.dp, ProBorder, RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center,
        ) {
            if (sticker == null) {
                Text(uiText("Pratinjau muncul setelah kamu tap objek.", "A preview will appear after you tap an object."))
            } else {
                Image(
                    bitmap = sticker.asImageBitmap(),
                    contentDescription = uiText("Pratinjau sticker", "Sticker preview"),
                    modifier = Modifier.size(230.dp),
                    contentScale = ContentScale.Fit,
                )
            }
        }

        if (isStickerEditor) {
            Button(
                onClick = onSave,
                enabled = sticker != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .stableId("command.save", "button"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                Text(uiText("Simpan sticker", "Save sticker"))
            }
            OutlinedButton(
                onClick = onSaveOriginal,
                enabled = sticker != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .stableId("command.save_original", "button"),
            ) {
                Text(uiText("Simpan ukuran asli", "Save original size"))
            }
        } else {
            Button(
                onClick = onSaveOriginal,
                enabled = sticker != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .stableId("command.save_original", "button"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                Text(uiText("Simpan ukuran asli", "Save original size"))
            }
        }
        Spacer(modifier = Modifier.width(1.dp))
    }
}
