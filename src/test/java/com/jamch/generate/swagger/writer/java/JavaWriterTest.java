package com.jamch.generate.swagger.writer.java;

import java.io.IOException;

import com.jamch.generate.swagger.swagger.entity.api.ApiAction;
import com.jamch.generate.swagger.swagger.entity.api.ApiDocument;
import com.jamch.generate.swagger.swagger.entity.api.ApiRequest;
import com.jamch.generate.swagger.swagger.entity.api.ApiResponse;
import com.jamch.generate.swagger.swagger.entity.api.ApiVariable;
import com.jamch.generate.swagger.swagger.writer.Writer;
import com.jamch.generate.swagger.swagger.writer.WriterConfiguration;
import com.jamch.generate.swagger.swagger.writer.java.JavaRequestAndResponseWriter;

import org.junit.jupiter.api.Test;

public class JavaWriterTest {

    @Test
    public void testWriter() throws IOException {
        WriterConfiguration configuration = new WriterConfiguration();
        configuration.setOutputPath("D:\\WorkSpace\\Java\\swagger-sdk-generate\\src\\main\\java");

        ApiDocument apiDocument = new ApiDocument();
        apiDocument.setBasePackage("com.jamch.generate.swagger.swagger");

        ApiAction action = new ApiAction();
        action.setUrl("/order/syncPay");

        ApiRequest request = new ApiRequest();
        request.addVariable(new ApiVariable().setName("apiParam").setType("object")
                .addChildren(new ApiVariable().setName("projectId").setType("string"))
                .addChildren(new ApiVariable().setName("serviceId").setType("long"))
                .addChildren(new ApiVariable().setName("userAddress").setType("object")
                        .addChildren(new ApiVariable().setName("provinceCode").setType("string"))
                        .addChildren(new ApiVariable().setName("provinceName").setType("string"))));

        ApiResponse response = new ApiResponse();
        response.addVariable(new ApiVariable().setName("ApiLists").setType("array")
                .addChildren(new ApiVariable().setType("array").addChildren(new ApiVariable().setName("apiResult")
                        .setType("object").addChildren(new ApiVariable().setName("name").setType("string")))));

        action.setRequest(request).setResponse(response);

        apiDocument.addAction(action);
        Writer writer = new JavaRequestAndResponseWriter(configuration);
        writer.write(apiDocument);
    }
}