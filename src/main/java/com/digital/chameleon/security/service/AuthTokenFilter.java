package com.digital.chameleon.security.service;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.digital.chameleon.common.CommonMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");
		String userId = null;
		String jwt = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			jwt = authHeader.substring(7);
			userId = tokenProvider.getUserIdFromToken(jwt);
		}

		if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
			if (userDetails.isEnabled()) {
				if (tokenProvider.validateToken(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			} else {
				CommonMessage commonMessage = new CommonMessage();
				commonMessage.setMessage("JWT Token Expired");
				response.setStatus(401);
				response.setContentType("application/json");
				// pass down the actual obj that exception handler normally send
				ObjectMapper mapper = new ObjectMapper();
				PrintWriter out = response.getWriter();
				out.print(mapper.writeValueAsString(commonMessage));
				out.flush();
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

}
