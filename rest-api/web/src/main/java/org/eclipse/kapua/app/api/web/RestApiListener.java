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
package org.eclipse.kapua.app.api.web;

import static com.google.common.base.MoreObjects.firstNonNull;
import static org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers.resolveJdbcUrl;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_JDBC_DRIVER;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_PASSWORD;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_SCHEMA;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_SCHEMA_ENV;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_SCHEMA_UPDATE;
import static org.eclipse.kapua.commons.setting.system.SystemSettingKey.DB_USERNAME;

import java.util.Optional;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.ServiceModuleBundle;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestApiListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(RestApiListener.class);

    private ServiceModuleBundle moduleBundle;

    @Override
    public void contextInitialized(final ServletContextEvent event) {
        SystemSetting config = SystemSetting.getInstance();
        if(config.getBoolean(DB_SCHEMA_UPDATE, false)) {
            logger.info("Initialize Liquibase embedded client.");
            String dbUsername = config.getString(DB_USERNAME);
            String dbPassword = config.getString(DB_PASSWORD);
            String schema = firstNonNull(config.getString(DB_SCHEMA_ENV), config.getString(DB_SCHEMA));

            // initialize driver
            try {
                Class.forName(config.getString(DB_JDBC_DRIVER));
            } catch (ClassNotFoundException e) {
                logger.warn("Could not find jdbc driver: {}", config.getString(DB_JDBC_DRIVER));
            }

            logger.debug("Starting Liquibase embedded client update - URL: {}, user/pass: {}/{}", new Object[]{resolveJdbcUrl(), dbUsername, dbPassword});
            new KapuaLiquibaseClient(resolveJdbcUrl(), dbUsername, dbPassword, Optional.of(schema)).update();
        }

        // Start service modules
        try {
            logger.info("Starting service modules...");
            if (moduleBundle == null) {
                moduleBundle = new ServiceModuleBundle();
            }
            moduleBundle.startup();
            logger.info("Starting service modules...DONE");
        } catch (KapuaException e) {
            logger.error("Cannot start service modules: {}", e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        // stop event modules
        try {
            logger.info("Stopping service modules...");
            if (moduleBundle != null) {
                moduleBundle.shutdown();;
                moduleBundle = null;
            }
            logger.info("Stopping service modules...DONE");
        } catch (KapuaException e) {
            logger.error("Cannot stop service modules: {}", e.getMessage(), e);
        }       
    }

}
