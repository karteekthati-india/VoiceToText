package com.voice.text

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.voice.text.ui.theme.VoiceToTextTheme
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.voice.text.permissions.PermissionHandler
import com.voice.text.permissions.PermissionViewModel

class MainActivity : ComponentActivity() {

    private val permissionViewModel by viewModels<PermissionViewModel>()
    private val voiceViewModel by viewModels<VoiceViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val allGranted = permissions.values.all { it }
                permissionViewModel.updatePermissionState(allGranted)
            }

        setContent {
            VoiceToTextTheme {
                val permissionGranted by permissionViewModel.permissionGranted.collectAsState()

                if (!permissionGranted) {
                    PermissionRequestScreen {
                        PermissionHandler.requestPermissions(
                            permissionLauncher,
                            PermissionHandler.voiceToTextPermissions
                        )
                    }
                } else {
                    VoiceToTextScreen(voiceViewModel)
                }
            }
        }
    }
}

@Composable
fun PermissionRequestScreen(onRequestPermissions: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "This app requires microphone access to convert your voice to text.",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = onRequestPermissions) {
                Text("Grant Permission")
            }
        }
    }
}

@Composable
fun VoiceToTextScreen(viewModel: VoiceViewModel) {
    val recognizedText by viewModel.recognizedText.collectAsState()
    var isListening by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (recognizedText.isEmpty()) "Speak something..." else recognizedText,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (isListening) viewModel.stopListening()
                    else viewModel.startListening()
                    isListening = !isListening
                }
            ) {
                Text(if (isListening) "Stop Listening" else "Start Listening")
            }
        }
    }
}