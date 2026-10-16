package com.example.data.model

data class FamilyMember(
  val id: String,
  val name: String,
  val relation: String,
  val deviceModel: String,
  val batteryLevel: Int,
  val storageUsedPercent: Int,
  val isOnline: Boolean,
  val hasIssue: Boolean = false,
  val lastReportedIssue: String? = null,
  val remoteCode: String
)

data class RepairShop(
  val id: String,
  val name: String,
  val address: String,
  val distance: String,
  val rating: Double,
  val reviewsCount: Int,
  val certifications: List<String>,
  val isOpen: Boolean = true,
  val estimatedCostSummary: String,
  val availableServices: List<RepairServiceOption>
)

data class RepairServiceOption(
  val name: String,
  val estimatedPrice: String,
  val duration: String
)

data class RepairBooking(
  val id: String,
  val shopName: String,
  val serviceName: String,
  val appointmentDate: String,
  val appointmentTime: String,
  val estimatedPrice: String,
  val bookingPin: String,
  val status: String = "Confirmed"
)

data class FixHistoryItem(
  val id: String,
  val timestampFormatted: String,
  val problemSpoken: String,
  val fixApplied: String,
  val impactSummary: String,
  val canRevert: Boolean = true,
  val isReverted: Boolean = false
)

data class SupportedLanguage(
  val code: String,
  val name: String,
  val nativeName: String,
  val flagEmoji: String,
  val welcomeAudioPhrase: String
)

object LanguageCatalog {
  val LANGUAGES = listOf(
    SupportedLanguage("en", "English", "English", "🇺🇸", "Hello! I am Voicedoc. How can I help your phone today?"),
    SupportedLanguage("hi", "Hindi", "हिन्दी", "🇮🇳", "नमस्ते! मैं वॉइसडॉक हूँ। आपके फोन में क्या समस्या आ रही है?"),
    SupportedLanguage("es", "Spanish", "Español", "🇪🇸", "¡Hola! Soy Voicedoc. ¿En qué puedo ayudarte hoy con tu teléfono?"),
    SupportedLanguage("fr", "French", "Français", "🇫🇷", "Bonjour! Je suis Voicedoc. Quel est le problème avec votre téléphone?"),
    SupportedLanguage("ar", "Arabic", "العربية", "🇸🇦", "مرحبًا! أنا فويس دوك. كيف يمكنني مساعدة هاتفك اليوم؟"),
    SupportedLanguage("bn", "Bengali", "বাংলা", "🇧🇩", "নমস্কার! আমি ভয়েসডক। আপনার ফোনের কি সমস্যা হচ্ছে বলুন?")
  )
}
