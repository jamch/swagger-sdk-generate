package com.jamch.generate.swagger.swagger.response;

import java.lang.Double;
import java.lang.String;
import java.util.Date;

public class OrderSyncPayResponse extends BaseResponse {
  private static final long serialVersionUID = 1L;

  private ApiResult apiResult;

  public void setApiResult(ApiResult apiResult) {
    this.apiResult = apiResult;
  }

  public ApiResult getApiResult() {
    return this.apiResult;
  }

  public class ApiResult {
    private String classificationName;

    private Double amountMoney;

    private Date quoteTime;

    public void setClassificationName(String classificationName) {
      this.classificationName = classificationName;
    }

    public String getClassificationName() {
      return this.classificationName;
    }

    public void setAmountMoney(Double amountMoney) {
      this.amountMoney = amountMoney;
    }

    public Double getAmountMoney() {
      return this.amountMoney;
    }

    public void setQuoteTime(Date quoteTime) {
      this.quoteTime = quoteTime;
    }

    public Date getQuoteTime() {
      return this.quoteTime;
    }
  }
}
