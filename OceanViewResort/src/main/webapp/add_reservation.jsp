<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // Auth guard
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    // Error message
    String ep = request.getParameter("error");
    String errorMsg = "";
    if ("true".equals(ep)) {
        errorMsg = "Could not save the reservation. Please check all fields and try again.";
    } else if ("date".equals(ep)) {
        errorMsg = "Check-out date must be after the check-in date.";
    }
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>New Reservation &mdash; Ocean View Resort</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>

<body data-page="reservation">

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
                        <span class="wt">New Reservation</span>
                    </div>
                    <div class="topbar-actions">
                        <a href="reservation?action=list" class="btn btn-ghost">
                            <i class="fas fa-arrow-left"></i> Dashboard
                        </a>
                    </div>
                </div>

                <!-- Two-column layout -->
                <div class="res-layout">

                    <!-- Main Form -->
                    <div class="panel">
                        <div class="img-hero" style="background-image:url('https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=1200&q=80&fit=crop');">
                            <div class="iho"><span class="ihl">New Booking</span></div>
                        </div>
                        <div class="pb">

                            <% if (!errorMsg.isEmpty()) { %>
                                <div class="error-banner">
                                    <i class="fas fa-exclamation-circle"></i>
                                    <%= errorMsg %>
                                </div>
                            <% } %>

                            <form action="reservation" method="post">
                                <input type="hidden" name="action" value="add">

                                <p class="section-label">
                                    <i class="fas fa-user"></i> Guest Information
                                </p>

                                <div class="form-grid">
                                    <div class="fg span2">
                                        <label>Full Name</label>
                                        <input type="text" name="guestName" placeholder="e.g. Nimal Perera" required>
                                    </div>
                                    <div class="fg span2">
                                        <label>Address</label>
                                        <textarea name="address" placeholder="Street, City, Country" required></textarea>
                                    </div>
                                    <div class="fg">
                                        <label>Contact Number</label>
                                        <input type="text" name="contact" placeholder="+94 77 000 0000" required>
                                    </div>
                                    <div class="fg">
                                        <label>Room Type</label>
                                        <select name="roomType" required>
                                            <option value="" disabled selected>Select a room</option>
                                            <option value="Standard">Standard - $100 / night</option>
                                            <option value="Deluxe">Deluxe - $150 / night</option>
                                            <option value="Suite">Suite - $250 / night</option>
                                            <option value="Ocean View">Ocean View - $300 / night</option>
                                        </select>
                                    </div>
                                </div>

                                <hr class="section-hr">

                                <p class="section-label">
                                    <i class="fas fa-calendar-alt"></i> Stay Dates
                                </p>

                                <div class="form-grid">
                                    <div class="fg">
                                        <label>Check-In Date</label>
                                        <input type="date" name="checkIn" required>
                                    </div>
                                    <div class="fg">
                                        <label>Check-Out Date</label>
                                        <input type="date" name="checkOut" required>
                                    </div>
                                </div>

                                <div class="btn-row" style="margin-top:1.4rem;">
                                    <a href="reservation?action=list" class="btn btn-secondary">
                                        <i class="fas fa-times"></i> Cancel
                                    </a>
                                    <button type="submit" class="btn btn-success" style="padding:12px 28px;">
                                        <i class="fas fa-check"></i> Confirm Booking
                                    </button>
                                </div>
                            </form>

                        </div>
                    </div>

                    <!-- Room Type Visual Panel -->
                    <div>
                        <div class="panel">
                            <div class="ph"><i class="fas fa-bed"></i>
                                <h4>Room Types</h4>
                            </div>
                            <div class="room-type-card" style="background-image:url('https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=600&q=78&fit=crop'); height:110px;">
                                <div class="rtc-overlay">
                                    <div>
                                        <div class="rtc-label">Ocean View</div>
                                        <div class="rtc-price">$300<small>/night</small></div>
                                    </div>
                                </div>
                            </div>
                            <div class="room-type-card" style="background-image:url('https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=600&q=78&fit=crop'); height:90px;">
                                <div class="rtc-overlay">
                                    <div>
                                        <div class="rtc-label">Suite</div>
                                        <div class="rtc-price">$250<small>/night</small></div>
                                    </div>
                                </div>
                            </div>
                            <div class="room-type-card" style="background-image:url('https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600&q=78&fit=crop'); height:80px;">
                                <div class="rtc-overlay">
                                    <div>
                                        <div class="rtc-label">Deluxe</div>
                                        <div class="rtc-price">$150<small>/night</small></div>
                                    </div>
                                </div>
                            </div>
                            <div class="room-type-card" style="background-image:url('https://images.unsplash.com/photo-1512918728675-ed5a9ecdebfd?w=600&q=78&fit=crop'); height:70px;">
                                <div class="rtc-overlay">
                                    <div>
                                        <div class="rtc-label">Standard</div>
                                        <div class="rtc-price">$100<small>/night</small></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>

            </div>
        </div>
    </div>
</body>

</html>