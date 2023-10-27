package com.ooooo.infra.auth.endpoint;

import com.alibaba.fastjson.JSON;
import com.ooooo.infra.context.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
public class InvalidTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	private static final String INVALID_TOKEN = JSON.toJSONString(Result.fail("无效的Token"));
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		if (response.isCommitted()) {
			log.trace("Did not write to response since already committed");
			return;
		}
		
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		PrintWriter writer = response.getWriter();
		writer.write(INVALID_TOKEN);
		writer.flush();
		writer.close();
	}
}
