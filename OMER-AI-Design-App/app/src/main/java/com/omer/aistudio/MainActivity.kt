package com.omer.aistudio

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF0B0812)
private val Panel = Color(0xFF191326)
private val Purple = Color(0xFF9B63F5)
private val Muted = Color(0xFFB8AFC8)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StudioContextHolder.context = applicationContext
        setContent { StudioApp() }
    }
}

@Composable
fun StudioApp() {
    var page by remember { mutableStateOf("Home") }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var prompt by remember { mutableStateOf("") }
    var script by remember { mutableStateOf("") }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedUri = uri
    }

    LaunchedEffect(selectedUri) {
        selectedUri?.let { uri ->
            runCatching {
                // The Android photo picker grants temporary read access to this URI.
                // Decode a bounded preview to keep memory usage reasonable.
                val input = StudioContextHolder.context?.contentResolver?.openInputStream(uri)
                input?.use { BitmapFactory.decodeStream(it) }
            }.getOrNull()?.let { selectedBitmap = it }
        }
    }

    MaterialTheme(colorScheme = darkColorScheme(
        primary = Purple, background = Bg, surface = Panel,
        onBackground = Color.White, onSurface = Color.White
    )) {
        Scaffold(
            containerColor = Bg,
            bottomBar = {
                NavigationBar(containerColor = Panel, contentColor = Muted) {
                    listOf("Home", "Photo", "Video", "Script", "Projects").forEach { item ->
                        NavigationBarItem(
                            selected = page == item,
                            onClick = { page = item },
                            icon = {
                                Icon(
                                    when (item) {
                                        "Photo" -> Icons.Default.Image
                                        "Video" -> Icons.Default.Movie
                                        "Script" -> Icons.Default.EditNote
                                        "Projects" -> Icons.Default.Folder
                                        else -> Icons.Default.Home
                                    }, contentDescription = item
                                )
                            },
                            label = { Text(item, fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = Purple.copy(alpha = .35f),
                                unselectedIconColor = Muted,
                                unselectedTextColor = Muted
                            )
                        )
                    }
                }
            }
        ) { padding ->
            when (page) {
                "Photo" -> PhotoPage(
                    modifier = Modifier.padding(padding),
                    bitmap = selectedBitmap,
                    onPick = { picker.launch("image/*") }
                )
                "Video" -> VideoPage(Modifier.padding(padding))
                "Script" -> ScriptPage(
                    modifier = Modifier.padding(padding),
                    prompt = prompt,
                    onPrompt = { prompt = it },
                    script = script,
                    onGenerate = {
                        val topic = prompt.ifBlank { "My next video" }
                        script = "TITLE: $topic\n\nHOOK (0–5 sec)\nKya aap ne kabhi socha hai $topic? Aaj hum isay simple tareeqe se samjhenge.\n\nMAIN STORY\nSab se pehle topic ka short introduction dein. Phir 3 important points examples ke saath explain karein. Har scene ke liye relevant visuals use karein.\n\nENDING\nAgar video pasand aaye to like karein aur follow/subscribe karein.\n\nNOTE: This is an editable starter script. AI cloud generation will be connected in a later build."
                    }
                )
                "Projects" -> SimplePage(Modifier.padding(padding), "My Projects", "Your saved edits and video projects will appear here.")
                else -> HomePage(
                    modifier = Modifier.padding(padding),
                    onOpen = { page = it },
                    onPick = { picker.launch("image/*") }
                )
            }
        }
    }
}

@Composable
fun HomePage(modifier: Modifier = Modifier, onOpen: (String) -> Unit, onPick: () -> Unit) {
    LazyColumn(modifier.fillMaxSize().padding(horizontal = 20.dp), contentPadding = PaddingValues(top = 28.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text("OMER", color = Purple, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 4.sp)
                    Text("AI STUDIO", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Your creative world", color = Muted, fontSize = 13.sp)
                }
                Surface(shape = RoundedCornerShape(16.dp), color = Panel) {
                    Icon(Icons.Default.AutoAwesome, null, tint = Purple, modifier = Modifier.padding(14.dp).size(28.dp))
                }
            }
        }
        item {
            Surface(shape = RoundedCornerShape(24.dp), color = Color(0xFF241638), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Create something amazing.", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Photo editing, video ideas and scripts in one studio.", color = Muted, fontSize = 13.sp)
                    Button(onClick = onPick, colors = ButtonDefaults.buttonColors(containerColor = Purple)) {
                        Icon(Icons.Default.AddPhotoAlternate, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Start with a photo")
                    }
                }
            }
        }
        item { Text("CREATIVE TOOLS", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp) }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ToolRow("AI Photo Editor", "Open a photo and start editing", Icons.Default.Image, onClick = { onOpen("Photo") })
                ToolRow("AI Video Maker", "Plan a video and its scenes", Icons.Default.Movie, onClick = { onOpen("Video") })
                ToolRow("AI Script Writer", "Create a starter script in seconds", Icons.Default.EditNote, onClick = { onOpen("Script") })
                ToolRow("My Projects", "Your creative workspace", Icons.Default.Folder, onClick = { onOpen("Projects") })
            }
        }
        item { Text("FREE ACCESS • NO SUBSCRIPTION • NO WATERMARK", color = Purple, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp) }
    }
}

