package com.happybot.vcoupon.application;

import android.app.Application;

import com.happybot.vcoupon.BuildConfig;
import com.squareup.leakcanary.LeakCanary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

public class VCouponApplication extends Application {
    // Logger for set log level
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize leak canary
        initializeLeakCanary();

        // Set log level
        setupLogLevel();
    }


    public static Boolean isInDebugMode() {
        return BuildConfig.DEBUG;
    }

    private void initializeLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }

        LeakCanary.install(this);
    }

    private void setupLogLevel() {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
                .getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        Level logLevel = Level.INFO;
        if (isInDebugMode()) {
            logLevel = Level.TRACE;
        }

        logger.setLevel(logLevel);
        LOG.info("Set log level to " + logLevel);
    }
}
