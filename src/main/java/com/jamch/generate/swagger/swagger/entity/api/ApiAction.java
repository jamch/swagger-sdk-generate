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

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ApiActionGroup getGroup() {
        return group;
    }

    public void setGroup(ApiActionGroup group) {
        this.group = group;
    }

    public ApiRequest getRequest() {
        return request;
    }

    public void setRequest(ApiRequest request) {
        this.request = request;
    }

    public ApiResponse getResponse() {
        return response;
    }

    public void setResponse(ApiResponse response) {
        this.response = response;
    }


}