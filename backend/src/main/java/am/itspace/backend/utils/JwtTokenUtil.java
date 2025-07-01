package am.itspace.backend.utils;

import am.itspace.backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenUtil {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

//  public Date getIssuedAtDateFromToken(String token) {
//    return getClaimFromToken(token, Claims::getIssuedAt);
//  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private boolean isTokenExpired(String token) {
    final Date expirationDateFromToken = getExpirationDateFromToken(token);
    return expirationDateFromToken.before(new Date());
  }

  public String generateToken(String email) {
    Map<String, Object> claims = new HashMap<>();
    return doGenerateToken(claims, email);
  }

  private String doGenerateToken(Map<String, Object> claims, String subject) {
    final Date createdDate = new Date();
    final Date expirationDate = calculateExpirationDate(createdDate);

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(createdDate)
        .setExpiration(expirationDate)
        .signWith(getSignInKey(), SignatureAlgorithm.HS512)
        .compact();
  }

  public String refreshToken(String token) {
    final Date createdDate = new Date();
    final Date expirationDate = calculateExpirationDate(createdDate);

    final Claims claims = getAllClaimsFromToken(token);
    claims.setIssuedAt(createdDate);
    claims.setExpiration(expirationDate);

    return Jwts.builder()
        .setClaims(claims)
        .signWith(getSignInKey(), SignatureAlgorithm.HS512)
        .compact();
  }

  public boolean validateToken(String token, String email) {
    final String username = getUsernameFromToken(token);
    return (username.equals(email) && !isTokenExpired(token));
  }

  private Date calculateExpirationDate(Date createdDate) {
    return new Date(createdDate.getTime() + expiration * 1000);
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

}
