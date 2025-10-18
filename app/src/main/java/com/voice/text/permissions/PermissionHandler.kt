package com.voice.text.permissions


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionHandler {

    // You can extend this list dynamically for other modules
    val voiceToTextPermissions = listOf(
        Manifest.permission.RECORD_AUDIO
    )

    /**
     * Check if all permissions are granted
     */
    fun hasPermissions(context: Context, permissions: List<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Request permissions via launcher
     */
    fun requestPermissions(
        launcher: ActivityResultLauncher<Array<String>>,
        permissions: List<String>
    ) {
        launcher.launch(permissions.toTypedArray())
    }

    /**
     * Get list of denied permissions (for rationale)
     */
    fun getDeniedPermissions(context: Context, permissions: List<String>): List<String> {
        return permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Check if we should show rationale for a specific permission
     */
    fun shouldShowRationale(activity: Activity, permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }
}