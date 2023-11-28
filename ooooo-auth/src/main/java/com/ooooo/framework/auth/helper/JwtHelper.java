package com.ooooo.framework.auth.helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
public class JwtHelper {
	
	public static final String ISSUER = "ooooo";
	
	public String signToken(Map<String, Object> header, Map<String, ?> payload) {
		Algorithm algorithm = Algorithm.none();
		return JWT.create().withHeader(header).withPayload(payload).withIssuer(ISSUER).sign(algorithm);
	}
	
	public DecodedJWT verifyToken(String token) {
		try {
			Algorithm algorithm = Algorithm.none();
			JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
			return verifier.verify(token);
			
		} catch (JWTVerificationException exception) {
			log.error("jwtToken[{}] 解密失败, {}", token, exception);
			throw new IllegalArgumentException("解密失败, jwtToken['" + token + "']");
		}
	}

}
