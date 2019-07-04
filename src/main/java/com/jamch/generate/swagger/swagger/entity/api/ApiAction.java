package com.jamch.generate.swagger.swagger.entity.api;

/**
 * Api接口信息
 *
 * @author Jamch[lowhgg@gmail.com]
 * @since 2019/07/03
 */
public class ApiAction extends BaseDocument {

    private static final long serialVersionUID = -9121669978108485573L;

    /**
     * Api请求的Url
     */
    private String url;

    /**
     * Api描述
     */
    private String description;

    /**
     * Api分组
     */
    private ApiActionGroup group;

    /**
     * Api请求体
     */
    private ApiRequest request;

    /**
     * Api返回体
     */
    private ApiResponse response;

    public String getUrl() {
        return url;
    }

    public ApiAction setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ApiAction setDescription(String description) {
        this.description = description;
        return this;
    }

    public ApiActionGroup getGroup() {
        return group;
    }

    public ApiAction setGroup(ApiActionGroup group) {
        this.group = group;
        return this;
    }

    public ApiRequest getRequest() {
        return request;
    }

    public ApiAction setRequest(ApiRequest request) {
        this.request = request;
        return this;
    }

    public ApiResponse getResponse() {
        return response;
    }

    public ApiAction setResponse(ApiResponse response) {
        this.response = response;
        return this;
    }


}