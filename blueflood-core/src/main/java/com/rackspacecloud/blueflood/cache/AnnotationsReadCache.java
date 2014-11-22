package com.rackspacecloud.blueflood.cache;

/**
 * Created by tg on 11/21/14.
 */
public interface AnnotationsReadCache {
    public String getValueOfAnnotation(String metricLocator);
    public void setValueOfAnnotation(String metricLocator, String metricValue);
}
