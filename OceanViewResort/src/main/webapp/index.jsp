<%-- Redirect to login page --%>
    <% if (session.getAttribute("user") !=null) { response.sendRedirect("reservation?action=list"); } else {
        response.sendRedirect("login.jsp"); } %>