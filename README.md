# A.P.E.X. — Advanced Processing & Execution Xpert
Created by Abdullah Bin Elias.

## Build APK
1. Create a GitHub repository.
2. Extract this ZIP and upload every item inside it (including the hidden `.github` folder) to the repository root.
3. Open Actions → **Build A.P.E.X. Android APK** → Run workflow.
4. Download the `APEX-Android-debug` artifact from a successful run.
5. Extract `app-debug.apk` and install it on your Android phone.

## Connect the AI brain
Open A.P.E.X. → Settings. Enter an HTTPS Chat Completions endpoint, supported model name, and API key from your AI provider. Default endpoint is OpenAI-compatible `https://api.openai.com/v1/chat/completions`. No API key is included. API usage may cost money. Do not share your key. For a public production app, use a secure backend proxy instead of embedding a personal key in an APK.

## Included
- Native Kotlin Android chat app.
- Configurable AI Chat Completions API.
- Local chat context and simple local memory.
- Bengali/English text and speech input modes.
- Male/Female/Auto voice preference, using installed TTS voices when available.
- JARVIS / FRIDAY / EDITH personality prompt presets.
- Launch apps by installed app label.
- Settings, camera, dialer, Google search, basic media key and volume commands.
- GitHub Actions debug APK build workflow.

## Example voice commands
- Switch to male voice / পুরুষ কণ্ঠ চালু করো
- Switch to female voice / নারী কণ্ঠ চালু করো
- Speak Bangla / বাংলায় কথা বলো
- Speak English / ইংরেজিতে কথা বলো
- Remember that ... / মনে রেখো ...
- Forget my memory / মেমোরি মুছে দাও
- Open YouTube / YouTube খোলো
- Next song / পরের গান
- Pause music / গান থামাও
- Search ... / সার্চ ...

## Important limitations
This is a functional first native foundation, **not yet equivalent to ChatGPT/Gemini or movie JARVIS**.
- AI requires an API key, internet connection and a compatible provider. Model capabilities, knowledge, speed and cost depend on that provider.
- The app does not include credentials or a provider subscription.
- Web search command opens Google in a browser; grounded AI web-search citations are not integrated yet.
- Voice recognition starts when the microphone is tapped. Always-on wake-word/background listening is not implemented.
- Mixed Bengali-English speech recognition depends on the Android recognition service. Select Bangla or English in Settings for more reliable recognition.
- Android TTS gender and language availability depend on installed voice data. The app tries to select a matching installed voice; if unavailable, Android uses a fallback voice.
- Media key events may be ignored by some music apps or Android versions.
- Android security restrictions prevent unrestricted control of every app or access to private app data.
- Memory/history are stored locally in app preferences, not encrypted or cloud-synced.
- Debug APK is for testing. A signed release build and secure backend are needed for public distribution.

## Technical
Android minSdk 26, targetSdk 35, Kotlin, Java 17, Gradle 8.9.
No Firebase required for this version.
