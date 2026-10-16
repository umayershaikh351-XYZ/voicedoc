package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.VoicedocAccentCyan
import com.example.ui.theme.VoicedocCardDark
import com.example.ui.theme.VoicedocDeepBlue
import com.example.ui.theme.VoicedocLightBlue
import com.example.ui.theme.VoicedocSuccessGreen

@Composable
fun FloatingCommandBar(
  isExpanded: Boolean,
  onToggleExpand: () -> Unit,
  onQuickVoice: () -> Unit,
  onQuickTuneUp: () -> Unit,
  onOpenAccessibility: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .shadow(16.dp, RoundedCornerShape(26.dp), spotColor = VoicedocDeepBlue)
        .clip(RoundedCornerShape(26.dp))
        .background(
          Brush.linearGradient(
            colors = listOf(
              VoicedocCardDark.copy(alpha = 0.96f),
              Color(0xFF0F172A).copy(alpha = 0.96f)
            )
          )
        )
        .border(1.5.dp, GlassBorder, RoundedCornerShape(26.dp))
        .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
      // Main Docked Command Bar Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Left: Assistant Status Pill
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(VoicedocDeepBlue.copy(alpha = 0.4f))
            .clickable { onToggleExpand() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(VoicedocSuccessGreen)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Voicedoc AI Live",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = if (isExpanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
            contentDescription = "Expand command bar",
            tint = VoicedocLightBlue,
            modifier = Modifier.size(16.dp)
          )
        }

        // Center / Right Quick Action: Voice Command Button
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
              Brush.horizontalGradient(
                colors = listOf(VoicedocDeepBlue, VoicedocLightBlue)
              )
            )
            .clickable { onQuickVoice() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag("floating_quick_voice_button"),
          contentAlignment = Alignment.Center
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Filled.Mic,
              contentDescription = "Quick Speak",
              tint = Color.White,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Speak Fix",
              color = Color.White,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      // Expandable drawer with fast shortcuts (like Superhuman floating bar)
      AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(1.dp)
              .background(GlassBorder.copy(alpha = 0.4f))
          )
          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "FAST SYSTEM COMMANDS",
            color = Color(0xFF94A3B8),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            // Quick Clean
            CommandChip(
              icon = Icons.Filled.CleaningServices,
              label = "Clean Cache",
              onClick = onQuickTuneUp,
              modifier = Modifier.weight(1f)
            )

            // Speed Boost
            CommandChip(
              icon = Icons.Filled.Speed,
              label = "Boost RAM",
              onClick = onQuickTuneUp,
              modifier = Modifier.weight(1f)
            )

            // Accessibility Check
            CommandChip(
              icon = Icons.Filled.Shield,
              label = "Doctor Shield",
              onClick = onOpenAccessibility,
              modifier = Modifier.weight(1f)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun CommandChip(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(Color(0xFF1E293B))
      .border(1.dp, GlassBorder.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
      .clickable { onClick() }
      .padding(vertical = 8.dp, horizontal = 6.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = VoicedocAccentCyan,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = label,
        color = Color.White,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium
      )
    }
  }
}
