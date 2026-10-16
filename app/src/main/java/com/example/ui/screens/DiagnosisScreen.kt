package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActionSeverity
import com.example.data.model.DiagnosticReport
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.VoicedocHeader
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.VoicedocAccentCyan
import com.example.ui.theme.VoicedocCardDark
import com.example.ui.theme.VoicedocDarkNavy
import com.example.ui.theme.VoicedocDeepBlue
import com.example.ui.theme.VoicedocErrorRed
import com.example.ui.theme.VoicedocLightBlue
import com.example.ui.theme.VoicedocSkyBlue
import com.example.ui.theme.VoicedocSuccessGreen
import com.example.ui.theme.VoicedocWarningAmber

@Composable
fun DiagnosisScreen(
  report: DiagnosticReport?,
  isAnalyzing: Boolean,
  analysisStep: String,
  onProceedToAutoFix: () -> Unit,
  onReplayAudio: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
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
        title = "AI Diagnosis",
        showBackButton = true,
        onBackClick = onBack
      )

      if (isAnalyzing || report == null) {
        AnalyzingView(analysisStep = analysisStep)
      } else {
        DiagnosisResultView(
          report = report,
          onProceedToAutoFix = onProceedToAutoFix,
          onReplayAudio = onReplayAudio
        )
      }
    }
  }
}

@Composable
private fun AnalyzingView(analysisStep: String) {
  val infiniteTransition = rememberInfiniteTransition(label = "AnalyzingPulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.92f,
    targetValue = 1.08f,
    animationSpec = infiniteRepeatable(
      animation = tween(900, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseScale"
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Box(
      modifier = Modifier
        .size(130.dp)
        .scale(pulseScale)
        .clip(CircleShape)
        .background(
          Brush.radialGradient(
            colors = listOf(VoicedocLightBlue.copy(alpha = 0.35f), Color.Transparent)
          )
        ),
      contentAlignment = Alignment.Center
    ) {
      CircularProgressIndicator(
        modifier = Modifier.size(80.dp),
        color = VoicedocLightBlue,
        trackColor = Color(0xFF1E293B),
        strokeWidth = 6.dp
      )
    }

    Spacer(modifier = Modifier.height(28.dp))

    Text(
      text = "Voicedoc Doctor at Work",
      color = Color.White,
      fontSize = 22.sp,
      fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = analysisStep,
      color = VoicedocAccentCyan,
      fontSize = 14.sp,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(24.dp))

    LinearProgressIndicator(
      modifier = Modifier
        .fillMaxWidth(0.7f)
        .height(6.dp)
        .clip(RoundedCornerShape(3.dp)),
      color = VoicedocLightBlue,
      trackColor = Color(0xFF1E293B)
    )
  }
}

@Composable
private fun DiagnosisResultView(
  report: DiagnosticReport,
  onProceedToAutoFix: () -> Unit,
  onReplayAudio: () -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Problem Query & Severity Pill
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "REPORTED ISSUE",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Text(
            text = "\"${report.userQuery}\"",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        val severityColor = when (report.severity) {
          ActionSeverity.HIGH -> VoicedocErrorRed
          ActionSeverity.MEDIUM -> VoicedocWarningAmber
          ActionSeverity.LOW -> VoicedocSuccessGreen
        }

        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(severityColor.copy(alpha = 0.15f))
            .border(1.dp, severityColor.copy(alpha = 0.5f), CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = "${report.severity.name} SEVERITY",
            color = severityColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    // Plain Language AI Explanation Card with Audio Replay
    item {
      GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 22.dp,
        borderColor = VoicedocLightBlue.copy(alpha = 0.4f)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(34.dp)
                  .clip(CircleShape)
                  .background(VoicedocLightBlue.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Filled.Lightbulb,
                  contentDescription = "Insight",
                  tint = VoicedocLightBlue,
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = "Plain Language Diagnosis",
                color = VoicedocSkyBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
            }

            IconButton(
              onClick = onReplayAudio,
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(VoicedocDeepBlue.copy(alpha = 0.6f))
                .testTag("diagnosis_read_aloud_button")
            ) {
              Icon(
                imageVector = Icons.Filled.VolumeUp,
                contentDescription = "Read Aloud",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = report.title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = report.simpleExplanation,
            color = Color(0xFFE2E8F0),
            fontSize = 14.sp,
            lineHeight = 22.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFF0F172A).copy(alpha = 0.7f))
              .padding(12.dp)
          ) {
            Row(verticalAlignment = Alignment.Top) {
              Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Root Cause",
                tint = VoicedocAccentCyan,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Root Cause: ${report.rootCause}",
                color = Color(0xFF94A3B8),
                fontSize = 12.sp,
                lineHeight = 16.sp
              )
            }
          }
        }
      }
    }

    // Subsystem Health Score Comparison
    item {
      GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 20.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceAround,
          verticalAlignment = Alignment.CenterVertically
        ) {
          ScoreColumn(
            label = "Current Health",
            score = report.healthScoreBefore,
            color = VoicedocWarningAmber
          )

          Icon(
            imageVector = Icons.Filled.ArrowForward,
            contentDescription = "Improves to",
            tint = VoicedocLightBlue,
            modifier = Modifier.size(24.dp)
          )

          ScoreColumn(
            label = "After Auto-Fix",
            score = report.healthScoreAfter,
            color = VoicedocSuccessGreen
          )
        }
      }
    }

    // Recommended Actions Header
    item {
      Text(
        text = "PROPOSED AUTO-FIX SEQUENCE (${report.recommendedActions.size} ACTIONS)",
        color = Color(0xFF94A3B8),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )
    }

    // Actions Checklist
    items(report.recommendedActions) { action ->
      GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 16.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.Top
        ) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(VoicedocLightBlue.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Filled.AutoFixHigh,
              contentDescription = action.title,
              tint = VoicedocLightBlue,
              modifier = Modifier.size(18.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = action.title,
              color = Color.White,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = action.description,
              color = Color(0xFF94A3B8),
              fontSize = 12.sp,
              lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Impact: ${action.impact}",
              color = VoicedocSuccessGreen,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }

    // Primary Action Button: Proceed to Auto-Fix
    item {
      Spacer(modifier = Modifier.height(8.dp))
      Button(
        onClick = onProceedToAutoFix,
        modifier = Modifier
          .fillMaxWidth()
          .height(58.dp)
          .shadow(16.dp, RoundedCornerShape(20.dp), spotColor = VoicedocLightBlue)
          .testTag("proceed_to_autofix_button"),
        colors = ButtonDefaults.buttonColors(containerColor = VoicedocDeepBlue),
        shape = RoundedCornerShape(20.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Filled.AutoFixHigh,
            contentDescription = "Auto Fix",
            tint = Color.White
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Review & Auto-Fix Now",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

@Composable
private fun ScoreColumn(label: String, score: Int, color: Color) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = label,
      color = Color(0xFF94A3B8),
      fontSize = 12.sp
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = "$score / 100",
      color = color,
      fontSize = 24.sp,
      fontWeight = FontWeight.Black
    )
  }
}
