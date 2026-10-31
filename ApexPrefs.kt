package com.abdullah.apex

import android.content.Context

class ApexPrefs(context: Context) {
    private val p = context.getSharedPreferences("apex_prefs", Context.MODE_PRIVATE)
    var apiKey: String
        get() = p.getString("api_key", "") ?: ""
        set(v) { p.edit().putString("api_key", v.trim()).apply() }
    var baseUrl: String
        get() = p.getString("base_url", "https://api.openai.com/v1/chat/completions") ?: ""
        set(v) { p.edit().putString("base_url", v.trim()).apply() }
    var model: String
        get() = p.getString("model", "gpt-4o-mini") ?: "gpt-4o-mini"
        set(v) { p.edit().putString("model", v.trim()).apply() }
    var language: String
        get() = p.getString("language", "auto") ?: "auto"
        set(v) { p.edit().putString("language", v).apply() }
    var voiceMode: String
        get() = p.getString("voice_mode", "auto") ?: "auto"
        set(v) { p.edit().putString("voice_mode", v).apply() }
    var personality: String
        get() = p.getString("personality", "jarvis") ?: "jarvis"
        set(v) { p.edit().putString("personality", v).apply() }
    var memory: String
        get() = p.getString("memory", "") ?: ""
        set(v) { p.edit().putString("memory", v).apply() }
    var history: String
        get() = p.getString("history", "[]") ?: "[]"
        set(v) { p.edit().putString("history", v).apply() }
}
