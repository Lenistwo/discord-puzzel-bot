package pl.lenistwo.puzzlebot.service.puzzel;

import lombok.Getter;
import lombok.Setter;
import pl.lenistwo.puzzlebot.config.BotProperties;
import pl.lenistwo.puzzlebot.model.Puzzel;
import pl.lenistwo.puzzlebot.model.button.Action;

import java.util.*;

class PuzzelService {

    private static final Map<String, Action> playersAllowedToGuess = new HashMap<>();

    private static final String SPACE = " ";
    private static final String PLACEHOLDER = "-";
    private static final int FIRST_INDEX = 0;
    private static final double DEFAULT_MULTIPLIER = 1.0;

    protected final BotProperties botProperties;

    @Getter
    private Puzzel puzzel;
    private Set<Character> guessedChars;
    @Getter
    private String guessedAnswer;
    @Getter
    @Setter
    private String messageId;
    @Getter
    private int guessCost;
    private double currentMultiplier;


    public PuzzelService(BotProperties botProperties) {
        this.guessedChars = new HashSet<>();
        this.botProperties = botProperties;
        this.guessCost = botProperties.guessCost();
        this.currentMultiplier = !botProperties.costMultipliers().isEmpty() ? botProperties.costMultipliers().get(FIRST_INDEX) : DEFAULT_MULTIPLIER;
    }

    public void setPuzzel(Puzzel puzzel) {
        this.puzzel = puzzel;
        this.guessedChars = new HashSet<>();
        this.guessedAnswer = buildAnswer();
        this.guessCost = botProperties.guessCost();
        this.currentMultiplier = !botProperties.costMultipliers().isEmpty() ? botProperties.costMultipliers().get(FIRST_INDEX) : DEFAULT_MULTIPLIER;
    }

    public boolean guessLetter(Character letter) {
        updateGuessCost();
        increaseMultiplier();
        this.guessedChars.add(letter);
        this.guessedAnswer = buildAnswer();

        return puzzel.answer().contains(letter.toString());
    }

    public void addAllowedPlayer(String playerId, Action action) {
        playersAllowedToGuess.put(playerId, action);
    }

    public boolean isPlayerAllowedToGuess(String playerId, Action action) {
        return Optional.ofNullable(playersAllowedToGuess.get(playerId))
                       .map(a -> a.equals(action))
                       .orElse(false);
    }

    public void removePlayer(String playerId) {
        playersAllowedToGuess.remove(playerId);
    }

    public boolean guessAnswer(String guess) {
        updateGuessCost();
        increaseMultiplier();
        return guess.equalsIgnoreCase(puzzel.answer());
    }

    private String buildAnswer() {
        var stringBuilder = new StringBuilder();

        var chars = puzzel.answer().toUpperCase().toCharArray();
        for (char c : chars) {
            if (guessedChars.contains(c)) {
                stringBuilder.append(c).append(SPACE);
                continue;
            }

            stringBuilder.append(PLACEHOLDER).append(SPACE);
        }

        return stringBuilder.toString();
    }

    private void updateGuessCost() {
        this.guessCost *= currentMultiplier;
    }

    private void increaseMultiplier() {
        var multipliers = botProperties.costMultipliers();
        if (multipliers.size() < 1) {
            return;
        }

        var indexOfCurrentMultiplier = multipliers.indexOf(this.currentMultiplier);

        if (indexOfCurrentMultiplier >= multipliers.size()) {
            return;
        }

        this.currentMultiplier = multipliers.get(indexOfCurrentMultiplier + 1);
    }
}
