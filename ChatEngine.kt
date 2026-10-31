package com.abdullah.apex

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ChatEngine(private val prefs: ApexPrefs) {
    fun ask(userText: String): String {
        if (prefs.apiKey.isBlank()) return "A.P.E.X.-এর AI Brain সংযুক্ত হয়নি। Settings-এ AI provider-এর API key, endpoint ও model দিন।"
        if (!prefs.baseUrl.startsWith("https://")) return "নিরাপত্তার জন্য HTTPS API endpoint ব্যবহার করুন।"
        var connection: HttpURLConnection? = null
        return try {
            connection = (URL(prefs.baseUrl).openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 20000
                readTimeout = 60000
                doOutput = true
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Authorization", "Bearer ${prefs.apiKey}")
            }
            val messages = JSONArray()
            val systemPrompt = "You are A.P.E.X. (Advanced Processing & Execution Xpert), a capable, warm, emotionally intelligent personal AI assistant created by Abdullah Bin Elias. Be honest, helpful, thoughtful and natural. Never claim an action was done unless it was. Understand Bengali, English and mixed Bengali-English. Reply in the language the user used. Be concise unless detail is requested. Explain limitations honestly. User memory/context: ${prefs.memory}. Personality mode: ${prefs.personality}."
            messages.put(JSONObject().put("role", "system").put("content", systemPrompt))
            try {
                val saved = JSONArray(prefs.history)
                val start = (saved.length() - 16).coerceAtLeast(0)
                for (i in start until saved.length()) messages.put(saved.getJSONObject(i))
            } catch (_: Exception) {}
            messages.put(JSONObject().put("role", "user").put("content", userText))
            val payload = JSONObject().put("model", prefs.model).put("messages", messages).put("temperature", 0.7)
            connection.outputStream.use { it.write(payload.toString().toByteArray(Charsets.UTF_8)) }
            val code = connection.responseCode
            val stream = if (code in 200..299) connection.inputStream else connection.errorStream
            val body = stream.bufferedReader().use { it.readText() }
            if (code !in 200..299) {
                val detail = try { JSONObject(body).optJSONObject("error")?.optString("message") } catch (_: Exception) { null }
                return "AI service error ($code): ${detail ?: body.take(250)}"
            }
            val reply = JSONObject(body).getJSONArray("choices").getJSONObject(0)
                .getJSONObject("message").optString("content").trim()
            val history = JSONArray()
            val old = try { JSONArray(prefs.history) } catch (_: Exception) { JSONArray() }
            val start = (old.length() - 14).coerceAtLeast(0)
            for (i in start until old.length()) history.put(old.getJSONObject(i))
            history.put(JSONObject().put("role", "user").put("content", userText))
            history.put(JSONObject().put("role", "assistant").put("content", reply))
            prefs.history = history.toString()
            reply.ifBlank { "আমি উত্তর তৈরি করতে পারিনি। আবার চেষ্টা করুন।" }
        } catch (e: Exception) {
            "সংযোগে সমস্যা হয়েছে: ${e.message ?: "Unknown error"}। ইন্টারনেট ও API settings পরীক্ষা করুন।"
        } finally { connection?.disconnect() }
    }
}
