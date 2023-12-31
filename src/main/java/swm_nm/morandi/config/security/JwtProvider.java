package swm_nm.morandi.config.security;


import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import swm_nm.morandi.domain.auth.constants.SecurityConstants;
import swm_nm.morandi.domain.auth.response.TokenDto;
import swm_nm.morandi.global.exception.errorcode.AuthErrorCode;
import swm_nm.morandi.global.exception.MorandiException;
import swm_nm.morandi.domain.member.entity.Member;

import java.security.Key;
import java.security.PublicKey;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtProvider {
    private final SecurityConstants securityConstants;
    private Optional<Jws<Claims>> parseTokenToJws(String token){
        if(token==null)
            throw new MorandiException(AuthErrorCode.INVALID_TOKEN);
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(getPublicKey())
                    .build()
                    .parseClaimsJws(token);
            return Optional.of(claimsJws);

        }  catch (ExpiredJwtException e) {
            log.info("만료된 JWT Token");
            throw new MorandiException(AuthErrorCode.EXPIRED_TOKEN);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("유효하지 않은 JWT signature");
            throw new MorandiException(AuthErrorCode.INVALID_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 토큰");
            throw new MorandiException(AuthErrorCode.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("IllegalArgumentException");

        }
        throw new MorandiException(AuthErrorCode.INVALID_TOKEN);

    }
    private Jws<Claims> getJws(String token) {
        return parseTokenToJws(token).orElseThrow(() -> new MorandiException(AuthErrorCode.INVALID_TOKEN));
    }
    public boolean validateToken(String token){
        return parseTokenToJws(token).isPresent();
    }

    public Long getUserIdfromToken(String token) {
        Claims claims = getJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }
    public TokenDto getTokens(Member member){
        String acccessToken = generateAccessToken(member.getMemberId(),"USER");
        
        String refreshToken = generateRefreshToken(member.getMemberId());
        return TokenDto.builder()
                .accessToken(acccessToken)
                .refreshToken(refreshToken)
                .build();
    }
    private Key getPrivateKey() {
        return securityConstants.getPrivateKey();
    }
    private PublicKey getPublicKey() {
        return securityConstants.getPublicKey();
    }
    private String buildAccessToken(
            Long id, Date issuedAt, Date accessTokenExpiresIn, String role) {
        final Key encodedKey = getPrivateKey();
        return Jwts.builder()
                .setIssuer("MORANDI")
                .setIssuedAt(issuedAt)
                .setSubject(id.toString())
                .claim("type", "access_token")
                .claim("role", role)
                .setExpiration(accessTokenExpiresIn)
                .signWith(encodedKey)
                .compact();
    }

    private String buildRefreshToken(Long id, Date issuedAt, Date accessTokenExpiresIn) {
        final Key encodedKey = getPrivateKey();
        return Jwts.builder()
                .setIssuer("MORANDI")
                .setIssuedAt(issuedAt)
                .setSubject(id.toString())
                .claim("type", "refresh_token")
                .setExpiration(accessTokenExpiresIn)
                .signWith(encodedKey)
                .compact();
    }

    public String generateAccessToken(Long id, String role) {
        final Date issuedAt = new Date();
        final Date accessTokenExpiresIn =
                //현재시각 + SecurityConstants의 JWT_EXPIRATION
                new Date(issuedAt.getTime() + securityConstants.JWT_EXPIRATION);

        return buildAccessToken(id, issuedAt, accessTokenExpiresIn, role);
    }
    public String generateRefreshToken(Long id) {
        final Date issuedAt = new Date();
        final Date refreshTokenExpiresIn =
                new Date(issuedAt.getTime() +  securityConstants.REFRESH_TOKEN_EXPIRATION);
        return buildRefreshToken(id, issuedAt, refreshTokenExpiresIn);
    }

//
//    public AccessTokenInfo parseAccessToken(String token) {
//        if (isAccessToken(token)) {
//            Claims claims = getJws(token).getBody();
//            return AccessTokenInfo.builder()
//                    .userId(Long.parseLong(claims.getSubject()))
//                    .role((String) claims.get(TOKEN_ROLE))
//                    .build();
//        }
//        throw InvalidTokenException.EXCEPTION;
//    }
//
//    public Long parseRefreshToken(String token) {
//        try {
//            if (isRefreshToken(token)) {
//                Claims claims = getJws(token).getBody();
//                return Long.parseLong(claims.getSubject());
//            }
//        } catch (ExpiredTokenException e) {
//            throw RefreshTokenExpiredException.EXCEPTION;
//        }
//        throw InvalidTokenException.EXCEPTION;
//    }
//
//    public Long getRefreshTokenTTlSecond() {
//        return jwtProperties.getRefreshExp();
//    }
//
//    public Long getAccessTokenTTlSecond() {
//        return jwtProperties.getAccessExp();
//    }
//

}