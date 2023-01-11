package generator;

import java.util.concurrent.atomic.AtomicLong;

public class Generator {
    private static final AtomicLong userId = new AtomicLong();
    private static final AtomicLong roleId = new AtomicLong();


    public static long generateUserId(){
        return userId.incrementAndGet();
    }
    public static long generateRoleId(){
        return roleId.incrementAndGet();
    }
}
