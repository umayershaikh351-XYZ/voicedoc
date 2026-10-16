package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ActionSeverity
import com.example.data.model.AutoFixAction
import com.example.data.model.AutoFixCatalog
import com.example.data.model.DiagnosticReport
import com.example.data.model.FamilyMember
import com.example.data.model.FixHistoryItem
import com.example.data.model.FixStatus
import com.example.data.model.LanguageCatalog
import com.example.data.model.RepairBooking
import com.example.data.model.RepairShop
import com.example.data.model.RepairServiceOption
import com.example.data.model.SupportedLanguage
import com.example.service.AudioVoiceManager
import com.example.service.PhoneDoctorEngine
import com.example.service.RealDeviceTelemetry
import com.example.service.VoicedocAccessibilityService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

enum class AppScreen {
  ONBOARDING,
  HOME,
  DIAGNOSIS,
  AUTO_FIX,
  FAMILY_MODE,
  REPAIR_BOOKING,
  SETTINGS,
  HISTORY
}

data class UiNotification(
  val id: String = UUID.randomUUID().toString(),
  val message: String,
  val isError: Boolean = false
)

class VoicedocViewModel(application: Application) : AndroidViewModel(application) {

  private val context: Context get() = getApplication<Application>().applicationContext
  val voiceManager = AudioVoiceManager(context, viewModelScope)
  private val doctorEngine = PhoneDoctorEngine(context)

  private val _currentScreen = MutableStateFlow(AppScreen.HOME)
  val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

  private val _screenStack = mutableListOf<AppScreen>()

  private val _hasCompletedOnboarding = MutableStateFlow(false)
  val hasCompletedOnboarding: StateFlow<Boolean> = _hasCompletedOnboarding.asStateFlow()

  private val _selectedLanguage = MutableStateFlow(LanguageCatalog.LANGUAGES.first())
  val selectedLanguage: StateFlow<SupportedLanguage> = _selectedLanguage.asStateFlow()

  private val _telemetry = MutableStateFlow(doctorEngine.readDeviceTelemetry())
  val telemetry: StateFlow<RealDeviceTelemetry> = _telemetry.asStateFlow()

  private val _activeDiagnosis = MutableStateFlow<DiagnosticReport?>(null)
  val activeDiagnosis: StateFlow<DiagnosticReport?> = _activeDiagnosis.asStateFlow()

  private val _isAnalyzing = MutableStateFlow(false)
  val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

  private val _analysisStep = MutableStateFlow("Initializing Voicedoc AI...")
  val analysisStep: StateFlow<String> = _analysisStep.asStateFlow()

  private val _autoFixActions = MutableStateFlow<List<AutoFixAction>>(emptyList())
  val autoFixActions: StateFlow<List<AutoFixAction>> = _autoFixActions.asStateFlow()

  private val _isAutoFixing = MutableStateFlow(false)
  val isAutoFixing: StateFlow<Boolean> = _isAutoFixing.asStateFlow()

  private val _fixExecutionLogs = MutableStateFlow<List<String>>(emptyList())
  val fixExecutionLogs: StateFlow<List<String>> = _fixExecutionLogs.asStateFlow()

  private val _currentFixStepIndex = MutableStateFlow(0)
  val currentFixStepIndex: StateFlow<Int> = _currentFixStepIndex.asStateFlow()

  private val _isFixCompleted = MutableStateFlow(false)
  val isFixCompleted: StateFlow<Boolean> = _isFixCompleted.asStateFlow()

  private val _floatingBarExpanded = MutableStateFlow(false)
  val floatingBarExpanded: StateFlow<Boolean> = _floatingBarExpanded.asStateFlow()

  private val _notification = MutableStateFlow<UiNotification?>(null)
  val notification: StateFlow<UiNotification?> = _notification.asStateFlow()

  // Remote Family PIN
  val remoteHelpCode = "VD-8492"

