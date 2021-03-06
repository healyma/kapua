/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.listener;

import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;

/**
 * Default camel pojo endpoint listener.
 *
 * @since 1.0
 */
public abstract class AbstractListener {

    // metrics
    private String metricComponentName = "listener";
    private final static MetricsService METRICS_SERVICE = MetricServiceFactory.getInstance();

    protected String name;

    /**
     * Create a listener with the specific name.<BR>
     * The "listener" constant will be used as metricComponentName.
     * 
     * @param name
     *            First level name to categorize the metrics inside the listener
     */
    protected AbstractListener(String name) {
        this.name = name;
    }

    /**
     * Create a listener with the specific metricComponentName and name
     * 
     * @param metricComponentName
     *            Root name to categorize the metrics inside the listener
     * @param name
     *            First level name to categorize the metrics inside the listener
     */
    protected AbstractListener(String metricComponentName, String name) {
        this(name);
        this.metricComponentName = metricComponentName;
    }

    /**
     * Register a Counter with the specified names as suffix.<BR>
     * The prefix is described by a combination of constructor parameters name and metricComponentName depending on which constructor will be used.
     * 
     * @param names
     * @return
     */
    protected Counter registerCounter(String... names) {
        return METRICS_SERVICE.getCounter(metricComponentName, name, names);
    }

    /**
     * Register a Timer with the specified names as suffix.<BR>
     * The prefix is described by a combination of constructor parameters name and metricComponentName depending on which constructor will be used.
     * 
     * @param names
     * @return
     */
    protected Timer registerTimer(String... names) {
        return METRICS_SERVICE.getTimer(metricComponentName, name, names);
    }

}
