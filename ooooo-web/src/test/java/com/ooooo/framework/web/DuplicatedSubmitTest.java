package com.ooooo.framework.web;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.concurrent.Callable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = WebTestConfiguration.class)
public class DuplicatedSubmitTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @SneakyThrows
  void test() {
    Callable<MvcResult> request = () -> {
      MvcResult mvcResult = mockMvc.perform(
          MockMvcRequestBuilders.request(HttpMethod.POST, "/test")
              .contentType(MediaType.APPLICATION_JSON)
              .content(JSONUtil.toJsonStr(MapUtil.of("abc", "123")))
      ).andReturn();
      return mvcResult;
    };

    assertThat(request.call().getResponse().getStatus()).isEqualTo(200);
    assertThatThrownBy(request::call).hasMessageContaining("您不能重复提交，请稍后再试");
  }

}
