package com.voice.text.permissions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PermissionViewModel(application: Application) : AndroidViewModel(application) {

    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted: StateFlow<Boolean> = _permissionGranted

    fun updatePermissionState(isGranted: Boolean) {
        _permissionGranted.value = isGranted
    }
}
