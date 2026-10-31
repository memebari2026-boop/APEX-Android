package com.abdullah.apex

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import java.util.Locale

class VoiceController(
    private val context: Context,
    private val prefs: ApexPrefs,
    private val onText: (String) -> Unit,
    private val onStatus: (String) -> Unit
) : TextToSpeech.OnInitListener {
    private var recognizer: SpeechRecognizer? = null
    private var tts: TextToSpeech? = null
    private var ready = false

    init { tts = TextToSpeech(context, this) }

    override fun onInit(status: Int) {
        ready = status == TextToSpeech.SUCCESS
        onStatus(if (ready) "Voice system ready" else "Text-to-speech unavailable")
    }

    fun listen() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            onStatus("এই ফোনে Speech Recognition পাওয়া যায়নি।"); return
        }
        try {
            recognizer?.destroy()
            recognizer = SpeechRecognizer.createSpeechRecognizer(context)
            recognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) { onStatus("Listening… / শুনছি…") }
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() { onStatus("Thinking… / ভাবছি…") }
                override fun onError(error: Int) { onStatus("শোনা যায়নি। আবার চেষ্টা করুন।") }
                override fun onResults(results: Bundle?) {
                    val value = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                    if (!value.isNullOrBlank()) onText(value) else onStatus("কোনো কথা শনাক্ত হয়নি।")
                }
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
                when (prefs.language) {
                    "bn" -> putExtra(RecognizerIntent.EXTRA_LANGUAGE, "bn-BD")
                    "en" -> putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                    else -> {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "bn-BD")
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "bn-BD")
                    }
                }
            }
            recognizer?.startListening(intent)
        } catch (e: Exception) { onStatus("Voice recognition error: ${e.message}") }
    }

    fun speak(text: String) {
        if (!ready) return
        val bangla = when (prefs.language) {
            "bn" -> true
            "en" -> false
            else -> text.any { it in '\u0980'..'\u09FF' }
        }
        val locale = if (bangla) Locale("bn", "BD") else Locale.US
        val engine = tts ?: return
        val voices = engine.voices ?: emptySet<Voice>()
        val candidates = voices.filter { it.locale.language == locale.language }
        val gender = when (prefs.voiceMode) {
            "male" -> "male"
            "female" -> "female"
            else -> if (bangla) "female" else "male"
        }
        val selected = candidates.firstOrNull {
            val name = it.name.lowercase(Locale.ROOT)
            if (gender == "female") name.contains("female") || name.contains("woman")
            else name.contains("male") || name.contains("man")
        } ?: candidates.firstOrNull()
        if (selected != null) engine.voice = selected else engine.language = locale
        engine.setSpeechRate(0.96f)
        engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "APEX_${System.currentTimeMillis()}")
    }

    fun destroy() {
        recognizer?.destroy()
        tts?.stop()
        tts?.shutdown()
    }
}
