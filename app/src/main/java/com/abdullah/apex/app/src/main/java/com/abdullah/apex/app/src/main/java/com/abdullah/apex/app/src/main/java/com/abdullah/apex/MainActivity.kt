<!DOCTYPE html>
<html lang="bn">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no">
<meta name="theme-color" content="#080d18">
<title>APCHAT — Aurora Messenger</title>
<meta name="description" content="APCHAT — private chats, stories and groups.">
<link rel="manifest" href="./manifest.json">
<script src="https://cdn.tailwindcss.com"></script>
<script src="https://unpkg.com/lucide@latest"></script>
<script src="https://www.gstatic.com/firebasejs/9.22.1/firebase-app-compat.js"></script>
<script src="https://www.gstatic.com/firebasejs/9.22.1/firebase-auth-compat.js"></script>
<script src="https://www.gstatic.com/firebasejs/9.22.1/firebase-database-compat.js"></script>
<style>
*{box-sizing:border-box}body{margin:0;background:#030712;color:#f9fafb;font-family:Arial,sans-serif;height:100dvh;overflow:hidden;-webkit-tap-highlight-color:transparent}
.hidden{display:none!important}button{cursor:pointer}button:disabled{opacity:.5;cursor:not-allowed}input,select,textarea{outline:none}
.brand{background:linear-gradient(120deg,#2563eb,#7c3aed)}#authScreen,#app{height:100dvh;width:100%;overflow:hidden}
#sidebar{width:100%;max-width:420px}@media(min-width:700px){#sidebar{width:350px}}
#messagesContainer{overflow-y:auto;scroll-behavior:smooth;background-size:cover;background-position:center;background-attachment:local}.chat-menu{position:fixed;z-index:2500;background:#111827;border:1px solid #4b5563;border-radius:14px;padding:8px;box-shadow:0 12px 35px #0009;min-width:190px}.chat-menu button{display:block;width:100%;text-align:left;padding:10px;border-radius:8px;font-size:13px}.chat-menu button:active{background:#374151}.apStory{flex:0 0 66px;text-align:center;font-size:11px;color:#d1d5db;overflow:hidden}
.apStory img{width:55px;height:55px;border-radius:50%;object-fit:cover;border:3px solid #8b5cf6;padding:2px}
.bubble{max-width:86%;min-width:90px;padding:10px 12px;border-radius:18px;overflow-wrap:anywhere}.emoji-only{max-width:100%;min-width:0;padding:0;background:transparent!important;border:0;box-shadow:none}.emoji-only-content{font-family:Arial,sans-serif;line-height:1.25;white-space:pre-wrap;overflow-wrap:anywhere;text-shadow:0 1px 2px #0005}.mine{background:#2563eb;border-bottom-right-radius:5px}.theirs{background:#1f2937;border-bottom-left-radius:5px}
.bubble img,.bubble video{max-width:100%;border-radius:10px;margin-top:5px}
.bubble .media-thumb{display:block;width:96px;height:96px;max-width:96px;max-height:96px;object-fit:cover;border-radius:10px;margin-top:6px;cursor:pointer;background:#111827}
.bubble .video-thumb-wrap{position:relative;width:96px;height:96px;margin-top:6px;cursor:pointer}
.bubble .video-thumb-wrap video{display:block;width:96px;height:96px;max-width:96px;max-height:96px;object-fit:cover;margin:0;border-radius:10px;pointer-events:none}
.bubble .video-thumb-play{position:absolute;inset:0;display:flex;align-items:center;justify-content:center;font-size:30px;text-shadow:0 2px 8px #000;pointer-events:none}
#mediaViewer{position:fixed;inset:0;z-index:3000;background:#000;display:flex;align-items:center;justify-content:center;padding:12px}
#mediaViewer.hidden{display:none!important}#mediaViewerContent{max-width:100%;max-height:100%;width:auto;height:auto;object-fit:contain}
#mediaViewerClose{position:absolute;top:env(safe-area-inset-top,10px);right:14px;z-index:2;width:42px;height:42px;border-radius:50%;background:#333c;color:white;font-size:28px;line-height:1}.privacy-choice{display:flex;align-items:flex-start;gap:10px;padding:12px;border:1px solid #374151;border-radius:12px;background:#111827}
.privacy-choice:has(input:checked){border-color:#3b82f6;background:#172554}
#storyModal{position:fixed;inset:0;background:#000e;z-index:1500;display:flex;align-items:center;justify-content:center;padding:14px;overflow-y:auto}
#storyCard{width:100%;max-width:520px;max-height:95vh;overflow-y:auto;background:#111827;border:1px solid #374151;border-radius:18px;padding:14px}
#storyMedia{max-width:100%;max-height:55vh;object-fit:contain;border-radius:12px;margin:auto}
#callNotice{position:fixed;inset:0;background:#000b;z-index:2000;display:flex;align-items:center;justify-content:center;padding:20px}
#apStories{display:flex;gap:12px;overflow-x:auto;padding:12px;border-bottom:1px solid #1f2937;min-height:94px}
.reply-quote{display:flex;align-items:center;gap:8px;padding:7px 9px;margin:0 0 8px;border-left:3px solid #93c5fd;border-radius:7px;background:#0003;cursor:pointer;text-align:left;width:100%;min-width:0}.reply-quote img,.reply-quote video{width:42px;height:42px;flex:0 0 42px;object-fit:cover;border-radius:6px;margin:0}.reply-quote .reply-quote-text{font-size:11px;line-height:1.35;overflow:hidden;text-overflow:ellipsis;display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical}.message-meta{font-size:10px;opacity:.68;margin:3px 5px 0}.message-row{display:flex;flex-direction:column;max-width:100%;margin:4px 0}.message-row.mine-row{align-items:flex-end}.message-row.their-row{align-items:flex-start}.message-highlight{outline:2px solid #facc15;box-shadow:0 0 0 5px #facc1533;transition:box-shadow .2s}.msg-tools button{font-size:11px;color:#dbeafe;margin-left:7px}.status-dot{width:8px;height:8px;border-radius:50%;display:inline-block;background:#22c55e}

.chat-topbar{min-height:60px}.chat-title-wrap{overflow:hidden;min-width:0}.chat-title-wrap #activeChatName{font-size:12px;line-height:1.2;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;display:block;max-width:100%;}.chat-title-wrap #activeChatBio{display:none!important}

/* Message interaction improvements */
.msg-tools{display:none!important}.message-meta{display:none}.message-meta.time-visible{display:block}.message-row.last-in-run .message-meta{display:block}.message-row .bubble,.message-row .emoji-only{touch-action:pan-y;user-select:none}.message-action-pop{position:fixed;z-index:4000;left:50%;transform:translateX(-50%);bottom:calc(82px + env(safe-area-inset-bottom));background:#111827;border:1px solid #475569;border-radius:18px;padding:10px;box-shadow:0 12px 35px #000b;max-width:calc(100vw - 24px)}.message-action-pop .quick-emojis{display:flex;gap:12px;justify-content:center;padding:4px 2px 10px}.message-action-pop button{padding:7px 10px;border-radius:9px;background:#263244;font-size:13px}.message-action-pop .quick-emojis button{font-size:25px;padding:2px;background:transparent}.group-chip{font-size:10px;color:#93c5fd;background:#1e3a8a66;border-radius:999px;padding:2px 7px}

/* APCHAT Aurora redesign — original visual system, Messenger-inspired information placement */
:root{--ap-bg:#080d18;--ap-panel:#101827;--ap-panel2:#151f30;--ap-line:#263348;--ap-text:#f4f7fb;--ap-muted:#93a4ba;--ap-cyan:#36d6d0;--ap-blue:#5687ff;--ap-violet:#8c70ff}
html,body{background:var(--ap-bg)!important;color:var(--ap-text);font-family:Inter,system-ui,-apple-system,"Segoe UI",Arial,sans-serif}
#app{background:radial-gradient(ellipse at 78% 0%,#18304b55,transparent 38%),var(--ap-bg)!important}
#sidebar{width:100%;max-width:410px;background:linear-gradient(180deg,#111c2c 0%,#0d1522 100%)!important;border-right:1px solid var(--ap-line)!important}
@media(min-width:700px){#sidebar{width:365px;max-width:365px}}
#sidebar>header{padding:18px 18px 12px!important;border:0!important;background:transparent!important}
#sidebar>header img{border-radius:14px!important;box-shadow:0 6px 18px #0005}
#sidebar>header h1,#sidebar>header .text-xl{letter-spacing:-.04em}
#sidebar .border-b,#sidebar .border-t{border-color:var(--ap-line)!important}
#sidebar nav{padding:4px 12px!important;gap:7px!important}
#sidebar nav button{border-radius:13px!important;padding:10px 13px!important;font-weight:650;transition:.18s}
#sidebar nav button:hover,#sidebar nav button:focus-visible{background:#24354c!important}
#apStories{min-height:112px;padding:14px 14px 12px!important;gap:15px!important;border-color:var(--ap-line)!important;scrollbar-width:none}
#apStories::-webkit-scrollbar{display:none}

.apStory{flex-basis:68px!important;color:#c4d0df!important;font-size:11px!important;font-weight:600}
.apStory img{width:58px!important;height:58px!important;border:2px solid var(--ap-cyan)!important;padding:2px!important;background:#101827;box-shadow:0 0 0 3px #36d6d01a}
#chatsView,#requestsView,#peopleView{padding:8px 12px 18px!important}
#chatsView button,#friendRequestsList button,#messageRequestsList button,#sentRequestsList button,#peopleView button{border-radius:15px!important;border:1px solid transparent!important;padding:12px!important;transition:background .15s,border-color .15s}
#chatsView button:hover,#friendRequestsList button:hover,#messageRequestsList button:hover,#sentRequestsList button:hover,#peopleView button:hover{background:#1b2a3d!important;border-color:#2b405c!important}
#groupList button{border:1px solid var(--ap-line)!important;background:#131f30!important;border-radius:13px!important;padding:11px!important}
#chatArea{background:#0b1220!important;min-width:0}
#emptyChatState{background:radial-gradient(circle at 50% 42%,#1a4260 0%,#0c1728 42%,#0b1220 72%)!important}
#emptyChatState>div{border-radius:28px!important;background:#15283b!important;border:1px solid #29435e!important;box-shadow:0 18px 50px #0005}
#activeChatInterface{background:linear-gradient(180deg,#0d1726,#0a1220)!important;min-width:0}
.chat-topbar{min-height:68px!important;padding:9px 14px!important;background:#111d2d!important;border-bottom:1px solid var(--ap-line)!important;box-shadow:0 5px 18px #0002;gap:10px!important}
.chat-topbar button{width:38px;height:38px;border-radius:50%;display:inline-flex;align-items:center;justify-content:center;font-size:18px!important;background:#1b2a3d!important;color:#dce8f6!important;flex-shrink:0}
.chat-topbar button:hover{background:#29415d!important}
#activeChatAvatar{width:42px!important;height:42px!important;border:2px solid #36d6d0!important;padding:1px}
.chat-title-wrap #activeChatName{font-size:15px!important;font-weight:750;letter-spacing:-.02em;color:#f6f9ff}
#activeChatBio{display:block!important;font-size:11px!important;color:#8ea4bd!important;line-height:1.25;margin-top:3px}
#typingIndicator{background:#101d2d!important;color:#5ee7df!important;padding:8px 18px!important}
#messagesContainer{padding:18px clamp(12px,3vw,34px) 22px!important;gap:9px!important;background-color:#0a1422!important;background-image:radial-gradient(circle at 12% 12%,#193a5030 0,transparent 30%),radial-gradient(circle at 88% 75%,#2a32602b 0,transparent 32%)!important;background-attachment:local!important}
.message-row{margin:5px 0!important}
.bubble{max-width:min(78%,520px)!important;padding:11px 15px!important;border-radius:19px!important;font-size:14px;line-height:1.45;box-shadow:0 3px 10px #0002}
.bubble.mine{background:linear-gradient(135deg,#247fbd,#3675e8)!important;color:#fff!important;border-bottom-right-radius:6px!important}
.bubble.theirs{background:#1a293b!important;color:#f0f5fc!important;border:1px solid #2a3b50!important;border-bottom-left-radius:6px!important}
.reply-quote{background:#07111e66!important;border-left-color:#47ded6!important;color:#e2edf8!important}
.message-meta{font-size:10px!important;color:#91a4ba!important;margin:3px 8px!important}
.message-action-pop{background:#111e2e!important;border:1px solid #36506c!important;border-radius:18px!important}
.message-action-pop button{background:#20334a!important;color:#f3f7fc!important}
#activeChatInterface footer{padding:12px 14px calc(12px + env(safe-area-inset-bottom))!important;background:#101b2a!important;border-top:1px solid var(--ap-line)!important}
#activeChatInterface footer>div:first-child{display:flex!important;align-items:center;gap:8px!important;margin:0 0 9px!important}
#activeChatInterface footer>div:first-child button{width:36px;height:36px;border-radius:50%;background:#1b2b40!important;display:inline-flex;align-items:center;justify-content:center;font-size:17px}
#activeChatInterface footer>div:first-child button:hover{background:#2b435d!important}
#activeChatInterface footer>div:last-child{align-items:flex-end!important;gap:8px!important}
#messageInput{min-height:46px!important;max-height:120px!important;background:#1a293b!important;border:1px solid #2b4058!important;border-radius:24px!important;padding:12px 17px!important;color:#f7fbff!important;font-size:14px!important}
#messageInput::placeholder{color:#8da1b8!important}
#quickReactionBtn{width:43px!important;height:43px!important;flex:0 0 43px;border-radius:50%!important;background:#1c3045!important;border:1px solid #304861!important}
#activeChatInterface footer>div:last-child>button:last-child{width:44px!important;height:44px!important;flex:0 0 44px;border-radius:50%!important;background:linear-gradient(135deg,var(--ap-cyan),#3b82f6)!important;color:#071321!important;font-size:18px}
#replyPreview{background:#17273a!important;border-left:3px solid var(--ap-cyan)!important;border-radius:10px!important}
#mediaViewer{background:#050a12f5!important}
#storyModal{background:#030711eF!important;backdrop-filter:blur(12px)}
#storyCard{background:#111d2d!important;border:1px solid #31445b!important;border-radius:24px!important}
#userProfileModal>div,#privacyModal>div,#storyPrivacyModal>div{background:#111d2d!important;border:1px solid #30445c!important;border-radius:24px!important}
#sidebar .brand,.brand{background:linear-gradient(120deg,#28c9c5,#477df5)!important;color:#06121e!important}
input,textarea,select{color-scheme:dark}
.chat-menu{background:#142236!important;border:1px solid #344b66!important;border-radius:16px!important;box-shadow:0 18px 45px #0009!important;padding:8px!important}
.chat-menu button{padding:12px!important;border-radius:10px!important;color:#e6eff9!important}
.chat-menu button:hover{background:#243b55!important}
.ap-attach-menu{position:absolute;z-index:2800;bottom:76px;left:14px;width:min(260px,calc(100vw - 28px));padding:8px;background:#172438;border:1px solid #38516d;border-radius:18px;box-shadow:0 18px 45px #000b}
.ap-attach-menu button{display:flex;align-items:center;gap:12px;width:100%;padding:12px;border-radius:11px;text-align:left;color:#e8f2fc;font-size:14px}
.ap-attach-menu button:hover{background:#263d58}
.ap-attach-menu button[disabled]{opacity:.42;cursor:not-allowed}
.ap-attach-menu .ap-attach-icon{width:30px;height:30px;display:grid;place-items:center;border-radius:10px;background:#243b56;color:#53ded6}
.ap-chat-info{position:fixed;inset:0;z-index:2700;background:#080d18;overflow:auto;color:#f2f6fb;padding:0 0 calc(22px + env(safe-area-inset-bottom));}
.ap-chat-info-head{height:58px;display:flex;align-items:center;justify-content:space-between;padding:0 18px;border-bottom:1px solid #26364b;background:#101b2b;position:sticky;top:0;z-index:1}
.ap-chat-info-head button{font-size:24px;width:38px;height:38px;border-radius:50%;background:#1c2d42}
.ap-chat-info-profile{text-align:center;padding:26px 18px 20px}
.ap-chat-info-profile img{width:104px;height:104px;object-fit:cover;border-radius:50%;margin:0 auto 14px;border:3px solid #39d5d0;padding:2px}
.ap-chat-info-shortcuts{display:flex;justify-content:center;gap:12px;margin:18px auto 4px;max-width:420px}
.ap-chat-info-shortcuts button{display:flex;flex-direction:column;align-items:center;gap:7px;min-width:66px;color:#c5d3e3;font-size:11px}
.ap-chat-info-shortcuts i{width:42px;height:42px;border-radius:50%;display:grid;place-items:center;background:#1c2d42;color:#4ce0d8;font-size:19px;font-style:normal}
.ap-info-section{padding:12px 18px 4px}
.ap-info-section h4{color:#8ea2bb;font-size:13px;font-weight:700;margin:8px 0 8px}
.ap-info-row{display:flex;align-items:center;gap:15px;width:100%;padding:14px 3px;text-align:left;border-bottom:1px solid #1c2b3e;color:#e6edf6;font-size:14px}
.ap-info-row .ap-info-icon{width:27px;text-align:center;color:#b7c9dc;font-size:19px}
.ap-info-row small{display:block;color:#8195ad;font-size:11px;margin-top:3px}
.ap-info-row.danger{color:#ff6477}
@media(max-width:699px){#sidebar{max-width:none;width:100%}#chatArea{position:absolute;inset:0;width:100%;z-index:5}#chatArea.hidden{display:none!important}.chat-topbar{padding:8px 9px!important;gap:6px!important}.chat-topbar button{width:35px;height:35px}.bubble{max-width:86%!important}}

</style>
</head>
<body>
<section id="authScreen" class="flex items-center justify-center p-4 bg-gray-950">
 <div class="w-full max-w-md bg-gray-900 border border-gray-800 rounded-3xl p-6 shadow-2xl">
