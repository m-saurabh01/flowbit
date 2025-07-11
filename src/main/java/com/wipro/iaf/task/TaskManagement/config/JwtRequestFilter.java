package com.wipro.iaf.task.TaskManagement.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wipro.iaf.task.TaskManagement.services.CustomUserDetailsService;
import com.wipro.iaf.task.TaskManagement.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 🔒 Avoid infinite loop by skipping login/auth/static URLs
		String path = request.getServletPath();
		if (path.equals("/login") || path.equals("/signup") || path.startsWith("/auth") || path.startsWith("/css")
				|| path.startsWith("/js") || path.startsWith("/static") || path.startsWith("/error")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = null;
		String username = null;

		final String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
		}

		if (token == null && request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("token")) {
					token = cookie.getValue();
					break;
				}
			}
		}

		if (token != null) {
			try {
				username = jwtUtil.extractUsername(token);
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				if (jwtUtil.validateToken(token, userDetails.getUsername())) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());

					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				} else {
					response.sendRedirect("/login");
					return;
				}

			} catch (ExpiredJwtException e) {
				System.out.println("JWT expired: " + e.getMessage());
				response.sendRedirect("/login?expired=true");
				return;
			} catch (Exception e) {
				System.out.println("JWT error: " + e.getMessage());
				response.sendRedirect("/login");
				return;
			}

		} else {
			response.sendRedirect("/login");
			return;
		}

		filterChain.doFilter(request, response);
	}
}
