package com.uqiansoft.gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.cloud.gateway.filter.LoadBalancerClientFilter.LOAD_BALANCER_CLIENT_FILTER_ORDER;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

@Slf4j
@Component
//@EnableConfigurationProperties(EnvConfig.class)
//@ConditionalOnProperty(prefix = "env", name = "name", havingValue = "test")
public class EnvironmentFilter implements GlobalFilter, Ordered {

    @Autowired
    LoadBalancerClient loadBalancer;

    @Autowired
    private DiscoveryClient discoveryClient;

//  @Autowired
//  private EnvConfig envConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//    ServerHttpRequest request = exchange.getRequest();
//    HttpHeaders requestHeaders = request.getHeaders();
//    String env = requestHeaders.getFirst("env");

//    if (StringUtils.isNotBlank(env) && StringUtils.isNotBlank(envConfig.getName())
//        && envConfig.getName().equalsIgnoreCase("test")) {
//        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
//        if (envConfig.getTestIps().isEmpty() || envConfig.getTestIps().contains(url.getHost())) {
//          return chain.filter(exchange);
//        }
//        checkAndSetUri(exchange,url,envConfig.getTestIps());
//        return chain.filter(exchange);
//    }
//    if (StringUtils.isNotBlank(env) && env.equalsIgnoreCase("dev")) {
//      URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
//      if (EvnStatisic.testIps.isEmpty() || EvnStatisic.testIps.contains(url.getHost())) {
//        return chain.filter(exchange);
//      }
//      checkAndSetUri(exchange,url,EvnStatisic.testIps);
//      return chain.filter(exchange);
//    }

        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        if (null != url && StringUtils.isNotBlank(url.getQuery()) && url.getQuery().contains("devIp=")) {
            String queryStr = url.getQuery();
            String[] params = queryStr.split("&");
            if (params.length > 0) {
                for (String p : params) {
                    if (p.contains("devIp=")) {
                        p = p.replace("devIp=", "").trim();
                        if (StringUtils.isNotBlank(p)) {
                            url = UriComponentsBuilder.fromUri(url).host(p).build().toUri();
                            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, url);
                            break;
                        }
                    }
                }
            }
        }

        return chain.filter(exchange);
    }

    //  private void checkAndSetUri(ServerWebExchange exchange, URI uri, Set<String> ips) {
//    Route gatewayRout = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
//    String serviceId = gatewayRout.getUri().getHost();
//    List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
//    for (ServiceInstance si:serviceInstances) {
//      if (ips.contains(si.getHost())) {
//        uri = UriComponentsBuilder.fromUri(uri).host(si.getHost()).port(si.getPort()).build().toUri();
//        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR,uri);
//      }
//    }
//
//  }
    @Override
    public int getOrder() {
        return LOAD_BALANCER_CLIENT_FILTER_ORDER + 1;
    }
}
