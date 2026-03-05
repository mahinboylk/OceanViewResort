package com.oceanview.service;

import com.oceanview.config.AppConfig;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * AI Chat Service - OpenRouter Integration
 * 
 * DESIGN PATTERN: Service Layer
 * - Encapsulates AI communication logic
 * - Abstracts API complexity from controllers
 * 
 * PRINCIPLE: Single Responsibility
 * - Only responsible for AI chat communication
 * 
 * PRINCIPLE: Open/Closed
 * - Can switch AI providers by changing config
 * - Can add new features without modifying existing code
 */
public class AIChatService {
    
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    
    // Shared HTTP client with timeouts
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    
    private final AppConfig config;
    
    // System prompt for hotel assistant persona
    private static final String SYSTEM_PROMPT = """
        You are the AI Assistant for Ocean View Resort, a luxury beachside hotel in Galle, Sri Lanka.
        
        YOUR ROLE:
        - Help guests with reservations, inquiries, and hotel information
        - Provide friendly, professional customer service
        - Know room types and pricing in Sri Lankan Rupees (LKR):
          * Standard: Rs. 25,000/night
          * Deluxe: Rs. 35,000/night
          * Suite: Rs. 50,000/night
          * Ocean View: Rs. 75,000/night
          * Presidential Suite: Rs. 150,000/night
        - Hotel has 20 rooms total
        - Check-in time: 2:00 PM, Check-out time: 11:00 AM
        - Contact: support@oceanviewresort.lk
        - Located in Galle, Sri Lanka (beautiful beachside location)
        
        RESPONSE GUIDELINES:
        - Keep responses concise (2-3 sentences max for chat)
        - Be warm and welcoming
        - For booking questions, direct guests to the reservation system
        - For complaints, apologize and offer to connect with management
        - Never make up prices or policies you don't know
        - Use emojis occasionally to be friendly
        
        LIMITATIONS:
        - You cannot actually make reservations (direct guests to staff)
        - You cannot process payments
        - You cannot access guest personal information
        """;
    
    /**
     * Constructor
     */
    public AIChatService() {
        this.config = AppConfig.getInstance();
    }
    
    /**
     * Send message to AI and get response
     * 
     * @param userMessage The user's message
     * @param conversationHistory Previous messages for context
     * @return AI response string
     */
    public String chat(String userMessage, List<Map<String, String>> conversationHistory) {
        // Always try the API first if configured
        if (config.isApiKeyConfigured()) {
            try {
                JSONObject requestBody = buildRequestBody(userMessage, conversationHistory);
                System.out.println("=== AI Chat Request ===");
                System.out.println("API URL: " + config.getApiUrl());
                System.out.println("Model: " + config.getModel());
                System.out.println("Request Body: " + requestBody.toString().substring(0, Math.min(500, requestBody.toString().length())) + "...");
                
                Request request = new Request.Builder()
                        .url(config.getApiUrl())
                        .addHeader("Authorization", "Bearer " + config.getApiKey())
                        .addHeader("Content-Type", "application/json")
                        .addHeader("HTTP-Referer", config.getAppUrl())
                        .addHeader("X-Title", config.getAppName())
                        .post(RequestBody.create(requestBody.toString(), JSON))
                        .build();
                
                try (Response response = client.newCall(request).execute()) {
                    System.out.println("=== API Response ===");
                    System.out.println("Status: " + response.code() + " " + response.message());
                    
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        System.out.println("Response Body: " + responseBody.substring(0, Math.min(500, responseBody.length())) + "...");
                        String parsed = parseResponse(responseBody);
                        if (parsed != null && !parsed.toLowerCase().contains("apologize")) {
                            return parsed;
                        }
                        // If parsed is null or contains "apologize", fall through to fallback
                    } else {
                        String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                        System.err.println("API Error (" + response.code() + "): " + errorBody);
                    }
                    
                    // API call failed or returned error message, use fallback
                    System.out.println("Using fallback response");
                    return getFallbackResponse(userMessage);
                }
            } catch (Exception e) {
                System.err.println("=== Chat Error ===");
                System.err.println("Message: " + e.getMessage());
                e.printStackTrace();
                
                // Exception occurred, use fallback
                return getFallbackResponse(userMessage);
            }
        }
        
