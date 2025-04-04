package com.portfolio.app;

import com.portfolio.app.config.CacheConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({CacheConfig.class})
@ComponentScan({
  "com.portfolio"
})
public class PortfolioSimulationApplication {

  public static void main(String[] args) {
    SpringApplication.run(PortfolioSimulationApplication.class, args);
  }

}
