package com.portfolio.app.core.console;

import com.portfolio.data.PortfolioCache;
import com.portfolio.dto.AssetWrapper;
import com.portfolio.dto.IAssetInterface;
import com.portfolio.app.util.LogTableUtil;
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

  private final PortfolioCache portfolioCache;
  private volatile boolean running;

  private final String[] headers = {"symbol", "price", "qty", "value"};
  // Define fixed column widths
  private final int[] columnWidths = {25, 15, 20, 20};

  private static final Logger logger = LoggerFactory.getLogger(PortfolioConsole.class);

  @Autowired
  public PortfolioConsole(PortfolioCache portfolioCache){
    this.portfolioCache = portfolioCache;
  }

  @PostConstruct
  public void run(){
    Thread simulatorThread = new Thread(this::display);
    simulatorThread.setDaemon(true);
    simulatorThread.start();
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
        logger.info("\n## Portfolio");

        LogTableUtil.logTable(headers, generateTableContent(), columnWidths);

        Thread.sleep(2000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt(); // Respect interruption
        break;
      }
    }
    logger.debug("Portfolio Console has Stopped");
  }

  private String[][] generateTableContent(){
    Set<Map.Entry<String, AssetWrapper<? extends IAssetInterface>>> entrySet =
            portfolioCache.getPortfolioEntrySets();

    PriorityQueue<AssetWrapper<? extends IAssetInterface>> pq =
            new PriorityQueue<>(Comparator.comparing(AssetWrapper::getSymbol));
    for(Map.Entry<String, AssetWrapper<? extends IAssetInterface>> entry: entrySet){
      pq.add(entry.getValue());
    }

    String[][] items = new String[entrySet.size()][headers.length];

    for(int i = 0; i<items.length;++i){
      AssetWrapper<? extends IAssetInterface> asset = pq.poll();
      if(Objects.nonNull(asset)){
        items[i][0] = asset.getSymbol();
        items[i][1] = asset.getPrice().setScale(2, RoundingMode.HALF_UP).toString();
        items[i][2] = String.valueOf(asset.getSize());
        items[i][3] = asset.getValue().setScale(2, RoundingMode.HALF_UP).toString();
      }
    }

    return items;
  }

}
