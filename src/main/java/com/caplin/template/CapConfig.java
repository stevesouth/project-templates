package com.caplin.template;

import java.io.IOException;
import java.util.Properties;

public class CapConfig {

    private static final Properties configuration = new Properties();
    static {
        try {
            configuration.load(CapConfig.class.getResourceAsStream("/adapterConfiguration.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static final String PRICING_PROVIDER_PATTERN = configuration.getProperty("capConfigPricingPattern");
}
