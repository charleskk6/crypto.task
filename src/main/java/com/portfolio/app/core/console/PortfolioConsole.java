package com.portfolio.app.core.console;

import com.portfolio.app.util.LogTableUtil;
import com.portfolio.data.Portfolio;
import com.portfolio.dto.AssetWrapper;
import com.portfolio.dto.IAssetInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.RoundingMode;
import java.util.*;

@Component
public class PortfolioConsole{

  private final Object displayBlockingLock;

  private volatile boolean running;

  private final String[] headers = {"symbol", "price", "qty", "value"};
  // Define fixed column widths
  private final int[] columnWidths = {25, 15, 20, 20};
  private final int totalColumnWidth = totalColumnWidth();

  private static final Logger logger = LoggerFactory.getLogger(PortfolioConsole.class);

  private final List<Portfolio> portfolios = new ArrayList<>();

  @Autowired
  public PortfolioConsole(Object displayBlockingLock){
    this.displayBlockingLock = displayBlockingLock;
  }

  @PostConstruct
  public void run(){
    Thread simulatorThread = new Thread(this::display);
    simulatorThread.setDaemon(true);
    simulatorThread.start();
  }

  public void registerPortfolio(Portfolio portfolio){
    logger.debug("Register New Portfolio: {}", portfolio.getName());
    portfolios.add(portfolio);
  }

  @PreDestroy
  public void stop(){
    running = false;
  }

  public void display(){
    running = true;
    logger.debug("Portfolio Console starts");

    while (running) {
      try {
        synchronized (displayBlockingLock) {
          // Wait until stock price change update complete
          displayBlockingLock.wait();
          for (Portfolio portfolio : portfolios) {
            logger.info("\n## {}", portfolio.getName());
            LogTableUtil.logTable(headers, generateTableContent(portfolio), columnWidths);

            double total = portfolio.getTotalValue().setScale(2, RoundingMode.HALF_UP).doubleValue();
            logger.info("#Total portfolio " + String.format("%," + totalColumnWidth + ".2f\n", total));
          }
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
    logger.debug("Portfolio Console has Stopped");
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
}
