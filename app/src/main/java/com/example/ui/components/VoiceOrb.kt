package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.OrbGlowInner
import com.example.ui.theme.OrbGlowOuter
import com.example.ui.theme.VoicedocAccentCyan
import com.example.ui.theme.VoicedocDeepBlue
import com.example.ui.theme.VoicedocLightBlue

@Composable
fun VoiceOrb(
  isListening: Boolean,
  isSpeaking: Boolean,
  audioAmplitude: Float,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  orbSize: Dp = 190.dp
) {
  val infiniteTransition = rememberInfiniteTransition(label = "VoiceOrbPulsing")

  // Gentle idle breathing
  val idleScale by infiniteTransition.animateFloat(
    initialValue = 0.96f,
    targetValue = 1.04f,
    animationSpec = infiniteRepeatable(
      animation = tween(2200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "idleScale"
  )

  // Rotating light angle for soft neumorphism / glow
  val rotationAngle by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(8000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "rotationAngle"
  )

  // Wave ripple expansion when active
  val rippleScale by infiniteTransition.animateFloat(
    initialValue = 1.0f,
    targetValue = 1.55f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "rippleScale"
  )

  val activePulseScale = when {
    isListening -> 1.0f + (audioAmplitude * 0.25f)
    isSpeaking -> 1.0f + (audioAmplitude * 0.18f)
    else -> idleScale
  }

  Box(
    modifier = modifier.size(orbSize * 1.5f),
    contentAlignment = Alignment.Center
  ) {
    // Outer animated ripples when listening or speaking
    if (isListening || isSpeaking) {
      Box(
        modifier = Modifier
          .size(orbSize)
          .scale(rippleScale)
          .clip(CircleShape)
          .background(
            Brush.radialGradient(
              colors = listOf(
                VoicedocLightBlue.copy(alpha = 0.25f),
                VoicedocAccentCyan.copy(alpha = 0.08f),
                Color.Transparent
              )
            )
          )
      )

      Box(
        modifier = Modifier
          .size(orbSize)
          .scale(rippleScale * 0.75f)
          .clip(CircleShape)
          .border(1.5.dp, VoicedocLightBlue.copy(alpha = 0.4f), CircleShape)
      )
    }

    // Outer soft glow shadow halo
    Canvas(modifier = Modifier.size(orbSize * 1.25f)) {
      val center = Offset(size.width / 2, size.height / 2)
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(
            OrbGlowInner.copy(alpha = if (isListening) 0.5f else 0.25f),
            OrbGlowOuter.copy(alpha = if (isListening) 0.2f else 0.08f),
            Color.Transparent
          ),
          center = center,
          radius = size.width / 2
        ),
        radius = size.width / 2,
        center = center
      )
    }

    // Central Neumorphic Pill/Orb Body
    Box(
      modifier = Modifier
        .size(orbSize)
        .scale(activePulseScale)
        .shadow(
          elevation = if (isListening) 24.dp else 12.dp,
          shape = CircleShape,
          spotColor = VoicedocLightBlue,
          ambientColor = VoicedocDeepBlue
        )
        .clip(CircleShape)
        .background(
          Brush.linearGradient(
            colors = listOf(
              VoicedocDeepBlue,
              Color(0xFF1E40AF),
              VoicedocLightBlue
            ),
            start = Offset(0f, 0f),
            end = Offset(400f, 400f)
          )
        )
        .border(2.dp, Brush.sweepGradient(listOf(VoicedocLightBlue, VoicedocAccentCyan, Color.White, VoicedocLightBlue)), CircleShape)
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = ripple(bounded = true, radius = orbSize / 2),
          onClick = onClick
        )
        .testTag("voice_orb_button"),
      contentAlignment = Alignment.Center
    ) {
      // Inner subtle glass reflection layer
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color.White.copy(alpha = 0.22f),
                Color.Transparent,
                Color.Black.copy(alpha = 0.25f)
              )
            )
          )
      )

      // Center Icon or Dynamic Waveform
      when {
        isListening -> {
          Icon(
            imageVector = Icons.Filled.GraphicEq,
            contentDescription = "Voicedoc is listening",
            tint = Color.White,
            modifier = Modifier.size(68.dp)
          )
        }
        isSpeaking -> {
          Icon(
            imageVector = Icons.Filled.Stop,
            contentDescription = "Stop speaking",
            tint = Color.White,
            modifier = Modifier.size(64.dp)
          )
        }
        else -> {
          Icon(
            imageVector = Icons.Filled.Mic,
            contentDescription = "Tap to speak to Voicedoc",
            tint = Color.White,
            modifier = Modifier.size(64.dp)
          )
        }
      }
    }
  }
}
