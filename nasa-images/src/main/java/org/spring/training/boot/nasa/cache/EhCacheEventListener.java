package org.spring.training.boot.nasa.cache;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListenerAdapter;

@Slf4j
public class EhCacheEventListener extends CacheEventListenerAdapter {

    @Override
    public void notifyElementRemoved(Ehcache cache, Element element) throws CacheException {
        Object cacheKey = element.getObjectKey();
        Object cacheValue = element.getObjectValue();
        String cacheName = cache.getName();
        log.info("Element '{} - {}' was removed from cache {}", cacheKey, cacheValue, cacheName);
    }

    @Override
    public void notifyElementPut(Ehcache cache, Element element) throws CacheException {
        Object cacheKey = element.getObjectKey();
        Object cacheValue = element.getObjectValue();
        String cacheName = cache.getName();
        log.info("Element '{} - {}' was put into cache {}", cacheKey, cacheValue, cacheName);
    }

    @Override
    public void notifyElementExpired(Ehcache cache, Element element) {
        Object cacheKey = element.getObjectKey();
        Object cacheValue = element.getObjectValue();
        String cacheName = cache.getName();
        log.info("Element '{} - {}' was expired in cache {}", cacheKey, cacheValue, cacheName);
    }

    @Override
    public void notifyElementEvicted(Ehcache cache, Element element) {
        Object cacheKey = element.getObjectKey();
        Object cacheValue = element.getObjectValue();
        String cacheName = cache.getName();
        log.info("Element '{} - {}' was evicted. Cache - {}", cacheKey, cacheValue, cacheName);
    }

    @Override
    public void notifyRemoveAll(Ehcache cache) {
        String cacheName = cache.getName();
        log.info("Cache {} - all elements removed", cacheName);
    }
}
