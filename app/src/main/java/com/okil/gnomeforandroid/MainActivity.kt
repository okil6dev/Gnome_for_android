package com.okil.gnomeforandroid

import android.app.usage.UsageStatsManager
import android.app.usage.UsageEvents
import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.media.AudioManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.Settings
import android.app.admin.DevicePolicyManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import android.view.accessibility.AccessibilityManager
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ComponentName
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.core.graphics.drawable.toBitmap
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import android.hardware.input.InputManager
import android.view.InputDevice
import android.graphics.BitmapFactory
import java.io.FileOutputStream
import java.io.File
import android.app.WallpaperManager
import android.graphics.drawable.BitmapDrawable
import android.graphics.RenderEffect
import android.graphics.Shader
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.foundation.Canvas
import androidx.compose.ui.draw.blur
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            window.attributes.flags = window.attributes.flags or WindowManager.LayoutParams.FLAG_BLUR_BEHIND
            window.attributes.blurBehindRadius = 20
        }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            GnomeLauncher()
        }
    }
}

@Composable
fun GnomeLauncherTheme(selectedTheme: String = "Adwaita Dark", content: @Composable () -> Unit) {
    val isLight = selectedTheme == "Adwaita Light"
    val isTransparency = selectedTheme == "Liquid Glass"

    val primaryColor = when(selectedTheme) {
        "Yaru" -> Color(0xFFE95420) // Ubuntu Orange
        "Liquid Glass" -> Color.White
        else -> Color(0xFF3584e4) // Adwaita Blue
    }

    val colorScheme = when {
        isLight -> lightColorScheme(
            primary = primaryColor,
            background = Color.Transparent,
            surface = Color(0xFFF2F2F2),
            onSurface = Color.Black,
            surfaceVariant = Color(0xFFE0E0E0),
            secondaryContainer = Color(0xFFE0E0E0),
            onSecondaryContainer = Color.Black,
            onSurfaceVariant = Color.Black
        )
        isTransparency -> darkColorScheme(
            primary = primaryColor,
            background = Color.Transparent,
            surface = Color.White.copy(alpha = 0.12f),
            onSurface = Color.White,
            surfaceVariant = Color.White.copy(alpha = 0.22f),
            secondaryContainer = Color.White.copy(alpha = 0.18f),
            onSecondaryContainer = Color.White,
            onSurfaceVariant = Color.White
        )
        else -> darkColorScheme(
            primary = primaryColor,
            background = Color.Transparent,
            surface = Color(0xFF242424),
            onSurface = Color.White,
            surfaceVariant = Color(0xFF353535),
            secondaryContainer = Color(0xFF353535),
            onSecondaryContainer = Color.White,
            onSurfaceVariant = Color.White
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

data class AppInfo(
    val name: String,
    val packageName: String,
    val icon: ImageBitmap,
    val isSystemApp: Boolean,
    val isVirtual: Boolean = false,
    val isOpen: Boolean = false
)

fun isAccessibilityServiceEnabled(context: Context): Boolean {
    val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
    return enabledServices.any {
        it.resolveInfo.serviceInfo.packageName == context.packageName &&
                it.resolveInfo.serviceInfo.name == GnomeAccessibilityService::class.java.name
    }
}

data class CalendarEvent(
    val title: String,
    val startTime: Long,
    val endTime: Long,
    val description: String?
)

@Composable
fun GnomeLauncher() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("gnome_launcher_prefs", Context.MODE_PRIVATE) }
    var selectedTheme by remember { mutableStateOf(prefs.getString("gnome_theme", "Adwaita Dark") ?: "Adwaita Dark") }
    var selectedIconPack by remember { mutableStateOf(prefs.getString("selected_icon_pack", null)) }
    var selectedIconShape by remember { mutableStateOf(prefs.getInt("icon_shape", 12)) }
    var isFreeformModeEnabled by remember { mutableStateOf(prefs.getBoolean("freeform_mode_enabled", false)) }

    var draggingApp by remember { mutableStateOf<AppInfo?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    var showOverview by remember { mutableStateOf(false) }
    var showQuickSettings by remember { mutableStateOf(false) }
    var showCalendar by remember { mutableStateOf(false) }
    var showTweaks by remember { mutableStateOf(false) }
    var isAnimationsEnabled by remember { mutableStateOf(prefs.getBoolean("animations_enabled", true)) }
    var wallpaperBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    val isTransparency = selectedTheme == "Liquid Glass"
    val isLight = selectedTheme == "Adwaita Light"

    val wallpaperManager = remember { WallpaperManager.getInstance(context) }

    // Request permission if needed
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                val drawable = wallpaperManager.drawable
                if (drawable is BitmapDrawable) {
                    wallpaperBitmap = drawable.bitmap.asImageBitmap()
                }
            } catch (e: Exception) {}
        }
    }

    LaunchedEffect(Unit) {
        if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            try {
                val drawable = wallpaperManager.drawable
                if (drawable != null) {
                    if (drawable is BitmapDrawable) {
                        wallpaperBitmap = drawable.bitmap.asImageBitmap()
                    } else {
                        val bitmap = Bitmap.createBitmap(
                            drawable.intrinsicWidth.coerceAtLeast(1),
                            drawable.intrinsicHeight.coerceAtLeast(1),
                            Bitmap.Config.ARGB_8888
                        )
                        val canvas = Canvas(bitmap)
                        drawable.setBounds(0, 0, canvas.width, canvas.height)
                        drawable.draw(canvas)
                        wallpaperBitmap = bitmap.asImageBitmap()
                    }
                }
            } catch (e: Exception) {}
        }
    }
    var pinnedApps by remember { mutableStateOf(prefs.getStringSet("pinned_apps", null)?.toSet() ?: setOf("com.android.chrome", "com.google.android.youtube", "com.okil.gnometweaks", "com.okil.gnomeforandroid.launcher")) }
    var isUbuntuDockEnabled by remember { mutableStateOf(prefs.getBoolean("ubuntu_dock_enabled", false)) }
    var isOpenAppsIndicatorEnabled by remember { mutableStateOf(prefs.getBoolean("open_apps_indicator_enabled", true)) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedAppInfo by remember { mutableStateOf<Pair<AppInfo, Offset>?>(null) }

    // Custom app data trigger
    var customAppDataVersion by remember { mutableIntStateOf(0) }

    val openPackages = remember { mutableStateListOf<String>() }
    val recentlyOpened = remember { mutableStateMapOf<String, Long>() }
    val lastState = remember { mutableStateMapOf<String, Int>() }
    var apps by remember { mutableStateOf(listOf<AppInfo>()) }
    var lastForegroundPackage by remember { mutableStateOf<String?>(null) }
    var isLauncherForeground by remember { mutableStateOf(true) }

    val reconcileOpenApps = {
        val currentOpen = lastState.keys.filter { pkg ->
            if (pkg == "com.okil.gnometweaks") return@filter showTweaks
            val state = lastState[pkg] ?: 0
            state == UsageEvents.Event.ACTIVITY_RESUMED ||
                    state == UsageEvents.Event.ACTIVITY_PAUSED ||
                    state == UsageEvents.Event.MOVE_TO_FOREGROUND ||
                    state == UsageEvents.Event.MOVE_TO_BACKGROUND
        }.toSet()

        if (currentOpen != openPackages.toSet()) {
            openPackages.clear()
            openPackages.addAll(currentOpen)
            val newApps = apps.map { app ->
                val isOpen = openPackages.contains(app.packageName)
                if (app.isOpen != isOpen) app.copy(isOpen = isOpen) else app
            }
            if (newApps != apps) {
                apps = newApps
            }
        }
    }

    LaunchedEffect(showTweaks) {
        reconcileOpenApps()
    }

    val calendarEvents = remember { mutableStateListOf<CalendarEvent>() }

    var hasCalendarPermission by remember {
        mutableStateOf(
            androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CALENDAR
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasCalendarPermission = androidx.core.content.ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_CALENDAR
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCalendarPermission = isGranted
        if (!isGranted) {
            val showRationale = (context as? Activity)?.shouldShowRequestPermissionRationale(Manifest.permission.READ_CALENDAR) ?: false
            if (!showRationale) {
                Toast.makeText(context, "Calendar permission is required. Please enable it in Settings.", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun fetchCalendarEvents() {
        if (!hasCalendarPermission) return

        val events = mutableListOf<CalendarEvent>()
        val projection = arrayOf(
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.DESCRIPTION
        )

        val now = Calendar.getInstance().timeInMillis
        val endOfDay = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis

        val selection = "${CalendarContract.Events.DTSTART} >= ? AND ${CalendarContract.Events.DTSTART} <= ?"
        val selectionArgs = arrayOf(now.toString(), endOfDay.toString())

        try {
            context.contentResolver.query(
                CalendarContract.Events.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                "${CalendarContract.Events.DTSTART} ASC"
            )?.use { cursor ->
                val titleIdx = cursor.getColumnIndex(CalendarContract.Events.TITLE)
                val startIdx = cursor.getColumnIndex(CalendarContract.Events.DTSTART)
                val endIdx = cursor.getColumnIndex(CalendarContract.Events.DTEND)
                val descIdx = cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION)

                while (cursor.moveToNext()) {
                    events.add(
                        CalendarEvent(
                            cursor.getString(titleIdx),
                            cursor.getLong(startIdx),
                            cursor.getLong(endIdx),
                            cursor.getString(descIdx)
                        )
                    )
                }
            }
        } catch (e: Exception) {}

        calendarEvents.clear()
        calendarEvents.addAll(events)
    }

    LaunchedEffect(hasCalendarPermission, showCalendar) {
        if (showCalendar && hasCalendarPermission) {
            fetchCalendarEvents()
        }
    }

    GnomeLauncherTheme(selectedTheme = selectedTheme) {
        val packageManager = context.packageManager

        DisposableEffect(context, isOpenAppsIndicatorEnabled) {
            if (!isOpenAppsIndicatorEnabled) return@DisposableEffect onDispose {}
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val pkg = intent?.getStringExtra("package_name")
                    if (pkg != null) {
                        val now = System.currentTimeMillis()
                        val isLauncher = pkg == context?.packageName
                        isLauncherForeground = isLauncher

                        if (!isLauncher) {
                            recentlyOpened[pkg] = now
                            lastState[pkg] = 1 // RESUMED
                            lastForegroundPackage = pkg
                        } else {
                            // If launcher is foreground, we don't have a specific "top" app to minimize
                            // But we keep lastForegroundPackage to know what was last opened
                        }
                        reconcileOpenApps()
                    }
                }
            }
            val filter = IntentFilter("com.okil.gnomeforandroid.APP_STATE_CHANGED")
            LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter)
            onDispose {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
            }
        }

        // Periodically reconcile indicators with system UsageEvents
        LaunchedEffect(isOpenAppsIndicatorEnabled) {
            if (!isOpenAppsIndicatorEnabled) {
                openPackages.clear()
                recentlyOpened.clear()
                lastState.clear()
                apps = apps.map { if (it.isOpen) it.copy(isOpen = false) else it }
                return@LaunchedEffect
            }

            val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            var lastQueryTime = System.currentTimeMillis() - 10000

            while(true) {
                val now = System.currentTimeMillis()
                val events = usm.queryEvents(lastQueryTime, now)
                val event = UsageEvents.Event()
                var changed = false

                while (events.hasNextEvent()) {
                    events.getNextEvent(event)
                    val pkg = event.packageName
                    if (event.timeStamp > lastQueryTime) lastQueryTime = event.timeStamp

                    if (pkg == context.packageName) {
                        val isForeground = (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED ||
                                event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND)
                        if (isLauncherForeground != isForeground) {
                            isLauncherForeground = isForeground
                            changed = true
                        }
                        continue
                    }

                    when (event.eventType) {
                        UsageEvents.Event.ACTIVITY_RESUMED,
                        UsageEvents.Event.MOVE_TO_FOREGROUND -> {
                            if (lastState[pkg] != event.eventType) {
                                lastState[pkg] = event.eventType
                                recentlyOpened[pkg] = event.timeStamp
                                lastForegroundPackage = pkg
                                isLauncherForeground = false
                                changed = true
                            }
                        }
                        UsageEvents.Event.ACTIVITY_PAUSED,
                        UsageEvents.Event.MOVE_TO_BACKGROUND,
                        23 -> { // ACTIVITY_STOPPED or TASK_STACK_REMOVED
                            if (lastState[pkg] != event.eventType) {
                                lastState[pkg] = event.eventType
                                recentlyOpened[pkg] = event.timeStamp
                                changed = true
                            }
                        }
                    }
                }

                if (changed) {
                    reconcileOpenApps()
                }

                kotlinx.coroutines.delay(2000)
            }
        }

        val updatePinnedApps = { newSet: Set<String> ->
            val safeSet = HashSet(newSet)
            pinnedApps = safeSet
            prefs.edit().putStringSet("pinned_apps", safeSet).apply()
        }

        val updateTheme = { theme: String ->
            selectedTheme = theme
            prefs.edit().putString("gnome_theme", theme).apply()
        }

        val updateIconPack = { iconPack: String? ->
            selectedIconPack = iconPack
            prefs.edit().putString("selected_icon_pack", iconPack).apply()
        }

        val updateFreeformMode = { enabled: Boolean ->
            isFreeformModeEnabled = enabled
            prefs.edit().putBoolean("freeform_mode_enabled", enabled).apply()
        }

        val getIconPacks = {
            val iconPackIntents = listOf(
                "com.novalauncher.THEME",
                "org.adw.launcher.THEMES",
                "com.gau.go.launcherex.theme",
                "com.fede.launcher.THEME_ICONPACK",
                "com.anddoes.launcher.THEME",
                "com.teslacoilsw.launcher.THEME"
            )
            val iconPacks = mutableListOf<Pair<String, String>>()
            iconPackIntents.forEach { action ->
                val intent = Intent(action)
                val resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.GET_META_DATA)
                resolveInfos.forEach { info ->
                    val pkg = info.activityInfo.packageName
                    val name = info.loadLabel(packageManager).toString()
                    if (!iconPacks.any { it.second == pkg }) {
                        iconPacks.add(name to pkg)
                    }
                }
            }
            iconPacks
        }

        var iconPackMapping by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

        val loadIconPackMapping = { packPkg: String ->
            val mapping = mutableMapOf<String, String>()
            try {
                val iconPackRes = packageManager.getResourcesForApplication(packPkg)
                val inputStream = try {
                    iconPackRes.assets.open("appfilter.xml")
                } catch (e: Exception) {
                    try {
                        val resId = iconPackRes.getIdentifier("appfilter", "xml", packPkg)
                        if (resId != 0) iconPackRes.getXml(resId) else null
                    } catch (e2: Exception) { null }
                }

                if (inputStream != null) {
                    val factory = org.xmlpull.v1.XmlPullParserFactory.newInstance()
                    val parser = if (inputStream is android.content.res.XmlResourceParser) inputStream else {
                        val p = factory.newPullParser()
                        p.setInput(inputStream as java.io.InputStream, "UTF-8")
                        p
                    }

                    var eventType = parser.eventType
                    while (eventType != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) {
                        if (eventType == org.xmlpull.v1.XmlPullParser.START_TAG && parser.name == "item") {
                            val component = parser.getAttributeValue(null, "component")
                            val drawable = parser.getAttributeValue(null, "drawable")
                            if (component != null && drawable != null) {
                                val pkg = component.substringAfter("{").substringBefore("/").substringBefore("}")
                                if (pkg.isNotEmpty()) mapping[pkg] = drawable
                            }
                        }
                        eventType = parser.next()
                    }
                }
            } catch (e: Exception) {}
            iconPackMapping = mapping
            mapping
        }

        val loadApps = { currentMapping: Map<String, String>? ->
            val mappingToUse = currentMapping ?: iconPackMapping
            val intent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val resolveInfos = packageManager.queryIntentActivities(intent, 0)
            val realApps = resolveInfos.mapNotNull { resolveInfo ->
                try {
                    val pkgName = resolveInfo.activityInfo.packageName
                    if (pkgName == context.packageName) return@mapNotNull null

                    val customName = prefs.getString("custom_name_$pkgName", null) ?: resolveInfo.loadLabel(packageManager).toString()
                    val customIconPath = prefs.getString("custom_icon_$pkgName", null)

                    var bitmap: Bitmap? = if (customIconPath != null) {
                        try { BitmapFactory.decodeFile(customIconPath) } catch (e: Exception) { null }
                    } else null

                    if (bitmap == null) {
                        var iconDrawable: android.graphics.drawable.Drawable? = null
                        selectedIconPack?.let { packPkg ->
                            try {
                                val iconPackRes = packageManager.getResourcesForApplication(packPkg)
                                val drawableName = mappingToUse[pkgName] ?: pkgName.replace(".", "_")
                                val resId = iconPackRes.getIdentifier(drawableName, "drawable", packPkg)
                                if (resId != 0) {
                                    iconDrawable = iconPackRes.getDrawable(resId, null)
                                }
                            } catch (e: Exception) {}
                        }

                        if (iconDrawable == null) {
                            iconDrawable = resolveInfo.loadIcon(packageManager)
                        }
                        bitmap = iconDrawable!!.toBitmap(width = 128, height = 128)
                    }

                    AppInfo(
                        name = customName,
                        packageName = pkgName,
                        icon = bitmap!!.asImageBitmap(),
                        isSystemApp = (packageManager.getApplicationInfo(pkgName, 0).flags and ApplicationInfo.FLAG_SYSTEM) != 0,
                        isOpen = openPackages.contains(pkgName)
                    )
                } catch (e: Exception) { null }
            }

            val tweaksBitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888)
            val tweaksCanvas = Canvas(tweaksBitmap)
            val tweaksPaint = Paint().apply {
                color = android.graphics.Color.parseColor("#5e5c64")
                isAntiAlias = true
            }
            tweaksCanvas.drawRoundRect(0f, 0f, 128f, 128f, 32f, 32f, tweaksPaint)

            val cogPaint = Paint().apply {
                color = android.graphics.Color.WHITE
                style = Paint.Style.STROKE
                strokeWidth = 10f
                isAntiAlias = true
            }
            tweaksCanvas.drawCircle(64f, 64f, 25f, cogPaint)
            cogPaint.strokeWidth = 8f
            for (i in 0 until 8) {
                val angle = i * 45 * PI / 180
                val startX = 64f + 25f * cos(angle).toFloat()
                val startY = 64f + 25f * sin(angle).toFloat()
                val endX = 64f + 38f * cos(angle).toFloat()
                val endY = 64f + 38f * sin(angle).toFloat()
                tweaksCanvas.drawLine(startX, startY, endX, endY, cogPaint)
            }

            val tweaksApp = AppInfo(
                name = "Gnome tweaks",
                packageName = "com.okil.gnometweaks",
                icon = tweaksBitmap.asImageBitmap(),
                isSystemApp = true,
                isVirtual = true,
                isOpen = openPackages.contains("com.okil.gnometweaks")
            )

            apps = (realApps + tweaksApp).sortedBy { it.name.lowercase(Locale.getDefault()) }
        }

        LaunchedEffect(selectedIconPack, customAppDataVersion) {
            val mapping = selectedIconPack?.let { loadIconPackMapping(it) } ?: run { iconPackMapping = emptyMap(); emptyMap() }
            loadApps(mapping)
        }

        LaunchedEffect(showOverview) {
            if (!showOverview) {
                searchQuery = ""
                selectedAppInfo = null
            }
        }

        val transitionProgress by animateFloatAsState(
            targetValue = if (showOverview) 1f else 0f,
            animationSpec = if (isAnimationsEnabled) {
                tween(durationMillis = 400, easing = CubicBezierEasing(0.25f, 0.1f, 0.25f, 1.0f))
            } else {
                snap()
            },
            label = "overview_transition"
        )

        Box(modifier = Modifier.fillMaxSize()) {
            // Background Layer: High-quality Blur using RenderEffect (API 31+)
            if (wallpaperBitmap != null && transitionProgress > 0.01f) {
                Image(
                    bitmap = wallpaperBitmap!!,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            alpha = transitionProgress
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                                renderEffect = android.graphics.RenderEffect.createBlurEffect(
                                    60f, 60f, android.graphics.Shader.TileMode.CLAMP
                                ).asComposeRenderEffect()
                            }
                        },
                    contentScale = ContentScale.Crop
                )
            }

            // Overlay Layer: Theme-specific tint
            if (transitionProgress > 0.01f) {
                val overlayColor = if (selectedTheme == "Liquid Glass") {
                    Color.White.copy(alpha = 0.12f * transitionProgress)
                } else {
                    Color.Black.copy(alpha = 0.4f * transitionProgress)
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(overlayColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            showOverview = false
                            selectedAppInfo = null
                        }
                )
            }

            Column(modifier = Modifier.fillMaxSize().zIndex(100f)) {
                TopBar(
                    showOverview = showOverview,
                    showQuickSettings = showQuickSettings,
                    showCalendar = showCalendar,
                    onActivitiesClick = {
                        showOverview = !showOverview
                        showQuickSettings = false
                        showCalendar = false
                    },
                    onQuickSettingsClick = {
                        showQuickSettings = !showQuickSettings
                        showCalendar = false
                    },
                    onTimeClick = {
                        showCalendar = !showCalendar
                        showQuickSettings = false
                    },
                    modifier = Modifier.zIndex(200f)
                )

                Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    if (isUbuntuDockEnabled) {
                        UbuntuDock(
                            apps = apps,
                            showOverview = showOverview,
                            onAppClick = { pkg ->
                                if (pkg == "com.okil.gnometweaks") {
                                    if (showTweaks) {
                                        showTweaks = false
                                    } else {
                                        recentlyOpened["com.okil.gnometweaks"] = System.currentTimeMillis()
                                        lastState["com.okil.gnometweaks"] = 1
                                        reconcileOpenApps()
                                        showTweaks = true
                                        showOverview = false
                                        showQuickSettings = false
                                        showCalendar = false
                                    }
                                } else {
                                    val launchIntent = packageManager.getLaunchIntentForPackage(pkg)
                                    if (launchIntent != null) {
                                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                                        if (isFreeformModeEnabled) {
                                            val options = android.app.ActivityOptions.makeBasic()
                                            try {
                                                val setWindowingMode = android.app.ActivityOptions::class.java.getMethod("setLaunchWindowingMode", Int::class.javaPrimitiveType)
                                                setWindowingMode.invoke(options, 5)
                                            } catch (e: Exception) {}
                                            val displayMetrics = context.resources.displayMetrics
                                            val left = displayMetrics.widthPixels / 10
                                            val top = displayMetrics.heightPixels / 10
                                            val right = displayMetrics.widthPixels * 9 / 10
                                            val bottom = displayMetrics.heightPixels * 9 / 10
                                            options.setLaunchBounds(android.graphics.Rect(left, top, right, bottom))
                                            context.startActivity(launchIntent, options.toBundle())
                                        } else {
                                            context.startActivity(launchIntent)
                                        }
                                        showOverview = false
                                        showQuickSettings = false
                                        showCalendar = false
                                        showTweaks = false
                                    }
                                }
                            },
                            onAppLongClick = { app, offset ->
                                selectedAppInfo = Pair(app, offset)
                            },
                            pinnedApps = pinnedApps,
                            onActivitiesClick = {
                                showOverview = !showOverview
                                if (showOverview) showQuickSettings = false
                            },
                            modifier = Modifier.zIndex(100f)
                        )
                    }

                    Box(modifier = Modifier.weight(1f).fillMaxHeight().clipToBounds()) {
                        if (transitionProgress > 0.01f) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        alpha = transitionProgress
                                        scaleX = 0.92f + (transitionProgress * 0.08f)
                                        scaleY = 0.92f + (transitionProgress * 0.08f)
                                    }
                            ) {
                                ActivitiesOverview(
                                    apps = apps,
                                    searchQuery = searchQuery,
                                    onSearchQueryChange = { searchQuery = it },
                                    onAppClick = { packageName ->
                                        if (packageName == "com.okil.gnometweaks") {
                                            if (showTweaks) {
                                                showTweaks = false
                                                showOverview = false
                                            } else {
                                                recentlyOpened["com.okil.gnometweaks"] = System.currentTimeMillis()
                                                lastState["com.okil.gnometweaks"] = 1
                                                reconcileOpenApps()
                                                showTweaks = true
                                                showOverview = false
                                                showQuickSettings = false
                                                showCalendar = false
                                            }
                                        } else {
                                            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                                            if (launchIntent != null) {
                                                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                                                if (isFreeformModeEnabled) {
                                                    val options = android.app.ActivityOptions.makeBasic()
                                                    try {
                                                        val setWindowingMode = android.app.ActivityOptions::class.java.getMethod("setLaunchWindowingMode", Int::class.javaPrimitiveType)
                                                        setWindowingMode.invoke(options, 5)
                                                    } catch (e: Exception) {}

                                                    val displayMetrics = context.resources.displayMetrics
                                                    val left = displayMetrics.widthPixels / 10
                                                    val top = displayMetrics.heightPixels / 10
                                                    val right = displayMetrics.widthPixels * 9 / 10
                                                    val bottom = displayMetrics.heightPixels * 9 / 10
                                                    options.setLaunchBounds(android.graphics.Rect(left, top, right, bottom))

                                                    context.startActivity(launchIntent, options.toBundle())
                                                } else {
                                                    context.startActivity(launchIntent)
                                                }
                                                showOverview = false
                                                showQuickSettings = false
                                                showCalendar = false
                                                showTweaks = false
                                                searchQuery = ""
                                            }
                                        }
                                    },
                                    onAppLongClick = { app, offset ->
                                        selectedAppInfo = Pair(app, offset)
                                    },
                                    onBackgroundClick = {
                                        showOverview = false
                                        searchQuery = ""
                                    },
                                    onRefresh = {
                                        loadApps(null)
                                        Toast.makeText(context, "Apps reloaded", Toast.LENGTH_SHORT).show()
                                    },
                                    iconShape = selectedIconShape,
                                    transitionProgress = transitionProgress,
                                    isTransparency = isTransparency
                                )
                            }
                        }

                        if (transitionProgress > 0.01f && searchQuery.isEmpty()) {
                            val density = LocalDensity.current
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 24.dp)
                                    .graphicsLayer {
                                        alpha = transitionProgress
                                        translationY = (1f - transitionProgress) * with(density) { 40.dp.toPx() }
                                    }
                            ) {
                                Dash(
                                    apps = apps,
                                    pinnedApps = pinnedApps,
                                    onAppClick = { packageName ->
                                        if (packageName == "com.okil.gnometweaks") {
                                            if (showTweaks) {
                                                showTweaks = false
                                                showOverview = false
                                            } else {
                                                recentlyOpened["com.okil.gnometweaks"] = System.currentTimeMillis()
                                                lastState["com.okil.gnometweaks"] = 1
                                                reconcileOpenApps()
                                                showTweaks = true
                                                showOverview = false
                                                showQuickSettings = false
                                                showCalendar = false
                                            }
                                        } else {
                                            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                                            if (launchIntent != null) {
                                                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                                                if (isFreeformModeEnabled) {
                                                    val options = android.app.ActivityOptions.makeBasic()
                                                    try {
                                                        val setWindowingMode = android.app.ActivityOptions::class.java.getMethod("setLaunchWindowingMode", Int::class.javaPrimitiveType)
                                                        setWindowingMode.invoke(options, 5)
                                                    } catch (e: Exception) {}

                                                    val displayMetrics = context.resources.displayMetrics
                                                    val left = displayMetrics.widthPixels / 10
                                                    val top = displayMetrics.heightPixels / 10
                                                    val right = displayMetrics.widthPixels * 9 / 10
                                                    val bottom = displayMetrics.heightPixels * 9 / 10
                                                    options.setLaunchBounds(android.graphics.Rect(left, top, right, bottom))

                                                    context.startActivity(launchIntent, options.toBundle())
                                                } else {
                                                    context.startActivity(launchIntent)
                                                }
                                                showOverview = false
                                                showQuickSettings = false
                                                showCalendar = false
                                                showTweaks = false
                                                searchQuery = ""
                                            }
                                        }
                                    },
                                    onAppLongClick = { app, offset ->
                                        selectedAppInfo = Pair(app, offset)
                                    },
                                    iconShape = selectedIconShape
                                )
                            }
                        }

                        if (showTweaks) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .zIndex(50f),
                                contentAlignment = Alignment.Center
                            ) {
                                GnomeTweaksWindow(
                                    isUbuntuDockEnabled = isUbuntuDockEnabled,
                                    onUbuntuDockToggle = {
                                        isUbuntuDockEnabled = it
                                        prefs.edit().putBoolean("ubuntu_dock_enabled", it).apply()
                                    },
                                    isAnimationsEnabled = isAnimationsEnabled,
                                    onAnimationsToggle = {
                                        isAnimationsEnabled = it
                                        prefs.edit().putBoolean("animations_enabled", it).apply()
                                    },
                                    isFreeformModeEnabled = isFreeformModeEnabled,
                                    onFreeformModeToggle = updateFreeformMode,
                                    selectedTheme = selectedTheme,
                                    onThemeChange = updateTheme,
                                    selectedIconPack = selectedIconPack,
                                    onIconPackChange = {
                                        updateIconPack(it)
                                    },
                                    selectedIconShape = selectedIconShape,
                                    onIconShapeChange = { shape ->
                                        selectedIconShape = shape
                                        prefs.edit().putInt("icon_shape", shape).apply()
                                    },
                                    iconPacks = getIconPacks(),
                                    apps = apps.filter { !it.isVirtual },
                                    onAppCustomizationChange = { pkg, name, iconUri ->
                                        if (name != null) {
                                            prefs.edit().putString("custom_name_$pkg", name).apply()
                                        }
                                        if (iconUri != null) {
                                            try {
                                                val inputStream = context.contentResolver.openInputStream(iconUri)
                                                val file = File(context.filesDir, "custom_icon_$pkg.png")
                                                val outputStream = FileOutputStream(file)
                                                inputStream?.copyTo(outputStream)
                                                inputStream?.close()
                                                outputStream.close()
                                                prefs.edit().putString("custom_icon_$pkg", file.absolutePath).apply()
                                            } catch (e: Exception) {}
                                        }
                                        if (name == null && iconUri == null) {
                                            // Reset
                                            prefs.edit().remove("custom_name_$pkg").remove("custom_icon_$pkg").apply()
                                            try { File(context.filesDir, "custom_icon_$pkg.png").delete() } catch (e: Exception) {}
                                        }
                                        loadApps(iconPackMapping)
                                    },
                                    onClose = { showTweaks = false }
                                )
                            }
                        }
                    }
                }
            }

            if (selectedAppInfo != null) {
                val (app, offset) = selectedAppInfo!!
                val density = LocalDensity.current
                Popup(
                    alignment = Alignment.TopStart,
                    offset = IntOffset(
                        (offset.x + with(density) { 20.dp.roundToPx() }).toInt(),
                        (offset.y + with(density) { 40.dp.roundToPx() }).toInt()
                    ),
                    onDismissRequest = { selectedAppInfo = null },
                    properties = PopupProperties(focusable = true)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp),
                        tonalElevation = 8.dp,
                        modifier = Modifier.width(220.dp)
                    ) {
                        AppContextMenuContent(
                            app = app,
                            isPinned = pinnedApps.contains(app.packageName),
                            onPinToggle = { pkg ->
                                val newSet = pinnedApps.toMutableSet()
                                if (newSet.contains(pkg)) newSet.remove(pkg) else newSet.add(pkg)
                                updatePinnedApps(newSet)
                            },
                            onDismiss = {
                                selectedAppInfo = null
                            }
                        )
                    }
                }
            }

            if (showCalendar || showQuickSettings) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(150f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            showCalendar = false
                            showQuickSettings = false
                        }
                )
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = showCalendar,
                enter = if (isAnimationsEnabled) fadeIn() + expandVertically(expandFrom = Alignment.Top) else EnterTransition.None,
                exit = if (isAnimationsEnabled) fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top) else ExitTransition.None,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 36.dp)
                    .zIndex(200f)
            ) {
                CalendarPopup(
                    hasPermission = hasCalendarPermission,
                    events = calendarEvents,
                    onDismiss = { showCalendar = false },
                    onRequestPermission = {
                        val showRationale = (context as? Activity)?.shouldShowRequestPermissionRationale(Manifest.permission.READ_CALENDAR) ?: false
                        if (!showRationale && prefs.getBoolean("calendar_permission_requested", false)) {
                            Toast.makeText(context, "Calendar permission is blocked. Please enable it in Settings.", Toast.LENGTH_LONG).show()
                            try {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {}
                        } else {
                            prefs.edit().putBoolean("calendar_permission_requested", true).apply()
                            launcher.launch(Manifest.permission.READ_CALENDAR)
                        }
                    }
                )
            }

            AnimatedVisibility(
                visible = showQuickSettings,
                enter = if (isAnimationsEnabled) fadeIn() + expandVertically(expandFrom = Alignment.Top) else EnterTransition.None,
                exit = if (isAnimationsEnabled) fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top) else ExitTransition.None,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(top = 36.dp, end = 8.dp)
                    .zIndex(210f)
            ) {
                QuickSettingsMenu(
                    onClose = { showQuickSettings = false },
                    onShowTweaks = { showTweaks = true },
                    isFreeformModeEnabled = isFreeformModeEnabled
                )
            }
        }
    }
}

