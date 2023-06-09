package org.launchpad.launchpad_backend.common;

import java.time.LocalDateTime;

public class DateTimeUtils {

    public static String getCurrentDateAsString() {
        return LocalDateTime.now().toString();
    }

}
