<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // Observer pattern: observing session state
    if (session.getAttribute("user") != null) {
        // User is logged in, redirect to home
        response.sendRedirect("home.jsp");
    } else {
        // User is not logged in, redirect to login page
        response.sendRedirect("login.jsp");
    }
%>