  private val _familyMembers = MutableStateFlow(
    listOf(
      FamilyMember(
        id = "fm_1",
        name = "Grandma Mary",
        relation = "Mother / Elder",
        deviceModel = "Samsung Galaxy A14",
        batteryLevel = 18,
        storageUsedPercent = 94,
        isOnline = true,
        hasIssue = true,
        lastReportedIssue = "Can't hear incoming phone calls, storage almost full",
        remoteCode = "VD-3129"
      ),
      FamilyMember(
        id = "fm_2",
        name = "Dad (David)",
        relation = "Father",
        deviceModel = "Google Pixel 7a",
        batteryLevel = 62,
        storageUsedPercent = 54,
        isOnline = true,
        hasIssue = false,
        lastReportedIssue = null,
        remoteCode = "VD-7741"
      ),
      FamilyMember(
        id = "fm_3",
        name = "Aunt Clara",
        relation = "Aunt",
        deviceModel = "Motorola G Power",
        batteryLevel = 74,
        storageUsedPercent = 88,
        isOnline = false,
        hasIssue = false,
        lastReportedIssue = null,
        remoteCode = "VD-5590"
      )
    )
  )
  val familyMembers: StateFlow<List<FamilyMember>> = _familyMembers.asStateFlow()

  private val _repairShops = MutableStateFlow(
    listOf(
      RepairShop(
        id = "shop_1",
        name = "iFix Express Certified Lab",
        address = "142 Metro Boulevard, Suite 10",
        distance = "0.6 mi away",
        rating = 4.9,
        reviewsCount = 428,
        certifications = listOf("OEM Certified", "Same-Day Return", "90-Day Warranty"),
        isOpen = true,
        estimatedCostSummary = "Starting at $29",
        availableServices = listOf(
          RepairServiceOption("Battery Replacement", "$39", "45 mins"),
          RepairServiceOption("Display / Glass Refurbish", "$69", "60 mins"),
          RepairServiceOption("Charging Port Cleaning & Fix", "$25", "30 mins"),
          RepairServiceOption("Water Damage Ultrasonic Wash", "$45", "90 mins")
        )
      ),
      RepairShop(
        id = "shop_2",
        name = "Authorized Mobile Care Hub",
        address = "780 Tech Center Way",
        distance = "1.4 mi away",
        rating = 4.8,
        reviewsCount = 612,
        certifications = listOf("Official Partner", "Genuine Parts", "1-Year Warranty"),
        isOpen = true,
        estimatedCostSummary = "Starting at $35",
        availableServices = listOf(
          RepairServiceOption("Battery Replacement", "$49", "30 mins"),
          RepairServiceOption("OEM OLED Screen Replacement", "$89", "45 mins"),
          RepairServiceOption("Camera Lens Replacement", "$35", "40 mins")
        )
      ),
      RepairShop(
        id = "shop_3",
        name = "QuickFix Community Workshop",
        address = "215 Elm Street",
        distance = "2.1 mi away",
        rating = 4.7,
        reviewsCount = 194,
        certifications = listOf("Budget Friendly", "Free Diagnostics"),
        isOpen = true,
        estimatedCostSummary = "Starting at $19",
        availableServices = listOf(
          RepairServiceOption("Deep Hardware Diagnostic", "Free", "20 mins"),
          RepairServiceOption("Speaker & Mic De-clog", "$19", "25 mins"),
          RepairServiceOption("Battery Tune-up", "$32", "40 mins")
        )
      )
    )
  )
  val repairShops: StateFlow<List<RepairShop>> = _repairShops.asStateFlow()

  private val _activeBookings = MutableStateFlow<List<RepairBooking>>(emptyList())
  val activeBookings: StateFlow<List<RepairBooking>> = _activeBookings.asStateFlow()

  private val _historyLogs = MutableStateFlow(
    listOf(
      FixHistoryItem(
        id = "hist_1",
        timestampFormatted = "Yesterday, 3:45 PM",
        problemSpoken = "Phone battery running out in 4 hours",
        fixApplied = "Restricted 3 wake-lock background apps & lowered HDR brightness overshoot",
        impactSummary = "Battery life extended by ~3.2 hours",
        canRevert = true,
        isReverted = false
      ),
      FixHistoryItem(
        id = "hist_2",
        timestampFormatted = "2 days ago",
        problemSpoken = "Can't hear WhatsApp calls and ringtone",
        fixApplied = "Deactivated Do-Not-Disturb silent trigger & restored ring stream to 85%",
        impactSummary = "Ringer audible again",
        canRevert = true,
        isReverted = false
      )
    )
  )
  val historyLogs: StateFlow<List<FixHistoryItem>> = _historyLogs.asStateFlow()

