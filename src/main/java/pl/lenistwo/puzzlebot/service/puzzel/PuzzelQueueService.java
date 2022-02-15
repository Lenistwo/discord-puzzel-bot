package pl.lenistwo.puzzlebot.service.puzzel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.lenistwo.puzzlebot.config.BotProperties;
import pl.lenistwo.puzzlebot.exception.puzzel.PuzzelRetentionException;
import pl.lenistwo.puzzlebot.model.Puzzel;
import pl.lenistwo.puzzlebot.rest.RestService;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.function.Predicate;

@Slf4j
@Service
public class PuzzelQueueService extends PuzzelService {

    private static final Deque<Puzzel> puzzelQueue = new ArrayDeque<>();

    private final RestService restService;

    public PuzzelQueueService(BotProperties botProperties, RestService restService) {
        super(botProperties);
        this.restService = restService;
    }

    public Puzzel newPuzzel() {
        var puzzel = drawPuzzel();
        log.info("New puzzel: {}", puzzel);
        puzzelQueue.add(puzzel);
        setPuzzel(puzzel);
        releaseFromQueue();
        return puzzel;
    }

    private void releaseFromQueue() {
        if (puzzelQueue.size() < super.botProperties.puzzelRetention()) {
            return;
        }
        log.info("Releasing from queue - current size: {}", puzzelQueue.size());
        puzzelQueue.pop();
    }

    private Puzzel drawPuzzel() {
        log.info("Drawing puzzel");
        var totalPuzzels = restService.getPuzzels().puzzels();
        var puzzelsAllowedByRetention = totalPuzzels.stream().filter(canBeDrawn()).toList();
        if (puzzelsAllowedByRetention.size() < 1) {
            log.error("Total puzzels size {} - puzzels allowed to draw is 0", totalPuzzels.size());
            throw new PuzzelRetentionException();
        }
        var random = new Random();
        return puzzelsAllowedByRetention.get(random.nextInt(puzzelsAllowedByRetention.size()));
    }

    private Predicate<Puzzel> canBeDrawn() {
        return p -> !puzzelQueue.contains(p);
    }
}
