package com.github.bingoohuang.westcache.utils;

import com.github.bingoohuang.westcache.cglib.Cglibs;
import lombok.experimental.UtilityClass;
import lombok.val;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2016/12/30.
 */
@UtilityClass
public class Keys {
    public String joinArgs(Object[] args) {
        val argsPart = new StringBuilder();

        for (val arg : args) {
            argsPart.append("_");
            if (arg == null) argsPart.append("null");
            else argsPart.append(arg);
        }

        return argsPart.toString();
    }

    public String removePackage(String className) {
        int lastDotPos = className.lastIndexOf('.');
        if (lastDotPos == -1) return className;
        return className.substring(lastDotPos + 1);
    }

    public boolean isPrefix(String str, String prefix) {
        if (!str.startsWith(prefix)) return false;
        if (str.length() == prefix.length()) return true;

        char nextChar = str.charAt(prefix.length());
        return nextChar == '.' || nextChar == '_';
    }

    public String createKeyMainPart(String methodName,
                                    Object bean,
                                    boolean removePackage) {
        val superClassName = Cglibs.getSuperClassName(bean);
        val simpleClassName = removePackage ? removePackage(superClassName) : superClassName;
        val dottedClassName = simpleClassName.replace('$', '.');
        return dottedClassName + "." + methodName;
    }
}