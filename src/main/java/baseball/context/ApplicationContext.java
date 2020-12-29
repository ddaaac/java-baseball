package baseball.context;

import baseball.controller.GameController;
import baseball.controller.GameControllerImpl;
import baseball.retry.RetryDynamicInvocationHandler;
import baseball.domain.NumberGenerateStrategy;
import baseball.domain.RandomBallNumbersGenerator;
import baseball.ui.InputView;
import baseball.ui.OutputView;

import java.lang.reflect.Proxy;
import java.util.Scanner;

public class ApplicationContext {
    private static final ApplicationContext applicationContext;

    static {
        NumberGenerateStrategy numberGenerateStrategy = new RandomBallNumbersGenerator();
        InputView inputView = new InputView(new Scanner(System.in));
        OutputView outputView = new OutputView();
        GameController gameController = (GameController) Proxy.newProxyInstance(
                ApplicationContext.class.getClassLoader(),
                new Class[]{GameController.class},
                new RetryDynamicInvocationHandler(new GameControllerImpl(inputView, outputView, numberGenerateStrategy), outputView)
        );

        applicationContext = new ApplicationContext(gameController);
    }

    public static GameController getGameController() {
        return applicationContext.gameController;
    }

    private final GameController gameController;

    private ApplicationContext(GameController gameController) {
        this.gameController = gameController;
    }
}
