package com.ooooo.framework.trace.web;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@SpringBootTest(classes = TraceTestConfiguration.class)
@AutoConfigureMockMvc
class WebTraceFilterTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @SneakyThrows
  void testGet() {
    MvcResult mvcResult = mockMvc.perform(
        MockMvcRequestBuilders.request(HttpMethod.GET, "/test").queryParam("abc", "123")
    ).andReturn();

    assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
  }

  @Test
  @SneakyThrows
  void testPost() {
    MvcResult mvcResult = mockMvc.perform(
        MockMvcRequestBuilders.request(HttpMethod.POST, "/test")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JSON.toJSONString(MapUtil.of("abc", "123")))
    ).andReturn();

    assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
  }

  @Test
  @SneakyThrows
  void testFile() {
    MvcResult mvcResult = mockMvc.perform(
        MockMvcRequestBuilders.request(HttpMethod.GET, "/test", "abc", "123")
    ).andReturn();

    assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
  }

}