package com.jamch.generate.swagger.swagger.entity.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Api文档
 *
 * @author Jamch [lowhgg@gmail.com]
 * @since 2019/7/3
 */
public class ApiDocument extends BaseDocument {

    private static final long serialVersionUID = -2879189660090688500L;

    /**
     * 接口文档标题
     */
    private String title;

    /**
     * 接口文档描述
     */
    private String description;

    /**
     * 基础包信息
     */
    private String basePackage;

    /**
     * Api文档的基准地址
     */
    private String basePath;

    /**
     * Api接口分组
     */
    private List<ApiActionGroup> groups;

    /**
     * Api接口列表
     */
    private List<ApiAction> actions;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void addActionGroup(ApiActionGroup apiActionGroup) {
        if(Objects.isNull(this.groups)) {
            this.groups = new ArrayList<>();
        }

        this.groups.add(apiActionGroup);
    }

    public List<ApiActionGroup> getGroups() {
        return Collections.unmodifiableList(this.groups);
    }

    public void addAction(ApiAction apiAction) {
        if(Objects.isNull(this.actions)) {
            this.actions = new ArrayList<>();
        }

        this.actions.add(apiAction);
    }

    public List<ApiAction> getActions() {
        return Collections.unmodifiableList(this.actions);
    }
}