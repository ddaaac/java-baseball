package baseball.controller;

import baseball.domain.BaseBallGame;

public interface GameController {
    BaseBallGame startGame();

    void guess(BaseBallGame game);

    boolean isProceeding(BaseBallGame game);

    boolean willRestartGame();
}
