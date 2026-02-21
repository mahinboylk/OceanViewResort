<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map" %>
<%
    /* ── Auth guard ── */
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    /* ── Data from ReportsServlet ── */
    double totalRevenue            = (Double)  request.getAttribute("totalRevenue");
    int    activeReservations      = (Integer) request.getAttribute("activeReservations");
    int    cancelledReservations   = (Integer) request.getAttribute("cancelledReservations");
    int    completedReservations   = (Integer) request.getAttribute("completedReservations");
    int    currentOccupancy        = (Integer) request.getAttribute("currentOccupancy");
    double occupancyRate           = (Double)  request.getAttribute("occupancyRate");

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> upcomingCheckins  = (List<Map<String, Object>>) request.getAttribute("upcomingCheckins");
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> recentRevenue     = (List<Map<String, Object>>) request.getAttribute("recentRevenue");
    @SuppressWarnings("unchecked")
    Map<String, Double>       revenueByRoomType = (Map<String, Double>)       request.getAttribute("revenueByRoomType");

    String formattedRevenue = String.format("%,.0f", totalRevenue);
    String formattedOccRate = String.format("%.1f", occupancyRate);

    /* ── Upcoming check-ins table rows (StringBuilder avoids Invalid tag location) ── */
    StringBuilder checkinRows = new StringBuilder();
    if (upcomingCheckins != null && !upcomingCheckins.isEmpty()) {
        for (Map<String, Object> c : upcomingCheckins) {
            String rt = (c.get("roomType") != null) ? c.get("roomType").toString().toLowerCase() : "";
            String bc = rt.contains("ocean") ? "bo" : rt.contains("suite") ? "bs" : rt.contains("deluxe") ? "bd" : "bst";
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

    /* ── Revenue by room type table rows ── */
    StringBuilder revenueRows = new StringBuilder();
    double grandTotal = 0;
    if (revenueByRoomType != null) {
        for (Double v : revenueByRoomType.values()) grandTotal += v;
    }
    if (revenueByRoomType != null && !revenueByRoomType.isEmpty()) {
        for (Map.Entry<String, Double> entry : revenueByRoomType.entrySet()) {
            String rt  = entry.getKey() != null ? entry.getKey().toLowerCase() : "";
            String bc  = rt.contains("ocean") ? "bo" : rt.contains("suite") ? "bs" : rt.contains("deluxe") ? "bd" : "bst";
            double pct = grandTotal > 0 ? (entry.getValue() / grandTotal * 100) : 0;
            revenueRows.append("<tr>");
            revenueRows.append("<td><span class='badge ").append(bc).append("'>").append(entry.getKey()).append("</span></td>");
            revenueRows.append("<td style='font-weight:600;color:var(--ocean);'>Rs. ").append(String.format("%,.0f", entry.getValue())).append("</td>");
            revenueRows.append("<td>");
            revenueRows.append("<div class='pct-bar-wrap'><div class='pct-bar' style='width:").append(String.format("%.1f", pct)).append("%;'></div></div>");
            revenueRows.append("<span style='font-size:0.75rem;color:var(--mist);'>").append(String.format("%.1f", pct)).append("%</span>");
            revenueRows.append("</td>");
            revenueRows.append("</tr>");
        }
    } else {
        revenueRows.append("<tr><td colspan='3' style='text-align:center;color:var(--mist);padding:1.2rem;'>No revenue data available</td></tr>");
    }

    /* ── Recent revenue month rows ── */
    StringBuilder monthRows = new StringBuilder();
    if (recentRevenue != null && !recentRevenue.isEmpty()) {
        for (Map<String, Object> m : recentRevenue) {
            double rev = (Double) m.get("revenue");
            monthRows.append("<tr>");
            monthRows.append("<td style='font-weight:600;'>").append(m.get("month")).append("</td>");
            monthRows.append("<td style='font-weight:600;color:var(--ocean);'>Rs. ").append(String.format("%,.0f", rev)).append("</td>");
            monthRows.append("</tr>");
        }
    } else {
        monthRows.append("<tr><td colspan='2' style='text-align:center;color:var(--mist);padding:1.2rem;'>No monthly data available</td></tr>");
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
                    <span class="rn">
                        <i class="fas fa-umbrella-beach" style="color:var(--gold); margin-right:8px;"></i>Ocean View Resort
                    </span>
                    <span class="wt">Reports &amp; Analytics</span>
                </div>
                <div class="topbar-actions">
                    <a href="reservation?action=list" class="btn btn-ghost">
                        <i class="fas fa-arrow-left"></i> Dashboard
                    </a>
                </div>
            </div>

            <!-- Hero strip -->
            <div class="panel" style="margin-bottom:1.2rem;">
                <div class="img-hero" style="background-image:url('https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=1200&q=80&fit=crop');">
                    <div class="iho"><span class="ihl">Business Intelligence</span></div>
                </div>
            </div>

            <!-- KPI Stat Strip -->
            <div class="stat-strip rpt-strip">

                <div class="stat-card rpt-card gold-accent">
                    <div class="stat-c">
                        <span class="stat-lbl">Total Revenue</span>
                        <span class="stat-val" style="font-size:1.5rem;">Rs. <%= formattedRevenue %></span>
                        <span class="stat-sub">Active &amp; Completed</span>
                    </div>
                    <i class="fas fa-rupee-sign stat-ico"></i>
                </div>

                <div class="stat-card rpt-card">
                    <div class="stat-c">
                        <span class="stat-lbl">Active Bookings</span>
                        <span class="stat-val"><%= activeReservations %></span>
                        <span class="stat-sub" style="color:#1B5E44;">Currently active</span>
                    </div>
                    <i class="fas fa-check-circle stat-ico"></i>
                </div>

                <div class="stat-card rpt-card">
                    <div class="stat-c">
                        <span class="stat-lbl">Completed</span>
                        <span class="stat-val"><%= completedReservations %></span>
                        <span class="stat-sub">Check-outs done</span>
                    </div>
                    <i class="fas fa-flag-checkered stat-ico"></i>
                </div>

                <div class="stat-card rpt-card">
                    <div class="stat-c">
                        <span class="stat-lbl">Cancelled</span>
                        <span class="stat-val" style="color:var(--coral);"><%= cancelledReservations %></span>
                        <span class="stat-sub" style="color:var(--coral);">Lost bookings</span>
                    </div>
                    <i class="fas fa-times-circle stat-ico"></i>
                </div>

            </div>

            <!-- Occupancy + Revenue two-col -->
            <div class="rpt-two-col">

                <!-- Occupancy Card -->
                <div class="panel">
                    <div class="ph"><i class="fas fa-hotel"></i><h4>Current Occupancy</h4></div>
                    <div class="pb" style="text-align:center; padding:2rem 1.6rem;">
                        <div class="occ-ring-wrap">
                            <svg class="occ-ring" viewBox="0 0 120 120">
                                <circle cx="60" cy="60" r="50" fill="none" stroke="rgba(232,217,184,0.5)" stroke-width="12"/>
                                <circle cx="60" cy="60" r="50" fill="none" stroke="var(--tide)" stroke-width="12"
                                    stroke-dasharray="<%= String.format("%.1f", occupancyRate * 3.14159) %> 314.159"
                                    stroke-linecap="round" transform="rotate(-90 60 60)"/>
                            </svg>
                            <div class="occ-ring-label">
                                <span class="occ-pct"><%= formattedOccRate %>%</span>
                                <span class="occ-sub">Occupied</span>
                            </div>
                        </div>
                        <div class="occ-detail">
                            <div class="occ-row">
                                <span class="legend-dot" style="background:var(--tide);"></span>
                                <span>Occupied: <strong><%= currentOccupancy %></strong> rooms</span>
                            </div>
                            <div class="occ-row">
                                <span class="legend-dot" style="background:rgba(232,217,184,0.6);"></span>
                                <span>Available: <strong><%= 20 - currentOccupancy %></strong> rooms</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Monthly Revenue -->
                <div class="panel">
                    <div class="ph"><i class="fas fa-chart-line"></i><h4>Monthly Revenue (Last 6 Months)</h4></div>
                    <table>
                        <thead>
                            <tr>
                                <th>Month</th>
                                <th>Revenue (LKR)</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%= monthRows.toString() %>
                        </tbody>
                    </table>
                </div>

            </div>

            <!-- Revenue by Room Type -->
            <div class="panel">
                <div class="ph"><i class="fas fa-chart-pie"></i><h4>Revenue by Room Type</h4></div>
                <table>
                    <thead>
                        <tr>
                            <th>Room Type</th>
                            <th>Total Revenue</th>
                            <th>Share</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%= revenueRows.toString() %>
                    </tbody>
                </table>
            </div>

            <!-- Upcoming Check-ins -->
            <div class="panel">
                <div class="ph"><i class="fas fa-plane-arrival"></i><h4>Upcoming Check-ins (Next 7 Days)</h4></div>
                <table>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Guest Name</th>
                            <th>Room Type</th>
                            <th>Check-in Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%= checkinRows.toString() %>
                    </tbody>
                </table>
            </div>

        </div>
    </div>
</div>

</body>
</html>