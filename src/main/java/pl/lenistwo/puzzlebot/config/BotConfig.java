package pl.lenistwo.puzzlebot.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.squareup.moshi.Moshi;
import dev.zacsweers.moshix.records.RecordsJsonAdapterFactory;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import okhttp3.OkHttpClient;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lenistwo.puzzlebot.rest.AuthHeaderInterceptor;
import pl.lenistwo.puzzlebot.rest.RestService;

import java.util.concurrent.TimeUnit;

@Configuration
public class BotConfig {

    @Bean
    @SneakyThrows
    public JDA jda(BotProperties properties) {
        return JDABuilder.createDefault(properties.token()).build();
    }

    @Bean
    public RestService restService(BotProperties properties) {
        var moshi = new Moshi.Builder()
                                   .add(new RecordsJsonAdapterFactory())
                                   .build();

        var okHttpClient = new OkHttpClient.Builder()
                                                      .addInterceptor(new AuthHeaderInterceptor(properties.apiAuthToken()))
                                                      .build();

        return new RestService(okHttpClient, moshi, properties.baseURL());
    }

    @Bean
    public CacheManager cacheManager(BotProperties properties) {
        var caffeineCacheManager = new CaffeineCacheManager();
        var caffeine = Caffeine.newBuilder().expireAfterWrite(properties.cacheLifetime(), TimeUnit.MINUTES);
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }
}
