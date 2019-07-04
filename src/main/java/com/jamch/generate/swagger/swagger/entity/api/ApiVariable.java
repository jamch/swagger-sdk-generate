package com.jamch.generate.swagger.swagger.entity.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Api请求变量
 *
 * @author Jamch[lowhgg@gmail.com]
 * @since 2019/07/03
 */
public class ApiVariable extends BaseDocument {

    private static final long serialVersionUID = 3622458402573401480L;

    /**
     * 变量名
     */
    private String name;

    /**
     * 变量类型
     */
    private String type;

    /**
     * 可否为空
     */
    private boolean nullable;

    /**
     * 子变量
     */
    private List<ApiVariable> childrens;

    public String getName() {
        return name;
    }

    public ApiVariable setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public ApiVariable setType(String type) {
        this.type = type;
        return this;
    }

    public boolean isNullable() {
        return nullable;
    }

    public ApiVariable setNullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public ApiVariable addChildren(ApiVariable variable) {
        if(Objects.isNull(this.childrens)) {
            this.childrens = new ArrayList<>();
        }
        this.childrens.add(variable);
        return this;
    }

    public List<ApiVariable> getChildrens() {
        return Collections.unmodifiableList(this.childrens);
    }
}