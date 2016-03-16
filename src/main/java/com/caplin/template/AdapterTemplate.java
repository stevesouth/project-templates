package com.caplin.template;

import com.caplin.datasource.DataSource;
import com.caplin.datasource.DataSourceFactory;
import com.caplin.datasource.namespace.PrefixNamespace;
import com.caplin.template.pricing.PricingTemplateDataProvider;

public class AdapterTemplate {

    private final DataSource dataSource;

    public AdapterTemplate(DataSource dataSource) {
        this.dataSource = dataSource;

        new PricingTemplateDataProvider(this.dataSource);
    }

    public static void main(String[] args) {
        DataSource dataSource = DataSourceFactory.createDataSource(args);

        new AdapterTemplate(dataSource);

        dataSource.start();
    }
}
