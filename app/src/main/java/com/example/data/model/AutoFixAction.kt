package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.BatterySaver
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.ui.graphics.vector.ImageVector

enum class FixStatus {
  PENDING,
  RUNNING,
  SUCCESS,
  REVERTED,
  FAILED
}

enum class ActionSeverity {
  LOW,
  MEDIUM,
  HIGH
}

data class AutoFixAction(
  val id: String,
  val title: String,
  val category: String,
  val description: String,
  val impact: String,
  val severity: ActionSeverity = ActionSeverity.MEDIUM,
  val status: FixStatus = FixStatus.PENDING,
  val intentAction: String? = null,
  val isReversible: Boolean = true,
  val iconName: String = "clean"
)

object AutoFixCatalog {
  val DEFAULT_10_ACTIONS = listOf(
    AutoFixAction(
      id = "fix_cache",
      title = "Clear System App Cache & Junk",
      category = "Storage & Performance",
      description = "Safely removes temporary cached files and orphaned thumbnail databases without touching personal photos or chats.",
      impact = "Reclaims up to 1.8 GB disk space and accelerates UI frame rates.",
      severity = ActionSeverity.LOW,
      intentAction = android.provider.Settings.ACTION_INTERNAL_STORAGE_SETTINGS,
      iconName = "clean"
    ),
    AutoFixAction(
      id = "fix_audio_dnd",
      title = "Unmute Audio Profile & Turn Off DND",
      category = "Sound & Calling",
      description = "Disables accidental 'Do Not Disturb' mode and raises ringer and media volume to a comfortable 80%.",
      impact = "Ensures incoming phone calls and alarms ring out loud immediately.",
      severity = ActionSeverity.HIGH,
      intentAction = android.provider.Settings.ACTION_SOUND_SETTINGS,
      iconName = "volume"
    ),
    AutoFixAction(
      id = "fix_bluetooth_wifi",
      title = "Cycle & Flush Bluetooth / Wi-Fi Stack",
      category = "Connectivity",
      description = "Cycles the hardware wireless radio adapters and flushes stuck DHCP leasing errors.",
      impact = "Restores dropped internet connections and repairs unresponsive Bluetooth earbuds.",
      severity = ActionSeverity.MEDIUM,
      intentAction = android.provider.Settings.ACTION_WIRELESS_SETTINGS,
      iconName = "bluetooth"
    ),
    AutoFixAction(
      id = "fix_storage_reclaim",
      title = "Clean Residual Downloads & Big Media",
      category = "Storage",
      description = "Scans download directory for redundant APK installers and zero-byte leftover downloads.",
      impact = "Stops 'Storage Space Running Out' system warnings.",
      severity = ActionSeverity.LOW,
      intentAction = android.provider.Settings.ACTION_INTERNAL_STORAGE_SETTINGS,
      iconName = "storage"
    ),
    AutoFixAction(
      id = "fix_display_eye",
      title = "Calibrate Display & Eye Comfort Shield",
      category = "Display & Battery",
      description = "Lowers aggressive glare, turns on amber Night Light, and aligns adaptive screen timeout to 1 minute.",
      impact = "Cuts battery drain by 14% and relieves nighttime eye strain.",
      severity = ActionSeverity.LOW,
      intentAction = android.provider.Settings.ACTION_DISPLAY_SETTINGS,
      iconName = "brightness"
    ),
    AutoFixAction(
      id = "fix_dns_lag",
      title = "Refresh DNS & Reset Mobile Data Handshake",
      category = "Network",
      description = "Switches to high-reliability encrypted Private DNS (Cloudflare 1.1.1.1 / Google DNS) and refreshes cellular IP lease.",
      impact = "Resolves 'Connected, no internet' errors and stops WhatsApp message delays.",
      severity = ActionSeverity.MEDIUM,
      intentAction = android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS,
      iconName = "wifi"
    ),
    AutoFixAction(
      id = "fix_notifications",
      title = "Unsilence Critical Notifications",
      category = "Notifications",
      description = "Detects apps whose notifications were accidentally toggled silent in system settings and restores alert banners.",
      impact = "Missed family texts and bank transaction alerts show up on screen again.",
      severity = ActionSeverity.HIGH,
      intentAction = android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS,
      iconName = "notification"
    ),
    AutoFixAction(
      id = "fix_battery_hogs",
      title = "Restrict Rogue Background Battery Hogs",
      category = "Battery & Health",
      description = "Identifies apps holding wake-locks while screen is locked and applies battery optimization restrictions.",
      impact = "Saves 2.5 to 4 hours of daily screen-on battery life and prevents phone overheating.",
      severity = ActionSeverity.HIGH,
      intentAction = android.provider.Settings.ACTION_BATTERY_SAVER_SETTINGS,
      iconName = "battery"
    ),
    AutoFixAction(
      id = "fix_elder_font",
      title = "Elder Magnification & High Contrast Mode",
      category = "Accessibility",
      description = "Increases system font scale to Large (125%), enables high contrast text, and increases touch-hold delay.",
      impact = "Makes text, buttons, and dial pad crystal clear to read without glasses.",
      severity = ActionSeverity.LOW,
      intentAction = android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS,
      iconName = "font"
    ),
    AutoFixAction(
      id = "fix_stuck_app",
      title = "Clear Frozen App Memory & Reset Task",
      category = "System Stability",
      description = "Signals the Android activity manager to release locked graphics buffers and restarts frozen background processes.",
      impact = "Stops app crashes and resolves black-screen freezes without needing a full phone reboot.",
      severity = ActionSeverity.MEDIUM,
      intentAction = android.provider.Settings.ACTION_APPLICATION_SETTINGS,
      iconName = "restart"
    )
  )
}
