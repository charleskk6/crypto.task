package com.portfolio.dto;

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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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
