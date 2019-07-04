package com.jamch.generate.swagger.swagger.request;

import com.jamch.generate.swagger.swagger.response.OrderSyncPayResponse;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;

public class OrderSyncPayRequest extends BaseRequest<OrderSyncPayResponse> {
  private static final long serialVersionUID = 1L;

  private ApiParam apiParam;

  public void setApiParam(ApiParam apiParam) {
    this.apiParam = apiParam;
  }

  public ApiParam getApiParam() {
    return this.apiParam;
  }

  @Override
  public String obtainApiAddress() {
    return "/order/syncPay";
  }

  @Override
  public Class<OrderSyncPayResponse> obtainResponseClass() {
    return OrderSyncPayResponse.class;
  }

  public class ApiParam {
    private String projectId;

    private Long serviceId;

    private UserAddress userAddress;

    public void setProjectId(String projectId) {
      this.projectId = projectId;
    }

    public String getProjectId() {
      return this.projectId;
    }

    public void setServiceId(Long serviceId) {
      this.serviceId = serviceId;
    }

    public Long getServiceId() {
      return this.serviceId;
    }

    public void setUserAddress(UserAddress userAddress) {
      this.userAddress = userAddress;
    }

    public UserAddress getUserAddress() {
      return this.userAddress;
    }

    public class UserAddress {
      private String provinceCode;

      private String provinceName;

      public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
      }

      public String getProvinceCode() {
        return this.provinceCode;
      }

      public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
      }

      public String getProvinceName() {
        return this.provinceName;
      }
    }
  }
}