@Composable
fun GnomeTweaksWindow(
    isUbuntuDockEnabled: Boolean,
    onUbuntuDockToggle: (Boolean) -> Unit,
    isAnimationsEnabled: Boolean,
    onAnimationsToggle: (Boolean) -> Unit,
    isFreeformModeEnabled: Boolean,
    onFreeformModeToggle: (Boolean) -> Unit,
    selectedTheme: String,
    onThemeChange: (String) -> Unit,
    selectedIconPack: String?,
    onIconPackChange: (String?) -> Unit,
    selectedIconShape: Int,
    onIconShapeChange: (Int) -> Unit,
    iconPacks: List<Pair<String, String>>,
    apps: List<AppInfo>,
    onAppCustomizationChange: (String, String?, Uri?) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var windowOffset by remember { mutableStateOf(IntOffset(0, 0)) }
    val tabs = listOf("Appearance", "Icon Packs", "Freeform mode", "Extensions", "App Customization")
    var activeTab by remember { mutableStateOf(tabs.first()) }
    val isLight = selectedTheme == "Adwaita Light"
    val isTransparency = selectedTheme == "Liquid Glass"
    val primary = MaterialTheme.colorScheme.primary

    var editingApp by remember { mutableStateOf<AppInfo?>(null) }
    var newAppName by remember { mutableStateOf("") }
    val iconPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        editingApp?.let { app ->
            onAppCustomizationChange(app.packageName, if (newAppName != app.name) newAppName else null, uri)
            editingApp = null
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Surface(
            modifier = Modifier
                .offset { IntOffset(windowOffset.x, windowOffset.y) }
                .width(620.dp)
                .height(480.dp),
            color = if (isTransparency) Color.White.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(12.dp),
            shadowElevation = if (isTransparency) 0.dp else 16.dp,
            border = if (isTransparency) BorderStroke(0.5.dp, Color.White.copy(alpha = 0.15f)) else null
        ) {
            Column(modifier = Modifier.then(if (isTransparency) Modifier.background(Color.White.copy(alpha = 0.05f)) else Modifier)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .background(
                            if (isTransparency) Color.White.copy(alpha = 0.05f)
                            else if (isLight) Color(0xFFEBEBEB)
                            else Color(0xFF303030)
                        )
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                windowOffset += IntOffset(dragAmount.x.toInt(), dragAmount.y.toInt())
                            }
                        }
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(30.dp)
                            .background(
                                if (isTransparency) Color.White.copy(alpha = 0.2f)
                                else if (isLight) Color.Black.copy(alpha = 0.05f)
                                else Color.White.copy(alpha = 0.1f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Rounded.Close,
                            null,
                            tint = if (isTransparency || !isLight) Color.White else Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text("Tweaks", color = if (isLight) Color.Black else Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.size(30.dp))
                }

                Row(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.width(180.dp).fillMaxHeight().background(
                        if (isTransparency) Color.White.copy(alpha = 0.05f)
                        else if (isLight) Color(0xFFF6F6F6)
                        else Color(0xFF2d2d2d)
                    ).padding(vertical = 12.dp)) {
                        tabs.forEach { tab ->
                            val isActive = activeTab == tab
                            Text(
                                text = tab,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (isActive) primary.copy(alpha = 0.15f) else Color.Transparent)
                                    .clickable { activeTab = tab }
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                color = if (isActive) primary else (if (isLight) Color.Black else Color.White),
                                fontSize = 14.sp,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                    Column(modifier = Modifier.padding(24.dp).weight(1f)) {
                        Text(activeTab, color = if (isLight) Color.Black else Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(24.dp))

                        if (activeTab == "Extensions") {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.verticalScroll(rememberScrollState())
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isTransparency) Color.White.copy(alpha = 0.12f)
                                        else if (isLight) Color(0xFFE0E0E0)
                                        else Color(0xFF303030)
                                    )
                                    .then(if (isTransparency) Modifier.border(0.5.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp)) else Modifier)
                                    .padding(16.dp)) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Ubuntu Dock", color = if (isLight) Color.Black else Color.White, fontWeight = FontWeight.Bold)
                                        Text("A permanent left-side dock", color = if (isLight) Color.DarkGray else Color.Gray, fontSize = 12.sp)
                                    }
                                    Switch(checked = isUbuntuDockEnabled, onCheckedChange = onUbuntuDockToggle, colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary))
                                }

                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isTransparency) Color.White.copy(alpha = 0.12f)
                                        else if (isLight) Color(0xFFE0E0E0)
                                        else Color(0xFF303030)
                                    )
                                    .then(if (isTransparency) Modifier.border(0.5.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp)) else Modifier)
                                    .padding(16.dp)) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Disable Animations", color = if (isLight) Color.Black else Color.White, fontWeight = FontWeight.Bold)
                                        Text("Disables all system animations", color = if (isLight) Color.DarkGray else Color.Gray, fontSize = 12.sp)
                                    }
                                    Switch(checked = !isAnimationsEnabled, onCheckedChange = { onAnimationsToggle(!it) }, colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary))
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isTransparency) Color.White.copy(alpha = 0.12f)
                                            else if (isLight) Color(0xFFE0E0E0)
                                            else Color(0xFF303030)
                                        )
                                        .then(if (isTransparency) Modifier.border(0.5.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp)) else Modifier)
                                        .clickable {
                                            context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                                        }
                                        .padding(16.dp)
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Power Menu Support", color = if (isLight) Color.Black else Color.White, fontWeight = FontWeight.Bold)
                                        Text(
                                            if (isAccessibilityServiceEnabled(context)) "Enabled (Accessibility)" else "Disabled (Tap to enable)",
                                            color = if (isAccessibilityServiceEnabled(context)) MaterialTheme.colorScheme.primary else (if (isLight) Color.DarkGray else Color.Gray),
                                            fontSize = 12.sp
                                        )
                                    }
                                    Icon(
                                        Icons.Rounded.ChevronRight,
                                        null,
                                        tint = if (isLight) Color.Gray else Color.LightGray
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isTransparency) Color.White.copy(alpha = 0.12f)
                                            else if (isLight) Color(0xFFE0E0E0)
                                            else Color(0xFF303030)
                                        )
                                        .then(if (isTransparency) Modifier.border(0.5.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp)) else Modifier)
                                        .clickable {
                                            try {
                                                context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                                            } catch (e: Exception) {}
                                        }
                                        .padding(16.dp)
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Open Apps Indicators", color = if (isLight) Color.Black else Color.White, fontWeight = FontWeight.Bold)
                                        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
                                        val time = System.currentTimeMillis()
                                        val stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 10000, time)
                                        val hasUsageAccess = stats.isNotEmpty()

                                        Text(
                                            if (hasUsageAccess) "Active" else "Requires Usage Access (Tap to grant)",
                                            color = if (hasUsageAccess) MaterialTheme.colorScheme.primary else (if (isLight) Color.DarkGray else Color.Gray),
                                            fontSize = 12.sp
                                        )
                                    }
                                    Icon(
                                        Icons.Rounded.ChevronRight,
                                        null,
                                        tint = if (isLight) Color.Gray else Color.LightGray
                                    )
                                }
                            }
                        } else if (activeTab == "Appearance") {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.verticalScroll(rememberScrollState())
                            ) {
                                val themes = listOf("Adwaita Dark (Default)", "Adwaita Light", "Yaru", "Liquid Glass")
                                themes.forEach { theme ->
                                    val isSelected = selectedTheme == theme || (theme == "Adwaita Dark (Default)" && selectedTheme == "Adwaita Dark")
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                                else if (isTransparency) Color.White.copy(alpha = 0.12f)
                                                else if (isLight) Color(0xFFE0E0E0)
                                                else Color(0xFF303030)
                                            )
                                            .then(if (isTransparency && !isSelected) Modifier.border(0.5.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp)) else Modifier)
                                            .clickable {
                                                val themeKey = if (theme == "Adwaita Dark (Default)") "Adwaita Dark" else theme
                                                onThemeChange(themeKey)
                                            }
                                            .padding(16.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clip(CircleShape)
                                                .background(when {
                                                    theme.contains("Adwaita") -> Color(0xFF3584e4)
                                                    theme == "Yaru" -> Color(0xFFE95420)
                                                    theme == "Liquid Glass" -> Color.White
                                                    else -> Color.Gray
                                                })
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(theme, color = if (isLight) Color.Black else Color.White, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                        Spacer(modifier = Modifier.weight(1f))
                                        if (isSelected) {
                                            Icon(Icons.Rounded.Check, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                        }
                                    }
                                }
                            }
                        } else if (activeTab == "Freeform mode") {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.verticalScroll(rememberScrollState())
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isTransparency) Color.White.copy(alpha = 0.12f)
                                            else if (isLight) Color(0xFFE0E0E0)
                                            else Color(0xFF303030)
                                        )
                                        .then(if (isTransparency) Modifier.border(0.5.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp)) else Modifier)
                                        .padding(16.dp)
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Freeform Window Mode", color = if (isLight) Color.Black else Color.White, fontWeight = FontWeight.Bold)
                                        Text("Launch apps in resizable windows", color = if (isLight) Color.DarkGray else Color.Gray, fontSize = 12.sp)
                                    }
                                    Switch(
                                        checked = isFreeformModeEnabled,
                                        onCheckedChange = onFreeformModeToggle,
                                        colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary)
                                    )
                                }

                                Text(
                                    "Note: This requires 'Enable freeform windows' to be enabled in Android Developer Options to work properly on some devices.",
                                    color = if (isLight) Color.Gray else Color.LightGray,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }
                        } else if (activeTab == "Icon Packs") {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(24.dp),
                                modifier = Modifier.verticalScroll(rememberScrollState())
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("Icon Shape", color = if (isLight) Color.Black else Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        val shapes = listOf(0, 8, 12, 18, 100)
                                        shapes.forEach { shape ->
                                            val isSelected = selectedIconShape == shape
                                            Box(
                                                modifier = Modifier
                                                    .size(48.dp)
                                                    .clip(RoundedCornerShape(shape.dp))
                                                    .background(
                                                        if (isSelected) MaterialTheme.colorScheme.primary
                                                        else if (isTransparency) Color.White.copy(alpha = 0.12f)
                                                        else if (isLight) Color(0xFFE0E0E0)
                                                        else Color(0xFF303030)
                                                    )
                                                    .then(if (isTransparency && !isSelected) Modifier.border(0.5.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(shape.dp)) else Modifier)
                                                    .clickable { onIconShapeChange(shape) }
                                                    .border(
                                                        width = if (isSelected) 2.dp else 0.dp,
                                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                        shape = RoundedCornerShape(shape.dp)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (isSelected) {
                                                    Icon(Icons.Rounded.Check, null, tint = Color.White, modifier = Modifier.size(24.dp))
                                                }
                                            }
                                        }
                                    }
                                }

                                Divider(color = (if (isLight) Color.Black else Color.White).copy(alpha = 0.1f))

                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text("Installed Packs", color = if (isLight) Color.Black else Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    val allIconPacks = listOf("Default" to null) + iconPacks.map { it.first to it.second }
                                    allIconPacks.forEach { (name, pkg) ->
                                        val isSelected = selectedIconPack == pkg
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                                    else if (isTransparency) Color.White.copy(alpha = 0.12f)
                                                    else if (isLight) Color(0xFFE0E0E0)
                                                    else Color(0xFF303030)
                                                )
                                                .then(if (isTransparency && !isSelected) Modifier.border(0.5.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp)) else Modifier)
                                                .clickable { onIconPackChange(pkg) }
                                                .padding(16.dp)
                                        ) {
                                            Icon(Icons.Rounded.AutoAwesomeMotion, null, tint = if (isSelected) MaterialTheme.colorScheme.primary else (if (isLight) Color.Black else Color.White), modifier = Modifier.size(20.dp))
                                            Spacer(modifier = Modifier.width(16.dp))
                                            Text(name, color = if (isLight) Color.Black else Color.White, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                            Spacer(modifier = Modifier.weight(1f))
                                            if (isSelected) {
                                                Icon(Icons.Rounded.Check, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (activeTab == "App Customization") {
                            if (editingApp == null) {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(apps) { app ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    if (isTransparency) Color.White.copy(alpha = 0.12f)
                                                    else if (isLight) Color(0xFFE0E0E0)
                                                    else Color(0xFF303030)
                                                )
                                                .then(if (isTransparency) Modifier.border(0.5.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp)) else Modifier)
                                                .clickable {
                                                    editingApp = app
                                                    newAppName = app.name
                                                }
                                                .padding(12.dp)
                                        ) {
                                            Image(
                                                bitmap = app.icon,
                                                contentDescription = null,
                                                modifier = Modifier.size(32.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(app.name, color = if (isLight) Color.Black else Color.White)
                                            Spacer(modifier = Modifier.weight(1f))
                                            Icon(Icons.Rounded.Edit, null, tint = if (isLight) Color.Gray else Color.LightGray, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = { editingApp = null }) {
                                            Icon(Icons.Rounded.ArrowBack, null, tint = if (isLight) Color.Black else Color.White)
                                        }
                                        Text("Customize ${editingApp?.name}", color = if (isLight) Color.Black else Color.White, fontWeight = FontWeight.Bold)
                                    }

                                    OutlinedTextField(
                                        value = newAppName,
                                        onValueChange = { newAppName = it },
                                        label = { Text("App Name", color = if (isLight) Color.Gray else Color.LightGray) },
                                        modifier = Modifier.fillMaxWidth().pointerInput(Unit) {},
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = if (isLight) Color.Black else Color.White,
                                            unfocusedTextColor = if (isLight) Color.Black else Color.White,
                                            cursorColor = primary,
                                            focusedBorderColor = primary,
                                            unfocusedBorderColor = if (isLight) Color.Gray else Color.DarkGray,
                                            focusedLabelColor = primary,
                                            unfocusedLabelColor = if (isLight) Color.Gray else Color.LightGray
                                        )
                                    )

                                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Button(
                                            onClick = {
                                                iconPickerLauncher.launch("image/*")
                                            },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Change Icon")
                                        }

                                        Button(
                                            onClick = {
                                                onAppCustomizationChange(editingApp!!.packageName, if (newAppName != editingApp!!.name) newAppName else null, null)
                                                editingApp = null
                                            },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Save Name Only")
                                        }
                                    }

                                    TextButton(
                                        onClick = {
                                            onAppCustomizationChange(editingApp!!.packageName, null, null)
                                            editingApp = null
                                            newAppName = ""
                                        },
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    ) {
                                        Text("Reset to Default", color = Color.Red)
                                    }

                                    Text(
                                        "Supported icon formats: .png, .jpg, .ico",
                                        color = if (isLight) Color.Gray else Color.LightGray,
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun UbuntuDock(apps: List<AppInfo>, pinnedApps: Set<String>, showOverview: Boolean, onAppClick: (String) -> Unit, onAppLongClick: (AppInfo, Offset) -> Unit, onActivitiesClick: () -> Unit, modifier: Modifier = Modifier) {
    var isHovered by remember { mutableStateOf(false) }
    val appsToDisplay = remember(apps, pinnedApps) {
        apps.filter { pinnedApps.contains(it.packageName) || it.isOpen }
            .distinctBy { it.packageName }
    }
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        modifier = modifier.width(68.dp).fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            appsToDisplay.forEach { app ->
                var itemOffset by remember { mutableStateOf(Offset.Zero) }
                var isHovered by remember { mutableStateOf(false) }
                val context = LocalContext.current
                val prefs = remember { context.getSharedPreferences("gnome_launcher_prefs", Context.MODE_PRIVATE) }
                val shapeCorner = prefs.getInt("icon_shape", 12)

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .onGloballyPositioned { coords -> itemOffset = coords.positionInWindow() }
                        .clip(RoundedCornerShape(shapeCorner.dp))
                        .background(if (isHovered) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f) else Color.Transparent)
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent(PointerEventPass.Initial)
                                    if (event.type == PointerEventType.Enter) isHovered = true
                                    if (event.type == PointerEventType.Exit) isHovered = false
                                    if (event.type == PointerEventType.Press && event.buttons.isSecondaryPressed) {
                                        onAppLongClick(app, itemOffset)
                                        event.changes.forEach { it.consume() }
                                        while (true) {
                                            val nextEvent = awaitPointerEvent(PointerEventPass.Initial)
                                            nextEvent.changes.forEach { it.consume() }
                                            if (nextEvent.type == PointerEventType.Release) break
                                        }
                                    }
                                }
                            }
                        }
                        .combinedClickable(
                            onClick = { onAppClick(app.packageName) },
                            onLongClick = { onAppLongClick(app, itemOffset) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = app.icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape((shapeCorner * 0.6).dp)),
                        contentScale = ContentScale.Fit
                    )
                    if (app.isOpen) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 1.dp)
                                .width(12.dp)
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(MaterialTheme.colorScheme.onSurface)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f), modifier = Modifier.width(32.dp))
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (showOverview) {
                            if (MaterialTheme.colorScheme.primary == Color(0xFFE95420)) {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                            }
                        } else if (isHovered) {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                        } else Color.Transparent
                    )
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                if (event.type == PointerEventType.Enter) isHovered = true
                                if (event.type == PointerEventType.Exit) isHovered = false
                                if (event.type == PointerEventType.Press && event.buttons.isSecondaryPressed) {
                                    event.changes.forEach { it.consume() }
                                    while (true) {
                                        val nextEvent = awaitPointerEvent(PointerEventPass.Initial)
                                        nextEvent.changes.forEach { it.consume() }
                                        if (nextEvent.type == PointerEventType.Release) break
                                    }
                                }
                            }
                        }
                    }
                    .clickable { onActivitiesClick() },
                contentAlignment = Alignment.Center
            ) {
                // Ubuntu-style 3x3 grid (9 dots)
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    repeat(3) {
                        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                            repeat(3) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.onSurface)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar(
    showOverview: Boolean,
    showQuickSettings: Boolean,
    showCalendar: Boolean,
    onActivitiesClick: () -> Unit,
    onQuickSettingsClick: () -> Unit,
    onTimeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var batteryPercentage by remember { mutableStateOf(0) }
    var isSpenDetected by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val inputManager = context.getSystemService(Context.INPUT_SERVICE) as InputManager

        fun checkSpen() {
            val deviceIds = inputManager.inputDeviceIds
            isSpenDetected = deviceIds.any { id ->
                val device = inputManager.getInputDevice(id)
                device != null && (
                        (device.sources and InputDevice.SOURCE_STYLUS) == InputDevice.SOURCE_STYLUS ||
                                device.name.lowercase().contains("s pen") ||
                                device.name.lowercase().contains("spen")
                        )
            }
        }

        val listener = object : InputManager.InputDeviceListener {
            override fun onInputDeviceAdded(deviceId: Int) = checkSpen()
            override fun onInputDeviceRemoved(deviceId: Int) = checkSpen()
            override fun onInputDeviceChanged(deviceId: Int) = checkSpen()
        }

        inputManager.registerInputDeviceListener(listener, null)
        checkSpen()

        while (true) {
            val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as android.os.BatteryManager
            batteryPercentage = batteryManager.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY)
            checkSpen()
            kotlinx.coroutines.delay(2000)
        }
    }
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
            Box(modifier = Modifier.fillMaxWidth().height(38.dp)) {
                var isActivitiesHovered by remember { mutableStateOf(false) }
                var isClockHovered by remember { mutableStateOf(false) }
                var isQuickSettingsHovered by remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 4.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            if (showOverview) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                            else if (isActivitiesHovered) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            else Color.Transparent
                        )
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent(PointerEventPass.Initial)
                                    if (event.type == PointerEventType.Enter) isActivitiesHovered = true
                                    if (event.type == PointerEventType.Exit) isActivitiesHovered = false
                                    if (event.type == PointerEventType.Press && event.buttons.isSecondaryPressed) {
                                        event.changes.forEach { it.consume() }
                                        while (true) {
                                            val nextEvent = awaitPointerEvent(PointerEventPass.Initial)
                                            nextEvent.changes.forEach { it.consume() }
                                            if (nextEvent.type == PointerEventType.Release) break
                                        }
                                    }
                                }
                            }
                        }
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onActivitiesClick() }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(42.dp)
                            .height(9.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurface)
                    )
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            if (showCalendar) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                            else if (isClockHovered) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            else Color.Transparent
                        )
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent(PointerEventPass.Initial)
                                    if (event.type == PointerEventType.Enter) isClockHovered = true
                                    if (event.type == PointerEventType.Exit) isClockHovered = false
                                    if (event.type == PointerEventType.Press && event.buttons.isSecondaryPressed) {
                                        event.changes.forEach { it.consume() }
                                        while (true) {
                                            val nextEvent = awaitPointerEvent(PointerEventPass.Initial)
                                            nextEvent.changes.forEach { it.consume() }
                                            if (nextEvent.type == PointerEventType.Release) break
                                        }
                                    }
                                }
                            }
                        }
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onTimeClick() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    GnomeClock()
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            if (showQuickSettings) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                            else if (isQuickSettingsHovered) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            else Color.Transparent
                        )
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent(PointerEventPass.Initial)
                                    if (event.type == PointerEventType.Enter) isQuickSettingsHovered = true
                                    if (event.type == PointerEventType.Exit) isQuickSettingsHovered = false
                                    if (event.type == PointerEventType.Press && event.buttons.isSecondaryPressed) {
                                        event.changes.forEach { it.consume() }
                                        while (true) {
                                            val nextEvent = awaitPointerEvent(PointerEventPass.Initial)
                                            nextEvent.changes.forEach { it.consume() }
                                            if (nextEvent.type == PointerEventType.Release) break
                                        }
                                    }
                                }
                            }
                        }
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onQuickSettingsClick() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    if (isSpenDetected) {
                        Icon(Icons.Rounded.Create, null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(15.dp))
                    }
                    Icon(Icons.Rounded.Wifi, null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(15.dp))
                    Icon(Icons.Rounded.BatteryFull, null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(15.dp))
                    Text(text = "$batteryPercentage%", color = MaterialTheme.colorScheme.onSurface, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Rounded.KeyboardArrowDown, null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

@Composable
fun GnomeClock() {
    var currentTime by remember { mutableStateOf("") }
    val dateFormat = remember { SimpleDateFormat("MMM d  HH:mm", Locale.getDefault()) }
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = dateFormat.format(Date())
            kotlinx.coroutines.delay(1000)
        }
    }
    Text(
        text = currentTime,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun AppContextMenuContent(app: AppInfo, isPinned: Boolean, onPinToggle: (String) -> Unit, onDismiss: () -> Unit) {
    val context = LocalContext.current
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = app.name, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), fontSize = 12.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

        ContextMenuItem(
            label = if (isPinned) "Unpin from Dash" else "Pin to Dash",
            icon = if (isPinned) Icons.Rounded.PushPin else Icons.Rounded.PushPin,
            onClick = { onPinToggle(app.packageName); onDismiss() }
        )

        ContextMenuItem(label = "App Details", icon = Icons.Rounded.Info, onClick = { context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply { data = Uri.fromParts("package", app.packageName, null); addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }); onDismiss() })
        if (app.isSystemApp) {
            ContextMenuItem(label = "Disable", icon = Icons.Rounded.Block, onClick = { context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply { data = Uri.fromParts("package", app.packageName, null); addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }); onDismiss() })
        } else {
            ContextMenuItem(label = "Uninstall", icon = Icons.Rounded.Delete, color = Color(0xFFf66151), onClick = { context.startActivity(Intent(Intent.ACTION_DELETE).apply { data = Uri.parse("package:${app.packageName}"); addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }); onDismiss() })
        }
    }
}

