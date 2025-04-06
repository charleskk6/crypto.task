package com.portfolio.app.core.console;

import com.portfolio.app.util.LogTableUtil;
import com.portfolio.data.Portfolio;
import com.portfolio.dto.AssetWrapper;
import com.portfolio.dto.IAssetInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Console display runnable task
 */
public class ConsoleDisplayRunnable implements Runnable {

  // Portfolios to be printed
  private final List<Portfolio> portfolios;
  // Lock waiting for completion of updating prices change event
  private final Object displayBlockingLock;

  // Keep daemon thread running
  private volatile boolean running;

  // Define fixed column widths
  private final String[] headers = {"symbol", "price", "qty", "value"};
  private final int[] columnWidths = {25, 15, 20, 20};
  private final int totalColumnWidth = totalColumnWidth();

  private static final Logger logger = LoggerFactory.getLogger(ConsoleDisplayRunnable.class);

  /**
   * Instantiates a new Console display runnable.
   *
   * @param portfolios          the portfolios
   * @param displayBlockingLock the display blocking lock
   */
  public ConsoleDisplayRunnable(List<Portfolio> portfolios, Object displayBlockingLock){
    this.portfolios = portfolios;
    this.displayBlockingLock = displayBlockingLock;
  }

  @Override
  public void run() {
    running = true;
    logger.debug("Portfolio Console starts");

    while (running) {
      try {
        synchronized (displayBlockingLock) {
          // Wait until stock prices update complete
          displayBlockingLock.wait();
          printPortfolios();
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
    logger.debug("Portfolio Console has Stopped");
  }

  private void printPortfolios(){
    for (Portfolio portfolio : portfolios) {
      logger.info("\n## {}", portfolio.getName());
      LogTableUtil.logTable(headers, generateTableContent(portfolio), columnWidths);

      double total = portfolio.getTotalValue().setScale(2, RoundingMode.HALF_UP).doubleValue();
      logger.info("#Total portfolio {}", String.format("%," + totalColumnWidth + ".2f\n", total));
    }
  }

  private String[][] generateTableContent(Portfolio portfolio){
    List<AssetWrapper<? extends IAssetInterface>> assets = portfolio.getAssets();
    String[][] items = new String[assets.size()][headers.length];

    for(int i = 0; i<items.length;++i){
      AssetWrapper<? extends IAssetInterface> asset = assets.get(i);
      if(Objects.nonNull(asset)){
        items[i][0] = asset.getSymbol();
        items[i][1] = Optional.ofNullable(asset.getPrice())
                .map(price -> price.setScale(2, RoundingMode.HALF_UP).toString()).orElse("");
        items[i][2] = String.valueOf(asset.getSize());
        items[i][3] = Optional.ofNullable(asset.getValue())
                .map(value -> value.setScale(2, RoundingMode.HALF_UP).toString()).orElse("");
      }
    }

    return items;
  }

  private int totalColumnWidth(){
    int total = 0;
    for(int width: columnWidths){
      total += width;
    }
    return total - 8; // offset
  }

  /**
   * Stop.
   */
  public void stop(){
    running = false;
  }
}
