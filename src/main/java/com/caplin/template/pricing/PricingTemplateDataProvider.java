package com.caplin.template.pricing;

import com.caplin.datasource.DataSource;
import com.caplin.datasource.SubjectError;
import com.caplin.datasource.SubjectErrorEvent;
import com.caplin.datasource.messaging.record.GenericMessage;
import com.caplin.datasource.namespace.PrefixNamespace;
import com.caplin.datasource.publisher.ActivePublisher;
import com.caplin.datasource.publisher.DataProvider;
import com.caplin.datasource.publisher.DiscardEvent;
import com.caplin.datasource.publisher.RequestEvent;

import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PricingTemplateDataProvider implements DataProvider {

    private final DataSource dataSource;
    private final ActivePublisher publisher;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final ConcurrentHashMap<String, ScheduledFuture> activeSubscriptionsMap = new ConcurrentHashMap<>();

    public PricingTemplateDataProvider(DataSource dataSource) {

        this.publisher = dataSource.createActivePublisher(new PrefixNamespace("/TEMPLATE/PRICING"), this);
        this.dataSource = dataSource;
    }

    @Override
    public void onRequest(RequestEvent requestEvent) {
        dataSource.getLogger().log(Level.INFO, "Got request for" + requestEvent.toString());

        String[] split = requestEvent.getSubject().split("/");

        if ( split.length == 4 && !split[3].isEmpty()) {
            activeSubscriptionsMap.put(requestEvent.getSubject(), executorService.scheduleAtFixedRate(new PriceGenerator(requestEvent.getSubject(), dataSource.getLogger()), 0, 2, TimeUnit.SECONDS));
        } else {
            SubjectErrorEvent errorEvent = publisher.getMessageFactory().createSubjectErrorEvent(requestEvent.getSubject(), SubjectError.NotFound);
            publisher.publishSubjectErrorEvent(errorEvent);
        }
    }

    @Override
    public void onDiscard(DiscardEvent discardEvent) {
        dataSource.getLogger().log(Level.INFO, "Got discard for" + discardEvent.toString());

        ScheduledFuture scheduledFuture = activeSubscriptionsMap.remove(discardEvent.getSubject());
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }


    private class PriceGenerator implements Runnable {
        private final String subject;
        private final Logger logger;
        private final ThreadLocalRandom random;
        private boolean isInitialResponse = true;
        private float price;

        public PriceGenerator(String subject, Logger logger) {
            this.subject = subject;
            this.logger = logger;
            random = ThreadLocalRandom.current();
            price = random.nextFloat() * (float) Math.pow(10, random.nextInt(3));
        }

        @Override
        public void run() {

            price = random.nextBoolean() ? price + random.nextFloat() : price - random.nextFloat();

            GenericMessage genericMessage = publisher.getMessageFactory().createGenericMessage(subject);

            genericMessage.setField("BestBid", String.valueOf(price));
            genericMessage.setField("BestAsk", String.valueOf(price - (price * random.nextInt(3) / 100)));

            if (isInitialResponse) {
                publisher.publishInitialMessage(genericMessage);
                isInitialResponse = false;
                logger.log(Level.INFO, "publishing initial message to {}", subject);

            } else {
                publisher.publishToSubscribedPeers(genericMessage);
                logger.log(Level.INFO, "publishing message to {}", subject);
            }
        }
    }
}
