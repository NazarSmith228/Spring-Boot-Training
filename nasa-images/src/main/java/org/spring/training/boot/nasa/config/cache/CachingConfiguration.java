package org.spring.training.boot.nasa.config.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching(proxyTargetClass = true)
public class CachingConfiguration {

    @Bean("ehCacheManager")
    public EhCacheCacheManager ehCacheManager() {
        CacheManager internalCacheManager = new CacheManager();
        internalCacheManager.addCache(nasaLargestImageCache());

        return new EhCacheCacheManager(internalCacheManager);
    }

    private Cache nasaLargestImageCache() {
        CacheConfiguration configuration = new CacheConfiguration()
                .eternal(false) // set to true - never evicted
                .timeToLiveSeconds(120)
                .timeToIdleSeconds(60)
                .maxEntriesLocalHeap(1000)
                .maxEntriesLocalDisk(5000)
                .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU) // Least Frequently Used
                .name("largestImage");

        return new Cache(configuration);
    }
}
