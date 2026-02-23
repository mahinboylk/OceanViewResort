<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.oceanview.model.Reservation"%>
<%
    /* ── Auth guard ── */
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Reservation res = (Reservation) request.getAttribute("reservation");
    if (res == null) {
        response.sendRedirect("reservation?action=list");
        return;
    }

    long diff    = res.getCheckOut().getTime() - res.getCheckIn().getTime();
    long nights  = Math.max(1, diff / (1000L * 60 * 60 * 24));
    String total = String.format("%,.2f", res.getTotalAmount());

    String status = res.getStatus();
    if (status == null) status = "Active";

    String cancelledMsg = request.getParameter("msg");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Receipt #<%= res.getReservationId() %> &mdash; Ocean View Resort</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body data-page="view">

<div class="page-overlay"></div>
<div class="page-content">
    <div class="inner-shell">
        <div class="content-col narrow">

            <!-- Topbar (hidden on print) -->
            <div class="topbar no-print">
                <div class="topbar-brand">
                    <span class="rn">
                        <i class="fas fa-umbrella-beach" style="color:var(--gold); margin-right:8px;"></i>Ocean View Resort
                    </span>
                    <span class="wt">Booking Receipt</span>
                </div>
                <div class="topbar-actions">
                    <a href="reservation?action=list" class="btn btn-ghost">
                        <i class="fas fa-arrow-left"></i> Back
                    </a>
                    <% if (!"Cancelled".equals(status) && !"Completed".equals(status)) { %>
                    <button onclick="confirmCancel(<%= res.getReservationId() %>)" class="btn btn-danger">
                        <i class="fas fa-times"></i> Cancel Booking
                    </button>
                    <% } %>
                    <button onclick="window.print()" class="btn btn-success">
                        <i class="fas fa-print"></i> Print Bill
                    </button>
                </div>
            </div>

            <!-- Post-cancel notification -->
            <% if ("cancelled".equals(cancelledMsg)) { %>
            <div class="alert-banner alert-cancelled no-print">
                <i class="fas fa-exclamation-circle"></i> This reservation has been successfully cancelled.
            </div>
            <% } %>

            <!-- Status banners -->
            <% if ("Cancelled".equals(status)) { %>
            <div class="alert-banner alert-cancelled">
                <i class="fas fa-ban"></i> This reservation has been <strong>CANCELLED</strong>
            </div>
            <% } else if ("Completed".equals(status)) { %>
            <div class="alert-banner alert-completed">
                <i class="fas fa-check-circle"></i> This reservation has been <strong>COMPLETED</strong>
            </div>
            <% } %>

            <!-- Receipt Card -->
            <div class="receipt-card">

                <!-- Photo header — .rph in style.css has the Unsplash background image + overlay -->
                <div class="rph">
                    <div class="rhc">
                        <p class="ra">Galle, Sri Lanka &nbsp;&middot;&nbsp; oceanviewresort.lk</p>
                        <div class="rgl"></div>
                        <p class="rr">Ocean View Resort</p>
                        <div class="rgl"></div>
                    </div>
                </div>

                <!-- Detail rows -->
                <div class="rb2">
                    <p class="rid">Booking Receipt &nbsp;#<%= res.getReservationId() %></p>

                    <div class="dr">
                        <span class="dl"><i class="fas fa-info-circle" style="width:14px;"></i> Status</span>
                        <span class="dv">
                            <% if ("Active".equals(status)) { %>
                                <span class="badge bo">Active</span>
                            <% } else if ("Cancelled".equals(status)) { %>
                                <span class="badge" style="background:#FEE8E4; color:#8B2A1E;">Cancelled</span>
                            <% } else if ("Completed".equals(status)) { %>
                                <span class="badge bd">Completed</span>
                            <% } else { %>
                                <%= status %>
                            <% } %>
                        </span>
                    </div>
                    <div class="dr">
                        <span class="dl"><i class="fas fa-user" style="width:14px;"></i> Guest</span>
                        <span class="dv"><%= res.getGuestName() %></span>
                    </div>
                    <div class="dr">
                        <span class="dl"><i class="fas fa-map-marker-alt" style="width:14px;"></i> Address</span>
                        <span class="dv"><%= res.getAddress() %></span>
                    </div>
                    <div class="dr">
                        <span class="dl"><i class="fas fa-phone" style="width:14px;"></i> Contact</span>
                        <span class="dv"><%= res.getContactNumber() %></span>
                    </div>
                    <div class="dr">
                        <span class="dl"><i class="fas fa-bed" style="width:14px;"></i> Room Type</span>
                        <span class="dv"><%= res.getRoomType() %></span>
                    </div>
                    <div class="dr">
                        <span class="dl"><i class="fas fa-calendar-check" style="width:14px;"></i> Check-In</span>
                        <span class="dv"><%= res.getCheckIn() %></span>
                    </div>
                    <div class="dr">
                        <span class="dl"><i class="fas fa-calendar-times" style="width:14px;"></i> Check-Out</span>
                        <span class="dv"><%= res.getCheckOut() %></span>
                    </div>
                    <div class="dr">
                        <span class="dl"><i class="fas fa-moon" style="width:14px;"></i> Duration</span>
                        <span class="dv"><%= nights %> night<%= nights != 1 ? "s" : "" %></span>
                    </div>
                </div>

                <!-- Total — .tb = navy bg, .tbl = label, .tam = gold amount -->
                <div class="tb">
                    <div>
                        <p class="tbl">Total Payable</p>
                        <p style="font-size:0.65rem; color:rgba(245,236,215,0.5); margin-top:2px;">
                            Includes all taxes &amp; service charges
                        </p>
                    </div>
                    <span class="tam">LKR <%= total %></span>
                </div>

                <!-- Footer -->
                <div class="rf">
                    <i class="fas fa-heart" style="color:var(--coral); font-size:0.66rem;"></i>
                    &nbsp; Thank you for choosing Ocean View Resort &nbsp;
                    <i class="fas fa-heart" style="color:var(--coral); font-size:0.66rem;"></i>
                </div>

            </div>

        </div>
    </div>
</div>

<script>
    function confirmCancel(reservationId) {
        if (confirm('Are you sure you want to cancel this reservation? This action cannot be undone.')) {
            window.location.href = 'reservation?action=cancel&id=' + reservationId;
        }
    }
</script>

</body>
</html>