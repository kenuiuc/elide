/*
 * Copyright 2016, Yahoo Inc.
 * Licensed under the Apache License, Version 2.0
 * See LICENSE file in project root for terms.
 */
package com.yahoo.elide.audit;

import com.yahoo.elide.security.ChangeSpec;
import org.eclipse.jetty.util.ConcurrentHashSet;

import java.io.IOException;

/**
 * Audit logger that stores messages in memory.
 */
public class InMemoryLogger extends AuditLogger {
    public final ConcurrentHashSet<String> logMessages = new ConcurrentHashSet<>();

    @Override
    public void commit() throws IOException {
        for (LogMessage message : messages.get()) {
            if (message.getChangeSpec().isPresent()) {
                logMessages.add(changeSpecToString(message.getChangeSpec().get()));
            }
            logMessages.add(message.getMessage());
        }
    }

    private String changeSpecToString(final ChangeSpec changeSpec) {
        if (changeSpec == null) {
            return "null";
        }
        String old = (changeSpec.getOriginal() == null) ? "null" : changeSpec.getOriginal().toString();
        String modified = (changeSpec.getModified() == null) ? "null" : changeSpec.getModified().toString();
        return "old: " + old + "\nnew: " + modified;
    }
}
