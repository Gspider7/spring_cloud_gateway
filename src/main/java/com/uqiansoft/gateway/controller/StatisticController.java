package com.uqiansoft.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.uqiansoft.gateway.config.EnvConfig;
import com.uqiansoft.gateway.constant.EvnStatisic;
import com.uqiansoft.gateway.constant.Statistic;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xutao
 * @date 2018-11-27 16:26
 */
@Controller
@RequestMapping("/statistic")
public class StatisticController {

//    @Autowired
//    EnvConfig envConfig;

    @ResponseBody
    @RequestMapping("/get")
    public String get() {
        Map<String, Integer> countMap = new HashMap<>();

        countMap.put("req_count", Statistic.req_count.get());
        countMap.put("fallback_count", Statistic.fallback_count.get());

        // 获取以后自动清空统计
        Statistic.req_count.set(0);
        Statistic.fallback_count.set(0);

        return JSON.toJSONString(countMap);
    }

    @ResponseBody
    @RequestMapping("/addTestIp")
    public Set<String> addTestIp(String ip) {
         Optional.of(ip).ifPresent(i -> EvnStatisic.testIps.add(ip));
         return EvnStatisic.testIps;
    }

    @ResponseBody
    @RequestMapping("/addDevIp")
    public Map<String,String> addDevIp(String serviceId,String ip) {
        if (StringUtils.isNotBlank(serviceId) && StringUtils.isNotBlank(ip)) {
            EvnStatisic.devIpMap.put(serviceId,ip);
        }
        return EvnStatisic.devIpMap;
    }

    @ResponseBody
    @RequestMapping("/clearDevIp")
    public Map<String,String> clearDevIp(){
        EvnStatisic.devIpMap.clear();
        return EvnStatisic.devIpMap;
    }

    @ResponseBody
    @RequestMapping("/getDevIp")
    public Map<String,String> getDevIp(){
        return EvnStatisic.devIpMap;
    }
}
