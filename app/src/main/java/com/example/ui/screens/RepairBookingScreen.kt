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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RepairBooking
import com.example.data.model.RepairServiceOption
import com.example.data.model.RepairShop
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.VoicedocHeader
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.VoicedocAccentCyan
import com.example.ui.theme.VoicedocCardDark
import com.example.ui.theme.VoicedocDarkNavy
import com.example.ui.theme.VoicedocDeepBlue
import com.example.ui.theme.VoicedocLightBlue
import com.example.ui.theme.VoicedocPurpleAccent
import com.example.ui.theme.VoicedocSkyBlue
import com.example.ui.theme.VoicedocSuccessGreen
import com.example.ui.theme.VoicedocWarningAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepairBookingScreen(
  repairShops: List<RepairShop>,
  activeBookings: List<RepairBooking>,
  onBookService: (RepairShop, RepairServiceOption, String, String) -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedShop by remember { mutableStateOf<RepairShop?>(null) }
  var selectedService by remember { mutableStateOf<RepairServiceOption?>(null) }
  var selectedDate by remember { mutableStateOf("Tomorrow, 11:30 AM") }
  val sheetState = rememberModalBottomSheetState()
  var showBookingSheet by remember { mutableStateOf(false) }

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
        title = "Hardware Repair Labs",
        showBackButton = true,
        onBackClick = onBack
      )

      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Active Bookings Banner
        if (activeBookings.isNotEmpty()) {
          item {
            Text(
              text = "YOUR CONFIRMED APPOINTMENTS",
              color = Color(0xFF94A3B8),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
          }

          items(activeBookings) { booking ->
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(VoicedocSuccessGreen.copy(alpha = 0.15f))
                .border(1.2.dp, VoicedocSuccessGreen.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                .padding(16.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Confirmed",
                    tint = VoicedocSuccessGreen,
                    modifier = Modifier.size(24.dp)
                  )
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(
                      text = "${booking.serviceName} • ${booking.shopName}",
                      color = Color.White,
                      fontSize = 14.sp,
                      fontWeight = FontWeight.Bold
                    )
                    Text(
                      text = "${booking.appointmentDate} • ${booking.appointmentTime}",
                      color = Color(0xFFCBD5E1),
                      fontSize = 12.sp
                    )
                  }
                }

                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(VoicedocDeepBlue)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                  Text(
                    text = booking.bookingPin,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        }

        item {
          Text(
            text = "VERIFIED NEARBY REPAIR LABS",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
        }

        items(repairShops) { shop ->
          GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 20.dp
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            ) {
              // Shop Header
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = shop.name,
                      color = Color.White,
                      fontSize = 16.sp,
                      fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                      imageVector = Icons.Filled.Verified,
                      contentDescription = "Certified",
                      tint = VoicedocAccentCyan,
                      modifier = Modifier.size(16.dp)
                    )
                  }
                  Spacer(modifier = Modifier.height(2.dp))
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Filled.LocationOn,
                      contentDescription = "Distance",
                      tint = Color(0xFF94A3B8),
                      modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "${shop.distance} • ${shop.address}",
                      color = Color(0xFF94A3B8),
                      fontSize = 12.sp
                    )
                  }
                }

                // Rating Pill
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(VoicedocWarningAmber.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Filled.Star,
                      contentDescription = "Rating",
                      tint = VoicedocWarningAmber,
                      modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "${shop.rating}",
                      color = Color.White,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Certification Badges
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                shop.certifications.forEach { cert ->
                  Box(
                    modifier = Modifier
                      .clip(CircleShape)
                      .background(Color(0xFF1E293B))
                      .padding(horizontal = 8.dp, vertical = 3.dp)
                  ) {
                    Text(
                      text = cert,
                      color = VoicedocSkyBlue,
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Medium
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              // Available Services List
              Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                shop.availableServices.forEach { service ->
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(10.dp))
                      .background(Color(0xFF0F172A).copy(alpha = 0.5f))
                      .clickable {
                        selectedShop = shop
                        selectedService = service
                        showBookingSheet = true
                      }
                      .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column {
                      Text(
                        text = service.name,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                      )
                      Text(
                        text = "Est. duration: ${service.duration}",
                        color = Color(0xFF94A3B8),
                        fontSize = 10.sp
                      )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Text(
                        text = service.estimatedPrice,
                        color = VoicedocSuccessGreen,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                      )
                      Spacer(modifier = Modifier.width(8.dp))
                      Box(
                        modifier = Modifier
                          .clip(RoundedCornerShape(8.dp))
                          .background(VoicedocDeepBlue)
                          .padding(horizontal = 8.dp, vertical = 4.dp)
                      ) {
                        Text("Book", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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

    // Modal Bottom Sheet for Appointment Booking
    if (showBookingSheet && selectedShop != null && selectedService != null) {
      ModalBottomSheet(
        onDismissRequest = { showBookingSheet = false },
        sheetState = sheetState,
        containerColor = Color(0xFF0F172A),
        contentColor = Color.White
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
          Text(
            text = "Confirm Walk-in Appointment",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "${selectedShop?.name} • ${selectedService?.name}",
            color = VoicedocLightBlue,
            fontSize = 14.sp
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Date & Time Picker Slots
          Text(
            text = "SELECT APPOINTMENT SLOT",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )

          Spacer(modifier = Modifier.height(8.dp))

          val slots = listOf("Today, 4:00 PM", "Tomorrow, 11:30 AM", "Tomorrow, 2:30 PM", "Saturday, 10:00 AM")
          slots.forEach { slot ->
            val isChosen = selectedDate == slot
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isChosen) VoicedocDeepBlue else Color(0xFF1E293B))
                .border(1.dp, if (isChosen) VoicedocLightBlue else GlassBorder, RoundedCornerShape(12.dp))
                .clickable { selectedDate = slot }
                .padding(12.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = null,
                    tint = if (isChosen) Color.White else VoicedocAccentCyan,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(text = slot, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }

                if (isChosen) {
                  Icon(Icons.Filled.CheckCircle, "Selected", tint = Color.White, modifier = Modifier.size(16.dp))
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(20.dp))

          Button(
            onClick = {
              onBookService(selectedShop!!, selectedService!!, selectedDate.substringBefore(","), selectedDate.substringAfter(","))
              showBookingSheet = false
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(54.dp)
              .testTag("confirm_booking_button"),
            colors = ButtonDefaults.buttonColors(containerColor = VoicedocDeepBlue),
            shape = RoundedCornerShape(16.dp)
          ) {
            Text(
              text = "Confirm Reservation (${selectedService?.estimatedPrice})",
              color = Color.White,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.height(24.dp))
        }
      }
    }
  }
}