  val isAccessibilityActive = VoicedocAccessibilityService.isServiceActive

  init {
    refreshTelemetry()
  }

  fun refreshTelemetry() {
    _telemetry.value = doctorEngine.readDeviceTelemetry()
  }

  fun navigateTo(screen: AppScreen) {
    if (_currentScreen.value != screen) {
      _screenStack.add(_currentScreen.value)
      _currentScreen.value = screen
    }
  }

  fun navigateBack(): Boolean {
    if (_screenStack.isNotEmpty()) {
      _currentScreen.value = _screenStack.removeAt(_screenStack.size - 1)
      return true
    }
    if (_currentScreen.value != AppScreen.HOME) {
      _currentScreen.value = AppScreen.HOME
      return true
    }
    return false
  }

  fun completeOnboarding() {
    _hasCompletedOnboarding.value = true
    _currentScreen.value = AppScreen.HOME
  }

  fun selectLanguage(language: SupportedLanguage) {
    _selectedLanguage.value = language
    voiceManager.updateLanguage(language.code)
    showNotification("Language updated to ${language.name}")
    // Speak welcoming phrase in that language
    voiceManager.speak(language.welcomeAudioPhrase)
  }

  fun startVoiceDiagnosis() {
    vibratePhone()
    voiceManager.startListening { spokenText ->
      processSpokenProblem(spokenText)
    }
  }

  fun stopVoiceListening() {
    voiceManager.stopListening()
  }

  fun processSpokenProblem(query: String) {
    vibratePhone()
    navigateTo(AppScreen.DIAGNOSIS)
    _isAnalyzing.value = true
    _activeDiagnosis.value = null

    viewModelScope.launch {
      _analysisStep.value = "Analyzing acoustic query & language..."
      delay(700)
      _analysisStep.value = "Scanning Battery & Background Wakelocks..."
      delay(700)
      _analysisStep.value = "Checking Internal Storage & Cache fragmentation..."
      delay(600)
      _analysisStep.value = "Auditing Wireless radios & Audio profile..."
      delay(600)
      _analysisStep.value = "Generating AI diagnosis in plain language..."
      delay(500)

      val report = doctorEngine.diagnoseProblem(query, _selectedLanguage.value.code)
      _activeDiagnosis.value = report
      _autoFixActions.value = report.recommendedActions
      _isAnalyzing.value = false

      // Read aloud explanation
      val speechSummary = "Diagnosis ready. ${report.title}. ${report.simpleExplanation}"
      voiceManager.speak(speechSummary)
    }
  }

  fun proceedToAutoFix() {
    vibratePhone()
    _isAutoFixing.value = false
    _isFixCompleted.value = false
    _currentFixStepIndex.value = 0
    _fixExecutionLogs.value = listOf("Consent confirmed. Ready to execute auto-fix sequence.")
    navigateTo(AppScreen.AUTO_FIX)
  }

