package com.github.bingoohuang.westcache.cglib;

import com.github.bingoohuang.westcache.utils.WestCacheAnns;
import com.github.bingoohuang.westcache.utils.WestCacheOption;
import com.google.common.base.Optional;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.Closeable;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2016/12/25.
 */
@Slf4j
public abstract class CacheMethodInterceptor<T> {
    protected abstract Object invokeRaw(Object obj,
                                        Object[] args,
                                        T methodProxy);

    protected abstract String getCacheKey(WestCacheOption option,
                                          Object obj,
                                          Method method,
                                          Object[] args,
                                          T proxy);

    public Object intercept(Object obj,
                            Method method,
                            Object[] args,
                            T methodProxy) {
        val ann = WestCacheAnns.parseWestCacheable(method);
        if (ann == null) return invokeRaw(obj, args, methodProxy);

        val option = WestCacheOption.newBuilder().build(ann);
        return cacheGet(option, obj, method, args, methodProxy);
    }


    @SneakyThrows
    private Object cacheGet(final WestCacheOption option,
                            final Object obj,
                            final Method method,
                            final Object[] args,
                            final T proxy) {
        val cacheKey = getCacheKey(option, obj, method, args, proxy);

        val start = System.currentTimeMillis();
        @Cleanup val i = new Closeable() {
            @Override public void close() {
                val end = System.currentTimeMillis();
                log.debug("get cache {} cost {} millis", cacheKey, (end - start));
            }
        };

        val optional = option.getManager().get(option, cacheKey,
                new Callable<Optional<Object>>() {
                    @Override public Optional<Object> call() {
                        Object raw = invokeRaw(obj, args, proxy);
                        return Optional.fromNullable(raw);
                    }
                });
        return optional.orNull();
    }
}