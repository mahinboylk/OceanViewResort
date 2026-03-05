package com.oceanview.controller;

import com.oceanview.service.AIChatService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Chat Servlet - Handles AI Chat Requests
 * 
 * DESIGN PATTERN: MVC Controller
 * - Receives HTTP requests
 * - Delegates to service layer
 * - Returns JSON responses
 * 
 * PRINCIPLE: Single Responsibility
 * - Only handles HTTP request/response for chat
 * 
 * PRINCIPLE: Dependency Inversion
 * - Depends on AIChatService abstraction
 */
@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private AIChatService chatService;
    
    @Override
    public void init() throws ServletException {
        chatService = new AIChatService();
    }
    
    /**
     * Handle GET requests
     * - Show chat page
     * - Get suggestions
     * - Clear chat history
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check authentication
        if (!isLoggedIn(request)) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("suggestions".equals(action)) {
            getSuggestions(response);
        } else if ("clear".equals(action)) {
            clearChatHistory(request, response);
        } else if ("status".equals(action)) {
            getStatus(response);
        } else {
            // Show chat page
            request.getRequestDispatcher("chat.jsp").forward(request, response);
        }
    }
    
    /**
     * Handle POST requests - Send message to AI
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check authentication
        if (!isLoggedIn(request)) {
            sendError(response, "Not authenticated");
            return;
        }
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Get user message
            String message = request.getParameter("message");
            if (message == null || message.trim().isEmpty()) {
                sendError(response, "Message cannot be empty");
                return;
            }
            
            // Get conversation history from session
            @SuppressWarnings("unchecked")
            List<Map<String, String>> history = (List<Map<String, String>>) 
                request.getSession().getAttribute("chatHistory");
            
            if (history == null) {
                history = new ArrayList<>();
            }
            
            // Add user message to history
            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", message.trim());
            history.add(userMsg);
            
            // Get AI response
            String aiResponse = chatService.chat(message, history);
            
            // Add AI response to history
            Map<String, String> aiMsg = new HashMap<>();
            aiMsg.put("role", "assistant");
            aiMsg.put("content", aiResponse);
            history.add(aiMsg);
            
            // Limit history to last 20 messages to prevent memory issues
            if (history.size() > 20) {
                history = new ArrayList<>(history.subList(history.size() - 20, history.size()));
            }
            
            // Save back to session
            request.getSession().setAttribute("chatHistory", history);
            
            // Send success response
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", true);
            jsonResponse.put("message", aiResponse);
            jsonResponse.put("timestamp", System.currentTimeMillis());
            
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
            sendError(response, "Failed to process message: " + e.getMessage());
        }
    }
    
    /**
     * Get suggested questions
     */
    private void getSuggestions(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        List<String> suggestions = chatService.getSuggestedQuestions();
        
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", true);
        jsonResponse.put("suggestions", new JSONArray(suggestions));
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }
    
    /**
     * Clear chat history
     */
    private void clearChatHistory(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        request.getSession().removeAttribute("chatHistory");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", true);
        jsonResponse.put("message", "Chat history cleared");
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }
    
    /**
     * Get AI service status
     */
    private void getStatus(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", true);
        jsonResponse.put("available", chatService.isAvailable());
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }
    
    /**
     * Send error response
     */
    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("success", false);
        errorResponse.put("error", message);
        
        PrintWriter out = response.getWriter();
        out.print(errorResponse.toString());
        out.flush();
    }
    
    /**
     * Check if user is logged in
     */
    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("user") != null;
    }
}