package com.colak.springtutorial.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AsyncService {

    @Async
    public CompletableFuture<String> asyncMethod() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        log.info("asyncMethod Authentication : {}", authentication);

        return CompletableFuture.completedFuture("asyncMethod completed");
    }

    public void virtualThread() {
        Runnable runnable = () -> {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();
            log.info("virtualThread Authentication : {}", authentication);
        };
        DelegatingSecurityContextRunnable delegatingSecurityContextRunnable = new DelegatingSecurityContextRunnable(runnable);
        Thread.ofVirtual().start(delegatingSecurityContextRunnable);
    }
}
