
package org.mule.modules.robo.pooling;

import javax.annotation.Generated;
import org.apache.commons.pool.KeyedObjectPool;

@Generated(value = "Mule DevKit Version 3.5.1", date = "2014-11-06T04:06:21-03:00", comments = "Build UNNAMED.1967.45d0eb0")
public class CachedObjectPoolAdapter<K,V >implements KeyedObjectPool<K,V>
{

    com.google.common.cache.LoadingCache<K,V> cache;
    org.apache.commons.pool.KeyedPoolableObjectFactory<K,V> factory;

    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CachedObjectPoolAdapter.class);

    com.google.common.cache.RemovalListener<K, V> removalListener = new com.google.common.cache.RemovalListener<K,V>() {
        public void onRemoval(com.google.common.cache.RemovalNotification<K, V> removal) {
            try {
                factory.destroyObject(removal.getKey(),removal.getValue());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    };

    public CachedObjectPoolAdapter(final org.apache.commons.pool.KeyedPoolableObjectFactory<K,V> factory, CacheConfig loadedConfig) {
        this.factory = factory;
        CacheConfig config = loadedConfig != null ? loadedConfig : new CacheConfig();
        this.cache = config.configure(com.google.common.cache.CacheBuilder.newBuilder())
                .removalListener(removalListener)
                .recordStats()
                .build(new com.google.common.cache.CacheLoader<K, V>() {
                    @Override
                    public V load(K K) throws Exception {
                        return factory.makeObject(K);
                    }
                });
    }

    @Override
    public V borrowObject(K key) throws Exception {
        return cache.get(key);
    }

    @Override
    public void returnObject(K key, V obj) throws Exception {
        //This is a cache so there's nothing to be done here
    }

    @Override
    public void invalidateObject(K key, V obj) throws Exception {
        cache.invalidate(key);
    }

    @Override
    public void addObject(K key) throws Exception {
        cache.get(key);
    }

    @Override
    public int getNumIdle(K key) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNumActive(K key) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNumIdle() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNumActive() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() throws Exception {
        cache.invalidateAll();
    }

    @Override
    public void clear(Object key) throws Exception {
        cache.invalidate(key);
    }

    @Override
    public void close() throws Exception {
        cache = null;
        factory = null;
    }

    @Override
    public void setFactory(org.apache.commons.pool.KeyedPoolableObjectFactory<K,V> factory) throws IllegalStateException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
