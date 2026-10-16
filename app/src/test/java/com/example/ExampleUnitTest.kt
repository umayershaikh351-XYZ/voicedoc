package com.example

import com.example.data.model.AutoFixCatalog
import com.example.data.model.LanguageCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testAutoFixActionsCatalogHasTenActions() {
    val actions = AutoFixCatalog.DEFAULT_10_ACTIONS
    assertEquals(10, actions.size)
    assertTrue(actions.any { it.id == "fix_cache" })
    assertTrue(actions.any { it.id == "fix_audio_dnd" })
    assertTrue(actions.any { it.id == "fix_battery_hogs" })
    assertTrue(actions.any { it.id == "fix_elder_font" })
    assertTrue(actions.any { it.id == "fix_stuck_app" })
  }

  @Test
  fun testSupportedLanguagesCatalog() {
    val languages = LanguageCatalog.LANGUAGES
    assertTrue(languages.any { it.code == "en" })
    assertTrue(languages.any { it.code == "hi" })
    assertTrue(languages.any { it.code == "es" })
  }
}

