<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String username = (String) session.getAttribute("user");
    String initial  = username.substring(0, 1).toUpperCase();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home &mdash; Ocean View Resort</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body data-page="home">

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
                    <span class="wt">Hotel Management System</span>
                </div>
                <div class="topbar-actions">
                    <div class="user-pill">
                        <div class="user-avatar-sm"><%= initial %></div>
                        <span class="user-name-sm"><%= username %></span>
                    </div>
                    <a href="logout" class="btn btn-logout">
                        <i class="fas fa-power-off"></i> Logout
                    </a>
                </div>
            </div>

            <!-- Welcome Hero Panel -->
            <div class="panel">
                <div class="img-hero" style="background-image:url('https://images.unsplash.com/photo-1566073771259-6a8506099945?w=1920&q=85&fit=crop'); height:220px;">
                    <div class="iho" style="flex-direction:column; align-items:flex-start; gap:6px; padding:1.8rem 2rem;">
                        <span style="font-size:0.65rem; font-weight:800; letter-spacing:0.28em; text-transform:uppercase; color:var(--gold);">
                            <i class="fas fa-map-marker-alt" style="margin-right:5px;"></i>Galle, Sri Lanka
                        </span>
                        <span class="ihl" style="font-size:2.2rem;">Welcome back, <%= username %></span>
                        <span style="font-size:0.82rem; color:rgba(245,236,215,0.7); letter-spacing:0.08em;">Manage your hotel operations from one place</span>
                    </div>
                </div>

                <!-- Quick Stats — updated to 5 room types -->
                <div class="pb" style="padding-bottom:0;">
                    <div class="home-stats">
                        <div class="home-stat">
                            <span class="hs-val">20</span>
                            <span class="hs-lbl">Total Rooms</span>
                        </div>
                        <div class="home-stat-div"></div>
                        <div class="home-stat">
                            <span class="hs-val">5</span>
                            <span class="hs-lbl">Room Types</span>
                        </div>
                        <div class="home-stat-div"></div>
                        <div class="home-stat">
                            <span class="hs-val">24/7</span>
                            <span class="hs-lbl">Support</span>
                        </div>
                        <div class="home-stat-div"></div>
                        <div class="home-stat">
                            <span class="hs-val" style="font-size:0.95rem; color:var(--tide);">LKR</span>
                            <span class="hs-lbl">Currency</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Navigation Cards -->
            <div class="grid-menu" style="grid-template-columns:repeat(4,1fr);">

                <a href="reservation?action=list" class="menu-card">
                    <div class="mc-bg" style="background-image:url('https://images.unsplash.com/photo-1506197603052-3cc9c3a201bd?w=400&q=75');"></div>
                    <div class="ci"><i class="fas fa-tachometer-alt"></i></div>
                    <span>Dashboard</span>
                    <small class="mc-sub">View all bookings</small>
                </a>

                <a href="add_reservation.jsp" class="menu-card">
                    <div class="mc-bg" style="background-image:url('https://images.unsplash.com/photo-1551882547-ff40c63fe2e2?w=400&q=75');"></div>
                    <div class="ci"><i class="fas fa-user-plus"></i></div>
                    <span>New Booking</span>
                    <small class="mc-sub">Create reservation</small>
                </a>

                <a href="reports" class="menu-card">
                    <div class="mc-bg" style="background-image:url('https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=400&q=75');"></div>
                    <div class="ci"><i class="fas fa-chart-pie"></i></div>
                    <span>Reports</span>
                    <small class="mc-sub">Analytics &amp; insights</small>
                </a>

                <a href="help.jsp" class="menu-card">
                    <div class="mc-bg" style="background-image:url('https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=400&q=75');"></div>
                    <div class="ci"><i class="fas fa-book-open"></i></div>
                    <span>User Guide</span>
                    <small class="mc-sub">Help &amp; documentation</small>
                </a>

            </div>

            <!-- Room Rate Reference Panel — all 5 types -->
            <div class="panel">
                <div class="ph"><i class="fas fa-bed"></i><h4>Room Rates &mdash; Quick Reference (LKR)</h4></div>
                <div class="pb" style="padding:0;">
                    <div style="display:grid; grid-template-columns:repeat(5,1fr);">

                        <!-- Presidential Suite — gold accent -->
                        <div class="room-type-card" style="background-image:url('https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=600&q=78&fit=crop'); height:120px;">
                            <div class="rtc-overlay">
                                <div>
                                    <div class="rtc-label" style="color:var(--gold);">
                                        <i class="fas fa-crown" style="margin-right:4px; font-size:0.65rem;"></i>Presidential
                                    </div>
                                    <div class="rtc-price">Rs. 150,000<small>/night</small></div>
                                </div>
                            </div>
                        </div>

                        <div class="room-type-card" style="background-image:url('https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=600&q=78&fit=crop'); height:120px;">
                            <div class="rtc-overlay">
                                <div>
                                    <div class="rtc-label">Ocean View</div>
                                    <div class="rtc-price">Rs. 75,000<small>/night</small></div>
                                </div>
                            </div>
                        </div>

                        <div class="room-type-card" style="background-image:url('https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=600&q=78&fit=crop'); height:120px;">
                            <div class="rtc-overlay">
                                <div>
                                    <div class="rtc-label">Suite</div>
                                    <div class="rtc-price">Rs. 55,000<small>/night</small></div>
                                </div>
                            </div>
                        </div>

                        <div class="room-type-card" style="background-image:url('https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600&q=78&fit=crop'); height:120px;">
                            <div class="rtc-overlay">
                                <div>
                                    <div class="rtc-label">Deluxe</div>
                                    <div class="rtc-price">Rs. 35,000<small>/night</small></div>
                                </div>
                            </div>
                        </div>

                        <div class="room-type-card" style="background-image:url('https://images.unsplash.com/photo-1512918728675-ed5a9ecdebfd?w=600&q=78&fit=crop'); height:120px;">
                            <div class="rtc-overlay">
                                <div>
                                    <div class="rtc-label">Standard</div>
                                    <div class="rtc-price">Rs. 25,000<small>/night</small></div>
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