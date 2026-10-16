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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FixHistoryItem
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.VoicedocHeader
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.VoicedocCardDark
import com.example.ui.theme.VoicedocDarkNavy
import com.example.ui.theme.VoicedocDeepBlue
import com.example.ui.theme.VoicedocLightBlue
import com.example.ui.theme.VoicedocSuccessGreen
import com.example.ui.theme.VoicedocWarningAmber

@Composable
fun HistoryScreen(
  historyLogs: List<FixHistoryItem>,
  onRevertFix: (String) -> Unit,
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
        title = "Fix History & Undo Logs",
        showBackButton = true,
        onBackClick = onBack
      )

      if (historyLogs.isEmpty()) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Box(
            modifier = Modifier
              .size(64.dp)
              .clip(CircleShape)
              .background(VoicedocLightBlue.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Filled.History,
              contentDescription = "No History",
              tint = VoicedocLightBlue,
              modifier = Modifier.size(32.dp)
            )
          }
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "No Previous Fixes Yet",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Once Voicedoc diagnoses and optimizes your phone, action logs will appear here.",
            color = Color(0xFF94A3B8),
            fontSize = 13.sp
          )
        }
      } else {
        LazyColumn(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          item {
            Text(
              text = "RECENT DIAGNOSTIC & AUTO-FIX AUDITS",
              color = Color(0xFF94A3B8),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
          }

          items(historyLogs) { item ->
            GlassmorphicCard(
              modifier = Modifier.fillMaxWidth(),
              cornerRadius = 18.dp,
              borderColor = if (item.isReverted) VoicedocWarningAmber.copy(alpha = 0.5f) else VoicedocSuccessGreen.copy(alpha = 0.4f)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(16.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                      modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(
                          if (item.isReverted) VoicedocWarningAmber.copy(alpha = 0.2f) else VoicedocSuccessGreen.copy(alpha = 0.2f)
                        ),
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(
                        imageVector = if (item.isReverted) Icons.Filled.Replay else Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = if (item.isReverted) VoicedocWarningAmber else VoicedocSuccessGreen,
                        modifier = Modifier.size(18.dp)
                      )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = if (item.isReverted) "Action Reverted" else "Auto-Fix Applied",
                      color = if (item.isReverted) VoicedocWarningAmber else VoicedocSuccessGreen,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }

                  Text(
                    text = item.timestampFormatted,
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp
                  )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                  text = "Spoken Query: \"${item.problemSpoken}\"",
                  color = Color.White,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                  text = item.fixApplied,
                  color = Color(0xFFCBD5E1),
                  fontSize = 12.sp,
                  lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = item.impactSummary,
                    color = VoicedocLightBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                  )

                  if (item.canRevert && !item.isReverted) {
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF334155))
                        .clickable { onRevertFix(item.id) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("history_undo_button_${item.id}")
                    ) {
                      Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                          imageVector = Icons.Filled.Undo,
                          contentDescription = "Undo",
                          tint = VoicedocLightBlue,
                          modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                          text = "Revert Fix",
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
      }
    }
  }
}
