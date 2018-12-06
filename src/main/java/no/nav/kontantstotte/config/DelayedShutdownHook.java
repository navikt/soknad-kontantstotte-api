package no.nav.kontantstotte.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

public class DelayedShutdownHook extends Thread {

    private static final Logger log = LoggerFactory.getLogger(DelayedShutdownHook.class);

    private final ConfigurableApplicationContext applicationContext;

    public DelayedShutdownHook(final ConfigurableApplicationContext applicationContext) {

        this.applicationContext = applicationContext;
    }

    @Override
    public void run() {
        try {
            log.info("shutdown initialized, allowing incoming requests for 5 seconds before continuing");
            // https://github.com/kubernetes/kubernetes/issues/64510
            // https://nav-it.slack.com/archives/C5KUST8N6/p1543497847341300
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("shutting down applicationcontext");
        applicationContext.close();
        log.info("shutdown ok");

        super.run();
    }
}
