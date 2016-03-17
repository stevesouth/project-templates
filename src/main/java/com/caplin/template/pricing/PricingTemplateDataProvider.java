package com.caplin.template.pricing;

import com.caplin.datasource.DataSource;
import com.caplin.datasource.messaging.record.GenericMessage;
import com.caplin.datasource.namespace.PrefixNamespace;
import com.caplin.datasource.publisher.ActivePublisher;
import com.caplin.datasource.publisher.DataProvider;
import com.caplin.datasource.publisher.DiscardEvent;
import com.caplin.datasource.publisher.RequestEvent;

import java.util.logging.Level;

public class PricingTemplateDataProvider implements DataProvider {

    private final DataSource dataSource;
    private final ActivePublisher publisher;

    public PricingTemplateDataProvider(DataSource dataSource) {
        this.publisher = dataSource.createActivePublisher(new PrefixNamespace("/TEMPLATE/PRICING"), this);
        this.dataSource = dataSource;
    }

    @Override
    public void onRequest(RequestEvent requestEvent) {
        dataSource.getLogger().log(Level.INFO, "Got request for" + requestEvent.toString());

        GenericMessage genericMessage = publisher.getMessageFactory().createGenericMessage(requestEvent.getSubject());

        genericMessage.setField("Field1", "Value1");
        genericMessage.setField("Field2", "Value2");

        publisher.publishInitialMessage(genericMessage);
    }

    @Override
    public void onDiscard(DiscardEvent discardEvent) {
        dataSource.getLogger().log(Level.INFO, "Got discard for" + discardEvent.toString());

    }
}
