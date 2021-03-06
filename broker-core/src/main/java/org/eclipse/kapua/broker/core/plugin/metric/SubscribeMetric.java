/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin.metric;

import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;

public class SubscribeMetric {

    private final static SubscribeMetric SUBSCRIBE_METRIC = new SubscribeMetric();

    private Counter allowedMessages;
    private Counter notAllowedMessages;
    private Timer time;

    public static SubscribeMetric getInstance() {
        return SUBSCRIBE_METRIC;
    }

    private SubscribeMetric() {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        allowedMessages = metricsService.getCounter("security", "subscribe", "allowed", "count");
        notAllowedMessages = metricsService.getCounter("security", "subscribe", "not_allowed", "count");
        time = metricsService.getTimer("security", "subscribe", "time", "s");
    }

    public Counter getAllowedMessages() {
        return allowedMessages;
    }

    public Counter getNotAllowedMessages() {
        return notAllowedMessages;
    }

    public Timer getTime() {
        return time;
    }

}
