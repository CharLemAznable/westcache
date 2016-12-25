package com.github.bingoohuang.westcache.spring;


import lombok.Setter;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class WestCacheableScannerRegistrar
        implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    @Setter private ResourceLoader resourceLoader;

    /**
     * {@inheritDoc}
     */
    public void registerBeanDefinitions(
            AnnotationMetadata importingClassMetadata,
            BeanDefinitionRegistry registry) {
        val name = WestCacheableScan.class.getName();
        val attributes = importingClassMetadata.getAnnotationAttributes(name);
        val annoAttrs = AnnotationAttributes.fromMap(attributes);
        val scanner = new WestCacheableClassPathScanner(registry);

        if (resourceLoader != null) { // this check is needed in Spring 3.1
            scanner.setResourceLoader(resourceLoader);
        }

        Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
        if (!BeanNameGenerator.class.equals(generatorClass)) {
            scanner.setBeanNameGenerator(BeanUtils.instantiateClass(generatorClass));
        }

        List<String> basePackages = new ArrayList<String>();
        for (String pkg : annoAttrs.getStringArray("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : annoAttrs.getStringArray("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : annoAttrs.getClassArray("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }

        if (basePackages.isEmpty()) {
            String className = importingClassMetadata.getClassName();
            basePackages.add(ClassUtils.getPackageName(className));
        }

        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }
}