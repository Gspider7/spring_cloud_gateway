package com.uqiansoft.gateway.filters;

import com.alibaba.fastjson.JSON;
import com.uqiansoft.gateway.constant.ShiroConfig;
import com.uqiansoft.gateway.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 获取请求地址和请求token，判断该请求是否有权限，如果有权限执行转发，没有权限返回错误信息
 * 参考：https://my.oschina.net/tongyufu/blog/2120317
 * 注意：如果是本地请求，不会进入这个filter
 *
 * @author xutao
 * @date 2018-11-27 09:14
 */
@Slf4j
@SuppressWarnings("all")
@Component
public class RequestForwardFilter implements GlobalFilter, Ordered {

    private List<String> definedWhiteList = null;

    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private DiscoveryClient discoveryClient;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders requestHeaders = request.getHeaders();

        // 如果请求地址不在白名单内
        String requestUri = request.getPath().pathWithinApplication().value();          // 请求地址，除去ip，port，context
        List<String> whiteList = getWhiteList();
        if (!whiteList.contains(requestUri) && !requestUri.contains("webSocket")) {
            // 如果没有token，返回错误信息
            String authToken = requestHeaders.getFirst(ShiroConfig.AUTH_TOKEN);
            if (authToken == null) {
                ResponseData responseData = ResponseData.getNoAuthority();
                return writeResponseData(response, responseData, HttpStatus.OK);
            }

            // 获取实际的请求地址
//            String realRequestUri = "/" + Arrays.stream(org.springframework.util.StringUtils.tokenizeToStringArray(requestUri, "/"))
//                    .skip(1).collect(Collectors.joining("/"));
            String realRequestUri = requestUri;

            // 如果未开启鉴权服务，直接放行
            List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(ShiroConfig.SHIRO_SERVICE_ID);
            if (CollectionUtils.isEmpty(serviceInstanceList)) {
                return chain.filter(exchange);
            }

            // 请求鉴权服务器进行鉴权
            int index = ThreadLocalRandom.current().nextInt(serviceInstanceList.size());
            String serviceUri = serviceInstanceList.get(index).getUri().toString();
            WebClient webClient = WebClient.create(serviceUri);
            String checkAuthPath = "/auth/checkAuth?token=" + authToken + "&url=" + realRequestUri;
            ResponseData res = new ResponseData();
            return webClient
                    .post()
                    .uri(checkAuthPath)
                    .retrieve()
                    .bodyToMono(ResponseData.class)
                    .map(t -> {
                        BeanUtils.copyProperties(t, res);
                        return "1";
                    }).then(Mono.using(
                            () -> res.getData(),
                            data -> {
                                log.debug("收到鉴权服务器的返回信息: {}", data);

                                Map authorization = JSON.parseObject(data, Map.class);
                                if (ResponseData.AUTHORITY_YES.equals(authorization.get("authorized"))) {
                                    // 将用户信息添加到header
                                    Map user = (Map) authorization.get("user");
                                    log.debug("鉴权成功，解析得到用户信息: {}", JSON.toJSONString(user));
                                    ServerHttpRequest newRequest = request.mutate()
                                            .header(ShiroConfig.USER_INFO, JSON.toJSONString(user))
                                            .build();
                                    ServerWebExchange newExchange = exchange.mutate()
                                            .request(newRequest)
                                            .response(response)
                                            .build();
                                    return chain.filter(newExchange);
                                } else {
                                    ResponseData responseData = ResponseData.getNoAuthority();
                                    return writeResponseData(exchange.getResponse(), responseData, HttpStatus.OK);
                                }
                            },
                            status -> res.setData(status)
                    ));
        }

        return chain.filter(exchange);
    }

    /**
     * 将响应信息写入response
     */
    private Mono<Void> writeResponseData(ServerHttpResponse response, ResponseData responseData, HttpStatus status) {
        byte[] resultStream = JSON.toJSONString(responseData).getBytes(Charset.forName("UTF-8"));
        DataBuffer buffer = response.bufferFactory().wrap(resultStream);

        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }


    private List<String> getWhiteList() {
        if (definedWhiteList == null) {
            definedWhiteList = new ArrayList<>();

            definedWhiteList.add(ShiroConfig.QUALITY_CHECK_AUTH_URI);
            definedWhiteList.add(ShiroConfig.QUALITY_LOGIN_URI);
            definedWhiteList.add(ShiroConfig.QUALITY_LOGOUT_URI);
            definedWhiteList.add(ShiroConfig.QUALITY_ADD_ADMIN_URI);

            definedWhiteList.add(ShiroConfig.QUALITY_POLY_WEBSOCKET);
        }

        return definedWhiteList;
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
