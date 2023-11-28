package com.ooooo.framework.context.expression;

import com.ooooo.framework.context.ContextTestConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SpEL Expression Parser
 *
 */
@Slf4j
@SpringBootTest(classes = {ContextTestConfiguration.class, ExpressionParserTest.TestExpressionConfiguration.class})
public class ExpressionParserTest {

  @Autowired
  private ExpressionParser expressionParser;

  private Map<String, Object> variables = new HashMap<>();

  private String expression;

  /**
   * 表达式场景1(普通表达式)
   */
  @Test
  void expressionParseCase1() {
    variables.put("age", 80);
    variables.put("risk_level", "C1");
    variables.put("asset_prop", 0);

    expression = "#{ #age >= 70 && {'C1', 'C2', 'C3'}.contains(#risk_level) || #asset_prop == 0 }";

    boolean result = expressionParser.parse(expression, variables, Boolean.class);
    log.info("expression: {}, result: {}", expression, result);
    assertTrue(result);
  }

  /**
   * 表达式场景2(new matches)
   */
  @Test
  void expressionParseCase2() {
    variables.put("busin_type", null);
    variables.put("name", "xiaoming");
    variables.put("id_no", "132457");

    expression = "#{ (busin_type = new String()) != null && #name matches 'xi\\S+' && #id_no.length() == 6 }";

    boolean result = expressionParser.parse(expression, variables, Boolean.class);
    log.info("expression: {}, result: {}", expression, result);
    assertTrue(result);
  }

  /**
   * 表达式场景3(contains instanceof)
   */
  @Test
  void expressionParseCase3() {
    variables.put("image_path", "/usr/local/dir");
    variables.put("busin_type", 13897);

    expression = "#{ #image_path.contains('usr/local') && #busin_type instanceof T(Integer) }";

    boolean result = expressionParser.parse(expression, variables, Boolean.class);
    log.info("expression: {}, result: {}", expression, result);
    assertTrue(result);
  }

  /**
   * 表达式场景4(. [] {} : substring containsValue... T())
   */
  @Test
  void expressionParseCase4() {
    Map<String, Object> nest_params = new HashMap<>();
    nest_params.put("name", "zhaosan");
    variables.put("people", nest_params);

    expression = "#{ {user_name:'zh', password:785}.containsValue(#people['name'].substring(0,2)) && T(java.math.RoundingMode).UP instanceof T(Enum) }";

    boolean result = expressionParser.parse(expression, variables, Boolean.class);
    log.info("expression: {}, result: {}", expression, result);
    assertTrue(result);
  }

  /**
   * 表达式场景5(# #Method #root #this @)
   */
  @Test
  void expressionParseCase5() throws Exception {
    List<Integer> cols = Stream.of(2, 4, 5, 6, 8, 9).collect(Collectors.toList());
    variables.put("startsWithIgnoreCase", StringUtils.class.getDeclaredMethod("startsWithIgnoreCase", String.class, String.class));
    variables.put("col", cols);
    variables.put("age", 20);

    expression = "#{ #startsWithIgnoreCase('chaoyun', 'ch') && (#col.?[#this>7]).size() == 2 && @userService.ageGreaterThan(#age) }";

    boolean result = expressionParser.parse(expression, variables, Boolean.class);
    log.info("expression: {}, result: {}", expression, result);
    assertTrue(result);
  }

  /**
   * 表达式场景6(? : ?: ?.)
   */
  @Test
  void expressionParseCase6() throws Exception {
    Map<String, Object> nest_params = new HashMap<>();
    nest_params.put("name", "zhaosan");
    variables.put("people", nest_params);
    variables.put("user_name", "xiaoyun");

    expression = "#{ (#user_name != null ? true : false) && !((#user_name = null)?:'yunxia').equals('xiaoyun') && #people?.name.length() > 5 }";

    boolean result = expressionParser.parse(expression, variables, Boolean.class);
    log.info("expression: {}, result: {}", expression, result);
    assertTrue(result);
  }

  /**
   * 表达式场景7(.? .!)
   */
  @Test
  void expressionParseCase7() throws Exception {
    Map<String, Object> nest_params = new HashMap<>();
    nest_params.put("age1", 10);
    nest_params.put("age2", 20);
    nest_params.put("age3", 30);
    List<PropertyValue> list_params = new LinkedList<>();
    list_params.add(new PropertyValue("x", 13));
    list_params.add(new PropertyValue("y", 15));

    variables.put("ages", nest_params);
    variables.put("groups", list_params);

    expression = "#{ #ages.?[value < 25].keySet().size() == 2 && groups.![name].contains('y') }";

    boolean result = expressionParser.parse(expression, variables, Boolean.class);
    log.info("expression: {}, result: {}", expression, result);
    assertTrue(result);
  }

  @Test
  void expressionFunctions() {
    expression = "#isNotBlank('1', '2')";

    Boolean result = expressionParser.parse(expression, variables, Boolean.class);
    log.info("expression: {}, result: {}", expression, result);
    assertTrue(result);

    variables.put("name", "tom");
    variables.put("age", "123");

    expression = "#isNotBlank(#name, #age)";
    result = expressionParser.parse(expression, variables, Boolean.class);
    log.info("expression: {}, result: {}", expression, result);
    assertTrue(result);

    variables.put("haha", "");

    expression = "#isNotBlank(#name, #age, #haha)";
    result = expressionParser.parse(expression, variables, Boolean.class);
    log.info("expression: {}, result: {}", expression, result);
    assertFalse(result);
  }

  @TestConfiguration
  public static class TestExpressionConfiguration {

    @Slf4j
    @Data
    @Component("userService")
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TestExpressionService {

      private String name;

      private Integer password;

      public boolean ageGreaterThan(int age) {
        return age > 18;
      }
    }
  }
}
