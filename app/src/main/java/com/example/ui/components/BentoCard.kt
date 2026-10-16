package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.VoicedocAccentCyan
import com.example.ui.theme.VoicedocCardDark
import com.example.ui.theme.VoicedocDeepBlue
import com.example.ui.theme.VoicedocLightBlue

@Composable
fun BentoCard(
  title: String,
  subtitle: String,
  icon: ImageVector,
  badgeText: String? = null,
  accentColor: Color = VoicedocLightBlue,
  testTag: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  GlassmorphicCard(
    modifier = modifier.testTag(testTag),
    cornerRadius = 22.dp,
    borderColor = accentColor.copy(alpha = 0.35f),
    onClick = onClick
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Icon capsule
        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
              Brush.linearGradient(
                colors = listOf(
                  accentColor.copy(alpha = 0.35f),
                  VoicedocDeepBlue.copy(alpha = 0.6f)
                )
              )
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = title,
            tint = accentColor,
            modifier = Modifier.size(26.dp)
          )
        }

        if (badgeText != null) {
          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(accentColor.copy(alpha = 0.18f))
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(
              text = badgeText,
              color = accentColor,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      Column {
        Text(
          text = title,
          color = Color.White,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = subtitle,
          color = Color(0xFF94A3B8),
          fontSize = 12.sp,
          lineHeight = 16.sp,
          maxLines = 2
        )
      }
    }
  }
}
