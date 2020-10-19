package example;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.impl.config.persistence.DefaultPersistenceConfiguration;
import org.ehcache.jsr107.Eh107Configuration;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.ehcache.jsr107.config.ConfigurationElementState;
import org.ehcache.jsr107.config.Jsr107Configuration;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.jcache.internal.JCacheRegionFactory;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.io.File;
import java.util.Collections;
import java.util.Map;

public class CacheConfiguration extends JCacheRegionFactory {
    private static org.ehcache.config.CacheConfiguration<Object, Object> DEFAULT_CACHE_CONFIGURATION =
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                    Object.class, Object.class,
                    ResourcePoolsBuilder.newResourcePoolsBuilder()
                            .disk(100, MemoryUnit.MB, true)
                            .build())
                    .build();

    @Override
    protected CacheManager resolveCacheManager(SessionFactoryOptions settings, Map properties) {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        if (!(cachingProvider instanceof EhcacheCachingProvider))
            throw new IllegalStateException("Missing ehcache");
        EhcacheCachingProvider ehcacheProvider = (EhcacheCachingProvider) cachingProvider;
        return ehcacheProvider.getCacheManager(ehcacheProvider.getDefaultURI(), getConfiguration());
    }

    private static DefaultConfiguration getConfiguration() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        assert cachingProvider instanceof EhcacheCachingProvider;
        EhcacheCachingProvider ehcacheProvider = (EhcacheCachingProvider) cachingProvider;
        DefaultConfiguration configuration = new DefaultConfiguration(
                ehcacheProvider.getDefaultClassLoader(),
                new DefaultPersistenceConfiguration(new File("./cache")),
                new Jsr107Configuration("default",
                        Collections.emptyMap(),
                        true,
                        ConfigurationElementState.DISABLED, //management
                        ConfigurationElementState.DISABLED //statistics
                ));
        return configuration;
    }

    protected Cache<Object, Object> createCache(String regionName) {
        return getCacheManager().createCache(regionName, Eh107Configuration.fromEhcacheCacheConfiguration(DEFAULT_CACHE_CONFIGURATION));
    }
}
