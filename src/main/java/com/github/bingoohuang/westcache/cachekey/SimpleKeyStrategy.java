package com.github.bingoohuang.westcache.cachekey;

import com.github.bingoohuang.westcache.base.WestCacheKeyStrategy;
import com.github.bingoohuang.westcache.cglib.Cglibs;
import com.github.bingoohuang.westcache.utils.WestCacheOption;
import lombok.val;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2016/12/28.
 */
public class SimpleKeyStrategy extends WestCacheKeyStrategy {
    @Override
    public String getCacheKey(WestCacheOption option,
                              String methodName,
                              Object bean,
                              Object... args) {
        if (option.getKey().length() > 0) return option.getKey();

        val superClassName = Cglibs.getSuperClassName(bean);
        val dottedInnerClassName = removePackage(superClassName).replace('$', '.');
        val mainPart = dottedInnerClassName + "." + methodName;

        if (args.length == 0) return mainPart;

        return mainPart + DefaultKeyStrategy.joinArgs(args);
    }

    private String removePackage(String className) {
        int lastDotPos = className.lastIndexOf('.');
        if (lastDotPos == -1) return className;
        return className.substring(lastDotPos + 1);
    }
}
