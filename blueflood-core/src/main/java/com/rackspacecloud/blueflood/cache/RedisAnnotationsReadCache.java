package com.rackspacecloud.blueflood.cache;

import redis.clients.jedis.Jedis;

/**
 * Created by tg on 11/21/14.
 */
public class RedisAnnotationsReadCache implements  AnnotationsReadCache {
    private static Jedis jedis = new Jedis("localhost");

    @Override
    public String getValueOfAnnotation(String metricLocator) {
        return jedis.get(metricLocator);
    }

    @Override
    public void setValueOfAnnotation(String metricLocator, String metricValue) {
        jedis.set(metricLocator,metricValue);
    }


}
