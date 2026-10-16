package com.example.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VoicedocAccessibilityService : AccessibilityService() {

  companion object {
    private const val TAG = "VoicedocAccessibility"
    private val _isServiceActive = MutableStateFlow(false)
    val isServiceActive: StateFlow<Boolean> = _isServiceActive

    private val _lastActionLogged = MutableStateFlow<String?>(null)
    val lastActionLogged: StateFlow<String?> = _lastActionLogged

    var instance: VoicedocAccessibilityService? = null
      private set
  }

  override fun onServiceConnected() {
    super.onServiceConnected()
    instance = this
    _isServiceActive.value = true
    _lastActionLogged.value = "Voicedoc Assistive Engine Connected"
    Log.d(TAG, "Voicedoc Accessibility Service connected successfully")
  }

  override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    // Monitored for dialog confirmations and automated setting toggles
  }

  override fun onInterrupt() {
    Log.d(TAG, "Voicedoc Accessibility Service interrupted")
  }

  override fun onUnbind(intent: Intent?): Boolean {
    instance = null
    _isServiceActive.value = false
    return super.onUnbind(intent)
  }

  fun performAssistiveClick(): Boolean {
    return performGlobalAction(GLOBAL_ACTION_BACK)
  }
}
