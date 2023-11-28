package com.ooooo.framework.trace.web;

import cn.hutool.core.util.StrUtil;
import com.ooooo.framework.context.result.Result;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@RestController
@SpringBootApplication
public class TraceTestConfiguration {

  @GetMapping("/test")
  public Result<String> testGet() {
    return Result.success("haha");
  }


  @PostMapping("/test")
  public Result<Map<String, Object>> testPost(@RequestBody Map<String, Object> body) {
    return Result.success(body);
  }


  @PostMapping("/file")
  public Result<String> testFile(@RequestParam("file") MultipartFile file) {
    return Result.success(StrUtil.join(":", file.getName(), file.getSize()));
  }
}
