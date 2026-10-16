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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LanguageCatalog
import com.example.data.model.SupportedLanguage
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.VoicedocHeader
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.VoicedocAccentCyan
import com.example.ui.theme.VoicedocCardDark
import com.example.ui.theme.VoicedocDarkNavy
import com.example.ui.theme.VoicedocDeepBlue
import com.example.ui.theme.VoicedocLightBlue
import com.example.ui.theme.VoicedocSuccessGreen
import com.example.ui.theme.VoicedocWarningAmber

@Composable
fun SettingsScreen(
  selectedLanguage: SupportedLanguage,
  isAccessibilityActive: Boolean,
  onLanguageSelected: (SupportedLanguage) -> Unit,
  onOpenAccessibilitySettings: () -> Unit,
  onTestVoice: (String) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isPrivacyModeOn by remember { mutableStateOf(true) }
  var isVoiceFeedbackOn by remember { mutableStateOf(true) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(VoicedocDarkNavy)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 20.dp)
    ) {
      VoicedocHeader(
        title = "Settings & Accessibility",
        showBackButton = true,
        onBackClick = onBack
      )

      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Accessibility Service Engine
        item {
          Text(
            text = "SYSTEM ACCESSIBILITY & AUTOMATION",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        }

        item {
          GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 20.dp,
            borderColor = if (isAccessibilityActive) VoicedocSuccessGreen.copy(alpha = 0.5f) else VoicedocWarningAmber.copy(alpha = 0.5f),
            onClick = onOpenAccessibilitySettings
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                      if (isAccessibilityActive) VoicedocSuccessGreen.copy(alpha = 0.2f) else VoicedocWarningAmber.copy(alpha = 0.2f)
                    ),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Filled.AccessibilityNew,
                    contentDescription = "Accessibility",
                    tint = if (isAccessibilityActive) VoicedocSuccessGreen else VoicedocWarningAmber,
                    modifier = Modifier.size(22.dp)
                  )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                  Text(
                    text = "Voicedoc Assistive Service",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = if (isAccessibilityActive) "Connected & Supervising Auto-Fixes" else "Tap to Enable in Android Settings",
                    color = if (isAccessibilityActive) VoicedocSuccessGreen else VoicedocWarningAmber,
                    fontSize = 12.sp
                  )
                }
              }

              Icon(
                imageVector = Icons.Filled.OpenInNew,
                contentDescription = "Open",
                tint = VoicedocLightBlue,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }

        // Language Selection & Voice Engine
        item {
          Text(
            text = "SPOKEN LANGUAGE & DIALECT",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        }

        items(LanguageCatalog.LANGUAGES) { lang ->
          val isSelected = lang.code == selectedLanguage.code
          GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 16.dp,
            backgroundColor = if (isSelected) VoicedocDeepBlue.copy(alpha = 0.5f) else VoicedocCardDark.copy(alpha = 0.7f),
            borderColor = if (isSelected) VoicedocLightBlue else GlassBorder,
            onClick = {
              onLanguageSelected(lang)
            }
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = lang.flagEmoji, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(
                    text = "${lang.nativeName} (${lang.name})",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = lang.welcomeAudioPhrase,
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    maxLines = 1
                  )
                }
              }

              if (isSelected) {
                Icon(
                  imageVector = Icons.Filled.Check,
                  contentDescription = "Active",
                  tint = VoicedocLightBlue,
                  modifier = Modifier.size(20.dp)
                )
              }
            }
          }
        }

        // Privacy & Preferences
        item {
          Text(
            text = "PRIVACY & FEEDBACK CONTROLS",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        }

        item {
          GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 18.dp
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = "On-Device Privacy Shield",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "Processes diagnostics locally without transmitting personal data.",
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp
                  )
                }
                Switch(
                  checked = isPrivacyModeOn,
                  onCheckedChange = { isPrivacyModeOn = it },
                  colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = VoicedocLightBlue
                  )
                )
              }

              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = "Audio Voice Feedback",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "Reads out plain-language explanations aloud using natural TTS.",
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp
                  )
                }
                Switch(
                  checked = isVoiceFeedbackOn,
                  onCheckedChange = { isVoiceFeedbackOn = it },
                  colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = VoicedocLightBlue
                  )
                )
              }
            }
          }
        }

        // About Voicedoc
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(18.dp))
              .background(VoicedocDeepBlue.copy(alpha = 0.3f))
              .border(1.dp, GlassBorder, RoundedCornerShape(18.dp))
              .padding(16.dp)
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
              Box(
                modifier = Modifier
                  .size(46.dp, 36.dp)
                  .clip(RoundedCornerShape(18.dp))
                  .background(
                    Brush.linearGradient(
                      colors = listOf(VoicedocDeepBlue, VoicedocLightBlue)
                    )
                  ),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Filled.Mic,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "VOICEDOC",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
              )
              Text(
                text = "AI Phone Support Agent • v1.0",
                color = VoicedocLightBlue,
                fontSize = 12.sp
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Your digital friend ('Sathi') for diagnosing and repairing smartphones.",
                color = Color(0xFF94A3B8),
                fontSize = 11.sp
              )
            }
          }
        }
      }
    }
  }
}
