# Crypto.com 
## Portfolio Assignment [by Charles SO]

### Description
A Spring boot application that simulate a Positioning system to show the real-time value of trader's portfolio which consist of three types of products:
1. Common stocks.
2. European Call options on common stocks.
3. European Put options on common stocks.

The application runs three daemons/background threads to simulate a MarketData, a Broker, and a Console, separated.
With:
1. MarketData simulates constant stock price change in the market
2. Broker simulates Portfolio management when the change of stock price happens
3. Console keeps logging Portfolio status and position

## Getting Started

### Prerequisites
- Java 8
- Spring Boot 2.7.6
- Gradle (version 6.9.1-all)

## Build and Run
```declarative
./gradlew run -Dorg.gradle.jvmargs="-Xms512m -Xmx1024m"
```

### Configuration
#### Application Configuration is defined in path: ./crypto.task/src/main/resources/application.yml

It covers how frequent a batch of Market Data update happens, which is a random time between 0.5 sec to 2sec
e.g.
```yaml
# Market Data Update duration
stock:
  price:
    simulate:
      duration:
        min: 500
        max: 2000
```

And also an initial mock of Market Data
```yaml
# Mock Stock Dict
stocks:
  data:
    AAPL:
      initialPrice: 110.00
      mu: 0.07
      sigma: 0.25
    TELSA:
      initialPrice: 450.00
      mu: 0.10
      sigma: 0.35
```
#### A mock Position is defined in path: ./crypto.task/src/main/resources/position.csv
It defines the symbol and position size of the asset.

## Author
Charles SO - [kinkwai6@gmail.com]