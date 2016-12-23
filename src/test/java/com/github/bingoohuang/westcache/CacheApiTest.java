package com.github.bingoohuang.westcache;

import com.github.bingoohuang.westcache.impl.WestCacheOption;
import com.google.common.base.Optional;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.junit.Test;

import java.util.concurrent.Callable;

import static com.github.bingoohuang.westcache.impl.WestCacheOption.newBuilder;
import static com.google.common.truth.Truth.assertThat;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2016/12/22.
 */
public class CacheApiTest {
    String north = "NORTH", south = "SOUTH";
    @Getter @Setter String homeArea;

    public String getHomeAreaWithCache() {
        return homeArea;
    }

    @Test
    public void apiBasic() {
        setHomeArea(north);
        String cacheKey = "api.cache.key";
        val option = newBuilder().build();
        Optional<String> cache = option.getManager().get(option, cacheKey, new Callable<Optional<String>>() {
            @Override public Optional<String> call() throws Exception {
                return Optional.fromNullable(getHomeAreaWithCache());
            }
        });
        assertThat(cache.orNull()).isEqualTo(north);

        setHomeArea(south);
        cache = option.getManager().get(option, cacheKey);
        assertThat(cache.orNull()).isEqualTo(north);
        assertThat(getHomeArea()).isEqualTo(south);
    }

    @Test
    public void apiFull() {

    }
}
