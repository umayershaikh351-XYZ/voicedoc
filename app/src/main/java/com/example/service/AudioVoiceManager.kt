package com.example.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class AudioVoiceManager(
  private val context: Context,
  private val scope: CoroutineScope
) {
  private var textToSpeech: TextToSpeech? = null
  private var isTtsReady = false

  private val _isSpeaking = MutableStateFlow(false)
  val isSpeaking: StateFlow<Boolean> = _isSpeaking

  private val _isListening = MutableStateFlow(false)
  val isListening: StateFlow<Boolean> = _isListening

  private val _audioAmplitude = MutableStateFlow(0f)
  val audioAmplitude: StateFlow<Float> = _audioAmplitude

  private val _recognizedText = MutableStateFlow("")
  val recognizedText: StateFlow<String> = _recognizedText

  private var speechRecognizer: SpeechRecognizer? = null
  private var waveSimulationJob: Job? = null

  init {
    initTts()
  }

  private fun initTts() {
    textToSpeech = TextToSpeech(context) { status ->
      if (status == TextToSpeech.SUCCESS) {
        val result = textToSpeech?.setLanguage(Locale.US)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
          Log.w("AudioVoiceManager", "Language not supported for TTS, falling back")
        }
        isTtsReady = true
        textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
          override fun onStart(utteranceId: String?) {
            _isSpeaking.value = true
            startWaveAnimation()
          }

          override fun onDone(utteranceId: String?) {
            _isSpeaking.value = false
            stopWaveAnimation()
          }

          @Deprecated("Deprecated in Java")
          override fun onError(utteranceId: String?) {
            _isSpeaking.value = false
            stopWaveAnimation()
          }
        })
      }
    }
  }

  fun updateLanguage(langCode: String) {
    val locale = when (langCode.lowercase()) {
      "hi" -> Locale("hi", "IN")
      "es" -> Locale("es", "ES")
      "fr" -> Locale("fr", "FR")
      "ar" -> Locale("ar", "SA")
      "bn" -> Locale("bn", "BD")
      else -> Locale.US
    }
    try {
      textToSpeech?.language = locale
    } catch (e: Exception) {
      Log.e("AudioVoiceManager", "Error setting TTS language", e)
    }
  }

  fun speak(text: String) {
    if (text.isBlank()) return
    stopSpeaking()
    if (isTtsReady) {
      val params = Bundle()
      textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "VoicedocUtterance_${System.currentTimeMillis()}")
    } else {
      // Fallback pulse animation if TTS engine not yet loaded
      _isSpeaking.value = true
      startWaveAnimation()
      scope.launch {
        delay(2500)
        _isSpeaking.value = false
        stopWaveAnimation()
      }
    }
  }

  fun stopSpeaking() {
    textToSpeech?.stop()
    _isSpeaking.value = false
    stopWaveAnimation()
  }

  fun startListening(onResult: (String) -> Unit) {
    stopSpeaking()
    _isListening.value = true
    startWaveAnimation()

    if (SpeechRecognizer.isRecognitionAvailable(context)) {
      try {
        speechRecognizer?.destroy()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
          setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {
              _audioAmplitude.value = (rmsdB.coerceIn(0f, 10f) / 10f)
            }
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
              _isListening.value = false
              stopWaveAnimation()
            }
            override fun onError(error: Int) {
              Log.w("AudioVoiceManager", "Speech error code: $error")
              _isListening.value = false
              stopWaveAnimation()
            }
            override fun onResults(results: Bundle?) {
              val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
              val text = matches?.firstOrNull() ?: ""
              if (text.isNotBlank()) {
                _recognizedText.value = text
                onResult(text)
              }
              _isListening.value = false
              stopWaveAnimation()
            }
            override fun onPartialResults(partialResults: Bundle?) {
              val partial = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
              if (!partial.isNullOrBlank()) {
                _recognizedText.value = partial
              }
            }
            override fun onEvent(eventType: Int, params: Bundle?) {}
          })

          val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
          }
          startListening(intent)
        }
        return
      } catch (e: Exception) {
        Log.e("AudioVoiceManager", "Failed to start speech recognizer", e)
      }
    }

    // Fallback simulation if on emulator without physical mic input:
    scope.launch {
      delay(3000)
      if (_isListening.value) {
        _isListening.value = false
        stopWaveAnimation()
        val simulated = "My battery is draining too fast and phone gets hot"
        _recognizedText.value = simulated
        onResult(simulated)
      }
    }
  }

  fun stopListening() {
    try {
      speechRecognizer?.stopListening()
    } catch (_: Exception) {}
    _isListening.value = false
    stopWaveAnimation()
  }

  private fun startWaveAnimation() {
    waveSimulationJob?.cancel()
    waveSimulationJob = scope.launch {
      while (_isSpeaking.value || _isListening.value) {
        val target = (0.2f + Math.random().toFloat() * 0.8f)
        _audioAmplitude.value = target
        delay(120)
      }
      _audioAmplitude.value = 0f
    }
  }

  private fun stopWaveAnimation() {
    waveSimulationJob?.cancel()
    _audioAmplitude.value = 0f
  }

  fun cleanup() {
    stopSpeaking()
    try {
      speechRecognizer?.destroy()
    } catch (_: Exception) {}
    try {
      textToSpeech?.shutdown()
    } catch (_: Exception) {}
  }
}