@Composable
fun ToolRow(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Surface(color = Panel, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = Purple.copy(alpha = .16f), shape = RoundedCornerShape(14.dp)) {
                Icon(icon, null, tint = Purple, modifier = Modifier.padding(12.dp).size(24.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = Muted, fontSize = 12.sp)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Muted)
        }
    }
}

@Composable
fun PhotoPage(modifier: Modifier = Modifier, bitmap: Bitmap?, onPick: () -> Unit) {
    Column(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PageTitle("Photo Editor", "Choose an image to begin")
        Surface(color = Panel, shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth().weight(1f).clickable { onPick() }) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(12.dp)) {
                if (bitmap != null) Image(bitmap.asImageBitmap(), null, modifier = Modifier.fillMaxSize())
                else Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(Icons.Default.AddPhotoAlternate, null, tint = Purple, modifier = Modifier.size(54.dp))
                    Text("Tap to select a photo", color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text("JPG • PNG • WEBP", color = Muted, fontSize = 12.sp)
                }
            }
        }
        Button(onClick = onPick, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Purple)) {
            Icon(Icons.Default.PhotoLibrary, null); Spacer(Modifier.width(8.dp)); Text("Choose from gallery")
        }
        Text("Starter build: photo import and preview are active. Editing tools will be added in upcoming builds.", color = Muted, fontSize = 12.sp)
    }
}

@Composable
fun VideoPage(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PageTitle("Video Maker", "Build your next short video")
        ToolRow("Photo slideshow", "Turn selected photos into a video", Icons.Default.Slideshow) {}
        ToolRow("Script to scenes", "Plan scenes from your script", Icons.Default.Movie) {}
        ToolRow("Voice-over", "Narration tools coming next", Icons.Default.RecordVoiceOver) {}
        Text("Video rendering and AI generation are planned modules; they are not active in this starter APK yet.", color = Muted, fontSize = 12.sp)
    }
}

@Composable
fun ScriptPage(modifier: Modifier = Modifier, prompt: String, onPrompt: (String) -> Unit, script: String, onGenerate: () -> Unit) {
    Column(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        PageTitle("AI Script Writer", "Create a video script outline")
        OutlinedTextField(
            value = prompt, onValueChange = onPrompt,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("What is your video about?") },
            placeholder = { Text("e.g. 30-second travel reel about Jeddah") },
            minLines = 2,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Purple, focusedLabelColor = Purple,
                unfocusedBorderColor = Color(0xFF51465F),
                focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                cursorColor = Purple
            )
        )
        Button(onClick = onGenerate, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Purple)) {
            Icon(Icons.Default.AutoAwesome, null); Spacer(Modifier.width(8.dp)); Text("Create script")
        }
        if (script.isNotBlank()) {
            Surface(color = Panel, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth().weight(1f)) {
                LazyColumn(Modifier.padding(16.dp)) { item { Text(script, color = Color.White, fontSize = 14.sp, lineHeight = 21.sp) } }
            }
        } else {
            Text("Your generated script will appear here. This first version uses an offline template; cloud AI will be integrated later.", color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
fun SimplePage(modifier: Modifier = Modifier, title: String, subtitle: String) {
    Column(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        PageTitle(title, subtitle)
        Text("Nothing saved yet.", color = Muted)
    }
}

@Composable
fun PageTitle(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(title, color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Text(subtitle, color = Muted, fontSize = 13.sp)
    }
}

// Application context is set by the Activity for decoding a selected image URI.
private object StudioContextHolder { var context: android.content.Context? = null }
