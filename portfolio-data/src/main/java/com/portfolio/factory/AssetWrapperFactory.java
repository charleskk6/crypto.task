package com.portfolio.factory;

import com.portfolio.dto.*;
import com.portfolio.util.MonthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * The type Asset wrapper factory.
 */
public class AssetWrapperFactory {

  private static final String STOCK_OPTION_CALL_SUFFIX = "-C";
  private static final String STOCK_OPTION_PUT_SUFFIX = "-P";

  private static final Logger logger = LoggerFactory.getLogger(AssetWrapperFactory.class);

  /**
   * Create asset wrapper for StockItem and StockOptionItem
   *
   * @param symbol                 the symbol
   * @param positionSize           the position size
   * @param priceOrUnderlyingPrice the price or underlying price
   * @return the asset wrapper
   */
  public static AssetWrapper<? extends IAssetInterface> create(
          final String symbol, final int positionSize, final BigDecimal priceOrUnderlyingPrice){
    logger.debug("Create asset with symbol: {} and positionSize: {}", symbol, positionSize);

    if(!symbol.contains("-")){ // Asset is Call or Put Stock Option
      return createStock(symbol, positionSize, priceOrUnderlyingPrice);
    } else {
      return createStockOption(symbol, positionSize, priceOrUnderlyingPrice);
    }
  }

  private static AssetWrapper<StockItem> createStock(final String symbol, final int positionSize, final BigDecimal price){
    AssetWrapper<StockItem> asset = new AssetWrapper<>(StockItem.builder()
            .symbol(symbol)
            .size(positionSize)
            .build());
    asset.setPriceOrUnderlyingPrice(price);
    return asset;
  }

  private static AssetWrapper<StockOptionItem> createStockOption(final String symbol, final int positionSize, final BigDecimal underlyingPrice){
    String[] arguments = symbol.split("-");

    StockOptionType optionType;
    if(symbol.endsWith(STOCK_OPTION_CALL_SUFFIX)){
      optionType = StockOptionType.CALL;
    } else {
      optionType = StockOptionType.PUT;
    }

    final String underlyingSymbol = arguments[0];
    final BigDecimal strikePrice = new BigDecimal(arguments[3]);

    // Defines ExpirationDate
    final int year = Integer.parseInt(arguments[2]);
    YearMonth yearMonth = YearMonth.of(year, MonthUtil.MONTH_MAP.get(arguments[1]));
    LocalDate expirationDate = yearMonth.atEndOfMonth();

    AssetWrapper<StockOptionItem> asset = new AssetWrapper<>(StockOptionItem.builder()
            .symbol(symbol)
            .underlyingSymbol(underlyingSymbol)
            .type(optionType)
            .strikePrice(strikePrice)
            .expirationDate(expirationDate)
            .size(positionSize)
            .build());
    asset.setPriceOrUnderlyingPrice(underlyingPrice);
    return asset;
  }


}
