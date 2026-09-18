package org.example.aiplaner.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

/**
 * 余额相关的返回体。
 *
 * <p>balance：当前余额；enough：是否还有余额（false = 余额已用完/为 0）；status：业务状态字符串。</p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BalanceDto {

    private BigDecimal balance;
    private Boolean enough;
    private String status;

    public BalanceDto() {
    }

    public BalanceDto(BigDecimal balance, Boolean enough, String status) {
        this.balance = balance;
        this.enough = enough;
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean getEnough() {
        return enough;
    }

    public void setEnough(Boolean enough) {
        this.enough = enough;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
