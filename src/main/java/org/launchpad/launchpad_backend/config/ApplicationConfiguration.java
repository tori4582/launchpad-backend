package org.launchpad.launchpad_backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@Configuration
public class ApplicationConfiguration {

    @EventListener
    void onStartup(ApplicationReadyEvent event) {
        log.info("[SERVER_STATE_NOTIFICATION] " + "\u001B[32m" + "✅ Server is up and ready to accept requests 👌" + "\u001B[0m");
    }

    @EventListener
    void onShutdown(ContextStoppedEvent event) {
        // do sth
    }
}
