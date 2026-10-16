package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryStd
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FamilyMember
import com.example.data.model.SupportedLanguage
import com.example.service.RealDeviceTelemetry
import com.example.ui.components.BentoCard
import com.example.ui.components.FloatingCommandBar
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.VoicedocHeader
import com.example.ui.components.VoiceOrb
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.VoicedocAccentCyan
import com.example.ui.theme.VoicedocCardDark
import com.example.ui.theme.VoicedocDarkNavy
import com.example.ui.theme.VoicedocDeepBlue
import com.example.ui.theme.VoicedocLightBlue
import com.example.ui.theme.VoicedocPurpleAccent
import com.example.ui.theme.VoicedocSkyBlue
import com.example.ui.theme.VoicedocSuccessGreen
import com.example.ui.theme.VoicedocWarningAmber

@Composable
fun VoiceHomeScreen(
  selectedLanguage: SupportedLanguage,
  telemetry: RealDeviceTelemetry,
  familyMembers: List<FamilyMember>,
  isListening: Boolean,
  isSpeaking: Boolean,
  audioAmplitude: Float,
  floatingBarExpanded: Boolean,
  onOrbClick: () -> Unit,
  onLanguageClick: () -> Unit,
  onSettingsClick: () -> Unit,
  onQuickPromptSelected: (String) -> Unit,
  onOpenDiagnose: () -> Unit,
  onOpenFamily: () -> Unit,
  onOpenRepair: () -> Unit,
  onOpenHistory: () -> Unit,
  onToggleFloatingBar: () -> Unit,
  onOpenAccessibility: () -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  val sampleQueries = listOf(
    "Battery drains in 3 hours" to "⚡ Battery",
    "Can't hear phone calls or ringtone" to "🔊 Sound",
    "Storage full, WhatsApp failing" to "💾 Storage",
    "Bluetooth earbuds won't connect" to "📶 Bluetooth",
    "Screen is too bright and text too small" to "👁 Vision",
    "Phone is frozen on black screen" to "❄ Freeze"
  )

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(VoicedocDarkNavy)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(bottom = 90.dp)
    ) {
      // Brand Header with Pill Logo
      VoicedocHeader(
        selectedLanguage = selectedLanguage,
        onLanguageClick = onLanguageClick,
        onSettingsClick = onSettingsClick
      )

      // Live Telemetry Health Bar
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        TelemetryChip(
          icon = Icons.Filled.BatteryStd,
          label = "${telemetry.batteryPercentage}%",
          sublabel = if (telemetry.isCharging) "Charging" else "Healthy",
          color = if (telemetry.batteryPercentage < 20) VoicedocWarningAmber else VoicedocSuccessGreen,
          modifier = Modifier.weight(1f)
        )
        TelemetryChip(
          icon = Icons.Filled.Storage,
          label = "${telemetry.freeStorageGb} GB",
          sublabel = "Free Space",
          color = if (telemetry.isStorageLow) VoicedocWarningAmber else VoicedocAccentCyan,
          modifier = Modifier.weight(1f)
        )
        TelemetryChip(
          icon = Icons.Filled.NetworkCheck,
          label = if (telemetry.isOnline) "Connected" else "Offline",
          sublabel = telemetry.networkType.take(12),
          color = if (telemetry.isOnline) VoicedocLightBlue else VoicedocWarningAmber,
          modifier = Modifier.weight(1.2f)
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Central Hero Section: Voice Orb + Spoken Prompt
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = if (isListening) "Listening to you..." else if (isSpeaking) "Voicedoc is speaking..." else "Speak Your Problem",
          color = Color.White,
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = if (isListening) "Speak in any language. Tap again to finish." else "Tap the blue orb and describe what's wrong with your phone in your own words.",
          color = if (isListening) VoicedocAccentCyan else Color(0xFF94A3B8),
          fontSize = 13.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Glowing Neumorphic Voice Orb
        VoiceOrb(
          isListening = isListening,
          isSpeaking = isSpeaking,
          audioAmplitude = audioAmplitude,
          onClick = onOrbClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Suggestion Prompts
        Text(
          text = "OR TAP A COMMON PROBLEM",
          color = Color(0xFF64748B),
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(sampleQueries) { (query, tag) ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(VoicedocCardDark.copy(alpha = 0.8f))
                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                .clickable { onQuickPromptSelected(query) }
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .testTag("quick_prompt_${tag.filter { it.isLetter() }}")
            ) {
              Text(
                text = tag,
                color = VoicedocSkyBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      // Bento UI Dashboard Grid
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
      ) {
        Text(
          text = "DIAGNOSTIC & ASSIST SERVICES",
          color = Color(0xFF94A3B8),
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Bento Row 1
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          BentoCard(
            title = "Diagnose Phone",
            subtitle = "Deep acoustic & hardware scan for hidden software bugs",
            icon = Icons.Filled.MedicalServices,
            badgeText = "AI Engine",
            accentColor = VoicedocLightBlue,
            testTag = "bento_diagnose_card",
            onClick = onOpenDiagnose,
            modifier = Modifier.weight(1f)
          )

          val issuesCount = familyMembers.count { it.hasIssue }
          BentoCard(
            title = "Family Mode",
            subtitle = "Connect to parents' phones & fix issues remotely via web",
            icon = Icons.Filled.Group,
            badgeText = if (issuesCount > 0) "$issuesCount Needs Help" else "PIN: VD-8492",
            accentColor = if (issuesCount > 0) VoicedocWarningAmber else VoicedocAccentCyan,
            testTag = "bento_family_card",
            onClick = onOpenFamily,
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Bento Row 2
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          BentoCard(
            title = "Repair Booking",
            subtitle = "Certified walk-in labs nearby with transparent pricing",
            icon = Icons.Filled.Build,
            badgeText = "0.6 mi away",
            accentColor = VoicedocPurpleAccent,
            testTag = "bento_repair_card",
            onClick = onOpenRepair,
            modifier = Modifier.weight(1f)
          )

          BentoCard(
            title = "Fix History",
            subtitle = "Review past optimizations with 1-tap instant Undo",
            icon = Icons.Filled.History,
            badgeText = "Safe Logs",
            accentColor = VoicedocSuccessGreen,
            testTag = "bento_history_card",
            onClick = onOpenHistory,
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    // Floating Command Bar docked at the bottom (Superhuman style)
    FloatingCommandBar(
      isExpanded = floatingBarExpanded,
      onToggleExpand = onToggleFloatingBar,
      onQuickVoice = onOrbClick,
      onQuickTuneUp = onOpenDiagnose,
      onOpenAccessibility = onOpenAccessibility,
      modifier = Modifier.align(Alignment.BottomCenter)
    )
  }
}

@Composable
private fun TelemetryChip(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  sublabel: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  GlassmorphicCard(
    modifier = modifier,
    cornerRadius = 16.dp,
    borderColor = color.copy(alpha = 0.25f)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(28.dp)
          .clip(CircleShape)
          .background(color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = label,
          tint = color,
          modifier = Modifier.size(16.dp)
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      Column {
        Text(
          text = label,
          color = Color.White,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = sublabel,
          color = Color(0xFF94A3B8),
          fontSize = 10.sp
        )
      }
    }
  }
}
