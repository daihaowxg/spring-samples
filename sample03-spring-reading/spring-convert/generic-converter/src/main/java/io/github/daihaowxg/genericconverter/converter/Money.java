package io.github.daihaowxg.genericconverter.converter;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

/**
 * 自定义金额类型，用于演示 GenericConverter 的多源类型转换能力。
 * <p>
 * 包含两个属性：
 * <ul>
 *     <li>{@code amount}：金额数值</li>
 *     <li>{@code currency}：货币类型（默认 CNY）</li>
 * </ul>
 */
public class Money {

    private BigDecimal amount;
    private Currency currency;

    /**
     * 默认构造函数。
     */
    public Money() {
        this.currency = Currency.getInstance("CNY");
    }

    /**
     * 使用金额和货币创建 Money 对象。
     *
     * @param amount   金额数值
     * @param currency 货币类型
     */
    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    /**
     * 使用金额创建 Money 对象（默认货币为 CNY）。
     *
     * @param amount 金额数值
     */
    public Money(BigDecimal amount) {
        this(amount, Currency.getInstance("CNY"));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) && Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount +
                ", currency=" + currency.getCurrencyCode() +
                '}';
    }
}
