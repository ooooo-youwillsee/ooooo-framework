package com.ooooo.framework.web;

import com.ooooo.framework.context.result.Result;
import com.ooooo.framework.web.annotation.DuplicatedSubmit;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@RestController
@SpringBootApplication
public class WebTestConfiguration {

  @PostMapping("/test")
  @DuplicatedSubmit
  public Result<Map<String, Object>> testPost(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> body) {
    return Result.success(body);
  }

}
