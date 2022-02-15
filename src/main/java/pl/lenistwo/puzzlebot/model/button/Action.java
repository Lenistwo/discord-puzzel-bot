package pl.lenistwo.puzzlebot.model.button;

import lombok.Getter;
import pl.lenistwo.puzzlebot.exception.action.ActionNotFoundException;

import java.util.Arrays;

public enum Action {
    GUESS_LETTER("guessLetter"),
    GUESS_ANSWER("guessAnswer"),
    CONFIRM_LETTER_GUESS("confirmLetterGuess"),
    CONFIRM_ANSWER_GUESS("confirmAnswerGuess"),
    RESIGN("resign");

    @Getter
    private final String id;

    Action(String id) {
        this.id = id;
    }

    public static Action getById(String identifier) {
        return Arrays.stream(values()).filter(action -> action.getId().equalsIgnoreCase(identifier)).findFirst().orElseThrow(() -> new ActionNotFoundException(identifier));
    }

}
