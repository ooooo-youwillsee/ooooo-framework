package com.ooooo.framework.trace.web;

import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@SpringBootTest(classes = TraceTestConfiguration.class)
public class DubboTraceFilterTest {

    @Test
    void test() {
        // provider
        ServiceConfig<UserService> serviceConfig = new ServiceConfig<>();
        serviceConfig.setInterface(UserService.class);
        serviceConfig.setRef(new UserServiceImpl());
        serviceConfig.setRegister(false);
        // consumer
        ReferenceConfig<UserService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(UserService.class);
        DubboBootstrap dubboBootstrap = DubboBootstrap.getInstance()
                .service(serviceConfig)
                .reference(referenceConfig)
                .application("test")
                .start();

        UserService userService = dubboBootstrap.getCache().get(UserService.class);

        String message = userService.say("hello");
        Assertions.assertThat(message).isEqualTo("say hello");
    }


    public interface UserService {

        String say(String message);
    }

    public static class UserServiceImpl implements UserService {

        @Override
        public String say(String message) {
            return "say " + message;
        }
    }

}
