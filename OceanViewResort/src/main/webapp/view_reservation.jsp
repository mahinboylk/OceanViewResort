<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="com.oceanview.model.Reservation" %>
        <% // Auth guard if (session.getAttribute("user")==null) { response.sendRedirect("login.jsp"); return; }
            Reservation res=(Reservation) request.getAttribute("reservation"); if (res==null) {
            response.sendRedirect("reservation?action=list"); return; } long diff=res.getCheckOut().getTime() -
            res.getCheckIn().getTime(); long nights=Math.max(1, diff / (1000L * 60 * 60 * 24)); String
            total=String.format("%.2f", res.getTotalAmount()); %>
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
                                        <i class="fas fa-umbrella-beach"
                                            style="color:var(--gold); margin-right:8px;"></i>Ocean View Resort
                                    </span>
                                    <span class="wt">Booking Receipt</span>
                                </div>
                                <div class="topbar-actions">
                                    <a href="reservation?action=list" class="btn btn-ghost">
                                        <i class="fas fa-arrow-left"></i> Back
                                    </a>
                                    <button onclick="window.print()" class="btn btn-success">
                                        <i class="fas fa-print"></i> Print Bill
                                    </button>
                                </div>
                            </div>

                            <!-- Receipt Card -->
                            <div class="receipt-card">

                                <!-- Photo header -->
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
                                    <p class="rid">Booking Receipt &nbsp;#<%= res.getReservationId() %>
                                    </p>

                                    <div class="dr">
                                        <span class="dl"><i class="fas fa-user" style="width:14px;"></i> Guest</span>
                                        <span class="dv">
                                            <%= res.getGuestName() %>
                                        </span>
                                    </div>
                                    <div class="dr">
                                        <span class="dl"><i class="fas fa-map-marker-alt" style="width:14px;"></i>
                                            Address</span>
                                        <span class="dv">
                                            <%= res.getAddress() %>
                                        </span>
                                    </div>
                                    <div class="dr">
                                        <span class="dl"><i class="fas fa-phone" style="width:14px;"></i> Contact</span>
                                        <span class="dv">
                                            <%= res.getContactNumber() %>
                                        </span>
                                    </div>
                                    <div class="dr">
                                        <span class="dl"><i class="fas fa-bed" style="width:14px;"></i> Room Type</span>
                                        <span class="dv">
                                            <%= res.getRoomType() %>
                                        </span>
                                    </div>
                                    <div class="dr">
                                        <span class="dl"><i class="fas fa-calendar-check" style="width:14px;"></i>
                                            Check-In</span>
                                        <span class="dv">
                                            <%= res.getCheckIn() %>
                                        </span>
                                    </div>
                                    <div class="dr">
                                        <span class="dl"><i class="fas fa-calendar-times" style="width:14px;"></i>
                                            Check-Out</span>
                                        <span class="dv">
                                            <%= res.getCheckOut() %>
                                        </span>
                                    </div>
                                    <div class="dr">
                                        <span class="dl"><i class="fas fa-moon" style="width:14px;"></i> Duration</span>
                                        <span class="dv">
                                            <%= nights %> night<%= nights !=1 ? "s" : "" %>
                                        </span>
                                    </div>
                                </div>

                                <!-- Total -->
                                <div class="tb">
                                    <div>
                                        <p class="tbl">Total Payable</p>
                                        <p style="font-size:0.65rem; color:var(--sand); opacity:0.5; margin-top:2px;">
                                            Includes all taxes & service charges
                                        </p>
                                    </div>
                                    <span class="tam">$<%= total %></span>
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
            </body>

            </html>