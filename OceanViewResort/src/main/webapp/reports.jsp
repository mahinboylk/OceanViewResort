<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map"%>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    /* ── Pull servlet attributes ── */
    Double  totalRevenue          = (Double)  request.getAttribute("totalRevenue");
    Integer activeReservations    = (Integer) request.getAttribute("activeReservations");
    Integer cancelledReservations = (Integer) request.getAttribute("cancelledReservations");
    Integer completedReservations = (Integer) request.getAttribute("completedReservations");
    Integer currentOccupancy      = (Integer) request.getAttribute("currentOccupancy");
    Double  occupancyRate         = (Double)  request.getAttribute("occupancyRate");

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> upcomingCheckins  = (List<Map<String, Object>>) request.getAttribute("upcomingCheckins");

    @SuppressWarnings("unchecked")
    Map<String, Double> revenueByRoomType       = (Map<String, Double>) request.getAttribute("revenueByRoomType");

    /* ── Null guards ── */
    if (totalRevenue          == null) totalRevenue          = 0.0;
    if (activeReservations    == null) activeReservations    = 0;
    if (cancelledReservations == null) cancelledReservations = 0;
    if (completedReservations == null) completedReservations = 0;
    if (currentOccupancy      == null) currentOccupancy      = 0;
    if (occupancyRate         == null) occupancyRate         = 0.0;

    /* ── Pre-format total revenue ── */
    String totalRevStr    = String.format("%,.0f", totalRevenue);
    String occupancyStr   = String.format("%.1f", occupancyRate);
    int    totalResCount  = activeReservations + cancelledReservations + completedReservations;

    /* ── Revenue by room type — compute totals & bars using StringBuilder ── */
    double revTotal = 0;
    if (revenueByRoomType != null) {
        for (Double v : revenueByRoomType.values()) revTotal += v;
    }

    StringBuilder revRows = new StringBuilder();
    if (revenueByRoomType != null && !revenueByRoomType.isEmpty()) {
        for (Map.Entry<String, Double> entry : revenueByRoomType.entrySet()) {
            String rt  = entry.getKey();
            double rev = entry.getValue();
            double pct = revTotal > 0 ? (rev / revTotal * 100) : 0;
            String bc  = rt.toLowerCase().contains("presidential") ? "bp"
                       : rt.toLowerCase().contains("ocean") ? "bo"
                       : rt.toLowerCase().contains("suite") ? "bs"
                       : rt.toLowerCase().contains("deluxe") ? "bd"
                       : "bst";
            revRows.append("<tr>");
            revRows.append("<td><span class='badge ").append(bc).append("'>").append(rt).append("</span></td>");
            revRows.append("<td style='font-weight:600;'>LKR ").append(String.format("%,.0f", rev)).append("</td>");
            revRows.append("<td>");
            revRows.append("<div class='prog-wrap'><div class='prog-bar' style='width:").append(String.format("%.1f", pct)).append("%;'></div></div>");
            revRows.append("<span style='font-size:0.75rem;color:var(--mist);margin-left:6px;'>").append(String.format("%.1f", pct)).append("%</span>");
            revRows.append("</td>");
            revRows.append("</tr>");
        }
    } else {
        revRows.append("<tr><td colspan='3' style='text-align:center;color:var(--mist);padding:1.2rem;'>No revenue data available</td></tr>");
    }

    /* ── Upcoming check-ins table rows ── */
    StringBuilder checkinRows = new StringBuilder();
    if (upcomingCheckins != null && !upcomingCheckins.isEmpty()) {
        for (Map<String, Object> c : upcomingCheckins) {
            String rt  = (c.get("roomType") != null) ? c.get("roomType").toString().toLowerCase() : "";
            String bc  = rt.contains("presidential") ? "bp"
                       : rt.contains("ocean")        ? "bo"
                       : rt.contains("suite")        ? "bs"
                       : rt.contains("deluxe")       ? "bd"
                       :                               "bst";
            checkinRows.append("<tr>");
            checkinRows.append("<td><span class='tid'>#").append(c.get("reservationId")).append("</span></td>");
            checkinRows.append("<td><strong>").append(c.get("guestName")).append("</strong></td>");
            checkinRows.append("<td><span class='badge ").append(bc).append("'>").append(c.get("roomType")).append("</span></td>");
            checkinRows.append("<td style='color:var(--mist);font-size:0.83rem;'>").append(c.get("checkIn")).append("</td>");
            checkinRows.append("</tr>");
        }
    } else {
        checkinRows.append("<tr><td colspan='4' style='text-align:center;color:var(--mist);padding:1.2rem;'>No upcoming check-ins in the next 7 days</td></tr>");
    }

%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reports &mdash; Ocean View Resort</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body data-page="reports">

