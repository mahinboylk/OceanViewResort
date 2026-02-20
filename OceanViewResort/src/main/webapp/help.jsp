<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Guide &mdash; Ocean View Resort</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body data-page="help">

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
                    <span class="wt">User Guide</span>
                </div>
                <div class="topbar-actions">
                    <a href="reservation?action=list" class="btn btn-ghost">
                        <i class="fas fa-arrow-left"></i> Dashboard
                    </a>
                </div>
            </div>

            <div class="panel">
                <div class="img-hero" style="background-image:url('https://images.unsplash.com/photo-1473496169851-1ef9a9e0fc48?w=1200&q=80&fit=crop'); height:175px;">
                    <div class="iho"><span class="ihl">Staff User Manual</span></div>
                </div>
                <div class="pb">

                    <div class="help-item">
                        <span class="hn">1</span>
                        <div class="hc">
                            <h5><i class="fas fa-lock" style="margin-right:6px; color:var(--tide);"></i>Logging In</h5>
                            <p>Use your assigned staff credentials to access the system.
                               If you encounter a login error, contact your system administrator
                               to verify your account status.</p>
                        </div>
                    </div>

                    <div class="help-item">
                        <span class="hn">2</span>
                        <div class="hc">
                            <h5><i class="fas fa-user-plus" style="margin-right:6px; color:var(--tide);"></i>Creating a New Reservation</h5>
                            <p>Click <strong>New Booking</strong> on the dashboard. Complete all guest
                               details &mdash; full name, address, contact number, room type, and stay
                               dates &mdash; then click <strong>Confirm Booking</strong> to save.</p>
                        </div>
                    </div>

                    <div class="help-item">
                        <span class="hn">3</span>
                        <div class="hc">
                            <h5><i class="fas fa-file-invoice-dollar" style="margin-right:6px; color:var(--tide);"></i>Generating a Bill</h5>
                            <p>From the dashboard, click <strong>View Bill</strong> next to any reservation.
                               The total is calculated automatically from the room rate and number of nights.
                               Click <strong>Print Bill</strong> to produce a physical receipt.</p>
                        </div>
                    </div>

                    <div class="help-item">
                        <span class="hn">4</span>
                        <div class="hc">
                            <h5><i class="fas fa-hotel" style="margin-right:6px; color:var(--tide);"></i>Room Status Grid</h5>
                            <p>The dashboard shows live availability for all 20 rooms.
                               <span style="color:#1B5E44; font-weight:700;">Green</span> rooms are available,
                               <span style="color:#8B2A1E; font-weight:700;">red</span> rooms are occupied.
                               The grid refreshes on every page load.</p>
                        </div>
                    </div>

                </div>
            </div>

        </div>
    </div>
</div>
</body>
</html>