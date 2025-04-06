package com.portfolio.factory;

import com.portfolio.dto.*;
import com.portfolio.util.MonthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public class AssetWrapperFactory {

  private static final String STOCK_OPTION_CALL_SUFFIX = "-C";
  private static final String STOCK_OPTION_PUT_SUFFIX = "-P";

  private static final Logger logger = LoggerFactory.getLogger(AssetWrapperFactory.class);

  public static AssetWrapper<? extends IAssetInterface> create(
          final String symbol, final int positionSize, final BigDecimal priceOrUnderlyingPrice){
    logger.debug("Create asset with symbol: {} and positionSize: {}", symbol, positionSize);

    String[] notations = symbol.split("-");

    if(symbol.endsWith(STOCK_OPTION_CALL_SUFFIX) || symbol.endsWith(STOCK_OPTION_PUT_SUFFIX)){
      StockOptionType optionType;
      if(symbol.endsWith(STOCK_OPTION_CALL_SUFFIX)){
        optionType = StockOptionType.CALL;
      } else {
        optionType = StockOptionType.PUT;
      }

      final String underlyingSymbol = notations[0];
      final BigDecimal strikePrice = new BigDecimal(notations[3]);
      final int year = Integer.parseInt(notations[2]);
      YearMonth yearMonth = YearMonth.of(year, MonthUtil.MONTH_MAP.get(notations[1]));
      LocalDate expirationDate = yearMonth.atEndOfMonth();

      AssetWrapper<StockOptionItem> asset = new AssetWrapper<>(StockOptionItem.builder()
              .symbol(symbol)
              .underlyingSymbol(underlyingSymbol)
              .type(optionType)
              .strikePrice(strikePrice)
              .expirationDate(expirationDate)
              .size(positionSize)
              .build());
      asset.setPriceOrUnderlyingPrice(priceOrUnderlyingPrice);
      return asset;
    } else {
      AssetWrapper<StockItem> asset = new AssetWrapper<>(StockItem.builder()
              .symbol(symbol)
              .size(positionSize)
              .build());
      asset.setPriceOrUnderlyingPrice(priceOrUnderlyingPrice);
       return asset;
    }
  }
}