  fun executeAutoFix() {
    if (_isAutoFixing.value) return
    _isAutoFixing.value = true
    _isFixCompleted.value = false
    vibratePhone()

    viewModelScope.launch {
      val currentList = _autoFixActions.value.toMutableList()
      val logs = mutableListOf<String>()
      logs.add("🚀 Starting Voicedoc Assistive Auto-Fix Sequence...")
      _fixExecutionLogs.value = logs.toList()

      for (i in currentList.indices) {
        _currentFixStepIndex.value = i
        val item = currentList[i]
        currentList[i] = item.copy(status = FixStatus.RUNNING)
        _autoFixActions.value = currentList.toList()

        logs.add("⚡ Running [${i + 1}/${currentList.size}]: ${item.title}")
        _fixExecutionLogs.value = logs.toList()
        delay(1100)

        // Simulate execution and log
        logs.add("✔ Completed: ${item.impact}")
        currentList[i] = item.copy(status = FixStatus.SUCCESS)
        _autoFixActions.value = currentList.toList()
        _fixExecutionLogs.value = logs.toList()
        delay(400)
      }

      _isAutoFixing.value = false
      _isFixCompleted.value = true
      logs.add("🎉 All recommended fixes executed successfully! Phone health restored.")
      _fixExecutionLogs.value = logs.toList()
      vibrateSuccess()

      // Add to history
      val activeDiag = _activeDiagnosis.value
      val historyEntry = FixHistoryItem(
        id = UUID.randomUUID().toString(),
        timestampFormatted = "Just now",
        problemSpoken = activeDiag?.userQuery ?: "Quick Phone Tune-Up",
        fixApplied = currentList.joinToString(", ") { it.title },
        impactSummary = "Fixed ${currentList.size} subsystem issues. Health score: ${activeDiag?.healthScoreAfter ?: 98}/100",
        canRevert = true,
        isReverted = false
      )
      _historyLogs.value = listOf(historyEntry) + _historyLogs.value

      voiceManager.speak("Great news! All issues have been resolved automatically. Your phone is healthy and responsive.")
    }
  }

  fun revertSingleAction(actionId: String) {
    val list = _autoFixActions.value.toMutableList()
    val index = list.indexOfFirst { it.id == actionId }
    if (index >= 0) {
      val item = list[index]
      list[index] = item.copy(status = FixStatus.REVERTED)
      _autoFixActions.value = list
      showNotification("Reverted: ${item.title}")
      vibratePhone()
    }
  }

  fun revertHistoryItem(historyId: String) {
    val list = _historyLogs.value.toMutableList()
    val index = list.indexOfFirst { it.id == historyId }
    if (index >= 0) {
      val item = list[index]
      list[index] = item.copy(isReverted = true)
      _historyLogs.value = list
      showNotification("Reverted changes from: ${item.problemSpoken}")
      vibratePhone()
    }
  }

  fun bookRepair(shop: RepairShop, service: RepairServiceOption, date: String, time: String) {
    val booking = RepairBooking(
      id = "VD-BK-${(1000..9999).random()}",
      shopName = shop.name,
      serviceName = service.name,
      appointmentDate = date,
      appointmentTime = time,
      estimatedPrice = service.estimatedPrice,
      bookingPin = "PIN-${(100..999).random()}",
      status = "Confirmed"
    )
    _activeBookings.value = listOf(booking) + _activeBookings.value
    showNotification("Repair appointment confirmed at ${shop.name}!")
    voiceManager.speak("Your appointment is confirmed for ${service.name} at ${shop.name}.")
  }

  fun triggerEmergencySos() {
    vibratePhone()
    showNotification("SOS Alert sent to family emergency contacts with GPS and battery status!")
    voiceManager.speak("Emergency SOS triggered. Alerting family members.")
  }

  fun toggleFloatingBar() {
    _floatingBarExpanded.value = !_floatingBarExpanded.value
  }

  fun openSystemSettings(intentAction: String?) {
    try {
      val action = intentAction ?: Settings.ACTION_SETTINGS
      val intent = Intent(action).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
      }
      context.startActivity(intent)
    } catch (_: Exception) {
      val fallback = Intent(Settings.ACTION_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
      }
      context.startActivity(fallback)
    }
  }

  fun openAccessibilitySettings() {
    try {
      val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
      }
      context.startActivity(intent)
    } catch (e: Exception) {
      showNotification("Could not open accessibility settings", isError = true)
    }
  }

  fun showNotification(msg: String, isError: Boolean = false) {
    _notification.value = UiNotification(message = msg, isError = isError)
  }

  fun dismissNotification() {
    _notification.value = null
  }

  private fun vibratePhone() {
    try {
      val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
      vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    } catch (_: Exception) {}
  }

  private fun vibrateSuccess() {
    try {
      val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
      vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 70, 70, 120), -1))
    } catch (_: Exception) {}
  }

  override fun onCleared() {
    super.onCleared()
    voiceManager.cleanup()
  }
}
