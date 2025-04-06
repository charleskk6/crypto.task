package com.portfolio.app;

import com.portfolio.app.config.CacheConfig;
import com.portfolio.app.config.StocksConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan({
  "com.portfolio"
})
@Import({StocksConfig.class, CacheConfig.class})
public class PortfolioSimulationApplication {

  public static void main(String[] args) {
    SpringApplication.run(PortfolioSimulationApplication.class, args);
  }

}
