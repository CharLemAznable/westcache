package com.github.bingoohuang.westcache.cglib;

import com.github.bingoohuang.westcache.utils.WestCacheOptions;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2016/12/21.
 */
@Slf4j @NoArgsConstructor @AllArgsConstructor
public class CglibCacheMethodInterceptor
        extends CacheMethodInterceptor<MethodProxy>
        implements MethodInterceptor {
    private Object target;

    @Override
    public Object intercept(Object obj,
                            Method method,
                            Object[] args,
                            MethodProxy methodProxy) {
        return super.intercept(obj, method, args, methodProxy);
    }

    @SneakyThrows
    protected Object invokeRaw(Object obj,
                               Object[] args,
                               MethodProxy methodProxy) {
        return target != null
                ? methodProxy.invoke(target, args)
                : methodProxy.invokeSuper(obj, args);
    }

    @Override
    protected String getCacheKey(WestCacheOptions option,
                                 Object obj,
                                 Method method,
                                 Object[] args,
                                 MethodProxy proxy) {
        return option.getKeyStrategy().getCacheKey(option,
                method, target != null ? target : obj, args);
    }
}
