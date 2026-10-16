package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FamilyMember
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.VoicedocHeader
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.VoicedocAccentCyan
import com.example.ui.theme.VoicedocCardDark
import com.example.ui.theme.VoicedocDarkNavy
import com.example.ui.theme.VoicedocDeepBlue
import com.example.ui.theme.VoicedocErrorRed
import com.example.ui.theme.VoicedocLightBlue
import com.example.ui.theme.VoicedocSuccessGreen
import com.example.ui.theme.VoicedocWarningAmber

@Composable
fun FamilyModeScreen(
  familyMembers: List<FamilyMember>,
  remoteCode: String,
  onDiagnoseRemoteMember: (FamilyMember) -> Unit,
  onTriggerSos: () -> Unit,
  onShowMessage: (String) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

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
        title = "Family Remote Care",
        showBackButton = true,
        onBackClick = onBack
      )

      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Hero Remote Connection Code Card
        item {
          GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 22.dp,
            borderColor = VoicedocLightBlue.copy(alpha = 0.5f)
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
                Text(
                  text = "YOUR REMOTE HELP CODE",
                  color = VoicedocLightBlue,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp
                )

                Box(
                  modifier = Modifier
                    .clip(CircleShape)
                    .background(VoicedocSuccessGreen.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                  Text(
                    text = "Live Backend Ready",
                    color = VoicedocSuccessGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = remoteCode,
                  color = Color.White,
                  fontSize = 32.sp,
                  fontWeight = FontWeight.Black,
                  fontFamily = FontFamily.Monospace,
                  letterSpacing = 3.sp
                )

                IconButton(
                  onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Voicedoc Code", remoteCode))
                    onShowMessage("Copied code $remoteCode to clipboard")
                  },
                  modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(VoicedocDeepBlue)
                    .testTag("copy_remote_code_button")
                ) {
                  Icon(
                    imageVector = Icons.Filled.ContentCopy,
                    contentDescription = "Copy Code",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              // Broken screen web rescue banner
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .background(Color(0xFF0F172A).copy(alpha = 0.8f))
                  .border(1.dp, GlassBorder.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                  .padding(12.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Filled.Link,
                    contentDescription = "Web Access",
                    tint = VoicedocAccentCyan,
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(
                      text = "Broken Screen Rescue Mode",
                      color = Color.White,
                      fontSize = 13.sp,
                      fontWeight = FontWeight.Bold
                    )
                    Text(
                      text = "If this phone's screen is unresponsive or shattered, family can visit voicedoc.help/connect to diagnose and fix it over the web.",
                      color = Color(0xFF94A3B8),
                      fontSize = 11.sp,
                      lineHeight = 15.sp
                    )
                  }
                }
              }
            }
          }
        }

        // SOS Emergency Alert Section
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(18.dp))
              .background(
                Brush.horizontalGradient(
                  colors = listOf(
                    VoicedocErrorRed.copy(alpha = 0.25f),
                    VoicedocDeepBlue.copy(alpha = 0.5f)
                  )
                )
              )
              .border(1.2.dp, VoicedocErrorRed.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
              .padding(16.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(VoicedocErrorRed),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Filled.Emergency,
                    contentDescription = "Emergency SOS",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                  )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text(
                    text = "Family Emergency SOS",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "1-tap phone alert with GPS & battery levels",
                    color = Color(0xFFCBD5E1),
                    fontSize = 11.sp
                  )
                }
              }

              Button(
                onClick = onTriggerSos,
                colors = ButtonDefaults.buttonColors(containerColor = VoicedocErrorRed),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.testTag("family_sos_button")
              ) {
                Text("Send SOS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
              }
            }
          }
        }

        // Connected Family Devices
        item {
          Text(
            text = "CONNECTED FAMILY DEVICES (${familyMembers.size})",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        }

        items(familyMembers) { member ->
          GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 20.dp,
            borderColor = if (member.hasIssue) VoicedocWarningAmber.copy(alpha = 0.6f) else GlassBorder
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Box(
                    modifier = Modifier
                      .size(42.dp)
                      .clip(CircleShape)
                      .background(
                        if (member.hasIssue) VoicedocWarningAmber.copy(alpha = 0.2f) else VoicedocLightBlue.copy(alpha = 0.2f)
                      ),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Filled.PhoneAndroid,
                      contentDescription = member.name,
                      tint = if (member.hasIssue) VoicedocWarningAmber else VoicedocLightBlue,
                      modifier = Modifier.size(22.dp)
                    )
                  }

                  Spacer(modifier = Modifier.width(12.dp))

                  Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Text(
                        text = member.name,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                      )
                      Spacer(modifier = Modifier.width(6.dp))
                      Box(
                        modifier = Modifier
                          .size(7.dp)
                          .clip(CircleShape)
                          .background(if (member.isOnline) VoicedocSuccessGreen else Color(0xFF64748B))
                      )
                    }
                    Text(
                      text = "${member.relation} • ${member.deviceModel}",
                      color = Color(0xFF94A3B8),
                      fontSize = 12.sp
                    )
                  }
                }

                // Battery & Storage Pills
                Column(horizontalAlignment = Alignment.End) {
                  Text(
                    text = "🔋 ${member.batteryLevel}%",
                    color = if (member.batteryLevel < 20) VoicedocWarningAmber else VoicedocSuccessGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "💾 ${member.storageUsedPercent}% full",
                    color = if (member.storageUsedPercent > 90) VoicedocWarningAmber else Color(0xFF94A3B8),
                    fontSize = 11.sp
                  )
                }
              }

              if (member.hasIssue && member.lastReportedIssue != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(VoicedocWarningAmber.copy(alpha = 0.15f))
                    .padding(10.dp)
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Filled.Warning,
                      contentDescription = "Alert",
                      tint = VoicedocWarningAmber,
                      modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = member.lastReportedIssue,
                      color = Color(0xFFFDE68A),
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Medium
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              // Action buttons row: Diagnose Remotely & Call
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Button(
                  onClick = { onDiagnoseRemoteMember(member) },
                  colors = ButtonDefaults.buttonColors(containerColor = VoicedocDeepBlue),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier.weight(1f)
                ) {
                  Icon(
                    imageVector = Icons.Filled.MedicalServices,
                    contentDescription = "Diagnose",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Remote Triage", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1E293B))
                    .clickable { onShowMessage("WebRTC screen share link sent to ${member.name}") }
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                  Icon(
                    imageVector = Icons.Filled.ScreenShare,
                    contentDescription = "Screen Share",
                    tint = VoicedocAccentCyan,
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
}
