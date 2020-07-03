package com.uqiansoft.gateway.constant;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public interface EvnStatisic {
  Set<String> testIps = new HashSet<>();

  Map<String,String> devIpMap = new  ConcurrentHashMap<>();
}
