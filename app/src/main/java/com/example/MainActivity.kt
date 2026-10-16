package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.AutoFixScreen
import com.example.ui.screens.DiagnosisScreen
import com.example.ui.screens.FamilyModeScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.RepairBookingScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.VoiceHomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.VoicedocCardDark
import com.example.ui.theme.VoicedocDarkNavy
import com.example.ui.theme.VoicedocErrorRed
import com.example.ui.theme.VoicedocLightBlue
import com.example.ui.theme.VoicedocSuccessGreen
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.VoicedocViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        VoicedocApp()
      }
    }
  }
}

@Composable
fun VoicedocApp(viewModel: VoicedocViewModel = viewModel()) {
  val context = LocalContext.current
  val currentScreen by viewModel.currentScreen.collectAsState()
  val hasCompletedOnboarding by viewModel.hasCompletedOnboarding.collectAsState()
  val selectedLanguage by viewModel.selectedLanguage.collectAsState()
  val telemetry by viewModel.telemetry.collectAsState()
  val activeDiagnosis by viewModel.activeDiagnosis.collectAsState()
  val isAnalyzing by viewModel.isAnalyzing.collectAsState()
  val analysisStep by viewModel.analysisStep.collectAsState()
  val autoFixActions by viewModel.autoFixActions.collectAsState()
  val isAutoFixing by viewModel.isAutoFixing.collectAsState()
  val isFixCompleted by viewModel.isFixCompleted.collectAsState()
  val currentFixStepIndex by viewModel.currentFixStepIndex.collectAsState()
  val fixLogs by viewModel.fixExecutionLogs.collectAsState()
  val floatingBarExpanded by viewModel.floatingBarExpanded.collectAsState()
  val familyMembers by viewModel.familyMembers.collectAsState()
  val repairShops by viewModel.repairShops.collectAsState()
  val activeBookings by viewModel.activeBookings.collectAsState()
  val historyLogs by viewModel.historyLogs.collectAsState()
  val notification by viewModel.notification.collectAsState()
  val isAccessibilityActive by viewModel.isAccessibilityActive.collectAsState()

  val voiceManager = viewModel.voiceManager
  val isListening by voiceManager.isListening.collectAsState()
  val isSpeaking by voiceManager.isSpeaking.collectAsState()
  val audioAmplitude by voiceManager.audioAmplitude.collectAsState()

  // Permission launcher for Microphone
  val micPermissionLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    if (isGranted) {
      viewModel.startVoiceDiagnosis()
    } else {
      viewModel.showNotification("Microphone permission needed to speak with Voicedoc", isError = true)
    }
  }

  fun requestMicAndListen() {
    val hasPermission = ContextCompat.checkSelfPermission(
      context,
      Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED

    if (hasPermission) {
      if (isListening) {
        viewModel.stopVoiceListening()
      } else {
        viewModel.startVoiceDiagnosis()
      }
    } else {
      micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
  }

  // Handle hardware Back button
  BackHandler(enabled = currentScreen != AppScreen.HOME && hasCompletedOnboarding) {
    viewModel.navigateBack()
  }

  // Auto-dismiss notification after 3.5s
  LaunchedEffect(notification) {
    if (notification != null) {
      delay(3500)
      viewModel.dismissNotification()
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = VoicedocDarkNavy
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      // Screen Flow Router
      if (!hasCompletedOnboarding) {
        OnboardingScreen(
          selectedLanguage = selectedLanguage,
          onLanguageSelected = { viewModel.selectLanguage(it) },
          onComplete = { viewModel.completeOnboarding() },
          onTestVoice = { phrase -> viewModel.voiceManager.speak(phrase) }
        )
      } else {
        AnimatedContent(
          targetState = currentScreen,
          label = "ScreenTransition"
        ) { screen ->
          when (screen) {
            AppScreen.ONBOARDING -> {
              OnboardingScreen(
                selectedLanguage = selectedLanguage,
                onLanguageSelected = { viewModel.selectLanguage(it) },
                onComplete = { viewModel.completeOnboarding() },
                onTestVoice = { phrase -> viewModel.voiceManager.speak(phrase) }
              )
            }
            AppScreen.HOME -> {
              VoiceHomeScreen(
                selectedLanguage = selectedLanguage,
                telemetry = telemetry,
                familyMembers = familyMembers,
                isListening = isListening,
                isSpeaking = isSpeaking,
                audioAmplitude = audioAmplitude,
                floatingBarExpanded = floatingBarExpanded,
                onOrbClick = { requestMicAndListen() },
                onLanguageClick = { viewModel.navigateTo(AppScreen.SETTINGS) },
                onSettingsClick = { viewModel.navigateTo(AppScreen.SETTINGS) },
                onQuickPromptSelected = { query -> viewModel.processSpokenProblem(query) },
                onOpenDiagnose = { viewModel.processSpokenProblem("Full System Tune-up") },
                onOpenFamily = { viewModel.navigateTo(AppScreen.FAMILY_MODE) },
                onOpenRepair = { viewModel.navigateTo(AppScreen.REPAIR_BOOKING) },
                onOpenHistory = { viewModel.navigateTo(AppScreen.HISTORY) },
                onToggleFloatingBar = { viewModel.toggleFloatingBar() },
                onOpenAccessibility = { viewModel.openAccessibilitySettings() }
              )
            }
            AppScreen.DIAGNOSIS -> {
              DiagnosisScreen(
                report = activeDiagnosis,
                isAnalyzing = isAnalyzing,
                analysisStep = analysisStep,
                onProceedToAutoFix = { viewModel.proceedToAutoFix() },
                onReplayAudio = {
                  val explanation = activeDiagnosis?.simpleExplanation ?: "Diagnosis ready."
                  viewModel.voiceManager.speak(explanation)
                },
                onBack = { viewModel.navigateBack() }
              )
            }
            AppScreen.AUTO_FIX -> {
              AutoFixScreen(
                actions = autoFixActions,
                isAutoFixing = isAutoFixing,
                isFixCompleted = isFixCompleted,
                currentFixStepIndex = currentFixStepIndex,
                executionLogs = fixLogs,
                isAccessibilityActive = isAccessibilityActive,
                onExecuteAutoFix = { viewModel.executeAutoFix() },
                onRevertAction = { actionId -> viewModel.revertSingleAction(actionId) },
                onOpenSystemSetting = { intent -> viewModel.openSystemSettings(intent) },
                onOpenAccessibilitySettings = { viewModel.openAccessibilitySettings() },
                onBack = { viewModel.navigateBack() }
              )
            }
            AppScreen.FAMILY_MODE -> {
              FamilyModeScreen(
                familyMembers = familyMembers,
                remoteCode = viewModel.remoteHelpCode,
                onDiagnoseRemoteMember = { member ->
                  viewModel.processSpokenProblem(member.lastReportedIssue ?: "Diagnose ${member.name}'s phone")
                },
                onTriggerSos = { viewModel.triggerEmergencySos() },
                onShowMessage = { msg -> viewModel.showNotification(msg) },
                onBack = { viewModel.navigateBack() }
              )
            }
            AppScreen.REPAIR_BOOKING -> {
              RepairBookingScreen(
                repairShops = repairShops,
                activeBookings = activeBookings,
                onBookService = { shop, service, date, time ->
                  viewModel.bookRepair(shop, service, date, time)
                },
                onBack = { viewModel.navigateBack() }
              )
            }
            AppScreen.SETTINGS -> {
              SettingsScreen(
                selectedLanguage = selectedLanguage,
                isAccessibilityActive = isAccessibilityActive,
                onLanguageSelected = { viewModel.selectLanguage(it) },
                onOpenAccessibilitySettings = { viewModel.openAccessibilitySettings() },
                onTestVoice = { phrase -> viewModel.voiceManager.speak(phrase) },
                onBack = { viewModel.navigateBack() }
              )
            }
            AppScreen.HISTORY -> {
              HistoryScreen(
                historyLogs = historyLogs,
                onRevertFix = { historyId -> viewModel.revertHistoryItem(historyId) },
                onBack = { viewModel.navigateBack() }
              )
            }
          }
        }
      }

      // Animated Notification Toast Banner
      AnimatedVisibility(
        visible = notification != null,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = Modifier
          .align(Alignment.TopCenter)
          .statusBarsPadding()
          .padding(top = 16.dp, start = 20.dp, end = 20.dp)
      ) {
        notification?.let { notif ->
          Box(
            modifier = Modifier
              .shadow(12.dp, RoundedCornerShape(16.dp))
              .clip(RoundedCornerShape(16.dp))
              .background(if (notif.isError) VoicedocErrorRed else VoicedocCardDark)
              .border(
                1.dp,
                if (notif.isError) Color.White.copy(alpha = 0.5f) else VoicedocLightBlue.copy(alpha = 0.5f),
                RoundedCornerShape(16.dp)
              )
              .padding(horizontal = 16.dp, vertical = 12.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = if (notif.isError) Icons.Filled.ErrorOutline else Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = if (notif.isError) Color.White else VoicedocSuccessGreen,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = notif.message,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }
    }
  }
}
