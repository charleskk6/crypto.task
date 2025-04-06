package com.portfolio.app.core.console;

import com.portfolio.data.Portfolio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

/**
 * PortfolioConsole is a daemon thread running in background.
 */
@Component
public class PortfolioConsole{

  // Subscribers
  private final List<Portfolio> portfolios = new ArrayList<>();

  private final ConsoleDisplayRunnable consoleDisplayRunnable;

  private static final Logger logger = LoggerFactory.getLogger(PortfolioConsole.class);

  /**
   * Instantiates a new Portfolio console.
   *
   * @param displayBlockingLock the display blocking lock
   */
  @Autowired
  public PortfolioConsole(Object displayBlockingLock){
    this.consoleDisplayRunnable = new ConsoleDisplayRunnable(portfolios, displayBlockingLock);
  }

  /**
   * Create and run daemon thread in background
   */
  @PostConstruct
  public void run(){
    Thread simulatorThread = new Thread(consoleDisplayRunnable);
    simulatorThread.setDaemon(true);
    simulatorThread.start();
  }

  /**
   * Register portfolio as new subscriber
   *
   * @param portfolio the portfolio
   */
  public void registerPortfolio(Portfolio portfolio){
    logger.debug("Register New Portfolio: {}", portfolio.getName());
    portfolios.add(portfolio);
  }

  /**
   * Stop.
   */
  @PreDestroy
  public void stop(){
    this.consoleDisplayRunnable.stop();
  }

}
