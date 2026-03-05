<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.oceanview.model.Reservation"%>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    @SuppressWarnings("unchecked")
    List<Reservation> results = (List<Reservation>) request.getAttribute("searchResults");

    String keyword  = (String) request.getAttribute("keyword");
    String roomType = (String) request.getAttribute("roomType");
    String status   = (String) request.getAttribute("status");
    String checkIn  = (String) request.getAttribute("checkIn");

    if (keyword  == null) keyword  = "";
    if (roomType == null) roomType = "";
    if (status   == null) status   = "";
    if (checkIn  == null) checkIn  = "";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Reservations &mdash; Ocean View Resort</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body data-page="search">

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
                    <span class="wt">Search Reservations</span>
                </div>
                <div class="topbar-actions">
                    <a href="reservation?action=list" class="btn btn-ghost">
                        <i class="fas fa-arrow-left"></i> Dashboard
                    </a>
                </div>
            </div>

            <!-- Search Form Panel -->
            <div class="panel">
                <div class="ph"><i class="fas fa-search"></i><h4>Search &amp; Filter</h4></div>
                <div class="pb">
                    <form action="reservation" method="get">
                        <input type="hidden" name="action" value="search">

                        <%-- 4 filter fields in auto-fit grid — wrap on small screens --%>
                        <div class="form-grid-auto" style="margin-bottom:1.1rem;">

                            <div class="fg" style="margin-bottom:0;">
                                <label><i class="fas fa-search"></i> Keyword</label>
                                <input type="text" name="keyword"
                                       placeholder="Guest name, ID, or contact"
                                       value="<%= keyword %>">
                            </div>

                            <div class="fg" style="margin-bottom:0;">
                                <label><i class="fas fa-bed"></i> Room Type</label>
                                <select name="roomType">
                                    <option value="">All Room Types</option>
                                    <option value="Standard"           <%= "Standard".equals(roomType)           ? "selected" : "" %>>Standard</option>
                                    <option value="Deluxe"             <%= "Deluxe".equals(roomType)             ? "selected" : "" %>>Deluxe</option>
                                    <option value="Suite"              <%= "Suite".equals(roomType)              ? "selected" : "" %>>Suite</option>
                                    <option value="Ocean View"         <%= "Ocean View".equals(roomType)         ? "selected" : "" %>>Ocean View</option>
                                    <option value="Presidential Suite" <%= "Presidential Suite".equals(roomType) ? "selected" : "" %>>Presidential Suite</option>
                                </select>
                            </div>

                            <div class="fg" style="margin-bottom:0;">
                                <label><i class="fas fa-info-circle"></i> Status</label>
                                <select name="status">
                                    <option value="">All Status</option>
                                    <option value="Active"    <%= "Active".equals(status)    ? "selected" : "" %>>Active</option>
                                    <option value="Cancelled" <%= "Cancelled".equals(status) ? "selected" : "" %>>Cancelled</option>
                                    <option value="Completed" <%= "Completed".equals(status) ? "selected" : "" %>>Completed</option>
                                </select>
                            </div>

                            <div class="fg" style="margin-bottom:0;">
                                <label><i class="fas fa-calendar"></i> Check-In Date</label>
                                <input type="date" name="checkIn" value="<%= checkIn %>">
                            </div>

                        </div>

                        <%-- Buttons in their own row — visible on light panel background --%>
                        <div class="btn-row" style="justify-content:flex-start;">
                            <button type="submit" class="btn btn-success">
                                <i class="fas fa-search"></i> Search
                            </button>
                            <a href="search.jsp" class="btn btn-secondary">
                                <i class="fas fa-redo"></i> Reset
                            </a>
                        </div>

                    </form>
                </div>
            </div>

            <!-- Results Banner — only rendered after a search -->
            <% if (results != null) { %>
            <div class="results-banner">
                <span>
                    <i class="fas fa-filter" style="margin-right:6px;"></i>
                    Found <strong><%= results.size() %></strong> reservation<%= results.size() != 1 ? "s" : "" %>
                </span>
                <a href="search.jsp"><i class="fas fa-times" style="margin-right:4px;"></i> Clear Search</a>
            </div>
            <% } %>

            <!-- Results Table Panel -->
            <div class="panel">
                <div class="ph"><i class="fas fa-list"></i><h4>Search Results</h4></div>

                <% if (results == null) { %>
                    <%-- Initial state — no search submitted yet --%>
                    <div class="no-results-state">
                        <i class="fas fa-search"></i>
                        <h3>Enter a Search Above</h3>
                        <p>Filter by guest name, room type, status, or check-in date — or combine multiple filters.</p>
                    </div>

                <% } else if (results.isEmpty()) { %>
                    <%-- Search ran but found nothing --%>
                    <div class="no-results-state">
                        <i class="fas fa-file-search"></i>
                        <h3>No Reservations Found</h3>
                        <p>Try adjusting your search criteria or clear all filters.</p>
                        <a href="search.jsp" class="btn btn-success" style="display:inline-flex; width:auto;">
                            <i class="fas fa-redo"></i> Reset Search
                        </a>
                    </div>

                <% } else { %>
                    <%-- Results found --%>
                    <div class="table-wrap">
                        <table>
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Guest Name</th>
                                    <th>Room Type</th>
                                    <th>Status</th>
                                    <th>Check-In</th>
                                    <th>Check-Out</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Reservation r : results) {
                                    String rt = (r.getRoomType() != null) ? r.getRoomType().toLowerCase() : "";
                                    /* Check presidential FIRST — its name also contains "suite" */
                                    String bc = rt.contains("presidential") ? "bp"
                                              : rt.contains("ocean")        ? "bo"
                                              : rt.contains("suite")        ? "bs"
                                              : rt.contains("deluxe")       ? "bd"
                                              :                               "bst";
                                    String st = r.getStatus();
                                    if (st == null) st = "Active";
                                    String sc = "Active".equals(st)    ? "bg-success"
                                              : "Cancelled".equals(st) ? "bg-danger"
                                              :                           "bg-info";
                                %>
                                <tr>
                                    <td><span class="tid">#<%= r.getReservationId() %></span></td>
                                    <td><strong><%= r.getGuestName() %></strong></td>
                                    <td><span class="badge <%= bc %>"><%= r.getRoomType() %></span></td>
                                    <td><span class="badge <%= sc %>"><%= st %></span></td>
                                    <td style="color:var(--mist);font-size:0.83rem;"><%= r.getCheckIn() %></td>
                                    <td style="color:var(--mist);font-size:0.83rem;"><%= r.getCheckOut() %></td>
                                    <td>
                                        <a href="reservation?action=view&id=<%= r.getReservationId() %>" class="tl">
                                            View Bill <i class="fas fa-arrow-right"></i>
                                        </a>
                                    </td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>

                <% } %>
            </div>

        </div>
    </div>
</div>

</body>
</html>