package com.jamch.generate.swagger.swagger.writer.java;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Objects;

import javax.lang.model.element.Modifier;

import com.jamch.generate.swagger.swagger.entity.api.ApiAction;
import com.jamch.generate.swagger.swagger.entity.api.ApiActionGroup;
import com.jamch.generate.swagger.swagger.entity.api.ApiDocument;
import com.jamch.generate.swagger.swagger.entity.api.ApiRequest;
import com.jamch.generate.swagger.swagger.entity.api.ApiResponse;
import com.jamch.generate.swagger.swagger.entity.api.ApiVariable;
import com.jamch.generate.swagger.swagger.writer.Writer;
import com.jamch.generate.swagger.swagger.writer.WriterConfiguration;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.squareup.javapoet.WildcardTypeName;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Java source Writer
 *
 * @author Jamch[lowhgg@gmail.com]
 * @since 2019/07/03
 */
public class JavaWriter implements Writer {

    protected WriterConfiguration configuration;

    public JavaWriter(WriterConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void write(ApiDocument apiDocument) throws IOException {
        checkAndCreateDirectory("request");
        checkAndCreateDirectory("response");
        initBaseRequest(apiDocument);
        initBaseResponse(apiDocument);
        initRequest(apiDocument);
        initResponse(apiDocument);
    }

    protected void initRequest(ApiDocument apiDocument) {
        // 先为所有的tag做好目录分类
        if (CollectionUtils.isNotEmpty(apiDocument.getGroups())) {
            for (ApiActionGroup group : apiDocument.getGroups()) {
                checkAndCreateDirectory("request", group.getName().toLowerCase());
            }
        }

        if (CollectionUtils.isEmpty(apiDocument.getActions())) {
            return;
        }

        for (ApiAction action : apiDocument.getActions()) {
            ApiRequest request = action.getRequest();
            if(Objects.isNull(request)) {
                continue;
            }


        }
    }

    protected void initResponse(ApiDocument apiDocument) {
        // 先为所有的tag做好目录分类
        if (CollectionUtils.isNotEmpty(apiDocument.getGroups())) {
            for (ApiActionGroup group : apiDocument.getGroups()) {
                checkAndCreateDirectory("response", group.getName().toLowerCase());
            }
        }

        if (CollectionUtils.isEmpty(apiDocument.getActions())) {
            return;
        }

        for (ApiAction action : apiDocument.getActions()) {
            ApiResponse response = action.getResponse();
            if(Objects.isNull(response)) {
                continue;
            }


        }
    }

    protected void initBaseRequest(ApiDocument apiDocument) throws IOException {
        MethodSpec getTraceId = generateSetter("traceId", String.class);
        MethodSpec setTraceId = generateSetter("traceId", String.class);
        MethodSpec obtainApiName = MethodSpec.methodBuilder("obtainApiName").addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.ABSTRACT).returns(String.class).build();
        MethodSpec obtainResponseClass = MethodSpec.methodBuilder("obtainResponseClass").addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.ABSTRACT)
                .returns(ParameterizedTypeName.get(ClassName.get(Class.class), TypeVariableName.get("T"))).build();

        TypeSpec baseRequest = TypeSpec.classBuilder("BaseRequest")
                .addTypeVariable(TypeVariableName.get("T",
                        ClassName.get(apiDocument.getBasePackage() + ".response", "BaseResponse")))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).addField(String.class, "traceId", Modifier.PRIVATE)
                .addMethod(getTraceId).addMethod(setTraceId).addMethod(obtainApiName).addMethod(obtainResponseClass)
                .build();

        JavaFile javaFile = JavaFile.builder(apiDocument.getBasePackage() + ".request", baseRequest).build();
        Path path = Paths.get(configuration.getOutputPath(), "request");
        javaFile.writeTo(path);
    }

    protected void initBaseResponse(ApiDocument apiDocument) throws IOException {
        TypeSpec baseResponse = TypeSpec.classBuilder("BaseResponse").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addSuperinterface(Serializable.class).build();

        JavaFile javaFile = JavaFile.builder(apiDocument.getBasePackage() + ".response", baseResponse).build();
        Path path = Paths.get(configuration.getOutputPath(), "response");
        javaFile.writeTo(path);
    }

    protected void checkAndCreateDirectory(String... names) {
        Path path = Paths.get(configuration.getOutputPath(), names);
        File file = path.toFile();
        if (file.exists()) {
            return;
        }

        file.mkdirs();
    }

    protected FieldSpec generateField(ApiVariable variable) {
        checkVariable(variable);
        return FieldSpec.builder(toJavaType(variable.getType()), variable.getName(), Modifier.PRIVATE).build();
    }

    protected MethodSpec generateSetter(ApiVariable variable) {
        checkVariable(variable);
        return generateSetter(variable.getName(), variable.getType());
    }

    protected MethodSpec generateSetter(String property, String type) {
        return MethodSpec.methodBuilder("set" + StringUtils.capitalize(property)).addModifiers(Modifier.PUBLIC)
                .addParameter(toJavaType(type), property).addStatement("this.$N = $N", property, property).build();
    }

    protected MethodSpec generateSetter(String property, Type type) {
        return MethodSpec.methodBuilder("set" + StringUtils.capitalize(property)).addModifiers(Modifier.PUBLIC)
                .addParameter(type, property).addStatement("this.$N = $N", property, property).build();
    }

    protected MethodSpec generateGetter(ApiVariable variable) {
        checkVariable(variable);
        return generateGetter(variable.getName(), variable.getType());
    }

    protected MethodSpec generateGetter(String property, String type) {
        return MethodSpec.methodBuilder("get" + StringUtils.capitalize(property)).addModifiers(Modifier.PUBLIC)
                .addStatement("return this.$N", property).returns(toJavaType(type)).build();
    }

    protected MethodSpec generateGetter(String property, Type type) {
        return MethodSpec.methodBuilder("get" + StringUtils.capitalize(property)).addModifiers(Modifier.PUBLIC)
                .addStatement("return this.$N", property).returns(type).build();
    }

    private void checkVariable(ApiVariable variable) {
        if (Objects.isNull(variable)) {
            throw new IllegalArgumentException("Null property");
        }
        if (StringUtils.isBlank(variable.getName())) {
            throw new IllegalArgumentException("Bad property [" + variable.getName() + "]");
        }
    }

    protected Type toJavaType(String type) {
        if (StringUtils.isBlank(type)) {
            throw new IllegalArgumentException("Null type");
        }
        switch (type) {
        case "string":
            return String.class;
        case "object":
            return Object.class;
        default:
            throw new IllegalArgumentException("Unknown type [" + type + "]");
        }
    }

    protected String url2ClassName(String url) {
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("Null url");
        }

        StringBuilder className = new StringBuilder();
        String[] urlSplit = StringUtils.split(url, "/");
        for (String innerUrl : urlSplit) {
            if (StringUtils.isBlank(innerUrl)) {
                continue;
            }

            className.append(StringUtils.capitalize(innerUrl));
        }
        return className.toString();
    }
}