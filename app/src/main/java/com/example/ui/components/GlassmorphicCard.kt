package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassWhiteHigh
import com.example.ui.theme.GlassWhiteLow
import com.example.ui.theme.GlassWhiteMedium
import com.example.ui.theme.VoicedocCardDark
import com.example.ui.theme.VoicedocDeepBlue

@Composable
fun GlassmorphicCard(
  modifier: Modifier = Modifier,
  cornerRadius: Dp = 20.dp,
  backgroundColor: Color = VoicedocCardDark.copy(alpha = 0.75f),
  borderColor: Color = GlassBorder,
  elevation: Dp = 6.dp,
  onClick: (() -> Unit)? = null,
  content: @Composable BoxScope.() -> Unit
) {
  val shape = RoundedCornerShape(cornerRadius)
  val clickModifier = if (onClick != null) {
    Modifier.clickable(
      interactionSource = remember { MutableInteractionSource() },
      indication = ripple(bounded = true),
      onClick = onClick
    )
  } else {
    Modifier
  }

  Box(
    modifier = modifier
      .shadow(elevation = elevation, shape = shape, spotColor = VoicedocDeepBlue.copy(alpha = 0.35f))
      .clip(shape)
      .background(backgroundColor)
      .border(1.2.dp, borderColor, shape)
      .then(clickModifier)
  ) {
    // Subtle frosted sheen on top half
    Box(
      modifier = Modifier
        .matchParentSize()
        .background(
          Brush.verticalGradient(
            colors = listOf(
              GlassWhiteHigh.copy(alpha = 0.12f),
              Color.Transparent
            )
          )
        )
    )

    content()
  }
}
