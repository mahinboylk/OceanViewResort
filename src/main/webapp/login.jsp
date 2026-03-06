<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <% if (session.getAttribute("user") !=null) { response.sendRedirect("reservation?action=list"); return; } String
        errorMsg="" ; Object attrErr=request.getAttribute("error"); if (attrErr !=null) { errorMsg=attrErr.toString(); }
        else if ("db_error".equals(request.getParameter("error"))) {
        errorMsg="Database connection failed. Please contact the administrator." ; } %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Ocean View Resort &mdash; Staff Login</title>
            <link rel="stylesheet" href="css/style.css">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        </head>

        <body data-page="login">

            <div class="page-overlay"></div>
            <div class="page-content">
                <div class="login-shell">

                    <div class="login-hero">
                        <span class="login-over">
                            <i class="fas fa-map-marker-alt" style="margin-right:6px;"></i>Galle, Sri Lanka
                        </span>
                        <h1>Where the <em>Indian Ocean</em><br>meets luxury.</h1>
                        <div class="login-divider"></div>
                        <p>Ocean View Resort offers an unparalleled escape on Sri Lanka's southern coast &mdash;
                            where golden sands, turquoise waters, and timeless hospitality converge.</p>
                    </div>

                    <div class="glass login-card">
                        <div class="hotel-icon"><i class="fas fa-umbrella-beach"></i></div>
                        <span class="eyebrow" style="display:block; text-align:center;">Staff Access Only</span>
                        <h2>Sign In</h2>
                        <p class="login-sub">Ocean View Resort Portal</p>

                        <% if (!errorMsg.isEmpty()) { %>
                            <div class="error-banner">
                                <i class="fas fa-exclamation-circle"></i>
                                <%= errorMsg %>
                            </div>
                            <% } %>

                                <form action="login" method="post">
                                    <div class="fg">
                                        <label><i class="fas fa-user"
                                                style="opacity:0.5; margin-right:4px;"></i>Username</label>
                                        <input type="text" name="username" placeholder="Enter your username" required
                                            autocomplete="username">
                                    </div>
                                    <div class="fg">
                                        <label><i class="fas fa-lock"
                                                style="opacity:0.5; margin-right:4px;"></i>Password</label>
                                        <input type="password" name="password" placeholder="Enter your password"
                                            required autocomplete="current-password">
                                    </div>
                                    <button type="submit" class="btn btn-primary btn-full">
                                        <i class="fas fa-sign-in-alt"></i> Sign In
                                    </button>
                                </form>
                    </div>

                </div>
            </div>

        </body>

        </html>