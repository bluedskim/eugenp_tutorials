package com.baeldung.guava.eventbus;

import com.baeldung.guava.eventbus.CustomEvent;
import com.baeldung.guava.eventbus.EventListener;
import com.google.common.eventbus.EventBus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class GuavaEventBusUnitTest {
    private static final Logger LOG = LoggerFactory.getLogger(GuavaEventBusUnitTest.class);

    private EventListener listener;
    private EventBus eventBus;

    @Before
    public void setUp() {
        eventBus = new EventBus();
        listener = new EventListener();

        eventBus.register(listener);
        eventBus.register(new EventListener());
    }

    @After
    public void tearDown() {
        eventBus.unregister(listener);
    }

    @Test
    public void givenStringEvent_whenEventHandled_thenSuccess() {
        listener.resetEventsHandled();
        LOG.info("posting String Event1");
        eventBus.post("String Event1");
        //assertEquals(1, listener.getEventsHandled());
        LOG.info("posting String Event2");
        eventBus.post("String Event2");
        assertEquals(4, listener.getEventsHandled());
    }

    @Test
    public void givenCustomEvent_whenEventHandled_thenSuccess() {
        listener.resetEventsHandled();

        CustomEvent customEvent = new CustomEvent("Custom Event");
        eventBus.post(customEvent);

        assertEquals(1, listener.getEventsHandled());
    }

    @Test
    public void givenUnSubscribedEvent_whenEventHandledByDeadEvent_thenSuccess() {
        listener.resetEventsHandled();

        eventBus.post(12345);
        assertEquals(1, listener.getEventsHandled());
    }

}