@Composable
fun ContextMenuItem(label: String, icon: ImageVector, color: Color? = null, onClick: () -> Unit) {
    val itemColor = color ?: MaterialTheme.colorScheme.onSurface
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) { Icon(icon, null, tint = itemColor, modifier = Modifier.size(18.dp)); Spacer(modifier = Modifier.width(12.dp)); Text(label, color = itemColor, fontSize = 14.sp) }
}

@Composable
fun CalendarPopup(
    hasPermission: Boolean,
    events: List<CalendarEvent>,
    onDismiss: () -> Unit,
    onRequestPermission: () -> Unit
) {
    val calendar = Calendar.getInstance()
    val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    val dayFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
    val eventTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    Surface(
        modifier = Modifier
            .width(400.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { /* Just to consume clicks */ },
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = dayFormat.format(calendar.time),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = monthFormat.format(calendar.time), fontWeight = FontWeight.Bold)
                Row {
                    Icon(Icons.Rounded.ChevronLeft, null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(Icons.Rounded.ChevronRight, null, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                    Text(text = day, modifier = Modifier.width(32.dp), textAlign = TextAlign.Center, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            for (row in 0 until 5) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    for (col in 0 until 7) {
                        val dayNum = (row * 7 + col + 1) % 31 + 1
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (dayNum == calendar.get(Calendar.DAY_OF_MONTH)) MaterialTheme.colorScheme.primary else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dayNum.toString(),
                                fontSize = 13.sp,
                                color = if (dayNum == calendar.get(Calendar.DAY_OF_MONTH)) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(16.dp))

            if (!hasPermission) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Calendar access is required to show events.",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onRequestPermission,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Grant Access")
                    }
                }
            } else if (events.isEmpty()) {
                Text(
                    text = "No Events Today",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    events.forEach { event ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(event.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(
                                    "${eventTimeFormat.format(Date(event.startTime))} - ${eventTimeFormat.format(Date(event.endTime))}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "No Notifications", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), fontSize = 14.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun QuickSettingsMenu(onClose: () -> Unit, onShowTweaks: () -> Unit, isFreeformModeEnabled: Boolean) {
    val context = LocalContext.current

    val isAccessibilityEnabled = isAccessibilityServiceEnabled(context)

    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as android.hardware.camera2.CameraManager

    var isWifiEnabled by remember { mutableStateOf(wifiManager.isWifiEnabled) }
    var isBluetoothEnabled by remember { mutableStateOf(bluetoothManager.adapter?.isEnabled == true) }
    var isAirplaneModeOn by remember { mutableStateOf(Settings.Global.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0) }
    var isDndOn by remember { mutableStateOf(notificationManager.currentInterruptionFilter != NotificationManager.INTERRUPTION_FILTER_ALL) }
    var isTorchOn by remember { mutableStateOf(false) }
    var isRotationLocked by remember { mutableStateOf(Settings.System.getInt(context.contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 0) }
    var isNightLightOn by remember { mutableStateOf(false) } // System private API mostly, but we can link to settings
    var currentBrightness by remember { mutableStateOf(Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 128) / 255f) }
    var currentVolume by remember { mutableStateOf(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat() / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) }
    var showPowerMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while(true) {
            isWifiEnabled = wifiManager.isWifiEnabled
            isBluetoothEnabled = bluetoothManager.adapter?.isEnabled == true
            isAirplaneModeOn = Settings.Global.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
            isDndOn = notificationManager.currentInterruptionFilter != NotificationManager.INTERRUPTION_FILTER_ALL
            isRotationLocked = Settings.System.getInt(context.contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 0
            kotlinx.coroutines.delay(1000)
        }
    }

    if (showPowerMenu) {
        GnomePowerDialog(
            isAccessibilityEnabled = isAccessibilityEnabled,
            onClose = { showPowerMenu = false },
            onConfirm = {
                showPowerMenu = false
                onClose()
            }
        )
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .width(360.dp)
            .padding(vertical = 8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { /* Consume clicks to prevent dismissal from scrim */ },
        tonalElevation = 6.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header row with circular buttons moved from bottom
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                            .clickable { onShowTweaks(); onClose() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Tune, null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                            .clickable {
                                val intent = Intent(Settings.ACTION_SETTINGS)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                                if (isFreeformModeEnabled) {
                                    val options = android.app.ActivityOptions.makeBasic()
                                    try {
                                        val setWindowingMode = android.app.ActivityOptions::class.java.getMethod("setLaunchWindowingMode", Int::class.javaPrimitiveType)
                                        setWindowingMode.invoke(options, 5)
                                    } catch (e: Exception) {}
                                    val displayMetrics = context.resources.displayMetrics
                                    val left = displayMetrics.widthPixels / 10
                                    val top = displayMetrics.heightPixels / 10
                                    val right = displayMetrics.widthPixels * 9 / 10
                                    val bottom = displayMetrics.heightPixels * 9 / 10
                                    options.setLaunchBounds(android.graphics.Rect(left, top, right, bottom))
                                    context.startActivity(intent, options.toBundle())
                                } else {
                                    context.startActivity(intent)
                                }
                                onClose()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Settings, null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                        .clickable { showPowerMenu = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.PowerSettingsNew, null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.LightMode, null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(20.dp))
                    Slider(value = currentBrightness, onValueChange = {
                        currentBrightness = it
                        if (Settings.System.canWrite(context)) {
                            try { Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, (it * 255).toInt()) } catch (e: Exception) {}
                        } else {
                            context.startActivity(Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply { data = Uri.parse("package:${context.packageName}"); addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
                        }
                    }, modifier = Modifier.weight(1f).padding(horizontal = 12.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.VolumeUp, null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(20.dp))
                    Slider(value = currentVolume, onValueChange = {
                        currentVolume = it
                        val maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (it * maxVol).toInt(), 0)
                    }, modifier = Modifier.weight(1f).padding(horizontal = 12.dp))
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickPill(
                        icon = Icons.Rounded.Wifi,
                        label = "WiFi",
                        active = isWifiEnabled,
                        onClick = {
                            val intent = if (android.os.Build.VERSION.SDK_INT >= 29) {
                                Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
                            } else {
                                Intent(Settings.ACTION_WIFI_SETTINGS)
                            }
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                context.startActivity(Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    QuickPill(icon = Icons.Rounded.Bluetooth, label = "Bluetooth", active = isBluetoothEnabled, onClick = { context.startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }, modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickPill(
                        icon = Icons.Rounded.Highlight,
                        label = "Torch",
                        active = isTorchOn,
                        onClick = {
                            try {
                                val cameraId = cameraManager.cameraIdList[0]
                                isTorchOn = !isTorchOn
                                cameraManager.setTorchMode(cameraId, isTorchOn)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Torch not available", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    QuickPill(icon = Icons.Rounded.AirplanemodeActive, label = "Airplane", active = isAirplaneModeOn, onClick = { context.startActivity(Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }, modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickPill(
                        icon = Icons.Rounded.ScreenRotation,
                        label = "Rotation",
                        active = !isRotationLocked,
                        onClick = {
                            if (Settings.System.canWrite(context)) {
                                val newState = if (isRotationLocked) 1 else 0 // 1 = Auto, 0 = Locked

                                if (newState == 0) {
                                    // Locking: capture current rotation
                                    val currentRotation = context.resources.configuration.orientation
                                    val userRotation = if (currentRotation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
                                        android.view.Surface.ROTATION_90
                                    } else {
                                        android.view.Surface.ROTATION_0
                                    }
                                    Settings.System.putInt(context.contentResolver, Settings.System.USER_ROTATION, userRotation)
                                }

                                Settings.System.putInt(context.contentResolver, Settings.System.ACCELEROMETER_ROTATION, newState)
                                isRotationLocked = !isRotationLocked
                            } else {
                                context.startActivity(Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply { data = Uri.parse("package:${context.packageName}"); addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun GnomePowerDialog(isAccessibilityEnabled: Boolean, onClose: () -> Unit, onConfirm: () -> Unit) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.padding(24.dp).widthIn(max = 400.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                "Power Off",
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                if (isAccessibilityEnabled) "The system will power off automatically in 60 seconds."
                else "Accessibility Service is required for system power actions. Please enable it in Settings.",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Divider(color = Color.White.copy(alpha = 0.1f), modifier = Modifier.padding(bottom = 8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    PowerButton("Suspend", Modifier.weight(1f), onClick = {
                        try {
                            val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                            val adminComponent = ComponentName(context, GnomeDeviceAdminReceiver::class.java)
                            if (dpm.isAdminActive(adminComponent)) {
                                dpm.lockNow()
                            } else {
                                Toast.makeText(context, "Device Admin required for Suspend", Toast.LENGTH_LONG).show()
                                val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                                    putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent)
                                    putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Required to lock the screen")
                                }
                                context.startActivity(intent)
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Suspend failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                        onConfirm()
                    })
                    PowerButton("Restart", Modifier.weight(1f), onClick = {
                        val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                        val adminComponent = ComponentName(context, GnomeDeviceAdminReceiver::class.java)

                        val triggerAcc = {
                            if (isAccessibilityEnabled) {
                                val intent = Intent(context, GnomeAccessibilityService::class.java).apply {
                                    action = GnomeAccessibilityService.ACTION_RESTART
                                }
                                context.startService(intent)
                            } else {
                                context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                                Toast.makeText(context, "Please enable 'Gnome Power Menu Support'", Toast.LENGTH_LONG).show()
                            }
                        }

                        try {
                            if (dpm.isAdminActive(adminComponent)) {
                                dpm.reboot(adminComponent)
                            } else {
                                triggerAcc()
                            }
                        } catch (e: Exception) {
                            triggerAcc()
                        }
                        onConfirm()
                    })
                }
                PowerButton("Power Off", Modifier.fillMaxWidth(), isPrimary = true, onClick = {
                    if (isAccessibilityEnabled) {
                        val intent = Intent(context, GnomeAccessibilityService::class.java).apply {
                            action = GnomeAccessibilityService.ACTION_POWER_OFF
                        }
                        context.startService(intent)
                    } else {
                        context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        Toast.makeText(context, "Please enable 'Gnome Power Menu Support'", Toast.LENGTH_LONG).show()
                    }
                    onConfirm()
                })
            }
        }
    )
}



@Composable
fun PowerButton(label: String, modifier: Modifier = Modifier, isPrimary: Boolean = false, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPrimary) Color(0xFFf66151) else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isPrimary) Color.White else MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun QuickPill(icon: ImageVector, label: String, active: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    var isHovered by remember { mutableStateOf(false) }
    val backgroundColor = if (active) MaterialTheme.colorScheme.primary else (if (isHovered) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f) else MaterialTheme.colorScheme.surfaceVariant)
    val contentColor = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    Row(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(backgroundColor)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        if (event.type == PointerEventType.Enter) isHovered = true
                        if (event.type == PointerEventType.Exit) isHovered = false
                    }
                }
            }
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(contentColor.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = contentColor, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, color = contentColor, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun ActivitiesOverview(
    apps: List<AppInfo>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onAppClick: (String) -> Unit,
    onAppLongClick: (AppInfo, Offset) -> Unit,
    onBackgroundClick: () -> Unit,
    onRefresh: () -> Unit,
    iconShape: Int = 12,
    transitionProgress: Float = 1f,
    isTransparency: Boolean = false
) {
    val isLight = MaterialTheme.colorScheme.onSurface == Color.Black
    val density = LocalDensity.current
    val refreshThreshold = with(density) { 60.dp.toPx() }
    val filteredApps = remember(searchQuery, apps) {
        val base = if (searchQuery.isEmpty()) apps else apps.filter { it.name.contains(searchQuery, ignoreCase = true) }
        base.distinctBy { it.packageName }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onBackgroundClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Surface(
                modifier = Modifier
                    .width(380.dp)
                    .height(42.dp)
                    .graphicsLayer {
                        translationY = (1f - transitionProgress) * (-20).dp.toPx()
                    },
                color = if (isTransparency) {
                    Color.White.copy(alpha = 0.15f)
                } else if (isLight) {
                    Color.White
                } else {
                    Color(0xFF353535)
                },
                tonalElevation = if (isLight) 8.dp else 0.dp,
                shadowElevation = if (isLight) 2.dp else 0.dp,
                shape = RoundedCornerShape(21.dp),
                border = if (isTransparency) {
                    BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
                } else if (isLight) {
                    BorderStroke(1.dp, Color.Black.copy(alpha = 0.08f))
                } else null
            ) {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(color = if (isLight) Color.Black else Color.White, fontSize = 15.sp),
                    cursorBrush = SolidColor(if (isLight) Color.Black else Color.White),
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    decorationBox = { innerTextField ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
                            Icon(imageVector = Icons.Rounded.Search, contentDescription = null, tint = if (isLight) Color.Black.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.5f), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                if (searchQuery.isEmpty()) {
                                    Text(text = "Type to search", color = if (isLight) Color.Black.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.5f), fontSize = 15.sp)
                                }
                                innerTextField()
                            }
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(48.dp))
            val gridState = rememberLazyGridState()
            var totalDrag by remember { mutableFloatStateOf(0f) }
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Adaptive(minSize = 100.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 40.dp)
                    .graphicsLayer {
                        val scale = 0.95f + (transitionProgress * 0.05f)
                        scaleX = scale
                        scaleY = scale
                        alpha = transitionProgress
                    }
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onDragEnd = {
                                if (totalDrag > refreshThreshold) onRefresh()
                                totalDrag = 0f
                            },
                            onDragCancel = { totalDrag = 0f },
                            onVerticalDrag = { _, dragAmount ->
                                // Only accumulate if we're at the top and pulling down
                                if (gridState.firstVisibleItemIndex == 0 && gridState.firstVisibleItemScrollOffset == 0) {
                                    totalDrag += dragAmount
                                } else {
                                    totalDrag = 0f
                                }
                            }
                        )
                    },
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(36.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(filteredApps, key = { it.packageName }) { app ->
                    AppIconItem(
                        app = app,
                        onClick = { onAppClick(app.packageName) },
                        onLongClick = { offset ->
                            onAppLongClick(app, offset)
                        },
                        iconShape = iconShape
                    )
                }
            }
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun AppIconItem(
    app: AppInfo,
    onClick: () -> Unit,
    onLongClick: (Offset) -> Unit,
    iconShape: Int = 12,
    modifier: Modifier = Modifier,
    isTarget: Boolean = false,
    isDragging: Boolean = false
) {
    var itemOffset by remember { mutableStateOf(Offset.Zero) }
    var isHovered by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(100.dp)
            .onGloballyPositioned { coords -> itemOffset = coords.positionInWindow() }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        if (event.type == PointerEventType.Enter) isHovered = true
                        if (event.type == PointerEventType.Exit) isHovered = false
                        if (event.type == PointerEventType.Press && event.buttons.isSecondaryPressed) {
                            onLongClick(itemOffset)
                            event.changes.forEach { it.consume() }
                            while (true) {
                                val nextEvent = awaitPointerEvent(PointerEventPass.Initial)
                                nextEvent.changes.forEach { it.consume() }
                                if (nextEvent.type == PointerEventType.Release) break
                            }
                        }
                    }
                }
            }
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick(itemOffset) }
            )
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(iconShape.dp))
                .background(
                    if (isTarget) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    else if (isHovered) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = if (isDragging) 0.4f else 0.05f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                bitmap = app.icon,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(iconShape.dp)),
                contentScale = ContentScale.Fit
            )
        }

        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .width(20.dp)
                .height(2.dp)
                .clip(RoundedCornerShape(1.dp))
                .background(if (app.isOpen) MaterialTheme.colorScheme.onSurface else Color.Transparent)
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(text = app.name, color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp, textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.Normal)
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun Dash(apps: List<AppInfo>, pinnedApps: Set<String>, onAppClick: (String) -> Unit, onAppLongClick: (AppInfo, Offset) -> Unit, iconShape: Int = 12) {
    val appsToDisplay = remember(apps, pinnedApps) {
        apps.filter { pinnedApps.contains(it.packageName) || it.isOpen }
            .distinctBy { it.packageName }
    }
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(32.dp),
        modifier = Modifier.padding(bottom = 24.dp).wrapContentWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            appsToDisplay.forEach { app ->
                var itemOffset by remember { mutableStateOf(Offset.Zero) }
                var isHovered by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .onGloballyPositioned { coords -> itemOffset = coords.positionInWindow() }
                        .clip(RoundedCornerShape(iconShape.dp))
                        .background(if (isHovered) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent(PointerEventPass.Initial)
                                    if (event.type == PointerEventType.Enter) isHovered = true
                                    if (event.type == PointerEventType.Exit) isHovered = false
                                    if (event.type == PointerEventType.Press && event.buttons.isSecondaryPressed) {
                                        onAppLongClick(app, itemOffset)
                                        event.changes.forEach { it.consume() }
                                        while (true) {
                                            val nextEvent = awaitPointerEvent(PointerEventPass.Initial)
                                            nextEvent.changes.forEach { it.consume() }
                                            if (nextEvent.type == PointerEventType.Release) break
                                        }
                                    }
                                }
                            }
                        }
                        .combinedClickable(
                            onClick = { onAppClick(app.packageName) },
                            onLongClick = { onAppLongClick(app, itemOffset) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(bitmap = app.icon, contentDescription = null, modifier = Modifier.size(42.dp), contentScale = ContentScale.Fit)
                    if (app.isOpen) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 4.dp)
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onSurface)
                        )
                    }
                }
            }
        }
    }
}
