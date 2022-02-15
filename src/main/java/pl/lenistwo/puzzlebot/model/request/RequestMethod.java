package pl.lenistwo.puzzlebot.model.request;

import lombok.Getter;

public enum RequestMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT");

    @Getter
    private final String method;

    RequestMethod(String method) {
        this.method = method;
    }
}
