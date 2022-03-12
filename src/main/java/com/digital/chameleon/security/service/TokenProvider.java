package com.digital.chameleon.security.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.digital.chameleon.security.entity.User;
import com.digital.chameleon.security.repo.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * @author Vineetkabacus@gmail.com
 * @Purpose This class handle all application JWT token creation operations.
 */
@Service
public class TokenProvider {

  private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

  @Value("${jwtauth.app.jwtSecret}")
  private String jwtSecret;

  @Value("${jwtauth.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Autowired
  private UserRepository userRepository;

  public String createTokenForUser(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("id", user.getId());
    claims.put("email", user.getEmailId());
    claims.put("phone_number", user.getMobile());
    claims.put("username", user.getFirstName() + "" + user.getLastName());
    return generateToken(claims);
  }

  public String createTokenForUserFinnacle(String username) {
    Map<String, Object> claims = new HashMap<>();
    User user = userRepository.findByEmailId(username);
    claims.put("id", user.getId());
    claims.put("email", user.getEmailId());
    claims.put("phone_number", user.getMobile());
    return generateToken(claims);
  }

  private String generateToken(Map<String, Object> claims) {
    return Jwts.builder().setClaims(claims).setSubject(String.valueOf(claims.get("id")))
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
  }

  public String getUserIdFromToken(String token) {
    Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    return claims.getSubject();
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public String extractUserId(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
    final Claims claims = extractAllClaims(token);
    return claimResolver.apply(claims);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public boolean validateToken(String authToken, UserDetails userDetails) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return (!isTokenExpired(authToken));
    } catch (SignatureException ex) {
      logger.error("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      logger.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      logger.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      logger.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      logger.error("JWT claims string is empty.");
    }
    return false;
  }
}