        // API not configured, use fallback
        return getFallbackResponse(userMessage);
    }
    
    /**
     * Fallback rule-based responses for common questions
     */
    public String getFallbackResponse(String userMessage) {
        String msg = userMessage.toLowerCase();
        
        if (msg.contains("room") && msg.contains("type") || msg.contains("available")) {
            return "We offer 5 luxurious room types at Ocean View Resort:\n" +
                   "- Standard: Rs. 25,000/night\n" +
                   "- Deluxe: Rs. 35,000/night\n" +
                   "- Suite: Rs. 50,000/night\n" +
                   "- Ocean View: Rs. 75,000/night\n" +
                   "- Presidential Suite: Rs. 150,000/night\n" +
                   "\nPlease contact our staff at " + config.getSupportEmail() + " to check availability.";
        }
        
        if (msg.contains("price") || msg.contains("cost")) {
            return "Our room rates start from Rs. 25,000 per night for a Standard room, " +
                   "up to Rs. 150,000 per night for our Presidential Suite. Rates include " +
                   "breakfast and all taxes. For special packages, please contact us.";
        }
        
        if (msg.contains("check-in") || msg.contains("checkout")) {
            return "Check-in time is 2:00 PM and checkout time is 11:00 AM. " +
                   "Early check-in or late checkout may be available upon request, subject to availability.";
        }
        
        if (msg.contains("amenity") || msg.contains("facility")) {
            return "Our hotel offers a wide range of amenities including:\n" +
                   "- Infinity pool with ocean views\n" +
                   "- Fine dining restaurant\n" +
                   "- Spa and wellness center\n" +
                   "- 24-hour room service\n" +
                   "- Fitness center\n" +
                   "- Business center\n" +
                   "- Free WiFi throughout the property";
        }
        
        if (msg.contains("beach") || msg.contains("location")) {
            return "Ocean View Resort is located right on the beautiful beaches of Galle, Sri Lanka. " +
                   "The beach is just a 2-minute walk from the hotel lobby.";
        }
        
        if (msg.contains("cancel") || msg.contains("refund")) {
            return "Cancellation policies vary by reservation type. For cancellations made at least 7 days " +
                   "before check-in, we offer a full refund. For cancellations made within 7 days, a " +
                   "50% cancellation fee applies. Please contact our staff for specific details.";
        }
        
        if (msg.contains("hello") || msg.contains("hi") || msg.contains("greeting")) {
            return "Hello! Welcome to Ocean View Resort in Galle, Sri Lanka! " +
                   "I'm here to help with your reservation, room inquiries, or any questions about our hotel. " +
                   "What can I assist you with today?";
        }
        
        if (msg.contains("contact") || msg.contains("email") || msg.contains("phone")) {
            return "You can reach us at:\n" +
                   "Email: " + config.getSupportEmail() + "\n" +
                   "Phone: +94 91 234 5678\n" +
                   "Address: Galle Road, Unawatuna, Galle, Sri Lanka";
        }
        
        if (msg.contains("breakfast")) {
            return "Breakfast is included in all room rates and is served from 6:30 AM to 10:30 AM. " +
                   "We offer a delicious spread of Sri Lankan and international cuisine.";
        }
        
        if (msg.contains("airport") || msg.contains("pickup")) {
            return "We offer airport pickup services from Bandaranaike International Airport (BIA) " +
                   "for Rs. 8,000 per vehicle. Please contact us 24 hours in advance to arrange this service.";
        }
        
        if (msg.contains("wifi") || msg.contains("internet")) {
            return "Free high-speed WiFi is available throughout the hotel, including all guest rooms " +
                   "and public areas. The network name is \"OceanViewResort\" and no password is required.";
        }
        
        if (msg.contains("location") || msg.contains("address")) {
            return "Ocean View Resort is located on Galle Road in Unawatuna, Galle, Sri Lanka. " +
                   "We're just 15 minutes from Galle Fort and 2 hours from Colombo. " +
                   "The beach is right at our doorstep!";
        }
        
        if (msg.contains("parking")) {
            return "Yes, we offer free parking for all guests! We have a secure underground parking area " +
                   "with 24-hour security. No reservation needed - parking is complimentary for all hotel guests.";
        }
        
        // Default fallback - always friendly and helpful
        return "Welcome to Ocean View Resort! I'm here to help you with reservations and hotel information.\n\n" +
               "Here are some things I can help you with:\n" +
               "- Room types and pricing\n" +
               "- Check-in/check-out times\n" +
               "- Hotel amenities and facilities\n" +
               "- Location and directions\n" +
               "- Booking inquiries\n\n" +
               "Please feel free to ask me any questions!";
    }
    
    /**
     * Build the JSON request body for the API
     */
    private JSONObject buildRequestBody(String userMessage, List<Map<String, String>> history) {
        JSONObject body = new JSONObject();
        body.put("model", config.getModel());
        
        JSONArray messages = new JSONArray();
        
        // Add system prompt
        JSONObject systemMsg = new JSONObject();
        systemMsg.put("role", "system");
        systemMsg.put("content", SYSTEM_PROMPT);
        messages.put(systemMsg);
        
        // Add conversation history (last 10 messages for context)
        int start = Math.max(0, history.size() - 10);
        for (int i = start; i < history.size(); i++) {
            Map<String, String> msg = history.get(i);
            JSONObject msgObj = new JSONObject();
            msgObj.put("role", msg.get("role"));
            msgObj.put("content", msg.get("content"));
            messages.put(msgObj);
        }
        
        // Add current user message
        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        messages.put(userMsg);
        
        body.put("messages", messages);
        body.put("max_tokens", 500);
        body.put("temperature", 0.7);
        
        return body;
    }
    
    /**
     * Parse the API response
     */
    private String parseResponse(String responseBody) {
        try {
            JSONObject json = new JSONObject(responseBody);
            JSONArray choices = json.getJSONArray("choices");
            
            if (choices.length() > 0) {
                JSONObject firstChoice = choices.getJSONObject(0);
                JSONObject message = firstChoice.getJSONObject("message");
                return message.getString("content");
            }
            
        } catch (Exception e) {
            System.err.println("Error parsing response: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get suggested questions for guests
     * @return List of suggested questions
     */
    public List<String> getSuggestedQuestions() {
        List<String> suggestions = new ArrayList<>();
        suggestions.add("What room types are available?");
        suggestions.add("What are your check-in/check-out times?");
        suggestions.add("What are the room prices?");
        suggestions.add("Is breakfast included?");
        suggestions.add("Do you offer airport pickup?");
        suggestions.add("Can I cancel my reservation?");
        suggestions.add("What amenities do you have?");
        suggestions.add("How far is the beach?");
        suggestions.add("What is your location?");
        suggestions.add("Do you have parking?");
        return suggestions;
    }
    
    /**
     * Check if the AI service is available
     * @return true if API key is configured
     */
    public boolean isAvailable() {
        return config.isApiKeyConfigured();
    }
}
