package com.ooooo.infra.auth.endpoint;

import com.alibaba.fastjson.JSON;
import com.ooooo.infra.context.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

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
public class NoPermissionAccessDeniedHandler implements AccessDeniedHandler {
	
	private static final String NO_PERMISSON = JSON.toJSONString(Result.fail("您没有权限访问"));
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		if (response.isCommitted()) {
			log.trace("Did not write to response since already committed");
			return;
		}
		
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		PrintWriter writer = response.getWriter();
		writer.write(NO_PERMISSON);
		writer.flush();
		writer.close();
	}
}
