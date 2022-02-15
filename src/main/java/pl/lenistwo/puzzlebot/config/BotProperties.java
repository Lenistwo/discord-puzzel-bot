package pl.lenistwo.puzzlebot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConstructorBinding
@ConfigurationProperties(value = "puzzel.bot")
public record BotProperties(
        String token,
        String riddleChannelId,
        String baseURL,
        String apiAuthToken,
        int cacheLifetime,
        int puzzelRetention,
        int guessCost,
        List<Double> costMultipliers) {
}
