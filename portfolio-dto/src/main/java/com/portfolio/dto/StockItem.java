package com.portfolio.dto;

import java.math.BigDecimal;

/**
 * The type Stock item.
 */
public class StockItem implements IAssetInterface {
    private String symbol;
    private int size;

    /**
     * Instantiates a new Stock item.
     */
    public StockItem(){}

    /**
     * Instantiates a new Stock item.
     *
     * @param symbol the symbol
     * @param size   the size
     */
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

    /**
     * Sets symbol.
     *
     * @param symbol the symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public int getSize() {
        return size;
    }

    /**
     * Sets size.
     *
     * @param size the size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * This method simply set new price directly for Stock Item
     * @param priceOrUnderlyingPrice the new price of Stock
     * @return the price in BigDecimal
     */
    @Override
    public BigDecimal calculatePrice(BigDecimal priceOrUnderlyingPrice) {
        return priceOrUnderlyingPrice;
    }

    /**
     * Builder builder.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * The type Builder.
     */
// Builder class
    public static class Builder {
        private String symbol;
        private int size;

        /**
         * Symbol builder.
         *
         * @param symbol the symbol
         * @return the builder
         */
        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        /**
         * Size builder.
         *
         * @param size the size
         * @return the builder
         */
        public Builder size(int size) {
            this.size = size;
            return this;
        }

        /**
         * Build stock item.
         *
         * @return the stock item
         */
        public StockItem build() {
            return new StockItem(this);
        }
    }
}
