package pl.lenistwo.puzzlebot.exception.action;

import pl.lenistwo.puzzlebot.model.button.Action;

public class PlayerIsNotAllowedToActionException extends RuntimeException {
    public PlayerIsNotAllowedToActionException(Action action) {
        super("Player Is Not Allowed To Action Exception: " + action.name());
    }
}
