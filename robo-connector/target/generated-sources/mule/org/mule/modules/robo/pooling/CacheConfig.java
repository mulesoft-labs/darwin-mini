
package org.mule.modules.robo.pooling;

import javax.annotation.Generated;

@Generated(value = "Mule DevKit Version 3.5.1", date = "2014-11-06T04:06:21-03:00", comments = "Build UNNAMED.1967.45d0eb0")
public class CacheConfig {

    private int initialCapacity = -1;
    private long expireAfterAccessMinutes = -1;
    private long expireAfterWriteMinutes = -1;

    public int getInitialCapacity() {
        return initialCapacity;
    }

    public void setInitialCapacity(int initialCapacity) {
        this.initialCapacity = initialCapacity;
    }

    public long getExpireAfterAccessMinutes() {
        return expireAfterAccessMinutes;
    }

    public void setExpireAfterAccessMinutes(long expireAfterAccessMinutes) {
        this.expireAfterAccessMinutes = expireAfterAccessMinutes;
    }

    public long getExpireAfterWriteMinutes() {
        return expireAfterWriteMinutes;
    }

    public void setExpireAfterWriteMinutes(long expireAfterWriteMinutes) {
        this.expireAfterWriteMinutes = expireAfterWriteMinutes;
    }

    protected <K,V> com.google.common.cache.CacheBuilder<K,V> configure(com.google.common.cache.CacheBuilder<K,V> builder) {
        if (expireAfterWriteMinutes > 0) builder.expireAfterWrite(expireAfterWriteMinutes, java.util.concurrent.TimeUnit.MINUTES);
        if (expireAfterAccessMinutes > 0) builder.expireAfterAccess(expireAfterAccessMinutes, java.util.concurrent.TimeUnit.MINUTES);
        if (initialCapacity > 0) builder.initialCapacity(initialCapacity);
        return builder;
    }
}
