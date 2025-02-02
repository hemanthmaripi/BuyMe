package com.example.demo.Filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.AuthService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = { "/api/*", "/admin/*" }) // Applies the filter to all api endpoints
public class AuthenticationFilter implements Filter {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private static final String ALLOWED_ORIGIN = "http://localhost:5173";

    private static final String[] UNAUTHENTICATED_PATHS = { "/api/users/register", "/api/auth/login" };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            executeFilterLogic(request, response, chain);
        } catch (Exception e) {
            logger.error("Unexpected error in AuthenticationFilter", e);
            sendErrorResponse((HttpServletResponse) response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal server error");
        }
    }

    private void executeFilterLogic(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
    	System.out.println("Im in filter");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        logger.info("Request URI: {}", requestURI);

        // Allow unauthenticated paths
        if (Arrays.asList(UNAUTHENTICATED_PATHS).contains(requestURI)) {
            chain.doFilter(request, response);
            return;
        }
        	
        setCORSHeaders(httpResponse);
        // Handle preflight (OPTIONS) requests
        if (httpRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
        	System.out.println("CORS setting...");
            setCORSHeaders(httpResponse);
            return;
        }
        
        
        

        // Extract and validate the token
        String token = getAuthTokenFromCookies(httpRequest);
        logger.info("Retrieved token from cookies: {}", token);
        if(token==null) {
        System.out.println("token is null");
        } else {
        	System.out.println("token is not null " + token);
        	System.err.println("Here is coming");
        }

        if (token == null || !authService.validateToken(token)) {
            logger.warn("Invalid or missing token");
            System.out.println(authService.validateToken(token));
            System.out.println("invalid");
            sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized: Invalid or missing token");
            return;
        }

        // Extract username and verify user
        String username = authService.extractUsername(token);
        Optional<User> userOptional = userRepository.findByUserName(username);
        if (userOptional.isEmpty()) {
            logger.warn("User not found for username: {}", username);
            sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: User not found");
            return;
        }

        // Get authenticated user and role
        User authenticatedUser = userOptional.get();
        Role role = authenticatedUser.getRole();
        System.out.println(role);
        logger.info("Authenticated User: {}, Role: {}", authenticatedUser.getUserName(), role);
        
        System.out.println(authenticatedUser.getUserName());
        System.out.println(role);
        
        // Role-based access control
        if (requestURI.startsWith("/admin/") && role != Role.admin) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_FORBIDDEN, "Forbidden: Admin access required");
            return;
        }

        if (requestURI.startsWith("/api/") && (role != Role.customer && role != Role.admin)) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_FORBIDDEN, "Forbidden: Customer access required");
            return;
        }

        // Attach user details to request
        httpRequest.setAttribute("authenticatedUser", authenticatedUser);
        chain.doFilter(request, response);
    }

    private void setCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.getWriter().write(message);
    }

    private String getAuthTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            logger.warn("No cookies found in the request");
            return null;
        }

        return Arrays.stream(cookies)
                .peek(cookie -> logger.debug("Cookie: {}={}", cookie.getName(), cookie.getValue()))
                .filter(cookie -> "authToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
