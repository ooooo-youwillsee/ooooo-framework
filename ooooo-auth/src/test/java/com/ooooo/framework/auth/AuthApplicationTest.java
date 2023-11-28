package com.ooooo.framework.auth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ooooo.framework.auth.endpoint.AccessToken;
import com.ooooo.framework.auth.endpoint.AuthorizeEndpoint;
import com.ooooo.framework.context.result.Result;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(classes = AuthTestConfiguration.class)
public class AuthApplicationTest {

  @Autowired
  private MockMvc mockMvc;

  @SneakyThrows
  @Test
  public void test403() {
    MvcResult mvcResult = mockMvc.perform(
        // the requested url [/403] isn't exist
        MockMvcRequestBuilders.request(HttpMethod.POST, "/403")
    ).andReturn();

    MockHttpServletResponse response = mvcResult.getResponse();
    log.info("status: {}", response.getStatus());
    log.info("content: {}", response.getContentAsString());
    log.info("errorMessage: {}", response.getErrorMessage());

    assertEquals(403, response.getStatus());
  }


  @SneakyThrows
  @Test
  public void testGetToken() {
    Map<String, Object> params = new HashMap<>();
    params.put("username", "tom");
    params.put("password", "123");

    MvcResult mvcResult = mockMvc.perform(
        MockMvcRequestBuilders.request(HttpMethod.POST, AuthorizeEndpoint.TOKEN_URL)
            .content(JSON.toJSONString(params))
            .contentType(MediaType.APPLICATION_JSON)
    ).andReturn();

    MockHttpServletResponse response = mvcResult.getResponse();
    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.getErrorMessage()).isNull();

    AccessToken accessToken = JSON.parseObject(response.getContentAsString(), new TypeReference<Result<AccessToken>>() {}).getData();
    assertThat(accessToken.getToken()).isNotBlank();
    assertThat(accessToken.getExpireAt()).isAfter(new Date());
  }


  /**
   * 先获取 accessToken, 然后再请求
   */
  @SneakyThrows
  @Test
  public void testGetTokenThenRequest() {
    Map<String, Object> params = new HashMap<>();
    params.put("username", "tom");
    params.put("password", "123");

    MvcResult mvcResult = mockMvc.perform(
        MockMvcRequestBuilders.request(HttpMethod.POST, AuthorizeEndpoint.TOKEN_URL)
            .content(JSON.toJSONString(params))
            .contentType(MediaType.APPLICATION_JSON)
            .locale(Locale.CHINA)
    ).andReturn();

    String content = mvcResult.getResponse().getContentAsString();
    AccessToken accessToken = JSON.parseObject(content, new TypeReference<Result<AccessToken>>() {}).getData();

    // request url for /auth/test2
    mvcResult = mockMvc.perform(
        MockMvcRequestBuilders.request(HttpMethod.POST, "/auth/test2")
            .header(HttpHeaders.AUTHORIZATION, accessToken.getToken())
    ).andReturn();

    MockHttpServletResponse response = mvcResult.getResponse();
    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.getErrorMessage()).isNull();

    String result = JSON.parseObject(response.getContentAsString(), new TypeReference<Result<String>>() {}).getData();
    assertThat(result).isEqualTo("test2:1");
  }

}
