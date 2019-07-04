package com.jamch.generate.swagger.swagger.request;

import com.jamch.generate.swagger.swagger.response.BaseResponse;
import java.io.Serializable;
import java.lang.Class;
import java.lang.String;

public abstract class BaseRequest<T extends BaseResponse> implements Serializable {
  private static final long serialVersionUID = 1L;

  private String method;

  private String traceId;

  public void setMethod(String method) {
    this.method = method;
  }

  public String getMethod() {
    return this.method;
  }

  public String getTraceId() {
    return this.traceId;
  }

  public void setTraceId(String traceId) {
    this.traceId = traceId;
  }

  public abstract String obtainApiAddress();

  public abstract Class<T> obtainResponseClass();
}
