<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String username    = (String) session.getAttribute("user");
    String userInitial = (username != null && username.length() > 0)
                         ? username.substring(0, 1).toUpperCase() : "U";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AI Assistant &mdash; Ocean View Resort</title>
    <%-- NO inline styles — all chat components defined in style.css using system tokens --%>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body data-page="chat">

<div class="page-overlay"></div>
<div class="page-content">
    <div class="inner-shell">
        <div class="content-col">

            <!-- Topbar -->
            <div class="topbar">
                <div class="topbar-brand">
                    <span class="rn">
                        <i class="fas fa-umbrella-beach" style="color:var(--gold); margin-right:8px;"></i>Ocean View Resort
                    </span>
                    <span class="wt">AI Assistant</span>
                </div>
                <div class="topbar-actions">
                    <a href="reservation?action=list" class="btn btn-ghost">
                        <i class="fas fa-arrow-left"></i> Dashboard
                    </a>
                    <button onclick="clearChat()" class="btn btn-danger">
                        <i class="fas fa-trash"></i> Clear Chat
                    </button>
                </div>
            </div>

            <!-- Chat Wrapper — uses system .chat-wrap / .chat-hd / .chat-msgs classes -->
            <div class="chat-wrap">

                <!-- Header -->
                <div class="chat-hd">
                    <div class="chat-hd-icon">
                        <i class="fas fa-robot"></i>
                    </div>
                    <div>
                        <div class="chat-hd-title">Ocean View AI Assistant</div>
                        <div class="chat-hd-sub">
                            <span class="chat-online-dot"></span> Online &mdash; Here to help 24/7
                        </div>
                    </div>
                </div>

                <!-- Message Area -->
                <div class="chat-msgs" id="chatMessages">
                    <!-- Welcome message -->
                    <div class="chat-msg ai">
                        <div class="chat-av"><i class="fas fa-robot"></i></div>
                        <div class="chat-bubble">
                            Welcome to Ocean View Resort! 🌊 I'm your AI assistant. How can I help you today?
                            You can ask me about room types, pricing, amenities, or any hotel information.
                        </div>
                    </div>
                </div>

                <!-- Typing Indicator -->
                <div class="chat-typing" id="typingIndicator">
                    <i class="fas fa-circle-notch fa-spin"></i> AI is thinking&hellip;
                </div>

                <!-- Suggestion Chips -->
                <div class="chat-chips" id="suggestions"></div>

                <!-- Input Row -->
                <div class="chat-in">
                    <input type="text" id="messageInput"
                           placeholder="Type your message&hellip;"
                           onkeypress="if(event.key==='Enter') sendMessage()">
                    <button onclick="sendMessage()" id="sendBtn">
                        <i class="fas fa-paper-plane"></i> Send
                    </button>
                </div>

            </div>
            <!-- /.chat-wrap -->

        </div>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        loadSuggestions();
        document.getElementById('messageInput').focus();
    });

    /* ── Suggestion chips ── */
    function loadSuggestions() {
        fetch('chat?action=suggestions')
            .then(r => r.json())
            .then(data => {
                if (!data.success) return;
                const container = document.getElementById('suggestions');
                container.innerHTML = data.suggestions
                    .map(s => '<div class="chip" onclick="useSuggestion(\'' + escapeHtml(s) + '\')">' + escapeHtml(s) + '</div>')
                    .join('');
            })
            .catch(err => console.error('Failed to load suggestions:', err));
    }

    function useSuggestion(text) {
        document.getElementById('messageInput').value = text;
        sendMessage();
    }

    /* ── Send message ── */
    function sendMessage() {
        const input   = document.getElementById('messageInput');
        const sendBtn = document.getElementById('sendBtn');
        const message = input.value.trim();
        if (!message) return;

        input.disabled   = true;
        sendBtn.disabled = true;

        addMessage(message, 'user');
        input.value = '';
        scrollToBottom();

        document.getElementById('typingIndicator').classList.add('active');

        const body = new URLSearchParams();
        body.append('message', message);

        fetch('chat', {
            method:  'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body:    body.toString()
        })
        .then(r => r.json())
        .then(data => {
            document.getElementById('typingIndicator').classList.remove('active');
            addMessage(data.success ? data.message : 'Sorry, I encountered an error. Please try again.', 'ai');
            input.disabled   = false;
            sendBtn.disabled = false;
            input.focus();
        })
        .catch(() => {
            document.getElementById('typingIndicator').classList.remove('active');
            addMessage('Connection error. Please check your internet and try again.', 'ai');
            input.disabled   = false;
            sendBtn.disabled = false;
        });
    }

    /* ── Render a message bubble ── */
    function addMessage(text, type) {
        const container = document.getElementById('chatMessages');
        const div       = document.createElement('div');
        div.className   = 'chat-msg ' + type;

        const avatarContent = (type === 'ai')
            ? '<i class="fas fa-robot"></i>'
            : '<%= userInitial %>';

        div.innerHTML =
            '<div class="chat-av">' + avatarContent + '</div>' +
            '<div class="chat-bubble">' + formatMessage(escapeHtml(text)) + '</div>';

        container.appendChild(div);
        scrollToBottom();
    }

    /* ── Markdown-lite formatting ── */
    function formatMessage(text) {
        return text
            .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
            .replace(/\*(.*?)\*/g,     '<em>$1</em>')
            .replace(/\n/g,            '<br>');
    }

    /* ── XSS guard ── */
    function escapeHtml(text) {
        const d = document.createElement('div');
        d.textContent = text;
        return d.innerHTML;
    }

    function scrollToBottom() {
        const c = document.getElementById('chatMessages');
        c.scrollTop = c.scrollHeight;
    }

    /* ── Clear history ── */
    function clearChat() {
        if (!confirm('Clear all chat history?')) return;
        fetch('chat?action=clear', { method: 'GET' })
            .then(() => location.reload())
            .catch(() => alert('Failed to clear chat'));
    }
</script>

</body>
</html>