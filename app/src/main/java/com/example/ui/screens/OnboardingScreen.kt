package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.LanguageCatalog
import com.example.data.model.SupportedLanguage
import com.example.ui.components.GlassmorphicCard
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.VoicedocAccentCyan
import com.example.ui.theme.VoicedocCardDark
import com.example.ui.theme.VoicedocDarkNavy
import com.example.ui.theme.VoicedocDeepBlue
import com.example.ui.theme.VoicedocLightBlue
import com.example.ui.theme.VoicedocSuccessGreen

@Composable
fun OnboardingScreen(
  selectedLanguage: SupportedLanguage,
  onLanguageSelected: (SupportedLanguage) -> Unit,
  onComplete: () -> Unit,
  onTestVoice: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var currentStep by remember { mutableIntStateOf(0) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(VoicedocDarkNavy)
      .statusBarsPadding()
      .padding(horizontal = 20.dp, vertical = 16.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // Step Indicator Dots
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
        horizontalArrangement = Arrangement.Center
      ) {
        repeat(3) { index ->
          Box(
            modifier = Modifier
              .padding(horizontal = 4.dp)
              .height(6.dp)
              .width(if (index == currentStep) 28.dp else 8.dp)
              .clip(CircleShape)
              .background(
                if (index == currentStep) VoicedocLightBlue else Color(0xFF334155)
              )
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Animated Step Content
      AnimatedContent(
        targetState = currentStep,
        label = "OnboardingSteps",
        modifier = Modifier.weight(1f)
      ) { step ->
        when (step) {
          0 -> StepLanguage(
            selectedLanguage = selectedLanguage,
            onSelect = { lang ->
              onLanguageSelected(lang)
            }
          )
          1 -> StepPermissions()
          else -> StepVoiceIntro(
            selectedLanguage = selectedLanguage,
            onTestVoice = onTestVoice
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Primary Next / Finish Button
      Button(
        onClick = {
          if (currentStep < 2) {
            currentStep++
          } else {
            onComplete()
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(58.dp)
          .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = VoicedocLightBlue)
          .testTag("onboarding_primary_button"),
        colors = ButtonDefaults.buttonColors(
          containerColor = VoicedocDeepBlue
        ),
        shape = RoundedCornerShape(20.dp)
      ) {
        Text(
          text = if (currentStep < 2) "Continue" else "Start Using Voicedoc",
          color = Color.White,
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

@Composable
private fun StepLanguage(
  selectedLanguage: SupportedLanguage,
  onSelect: (SupportedLanguage) -> Unit
) {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Brand Pill Logo
    Box(
      modifier = Modifier
        .size(64.dp, 48.dp)
        .clip(RoundedCornerShape(24.dp))
        .background(
          Brush.linearGradient(
            colors = listOf(VoicedocDeepBlue, VoicedocLightBlue)
          )
        ),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Filled.Mic,
        contentDescription = "Voicedoc",
        tint = Color.White,
        modifier = Modifier.size(28.dp)
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Choose Your Language",
      color = Color.White,
      fontSize = 24.sp,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = "Speak naturally in your mother tongue. Voicedoc diagnoses in real-time.",
      color = Color(0xFF94A3B8),
      fontSize = 14.sp,
      textAlign = TextAlign.Center,
      lineHeight = 20.sp
    )

    Spacer(modifier = Modifier.height(20.dp))

    LazyColumn(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(LanguageCatalog.LANGUAGES) { lang ->
        val isSelected = lang.code == selectedLanguage.code
        GlassmorphicCard(
          modifier = Modifier.fillMaxWidth(),
          cornerRadius = 18.dp,
          backgroundColor = if (isSelected) VoicedocDeepBlue.copy(alpha = 0.5f) else VoicedocCardDark.copy(alpha = 0.7f),
          borderColor = if (isSelected) VoicedocLightBlue else GlassBorder,
          onClick = { onSelect(lang) }
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = lang.flagEmoji,
                fontSize = 24.sp
              )
              Spacer(modifier = Modifier.width(14.dp))
              Column {
                Text(
                  text = lang.nativeName,
                  color = Color.White,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = lang.name,
                  color = Color(0xFF94A3B8),
                  fontSize = 12.sp
                )
              }
            }

            if (isSelected) {
              Box(
                modifier = Modifier
                  .size(28.dp)
                  .clip(CircleShape)
                  .background(VoicedocLightBlue),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Filled.Check,
                  contentDescription = "Selected",
                  tint = VoicedocDarkNavy,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun StepPermissions() {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .size(60.dp)
        .clip(CircleShape)
        .background(VoicedocLightBlue.copy(alpha = 0.2f)),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Filled.Security,
        contentDescription = "Permissions",
        tint = VoicedocLightBlue,
        modifier = Modifier.size(32.dp)
      )
    }

    Spacer(modifier = Modifier.height(14.dp))

    Text(
      text = "Safe & Supervised Access",
      color = Color.White,
      fontSize = 22.sp,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = "Voicedoc fixes problems automatically with your explicit consent.",
      color = Color(0xFF94A3B8),
      fontSize = 14.sp,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(20.dp))

    PermissionItem(
      icon = Icons.Filled.Mic,
      title = "Microphone Access",
      desc = "To understand your voice when you speak your phone problem."
    )

    Spacer(modifier = Modifier.height(12.dp))

    PermissionItem(
      icon = Icons.Filled.AccessibilityNew,
      title = "Android Accessibility Service",
      desc = "Allows Voicedoc to automate stuck settings, clear frozen cache, and fix DND silences with 1 tap."
    )

    Spacer(modifier = Modifier.height(12.dp))

    PermissionItem(
      icon = Icons.Filled.Security,
      title = "Zero Privacy Intrusion",
      desc = "No personal messages, photos, or banking details are accessed. Every fix has an instant Undo."
    )
  }
}

@Composable
private fun PermissionItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  desc: String
) {
  GlassmorphicCard(
    modifier = Modifier.fillMaxWidth(),
    cornerRadius = 18.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.Top
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(VoicedocDeepBlue.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = VoicedocAccentCyan,
          modifier = Modifier.size(22.dp)
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          color = Color.White,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = desc,
          color = Color(0xFF94A3B8),
          fontSize = 12.sp,
          lineHeight = 16.sp
        )
      }
    }
  }
}

@Composable
private fun StepVoiceIntro(
  selectedLanguage: SupportedLanguage,
  onTestVoice: (String) -> Unit
) {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    // Beautiful Family support image generated
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(180.dp)
        .clip(RoundedCornerShape(24.dp))
        .border(1.5.dp, GlassBorder, RoundedCornerShape(24.dp))
    ) {
      Image(
        painter = painterResource(id = R.drawable.img_family_support),
        contentDescription = "Family Support",
        modifier = Modifier.fillMaxSize()
      )
    }

    Spacer(modifier = Modifier.height(20.dp))

    Text(
      text = "Meet Voicedoc: Your Digital Sathi",
      color = Color.White,
      fontSize = 22.sp,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "Whether you're helping your parents or fixing your own phone, just press the mic and speak. No technical jargon required.",
      color = Color(0xFF94A3B8),
      fontSize = 14.sp,
      textAlign = TextAlign.Center,
      lineHeight = 20.sp
    )

    Spacer(modifier = Modifier.height(20.dp))

    // Interactive Voice Preview Card
    GlassmorphicCard(
      modifier = Modifier.fillMaxWidth(),
      cornerRadius = 18.dp,
      borderColor = VoicedocLightBlue.copy(alpha = 0.5f),
      onClick = { onTestVoice(selectedLanguage.welcomeAudioPhrase) }
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(VoicedocLightBlue),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Filled.VolumeUp,
            contentDescription = "Test Voice",
            tint = VoicedocDarkNavy,
            modifier = Modifier.size(24.dp)
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Hear Voice Greeting (${selectedLanguage.name})",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = selectedLanguage.welcomeAudioPhrase,
            color = VoicedocLightBlue,
            fontSize = 12.sp,
            maxLines = 1
          )
        }
      }
    }
  }
}
