package com.jamch.generate.swagger.swagger.entity.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApiRequest extends BaseDocument {

    private static final long serialVersionUID = 1995947996049390644L;

    /**
     * 请求体类型
     */
    private String consume;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private List<ApiVariable> variables;

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public List<ApiVariable> getVariables() {
        return variables;
    }

    public void addVariable(ApiVariable apiVariable) {
        if (Objects.isNull(this.variables)) {
            this.variables = new ArrayList<>();
        }

        this.variables.add(apiVariable);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


}