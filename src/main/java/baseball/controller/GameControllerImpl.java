package baseball.controller;

import baseball.domain.BaseBallGame;
import baseball.domain.BaseBallNumbers;
import baseball.domain.GameResult;
import baseball.domain.NumberGenerateStrategy;
import baseball.retry.RetryIfException;
import baseball.ui.GameResultResponse;
import baseball.ui.InputView;
import baseball.ui.OutputView;

public class GameControllerImpl implements GameController {
    private final InputView inputView;
    private final OutputView outputView;
    private final NumberGenerateStrategy numberGenerateStrategy;

    public GameControllerImpl(InputView inputView, OutputView outputView, NumberGenerateStrategy numberGenerateStrategy) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.numberGenerateStrategy = numberGenerateStrategy;
    }

    public BaseBallGame startGame() {
        return BaseBallGame.initialize(numberGenerateStrategy);
    }

    @RetryIfException
    public void guess(BaseBallGame game) {
        GameResult result = game.guess(BaseBallNumbers.from(inputView.enterBallNumbers()));
        outputView.printResult(GameResultResponse.from(result));
    }

    public boolean isProceeding(BaseBallGame game) {
        return !game.isEnd();
    }

    @RetryIfException
    public boolean willRestartGame() {
        return inputView.enterIfRestart()
                .isRestart();
    }
}
