package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AutoFixAction
import com.example.data.model.FixStatus
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.VoicedocHeader
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.VoicedocAccentCyan
import com.example.ui.theme.VoicedocCardDark
import com.example.ui.theme.VoicedocDarkNavy
import com.example.ui.theme.VoicedocDeepBlue
import com.example.ui.theme.VoicedocLightBlue
import com.example.ui.theme.VoicedocSkyBlue
import com.example.ui.theme.VoicedocSuccessGreen
import com.example.ui.theme.VoicedocWarningAmber

@Composable
fun AutoFixScreen(
  actions: List<AutoFixAction>,
  isAutoFixing: Boolean,
  isFixCompleted: Boolean,
  currentFixStepIndex: Int,
  executionLogs: List<String>,
  isAccessibilityActive: Boolean,
  onExecuteAutoFix: () -> Unit,
  onRevertAction: (String) -> Unit,
  onOpenSystemSetting: (String?) -> Unit,
  onOpenAccessibilitySettings: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showConsentNotice by remember { mutableStateOf(!isFixCompleted && !isAutoFixing) }

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
        title = "Supervised Auto-Fix",
        showBackButton = true,
        onBackClick = onBack
      )

      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Consent & Safety Shield Card
        item {
          GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 20.dp,
            borderColor = if (isAccessibilityActive) VoicedocSuccessGreen.copy(alpha = 0.5f) else VoicedocWarningAmber.copy(alpha = 0.5f)
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(34.dp)
                      .clip(CircleShape)
                      .background(
                        if (isAccessibilityActive) VoicedocSuccessGreen.copy(alpha = 0.2f) else VoicedocWarningAmber.copy(alpha = 0.2f)
                      ),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Filled.Security,
                      contentDescription = "Consent Shield",
                      tint = if (isAccessibilityActive) VoicedocSuccessGreen else VoicedocWarningAmber,
                      modifier = Modifier.size(20.dp)
                    )
                  }
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(
                      text = "Safety & Supervised Execution",
                      color = Color.White,
                      fontSize = 14.sp,
                      fontWeight = FontWeight.Bold
                    )
                    Text(
                      text = if (isAccessibilityActive) "Accessibility Service Active" else "Supervised Assistant Ready",
                      color = if (isAccessibilityActive) VoicedocSuccessGreen else VoicedocWarningAmber,
                      fontSize = 12.sp
                    )
                  }
                }

                if (!isAccessibilityActive) {
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(12.dp))
                      .background(VoicedocDeepBlue)
                      .clickable { onOpenAccessibilitySettings() }
                      .padding(horizontal = 8.dp, vertical = 4.dp)
                  ) {
                    Text(
                      text = "Enable Service",
                      color = Color.White,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = "Voicedoc will execute only the approved fixes listed below. Nothing will be deleted without your permission, and every action can be undone with 1 tap.",
                color = Color(0xFFCBD5E1),
                fontSize = 12.sp,
                lineHeight = 17.sp
              )
            }
          }
        }

        // Action Trigger Button or Status Banner
        item {
          if (!isAutoFixing && !isFixCompleted) {
            Button(
              onClick = onExecuteAutoFix,
              modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .shadow(16.dp, RoundedCornerShape(20.dp), spotColor = VoicedocLightBlue)
                .testTag("start_autofix_execution_button"),
              colors = ButtonDefaults.buttonColors(containerColor = VoicedocDeepBlue),
              shape = RoundedCornerShape(20.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Filled.PlayArrow,
                  contentDescription = "Start",
                  tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Confirm & Execute ${actions.size} Auto-Fixes",
                  color = Color.White,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          } else if (isAutoFixing) {
            GlassmorphicCard(
              modifier = Modifier.fillMaxWidth(),
              cornerRadius = 16.dp,
              borderColor = VoicedocLightBlue
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                CircularProgressIndicator(
                  modifier = Modifier.size(24.dp),
                  color = VoicedocLightBlue,
                  strokeWidth = 3.dp
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                  text = "Executing Fix [${currentFixStepIndex + 1}/${actions.size}]...",
                  color = Color.White,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          } else {
            // Completed Success Banner
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(VoicedocSuccessGreen.copy(alpha = 0.15f))
                .border(1.2.dp, VoicedocSuccessGreen.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(14.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Filled.CheckCircle,
                  contentDescription = "Success",
                  tint = VoicedocSuccessGreen,
                  modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = "All Auto-Fixes Completed Successfully!",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "Subsystem health restored to 98%. Tap Revert on any item if needed.",
                    color = VoicedocSuccessGreen,
                    fontSize = 12.sp
                  )
                }
              }
            }
          }
        }

        // Step by Step Actions Checklist with Undo & Intent Deep Links
        item {
          Text(
            text = "ACTION CHECKLIST & CONTROLS",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        }

        itemsIndexed(actions) { index, action ->
          val isCurrent = isAutoFixing && index == currentFixStepIndex
          val isDone = action.status == FixStatus.SUCCESS
          val isReverted = action.status == FixStatus.REVERTED

          GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 18.dp,
            borderColor = when {
              isCurrent -> VoicedocLightBlue
              isDone -> VoicedocSuccessGreen.copy(alpha = 0.5f)
              isReverted -> VoicedocWarningAmber.copy(alpha = 0.5f)
              else -> GlassBorder
            }
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.Top) {
                  // Status Icon
                  Box(
                    modifier = Modifier
                      .size(32.dp)
                      .clip(CircleShape)
                      .background(
                        when {
                          isDone -> VoicedocSuccessGreen.copy(alpha = 0.2f)
                          isCurrent -> VoicedocLightBlue.copy(alpha = 0.2f)
                          isReverted -> VoicedocWarningAmber.copy(alpha = 0.2f)
                          else -> Color(0xFF334155)
                        }
                      ),
                    contentAlignment = Alignment.Center
                  ) {
                    when {
                      isDone -> Icon(Icons.Filled.Check, "Done", tint = VoicedocSuccessGreen, modifier = Modifier.size(18.dp))
                      isCurrent -> CircularProgressIndicator(color = VoicedocLightBlue, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                      isReverted -> Icon(Icons.Filled.Replay, "Reverted", tint = VoicedocWarningAmber, modifier = Modifier.size(16.dp))
                      else -> Text("${index + 1}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                  }

                  Spacer(modifier = Modifier.width(12.dp))

                  Column {
                    Text(
                      text = action.title,
                      color = if (isReverted) Color(0xFF94A3B8) else Color.White,
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
                  }
                }

                // Deep Link into Android Settings
                IconButton(
                  onClick = { onOpenSystemSetting(action.intentAction) },
                  modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E293B))
                ) {
                  Icon(
                    imageVector = Icons.Filled.OpenInNew,
                    contentDescription = "Open Settings",
                    tint = VoicedocSkyBlue,
                    modifier = Modifier.size(16.dp)
                  )
                }
              }

              // Impact & Undo Row when completed
              if (isDone || isReverted) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = if (isReverted) "Action Reverted" else action.impact,
                    color = if (isReverted) VoicedocWarningAmber else VoicedocSuccessGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                  )

                  if (isDone && action.isReversible) {
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF334155))
                        .clickable { onRevertAction(action.id) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                          imageVector = Icons.Filled.Undo,
                          contentDescription = "Undo",
                          tint = VoicedocLightBlue,
                          modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                          text = "Undo",
                          color = Color.White,
                          fontSize = 11.sp,
                          fontWeight = FontWeight.Bold
                        )
                      }
                    }
                  }
                }
              }
            }
          }
        }

        // Live Execution Terminal Logs
        if (executionLogs.isNotEmpty()) {
          item {
            Text(
              text = "EXECUTION LOG (SUPERVISED TRACE)",
              color = Color(0xFF94A3B8),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
          }

          item {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF0A0F1D))
                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
                .padding(14.dp)
            ) {
              Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                executionLogs.forEach { log ->
                  Text(
                    text = log,
                    color = if (log.startsWith("✔")) VoicedocSuccessGreen else if (log.startsWith("🎉")) VoicedocAccentCyan else Color(0xFFCBD5E1),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 16.sp
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
