package pl.lenistwo.puzzlebot.rest;

import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@RequiredArgsConstructor
public class AuthHeaderInterceptor implements Interceptor {

    private static final String API_AUTH_TOKEN_HEADER = "X-API-KEY";
    private final String API_TOKEN;

    @Override
    public @NotNull Response intercept(Chain chain) throws IOException {
        var authRequest = chain.request().newBuilder().header(API_AUTH_TOKEN_HEADER, API_TOKEN).build();
        return chain.proceed(authRequest);
    }
}

