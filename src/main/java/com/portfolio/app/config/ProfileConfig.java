package com.portfolio.app.config;

import com.portfolio.app.core.console.PortfolioConsole;
import com.portfolio.app.core.processor.Impl.BrokerProcessor;
import com.portfolio.data.Portfolio;
import com.portfolio.data.StockPriceCache;
import com.portfolio.data.UserProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProfileConfig {

  // Boostrap a Demo User
  @Bean
  public UserProfile profile(
          StockPriceCache stockPriceCache,
          BrokerProcessor brokerProcessor,
          PortfolioConsole portfolioConsole){
    Portfolio portfolio = new Portfolio("Portfolio", "/position.csv", stockPriceCache);
    UserProfile userProfile = new UserProfile(portfolio);
    brokerProcessor.registerUserProfile(userProfile);
    portfolioConsole.registerPortfolio(userProfile.getPortfolio());
    return userProfile;
  }
}