<div class="page-overlay"></div>
<div class="page-content">
    <div class="inner-shell">
        <div class="content-col">

            <!-- Topbar -->
            <div class="topbar">
                <div class="topbar-brand">
                    <span class="rn"><i class="fas fa-umbrella-beach" style="color:var(--gold); margin-right:8px;"></i>Ocean View Resort</span>
                    <span class="wt">Management Reports</span>
                </div>
                <div class="topbar-actions">
                    <a href="reservation?action=list" class="btn btn-ghost"><i class="fas fa-arrow-left"></i> Dashboard</a>
                    <button onclick="window.print()" class="btn btn-success no-print"><i class="fas fa-print"></i> Print</button>
                </div>
            </div>

            <!-- ── KPI Strip ── -->
            <div class="stat-strip" style="grid-template-columns:repeat(4,1fr);">

                <div class="stat-card">
                    <div class="stat-bg" style="background-image:url('https://images.unsplash.com/photo-1579621970563-ebec7560ff3e?w=400&q=75');"></div>
                    <div class="stat-c">
                        <span class="stat-lbl">Total Revenue (LKR)</span>
                        <span class="stat-val" style="font-size:1.35rem;">Rs. <%= totalRevStr %></span>
                        <span class="stat-sub">Active + Completed</span>
                    </div>
                    <i class="fas fa-coins stat-ico"></i>
                </div>

                <div class="stat-card">
                    <div class="stat-bg" style="background-image:url('https://images.unsplash.com/photo-1540202404-1b927e27fa8b?w=400&q=75');"></div>
                    <div class="stat-c">
                        <span class="stat-lbl">Occupancy Rate</span>
                        <span class="stat-val"><%= occupancyStr %>%</span>
                        <span class="stat-sub"><%= currentOccupancy %> of 20 rooms</span>
                    </div>
                    <i class="fas fa-hotel stat-ico"></i>
                </div>

                <div class="stat-card">
                    <div class="stat-bg" style="background-image:url('https://images.unsplash.com/photo-1582610285985-a42d9193f2fd?w=400&q=75');"></div>
                    <div class="stat-c">
                        <span class="stat-lbl">Active Bookings</span>
                        <span class="stat-val"><%= activeReservations %></span>
                        <span class="stat-sub">currently active</span>
                    </div>
                    <i class="fas fa-check-circle stat-ico"></i>
                </div>

                <div class="stat-card">
                    <div class="stat-bg" style="background-image:url('https://images.unsplash.com/photo-1510414842594-a61c69b5ae57?w=400&q=75');"></div>
                    <div class="stat-c">
                        <span class="stat-lbl">Total Reservations</span>
                        <span class="stat-val"><%= totalResCount %></span>
                        <span class="stat-sub">all-time</span>
                    </div>
                    <i class="fas fa-chart-line stat-ico"></i>
                </div>

            </div>

            <!-- ── Reservation Status Summary ── -->
            <div class="panel">
                <div class="ph"><i class="fas fa-chart-pie"></i><h4>Reservation Status Summary</h4></div>
                <div class="pb">
                    <div class="status-grid">

                        <div class="status-tile active-tile">
                            <div class="st-icon"><i class="fas fa-check-circle"></i></div>
                            <div class="st-body">
                                <p class="st-label">Active</p>
                                <p class="st-value"><%= activeReservations %></p>
                            </div>
                        </div>

                        <div class="status-tile completed-tile">
                            <div class="st-icon"><i class="fas fa-flag-checkered"></i></div>
                            <div class="st-body">
                                <p class="st-label">Completed</p>
                                <p class="st-value"><%= completedReservations %></p>
                            </div>
                        </div>

                        <div class="status-tile cancelled-tile">
                            <div class="st-icon"><i class="fas fa-times-circle"></i></div>
                            <div class="st-body">
                                <p class="st-label">Cancelled</p>
                                <p class="st-value"><%= cancelledReservations %></p>
                            </div>
                        </div>

                        <div class="status-tile total-tile">
                            <div class="st-icon"><i class="fas fa-list"></i></div>
                            <div class="st-body">
                                <p class="st-label">Total</p>
                                <p class="st-value"><%= totalResCount %></p>
                            </div>
                        </div>

                    </div>
                </div>
            </div>

            <!-- ── Revenue by Room Type (full width) ── -->
            <div class="panel">
                <div class="ph"><i class="fas fa-bed"></i><h4>Revenue by Room Type (LKR)</h4></div>
                <table>
                    <thead>
                        <tr><th>Room Type</th><th>Revenue</th><th>Share</th></tr>
                    </thead>
                    <tbody><%= revRows.toString() %></tbody>
                </table>
            </div>

            <!-- ── Upcoming Check-ins ── -->
            <div class="panel">
                <div class="ph"><i class="fas fa-plane-arrival"></i><h4>Upcoming Check-ins &mdash; Next 7 Days</h4></div>
                <table>
                    <thead>
                        <tr><th>#</th><th>Guest Name</th><th>Room Type</th><th>Check-In Date</th></tr>
                    </thead>
                    <tbody><%= checkinRows.toString() %></tbody>
                </table>
            </div>

        </div>
    </div>
</div>

</body>
</html>