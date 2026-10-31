package com.abdullah.apex

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import java.util.Locale
import java.util.concurrent.Executors

class MainActivity : Activity() {
    private lateinit var prefs: ApexPrefs
    private lateinit var voice: VoiceController
    private lateinit var actions: DeviceActions
    private lateinit var engine: ChatEngine
    private lateinit var messages: LinearLayout
    private lateinit var scroll: ScrollView
    private lateinit var input: EditText
    private lateinit var status: TextView
    private val executor = Executors.newSingleThreadExecutor()
    private val bg = Color.rgb(7, 12, 25)
    private val panel = Color.rgb(15, 27, 48)
    private val cyan = Color.rgb(0, 220, 255)
    private val white = Color.WHITE
    private val muted = Color.rgb(160, 180, 205)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = ApexPrefs(this)
        actions = DeviceActions(this)
        engine = ChatEngine(prefs)
        voice = VoiceController(this, prefs,
            { value -> runOnUiThread { handleInput(value) } },
            { value -> runOnUiThread { status.text = value } })
        buildUi()
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 10)
        addMessage("A.P.E.X.", "A.P.E.X. system online, Boss. আমি প্রস্তুত। আপনার নির্দেশ দিন।", false)
    }

    private fun buildUi() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(14), dp(16), dp(12))
            setBackgroundColor(bg)
        }
        val top = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        val titleBox = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; layoutParams = LinearLayout.LayoutParams(0, -2, 1f) }
        titleBox.addView(text("A.P.E.X.", 26f, cyan))
        titleBox.addView(text("PERSONAL AI • ANDROID CONTROL", 9f, muted))
        top.addView(titleBox)
        top.addView(Button(this).apply {
            text = "⚙"; textSize = 20f; setTextColor(cyan); setBackgroundColor(panel)
            setOnClickListener { showSettings() }
        }, LinearLayout.LayoutParams(dp(52), dp(48)))
        root.addView(top)
        status = text("Voice system starting…", 11f, muted).apply { setPadding(0, dp(8), 0, dp(8)) }
        root.addView(status)
        scroll = ScrollView(this).apply { isFillViewport = true }
        messages = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(0, dp(8), 0, dp(8)) }
        scroll.addView(messages)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        val controls = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        input = EditText(this).apply {
            hint = "Message A.P.E.X. / লিখুন…"
            setHintTextColor(muted); setTextColor(white); textSize = 15f
            maxLines = 3; setPadding(dp(12), dp(8), dp(12), dp(8)); setBackgroundColor(panel)
        }
        controls.addView(input, LinearLayout.LayoutParams(0, dp(54), 1f))
        controls.addView(Button(this).apply {
            text = "🎙"; setTextColor(cyan); setBackgroundColor(panel)
            setOnClickListener {
                if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) voice.listen()
                else requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 10)
            }
        }, LinearLayout.LayoutParams(dp(54), dp(54)).apply { leftMargin = dp(6) })
        controls.addView(Button(this).apply {
            text = "➤"; setTextColor(Color.BLACK)
            setBackgroundTintList(android.content.res.ColorStateList.valueOf(cyan))
            setOnClickListener {
                val value = input.text.toString().trim()
                if (value.isNotEmpty()) { input.setText(""); handleInput(value) }
            }
        }, LinearLayout.LayoutParams(dp(54), dp(54)).apply { leftMargin = dp(6) })
        root.addView(controls)
        root.addView(text("Created by Abdullah Bin Elias", 10f, muted).apply {
            gravity = Gravity.CENTER; setPadding(0, dp(8), 0, 0)
        })
        setContentView(root)
    }

    private fun handleInput(raw: String) {
        val command = raw.trim()
        if (command.isEmpty()) return
        addMessage("You", command, true)
        val c = command.lowercase(Locale.ROOT)
        when {
            has(c, "switch to male", "male voice", "male mode", "মেল ভয়েস", "পুরুষ কণ্ঠ", "পুরুষ ভয়েস") -> {
                prefs.voiceMode = "male"; reply("Male voice mode selected. পুরুষ কণ্ঠ নির্বাচন করা হয়েছে。"); return
            }
            has(c, "switch to female", "female voice", "female mode", "ফিমেল ভয়েস", "নারী কণ্ঠ", "মেয়ে কণ্ঠ") -> {
                prefs.voiceMode = "female"; reply("Female voice mode selected. নারী কণ্ঠ নির্বাচন করা হয়েছে।"); return
            }
            has(c, "auto voice", "automatic voice", "স্বয়ংক্রিয় কণ্ঠ", "অটো ভয়েস") -> {
                prefs.voiceMode = "auto"; reply("Automatic voice mode enabled. স্বয়ংক্রিয় কণ্ঠ চালু হয়েছে।"); return
            }
            has(c, "switch to bangla", "speak bangla", "বাংলায় কথা বল", "বাংলা ভাষা চালু") -> {
                prefs.language = "bn"; reply("বাংলা ভাষা চালু হয়েছে।"); return
            }
            has(c, "switch to english", "speak english", "ইংরেজিতে কথা বল", "ইংরেজি ভাষা চালু") -> {
                prefs.language = "en"; reply("English language is enabled."); return
            }
            has(c, "auto language", "automatic language", "স্বয়ংক্রিয় ভাষা") -> {
                prefs.language = "auto"; reply("Automatic language mode enabled. স্বয়ংক্রিয় ভাষা চালু হয়েছে।"); return
            }
            has(c, "remember that", "মনে রেখো", "মনে রাখবে") -> {
                prefs.memory = (prefs.memory + "\n" + command).trim().takeLast(4000)
                reply("I saved that in this device's local memory. আপনার কথাটি এই ফোনে সংরক্ষণ করেছি।"); return
            }
            has(c, "forget my memory", "clear memory", "সব স্মৃতি মুছে", "মেমোরি মুছে") -> {
                prefs.memory = ""; prefs.history = "[]"
                reply("Local memory and chat history cleared. স্থানীয় মেমোরি ও চ্যাট ইতিহাস মুছে দিয়েছি।"); return
            }
        }
        val action = try { actions.run(command) } catch (e: Exception) { "কাজটি করা যায়নি: ${e.message}" }
        if (action != null) { reply(action); return }
        addMessage("A.P.E.X.", "Thinking… / ভাবছি…", false)
        val pending = messages.getChildAt(messages.childCount - 1)
        executor.execute {
            val result = engine.ask(command)
            runOnUiThread {
                messages.removeView(pending)
                addMessage("A.P.E.X.", result, false)
                voice.speak(result)
                status.text = "AI ready"
            }
        }
    }

    private fun reply(value: String) { addMessage("A.P.E.X.", value, false); voice.speak(value) }

    private fun addMessage(sender: String, body: String, fromUser: Boolean) {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(13), dp(10), dp(13), dp(10))
            setBackgroundColor(if (fromUser) Color.rgb(24, 47, 72) else panel)
        }
        card.addView(text(sender.uppercase(Locale.ROOT), 10f, if (fromUser) muted else cyan))
        card.addView(text(body, 14f, white).apply { setPadding(0, dp(5), 0, 0) })
        val params = LinearLayout.LayoutParams(-1, -2).apply {
            bottomMargin = dp(9)
            leftMargin = if (fromUser) dp(28) else 0
            rightMargin = if (fromUser) 0 else dp(28)
        }
        messages.addView(card, params)
        scroll.post { scroll.fullScroll(View.FOCUS_DOWN) }
    }

    private fun showSettings() {
        val box = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(18), dp(6), dp(18), dp(4)) }
        fun field(label: String, value: String, hintText: String = ""): EditText {
            box.addView(text(label, 12f, cyan))
            return EditText(this).apply {
                setText(value); hint = hintText; setTextColor(Color.BLACK); textSize = 14f; setSingleLine(true)
                box.addView(this, LinearLayout.LayoutParams(-1, dp(48)))
            }
        }
        val endpoint = field("AI API endpoint (HTTPS)", prefs.baseUrl)
        val model = field("Model name", prefs.model)
        val key = field("API key (stored on this phone)", prefs.apiKey, "Paste provider API key")
        val language = Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, listOf("Auto / স্বয়ংক্রিয়", "Bangla", "English"))
            setSelection(when (prefs.language) { "bn" -> 1; "en" -> 2; else -> 0 })
        }
        box.addView(text("Language", 12f, cyan)); box.addView(language)
        val voiceMode = Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, listOf("Auto: Bangla female / English male", "Male", "Female"))
            setSelection(when (prefs.voiceMode) { "male" -> 1; "female" -> 2; else -> 0 })
        }
        box.addView(text("Voice preference", 12f, cyan)); box.addView(voiceMode)
        val personality = Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, listOf("JARVIS — Professional", "FRIDAY — Friendly", "EDITH — Direct"))
            setSelection(when (prefs.personality) { "friday" -> 1; "edith" -> 2; else -> 0 })
        }
        box.addView(text("Personality", 12f, cyan)); box.addView(personality)
        AlertDialog.Builder(this).setTitle("A.P.E.X. Settings")
            .setView(ScrollView(this).apply { addView(box) })
            .setPositiveButton("Save") { _, _ ->
                prefs.baseUrl = endpoint.text.toString()
                prefs.model = model.text.toString()
                prefs.apiKey = key.text.toString()
                prefs.language = when (language.selectedItemPosition) { 1 -> "bn"; 2 -> "en"; else -> "auto" }
                prefs.voiceMode = when (voiceMode.selectedItemPosition) { 1 -> "male"; 2 -> "female"; else -> "auto" }
                prefs.personality = when (personality.selectedItemPosition) { 1 -> "friday"; 2 -> "edith"; else -> "jarvis" }
                reply("Settings saved. সেটিংস সংরক্ষণ করা হয়েছে।")
            }.setNegativeButton("Cancel", null).show()
    }

    private fun text(value: String, size: Float, color: Int) = TextView(this).apply {
        text = value; textSize = size; setTextColor(color)
    }
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
    private fun has(value: String, vararg keys: String) = keys.any { value.contains(it) }

    override fun onDestroy() {
        voice.destroy()
        executor.shutdownNow()
        super.onDestroy()
    }
}
