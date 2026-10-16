package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.SupportedLanguage
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.VoicedocAccentCyan
import com.example.ui.theme.VoicedocDeepBlue
import com.example.ui.theme.VoicedocLightBlue

@Composable
fun VoicedocHeader(
  title: String? = null,
  showBackButton: Boolean = false,
  selectedLanguage: SupportedLanguage? = null,
  onBackClick: () -> Unit = {},
  onLanguageClick: () -> Unit = {},
  onSettingsClick: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .statusBarsPadding()
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    // Left side: Back Button OR Voicedoc Brand Logo
    if (showBackButton) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
          onClick = onBackClick,
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color(0xFF1E293B))
            .border(1.dp, GlassBorder, CircleShape)
            .testTag("header_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.White
          )
        }
        if (title != null) {
          Spacer(modifier = Modifier.width(12.dp))
          Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    } else {
      // Brand Logo + Typography
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onSettingsClick() }
      ) {
        // Rounded Pill Icon in Blue Gradient with white stylized mic and sound waves
        Box(
          modifier = Modifier
            .size(42.dp, 36.dp)
            .shadow(6.dp, RoundedCornerShape(18.dp), spotColor = VoicedocLightBlue)
            .clip(RoundedCornerShape(18.dp))
            .background(
              Brush.linearGradient(
                colors = listOf(VoicedocDeepBlue, VoicedocLightBlue)
              )
            )
            .border(1.2.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(18.dp)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Filled.Mic,
            contentDescription = "Voicedoc Capsule",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
          Text(
            text = "VOICEDOC",
            color = Color.White,
            fontSize = 19.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.2.sp
          )
          Text(
            text = "AI Phone Support Agent",
            color = VoicedocLightBlue,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }

    // Right side: Language Selector Pill & Settings Icon
    Row(verticalAlignment = Alignment.CenterVertically) {
      if (selectedLanguage != null) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF1E293B))
            .border(1.dp, GlassBorder, RoundedCornerShape(18.dp))
            .clickable { onLanguageClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("language_selector_pill"),
          contentAlignment = Alignment.Center
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = selectedLanguage.flagEmoji,
              fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = selectedLanguage.code.uppercase(),
              color = Color.White,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      IconButton(
        onClick = onSettingsClick,
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(Color(0xFF1E293B).copy(alpha = 0.7f))
          .border(1.dp, GlassBorder.copy(alpha = 0.5f), CircleShape)
          .testTag("header_settings_button")
      ) {
        Icon(
          imageVector = Icons.Filled.Settings,
          contentDescription = "Settings",
          tint = VoicedocLightBlue,
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}
