package pl.lenistwo.puzzlebot.rest;

import com.squareup.moshi.Moshi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.cache.annotation.Cacheable;
import pl.lenistwo.puzzlebot.exception.api.ApiCallException;
import pl.lenistwo.puzzlebot.exception.api.UnsupportedMethodException;
import pl.lenistwo.puzzlebot.model.request.RequestMethod;
import pl.lenistwo.puzzlebot.model.request.UpdatePlayerPointsRequest;
import pl.lenistwo.puzzlebot.model.request.WinnerRequest;
import pl.lenistwo.puzzlebot.model.response.PlayerPointsResponse;
import pl.lenistwo.puzzlebot.model.response.PuzzelResponse;

import static java.util.Objects.requireNonNull;

@Slf4j
@RequiredArgsConstructor
public class RestService {

    private static final String APPLICATION_JSON_MIME_TYPE = "application/json";

    private final OkHttpClient client;
    private final Moshi moshi;
    private final String baseURL;

    public void updatePlayerPoints(String playerName, int amount) {
        var updatePlayerPointsRequest = new UpdatePlayerPointsRequest(playerName, amount);
        sendRequestWithBody("/player/points", updatePlayerPointsRequest, UpdatePlayerPointsRequest.class, RequestMethod.PUT);
    }

    public PlayerPointsResponse getPlayerPoints(String playerName) {
        return sendGetRequest(String.format("/player/%s/points", playerName), PlayerPointsResponse.class);
    }

    @Cacheable(cacheNames = "riddles")
    public PuzzelResponse getPuzzels() {
        return sendGetRequest("/puzzels", PuzzelResponse.class);
    }

    public void saveWinner(String playerName, int puzzelId) {
        var winnerRequest = new WinnerRequest(playerName, puzzelId);
        sendRequestWithBody("/winner", winnerRequest, WinnerRequest.class, RequestMethod.POST);
    }

    private <RES> RES sendGetRequest(String endpoint, Class<RES> type) {
        var request = new Request.Builder().url(baseURL + endpoint).get().build();

        try (var response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new ApiCallException(RequestMethod.GET.getMethod(), endpoint);
            }

            return moshi.adapter(type).fromJson(requireNonNull(response.body()).string());
        } catch (Exception e) {
            log.error("Exception reason: {}", e.getMessage());
            throw new ApiCallException(RequestMethod.GET.getMethod(), endpoint);
        }
    }

    private <REQ> void sendRequestWithBody(String endpoint, REQ body, Class<REQ> type, RequestMethod requestMethod) {
        var jsonBody = moshi.adapter(type).toJson(body);
        var requestBuilder = new Request.Builder().url(baseURL + endpoint);
        var request = switch (requestMethod) {
            case GET -> throw new UnsupportedMethodException(endpoint);
            case POST -> requestBuilder.post(RequestBody.create(jsonBody, MediaType.parse(APPLICATION_JSON_MIME_TYPE))).build();
            case PUT -> requestBuilder.put(RequestBody.create(jsonBody, MediaType.parse(APPLICATION_JSON_MIME_TYPE))).build();
        };

        try (var response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new ApiCallException(requestMethod.getMethod(), endpoint);
            }
        } catch (Exception e) {
            log.error("Exception reason: {}", e.getMessage());
            throw new ApiCallException(requestMethod.getMethod(), endpoint);
        }
    }
}
