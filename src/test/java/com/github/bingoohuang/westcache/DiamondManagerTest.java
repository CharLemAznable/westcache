package com.github.bingoohuang.westcache;

import com.github.bingoohuang.westcache.utils.DiamondFiles;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import java.io.File;

import static com.github.bingoohuang.westcache.utils.WestCacheOption.newBuilder;
import static com.google.common.truth.Truth.assertThat;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2016/12/25.
 */
public class DiamondManagerTest {
    public interface DiamondService {
        @WestCacheable(manager = "diamond", specs = "static.key=yes")
        String getBigData();
    }

    @Test @SneakyThrows
    public void test() {
        val service = WestCacheFactory.create(DiamondService.class);
        val groupDir = DiamondFiles.getCacheGroupDir();
        val keyStrategy = WestCacheRegistry.getKeyStrategy("default");
        val option = newBuilder().manager("diamond").specs("static.key=yes").build();
        val cacheKey = keyStrategy.getCacheKey(option, "getBigData", service);
        val diamondFile = new File(groupDir, cacheKey + ".diamond");
        String content = "Here is Bingoo!" + System.currentTimeMillis();
        DiamondFiles.writeDiamond(diamondFile, content);

        assertThat(service.getBigData()).isEqualTo(content);
    }
}
