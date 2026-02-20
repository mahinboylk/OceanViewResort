<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.oceanview.model.Reservation" %>
<%
    // Auth guard
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Read data
    String staffName = (String) session.getAttribute("user");
    String msg       = request.getParameter("msg");

    @SuppressWarnings("unchecked")
    List<Reservation> list = (List<Reservation>) request.getAttribute("resList");

    int totalBookings  = (list != null) ? list.size() : 0;
    int occupiedCount  = Math.min(totalBookings, 20);
    int availableCount = 20 - occupiedCount;

    // Build room-grid HTML using StringBuilder
    StringBuilder roomGrid = new StringBuilder();
    for (int i = 1; i <= 20; i++) {
        boolean occupied = false;
        if (list != null) {
            for (Reservation r : list) {
                if (r.getReservationId() % 20 == i % 20) {
                    occupied = true;
                    break;
                }
            }
        }
        String cls  = occupied ? "oc" : "av";
        String icon = occupied ? "fa-lock" : "fa-check-circle";
        roomGrid.append("<div class='rb ").append(cls).append("'>");
        roomGrid.append("<i class='fas ").append(icon).append("'></i>");
        roomGrid.append("<span>").append(i).append("</span>");
        roomGrid.append("</div>");
    }

    // Build table rows using StringBuilder
    StringBuilder tableRows = new StringBuilder();
    if (list != null && !list.isEmpty()) {
        for (Reservation r : list) {
            String rt = (r.getRoomType() != null) ? r.getRoomType().toLowerCase() : "";
            String bc = rt.contains("ocean") ? "bo"
                      : rt.contains("suite") ? "bs"
                      : rt.contains("deluxe") ? "bd" : "bst";
            tableRows.append("<tr>");
            tableRows.append("<td><span class='tid'>#").append(r.getReservationId()).append("</span></td>");
            tableRows.append("<td><strong>").append(r.getGuestName()).append("</strong></td>");
            tableRows.append("<td><span class='badge ").append(bc).append("'>");
            tableRows.append(r.getRoomType()).append("</span></td>");
            tableRows.append("<td style='color:var(--mist);font-size:0.83rem;'>");
            tableRows.append(r.getCheckIn()).append("</td>");
            tableRows.append("<td><a href='reservation?action=view&id=");
            tableRows.append(r.getReservationId()).append("' class='tl'>");
            tableRows.append("View Bill <i class='fas fa-arrow-right' style='font-size:0.58rem;'></i>");
            tableRows.append("</a></td>");
            tableRows.append("</tr>");
        }
    }

    // Success / empty message
    String successHtml = "";
    if ("success".equals(msg)) {
        successHtml = "<div class='success-banner'><i class='fas fa-check-circle'></i> Reservation saved successfully.</div>";
    }
    String emptyHtml = "";
    if (list == null || list.isEmpty()) {
        emptyHtml = "<p style='color:var(--mist); font-size:0.86rem; padding:1.5rem;'>No reservations yet. <a href='add_reservation.jsp' style='color:var(--tide);'>Add the first booking</a>.</p>";
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard &mdash; Ocean View Resort</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body data-page="dashboard">

<div class="page-overlay"></div>
<div class="page-content">
    <div class="inner-shell">
        <div class="content-col">

            <%= successHtml %>

            <!-- Topbar -->
            <div class="topbar">
                <div class="topbar-brand">
                    <span class="rn">
                        <i class="fas fa-umbrella-beach" style="color:var(--gold); margin-right:8px;"></i>Ocean View Resort
                    </span>
                    <span class="wt">Welcome back, <%= staffName %></span>
                </div>
                <div class="topbar-actions">
                    <a href="logout" class="btn btn-logout">
                        <i class="fas fa-power-off"></i> Logout
                    </a>
                </div>
            </div>

            <!-- Stat Strip -->
            <div class="stat-strip">
                <div class="stat-card">
                    <div class="stat-bg" style="background-image:url('https://images.unsplash.com/photo-1582610285985-a42d9193f2fd?w=500&q=75');"></div>
                    <div class="stat-c">
                        <span class="stat-lbl">Occupied Rooms</span>
                        <span class="stat-val"><%= occupiedCount %></span>
                        <span class="stat-sub">of 20 rooms</span>
                    </div>
                    <i class="fas fa-bed stat-ico"></i>
                </div>
                <div class="stat-card">
                    <div class="stat-bg" style="background-image:url('https://images.unsplash.com/photo-1540202404-1b927e27fa8b?w=500&q=75');"></div>
                    <div class="stat-c">
                        <span class="stat-lbl">Available Rooms</span>
                        <span class="stat-val"><%= availableCount %></span>
                        <span class="stat-sub">ready for booking</span>
                    </div>
                    <i class="fas fa-check-circle stat-ico"></i>
                </div>
                <div class="stat-card">
                    <div class="stat-bg" style="background-image:url('https://images.unsplash.com/photo-1510414842594-a61c69b5ae57?w=500&q=75');"></div>
                    <div class="stat-c">
                        <span class="stat-lbl">Total Bookings</span>
                        <span class="stat-val"><%= totalBookings %></span>
                        <span class="stat-sub">all-time reservations</span>
                    </div>
                    <i class="fas fa-chart-line stat-ico"></i>
                </div>
            </div>

            <!-- Quick Actions -->
            <div class="grid-menu">
                <a href="add_reservation.jsp" class="menu-card">
                    <div class="mc-bg" style="background-image:url('https://images.unsplash.com/photo-1551882547-ff40c63fe2e2?w=400&q=75');"></div>
                    <div class="ci"><i class="fas fa-user-plus"></i></div>
                    <span>New Booking</span>
                </a>
                <a href="reservation?action=list" class="menu-card">
                    <div class="mc-bg" style="background-image:url('https://images.unsplash.com/photo-1506197603052-3cc9c3a201bd?w=400&q=75');"></div>
                    <div class="ci"><i class="fas fa-list-ul"></i></div>
                    <span>All Bookings</span>
                </a>
                <a href="help.jsp" class="menu-card">
                    <div class="mc-bg" style="background-image:url('https://images.unsplash.com/photo-1473496169851-1ef9a9e0fc48?w=400&q=75');"></div>
                    <div class="ci"><i class="fas fa-book-open"></i></div>
                    <span>User Guide</span>
                </a>
                <a href="logout" class="menu-card danger">
                    <div class="ci"><i class="fas fa-door-open"></i></div>
                    <span>Exit System</span>
                </a>
            </div>

            <!-- Room Status Grid -->
            <div class="panel">
                <div class="ph">
                    <i class="fas fa-hotel"></i>
                    <h4>Live Room Status</h4>
                </div>
                <div class="pb">
                    <div class="room-grid">
                        <%= roomGrid.toString() %>
                    </div>
                    <div class="legend">
                        <span class="legend-item">
                            <span class="legend-dot" style="background:#1B5E44;"></span>
                            Available (<%= availableCount %>)
                        </span>
                        <span class="legend-item">
                            <span class="legend-dot" style="background:#8B2A1E;"></span>
                            Occupied (<%= occupiedCount %>)
                        </span>
                    </div>
                </div>
            </div>

            <!-- Reservations Table -->
            <div class="panel">
                <div class="ph">
                    <i class="fas fa-calendar-alt"></i>
                    <h4>Recent Reservations</h4>
                </div>
                <% if (list == null || list.isEmpty()) { %>
                    <%= emptyHtml %>
                <% } else { %>
                <table>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Guest Name</th>
                            <th>Room Type</th>
                            <th>Check-In</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%= tableRows.toString() %>
                    </tbody>
                </table>
                <% } %>
            </div>

        </div>
    </div>
</div>
</body>
</html>
