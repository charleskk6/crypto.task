package com.portfolio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StockOptionItem implements IAssetInterface {
    private String symbol;
    private String underlyingSymbol;
    private StockOptionType type;
    private BigDecimal strikePrice;
    private LocalDate expirationDate;
    private int size;

    public StockOptionItem(){}

    public StockOptionItem(String symbol, String underlyingSymbol, StockOptionType type, BigDecimal strikePrice, LocalDate expirationDate, int size) {
        this.symbol = symbol;
        this.underlyingSymbol = underlyingSymbol;
        this.type = type;
        this.strikePrice = strikePrice;
        this.expirationDate = expirationDate;
        this.size = size;
    }

    private StockOptionItem(Builder builder) {
        this.symbol = builder.symbol;
        this.underlyingSymbol = builder.underlyingSymbol;
        this.strikePrice = builder.strikePrice;
        this.expirationDate = builder.expirationDate;
        this.type = builder.type;
        this.size = builder.size;
    }

    // Getter methods
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getUnderlyingSymbol() {
        return underlyingSymbol;
    }

    public void setUnderlyingSymbol(String underlyingSymbol) {
        this.underlyingSymbol = underlyingSymbol;
    }

    public StockOptionType getType() {
        return type;
    }

    public void setType(StockOptionType type) {
        this.type = type;
    }

    public BigDecimal getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(BigDecimal strikePrice) {
        this.strikePrice = strikePrice;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Builder class for StockOptionItem
    public static class Builder {
        private String symbol;
        private String underlyingSymbol;
        private StockOptionType type;
        private BigDecimal strikePrice;
        private LocalDate expirationDate;
        private int size;

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder underlyingSymbol(String underlyingSymbol) {
            this.underlyingSymbol = underlyingSymbol;
            return this;
        }

        public Builder type(StockOptionType type) {
            this.type = type;
            return this;
        }

        public Builder strikePrice(BigDecimal strikePrice){
            this.strikePrice = strikePrice;
            return this;
        }

        public Builder expirationDate(LocalDate expirationDate){
            this.expirationDate = expirationDate;
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public StockOptionItem build() {
            return new StockOptionItem(this);
        }
    }
}
