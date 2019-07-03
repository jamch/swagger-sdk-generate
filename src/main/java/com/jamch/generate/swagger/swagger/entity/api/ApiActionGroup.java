package com.jamch.generate.swagger.swagger.entity.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Api文档分组
 *
 * @author Jamch[lowhgg@gmail.com]
 * @since 2019/07/03
 */
public class ApiActionGroup extends BaseDocument {

    private static final long serialVersionUID = 1L;

    /**
     * 分组名
     */
    private String name;

    /**
     * Api接口列表
     */
    private List<ApiAction> actions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addAction(ApiAction apiAction) {
        if (Objects.isNull(apiAction)) {
            this.actions = new ArrayList<>();
        }

        this.actions.add(apiAction);
        apiAction.setGroup(this);
    }

    public List<ApiAction> getActions() {
        return Collections.unmodifiableList(this.actions);
    }
}