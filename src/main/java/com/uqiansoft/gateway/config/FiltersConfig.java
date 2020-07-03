package com.uqiansoft.gateway.config;

import com.uqiansoft.gateway.filters.MyFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * @author xutao
 * @date 2018-11-26 14:40
 */
//@Configuration
public class FiltersConfig {

    /**
     * 自定义filter会覆盖application.yml里面对指定path的路由配置
     * 注意：全局的路由设置都会失效！！！
     * 不推荐通过代码控制过滤器链
     */
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes().route(r ->
            r.path("/testPath/**")
                .filters(f -> f.stripPrefix(1)                                          // filter链
                        .filter(new MyFilter())
//                        .hystrix(config -> {
//                            config.setName("testFallbackCmd");
//                            config.setFallbackUri("forward:/testFallback");
//                        })
                        .retry(1)

                    )
                .uri("lb://eureka-client")                                              // filter成功后跳转uri
                .id("api-a"))
            .build();
    }
}
