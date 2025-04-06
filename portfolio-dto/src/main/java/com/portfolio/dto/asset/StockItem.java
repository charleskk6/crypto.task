package com.portfolio.dto.asset;

import java.math.BigDecimal;

public class StockItem implements IAssetInterface {
    private String symbol;
    private int size;

    public StockItem(){}

    public StockItem(String symbol, int size) {
        this.symbol = symbol;
        this.size = size;
    }

    private StockItem(Builder builder) {
        this.symbol = builder.symbol;
        this.size = builder.size;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public BigDecimal calculatePrice(BigDecimal price) {
        return price;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Builder class
    public static class Builder {
        private String symbol;
        private int size;

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public StockItem build() {
            return new StockItem(this);
        }
    }
}
