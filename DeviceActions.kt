package com.abdullah.apex

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.view.KeyEvent
import java.util.Locale

class DeviceActions(private val context: Context) {
    fun run(command: String): String? {
        val c = command.lowercase(Locale.ROOT)
        when {
            has(c, "open settings", "সেটিংস খোলো", "সেটিংস ওপেন") -> {
                context.startActivity(Intent(android.provider.Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                return "সেটিংস খুলছি।"
            }
            has(c, "open camera", "ক্যামেরা খোলো") -> {
                context.startActivity(Intent("android.media.action.IMAGE_CAPTURE").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                return "ক্যামেরা খুলছি।"
            }
            has(c, "open phone", "open dialer", "ফোন খোলো", "ডায়ালার খোলো") -> {
                context.startActivity(Intent(Intent.ACTION_DIAL).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                return "ফোন ডায়ালার খুলছি।"
            }
            has(c, "play music", "resume music", "গান চালাও", "মিউজিক চালাও") -> {
                media(KeyEvent.KEYCODE_MEDIA_PLAY); return "মিউজিক চালানোর কমান্ড পাঠিয়েছি।"
            }
            has(c, "pause music", "stop music", "গান থামাও", "মিউজিক বন্ধ") -> {
                media(KeyEvent.KEYCODE_MEDIA_PAUSE); return "মিউজিক Pause করার কমান্ড পাঠিয়েছি।"
            }
            has(c, "next song", "next track", "পরের গান", "নেক্সট গান") -> {
                media(KeyEvent.KEYCODE_MEDIA_NEXT); return "পরের গান চালানোর কমান্ড পাঠিয়েছি।"
            }
            has(c, "previous song", "previous track", "আগের গান", "প্রিভিয়াস গান") -> {
                media(KeyEvent.KEYCODE_MEDIA_PREVIOUS); return "আগের গান চালানোর কমান্ড পাঠিয়েছি।"
            }
            has(c, "volume up", "ভলিউম বাড়াও", "শব্দ বাড়াও") -> {
                audio().adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI)
                return "ভলিউম বাড়িয়েছি।"
            }
            has(c, "volume down", "ভলিউম কমাও", "শব্দ কমাও") -> {
                audio().adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI)
                return "ভলিউম কমিয়েছি।"
            }
            c.startsWith("search ") || c.startsWith("সার্চ ") || c.startsWith("গুগলে ") -> {
                val q = command.replaceFirst(Regex("(?i)^(search|সার্চ|গুগলে)\\s*"), "")
                context.startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/search?q=" + Uri.encode(q))).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                return "ওয়েবে খুঁজছি: $q"
            }
        }
        val appName = extractAppName(command)
        if (appName != null) {
            val launch = findLaunchIntent(appName)
            if (launch != null) {
                launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(launch)
                return "$appName খুলছি।"
            }
        }
        return null
    }

    private fun extractAppName(raw: String): String? {
        val prefixes = listOf("open ", "launch ", "খোলো ", "চালু করো ", "ওপেন ")
        val lower = raw.lowercase(Locale.ROOT)
        val prefix = prefixes.firstOrNull { lower.startsWith(it) } ?: return null
        return raw.substring(prefix.length).trim().takeIf { it.isNotBlank() }
    }

    private fun findLaunchIntent(label: String): Intent? {
        val pm = context.packageManager
        val apps = pm.queryIntentActivities(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0)
        val found = apps.firstOrNull { it.loadLabel(pm).toString().equals(label, true) }
            ?: apps.firstOrNull { it.loadLabel(pm).toString().contains(label, true) }
        return found?.let { pm.getLaunchIntentForPackage(it.activityInfo.packageName) }
    }

    private fun audio() = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private fun media(code: Int) {
        val am = audio()
        am.dispatchMediaKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, code))
        am.dispatchMediaKeyEvent(KeyEvent(KeyEvent.ACTION_UP, code))
    }
    private fun has(value: String, vararg keys: String) = keys.any { value.contains(it) }
}
