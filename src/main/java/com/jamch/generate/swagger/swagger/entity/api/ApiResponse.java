package com.jamch.generate.swagger.swagger.entity.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Api返回结果
 *
 * @author Jamch[lowhgg@gmail.com]
 * @since 2019/07/03
 */
public class ApiResponse extends BaseDocument {

    private static final long serialVersionUID = 4970889246671520720L;

    /**
     * 返回体类型
     */
    private String produce;

    /**
     * 返回变量
     */
    private List<ApiVariable> variables;

    public List<ApiVariable> getVariables() {
        return variables;
    }

    public ApiResponse addVariable(ApiVariable apiVariable) {
        if (Objects.isNull(this.variables)) {
            this.variables = new ArrayList<>();
        }

        this.variables.add(apiVariable);
        return this;
    }

    public String getProduce() {
        return produce;
    }

    public ApiResponse setProduce(String produce) {
        this.produce = produce;
        return this;
    }
}