package com.jamch.generate.swagger.swagger.writer.java;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.lang.model.element.Modifier;

import com.jamch.generate.swagger.swagger.entity.api.ApiAction;
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
import com.squareup.javapoet.TypeSpec.Builder;
import com.squareup.javapoet.TypeVariableName;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Java request and response source Writer
 *
 * @author Jamch[lowhgg@gmail.com]
 * @since 2019/07/03
 */
public class JavaRequestAndResponseWriter implements Writer {

    protected WriterConfiguration configuration;

    public JavaRequestAndResponseWriter(WriterConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void write(ApiDocument apiDocument) throws IOException {
        initBaseRequest(apiDocument);
        initBaseResponse(apiDocument);
        initResponse(apiDocument);
        initRequest(apiDocument);
    }

    protected void initRequest(ApiDocument apiDocument) {
        if (CollectionUtils.isEmpty(apiDocument.getActions())) {
            return;
        }

        for (ApiAction action : apiDocument.getActions()) {
            ApiRequest request = action.getRequest();
            ApiResponse response = action.getResponse();
            if (Objects.isNull(request)) {
                continue;
            }
            if (Objects.isNull(response)) {
                continue;
            }

            String requestPackageName = apiDocument.getBasePackage() + ".request";
            String responsePackageName = apiDocument.getBasePackage() + ".response";
            String requestClassName = StringUtils.capitalize(url2ClassName(action.getUrl()) + "Request");
            String responseClassName = StringUtils.capitalize(url2ClassName(action.getUrl()) + "Response");
            TypeSpec.Builder requestClassBuilder = TypeSpec.classBuilder(requestClassName).addModifiers(Modifier.PUBLIC)
                    .addField(generateSerialVersionUID());

            // init variables
            requestClassBuilder = initVariables(requestClassBuilder, request.getVariables());

            // init structure
            ParameterizedTypeName extendClass = ParameterizedTypeName.get(
                    ClassName.bestGuess("BaseRequest"),
                    ClassName.bestGuess(responseClassName));
            requestClassBuilder = requestClassBuilder.superclass(extendClass);

            // init api obtainApiAddress
            MethodSpec obtainApiAddress = MethodSpec.methodBuilder("obtainApiAddress").addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class).returns(String.class).addStatement("return $S", action.getUrl())
                    .build();
            requestClassBuilder = requestClassBuilder.addMethod(obtainApiAddress);

            // init api obtainResponseClass
            ParameterizedTypeName obtainResponseClassReturn = ParameterizedTypeName.get(ClassName.get(Class.class),
                    ClassName.get(responsePackageName, responseClassName));

            MethodSpec obtainResponseClass = MethodSpec.methodBuilder("obtainResponseClass")
                    .addAnnotation(Override.class).addModifiers(Modifier.PUBLIC).returns(obtainResponseClassReturn)
                    .addStatement("return $T.class", ClassName.get(responsePackageName, responseClassName)).build();
            requestClassBuilder = requestClassBuilder.addMethod(obtainResponseClass);

            JavaFile javaFile = JavaFile.builder(requestPackageName, requestClassBuilder.build()).build();

            try {
                javaFile.writeTo(Paths.get(configuration.getOutputPath()));
            } catch (IOException e) {
                System.err.println("Generate [" + requestClassName + "] failure");
                e.printStackTrace();
            }
        }
    }

    protected void initResponse(ApiDocument apiDocument) {
        if (CollectionUtils.isEmpty(apiDocument.getActions())) {
            return;
        }

        for (ApiAction action : apiDocument.getActions()) {
            ApiRequest request = action.getRequest();
            ApiResponse response = action.getResponse();
            if (Objects.isNull(request)) {
                continue;
            }
            if (Objects.isNull(response)) {
                continue;
            }

            String packageName = apiDocument.getBasePackage() + ".response";
            String responseClassName = StringUtils.capitalize(url2ClassName(action.getUrl()) + "Response");
            TypeSpec.Builder responseClassBuilder = TypeSpec.classBuilder(responseClassName)
                    .addModifiers(Modifier.PUBLIC).addField(generateSerialVersionUID());

            // init variables
            responseClassBuilder = initVariables(responseClassBuilder, response.getVariables());

            // init structure
            responseClassBuilder = responseClassBuilder.superclass(ClassName.get(packageName, "BaseResponse"));

            JavaFile javaFile = JavaFile.builder(packageName, responseClassBuilder.build()).build();

            try {
                javaFile.writeTo(Paths.get(configuration.getOutputPath()));
            } catch (IOException e) {
                System.err.println("Generate [" + responseClassName + "] failure");
                e.printStackTrace();
            }
        }
    }

    protected TypeSpec.Builder initVariables(TypeSpec.Builder tBuilder, List<ApiVariable> variables) {
        for (ApiVariable variable : variables) {
            switch (variable.getType()) {
            case "string":
            case "integer":
            case "float":
            case "boolean":
            case "long":
            case "date":
                // base type
                MethodSpec generateGetter = generateGetter(variable);
                MethodSpec generateSetter = generateSetter(variable);
                FieldSpec generateField = generateField(variable);
                tBuilder = tBuilder.addField(generateField).addMethod(generateSetter).addMethod(generateGetter);
                break;
            case "object":
                // object

                // init inner object class
                String property = StringUtils.uncapitalize(variable.getName());
                String objectName = StringUtils.capitalize(variable.getName());

                generateGetter = generateGetter(property, ClassName.bestGuess(objectName));
                generateSetter = generateSetter(property, ClassName.bestGuess(objectName));
                generateField = generateField(ClassName.bestGuess(objectName), property);

                tBuilder = tBuilder.addType(initInnerObject(tBuilder, variable)).addField(generateField)
                        .addMethod(generateSetter).addMethod(generateGetter);
                break;
            case "array":
                // array
                break;
            default:
                throw new IllegalArgumentException("Unknown type [" + variable.getType() + "]");
            }
        }

        return tBuilder;
    }

    protected TypeSpec initInnerObject(TypeSpec.Builder tBuilder, ApiVariable variable) {
        Builder objectClassBuilder = TypeSpec.classBuilder(StringUtils.capitalize(variable.getName()))
                .addModifiers(Modifier.PUBLIC);

        initVariables(objectClassBuilder, variable.getChildrens());
        return objectClassBuilder.build();
    }

    /**
     * Init base Request
     *
     * @param apiDocument apiDocument
     * @throws IOException
     */
    protected void initBaseRequest(ApiDocument apiDocument) throws IOException {
        MethodSpec getTraceId = generateGetter("traceId", String.class);
        MethodSpec setTraceId = generateSetter("traceId", String.class);
        MethodSpec getMethod = generateGetter("method", String.class);
        MethodSpec setMethod = generateSetter("method", String.class);
        MethodSpec obtainApiAddress = MethodSpec.methodBuilder("obtainApiAddress").addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.ABSTRACT).returns(String.class).build();
        MethodSpec obtainResponseClass = MethodSpec.methodBuilder("obtainResponseClass").addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.ABSTRACT)
                .returns(ParameterizedTypeName.get(ClassName.get(Class.class), TypeVariableName.get("T"))).build();

        TypeSpec baseRequest = TypeSpec.classBuilder("BaseRequest")
                .addTypeVariable(TypeVariableName.get("T",
                        ClassName.get(apiDocument.getBasePackage() + ".response", "BaseResponse")))
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT).addField(generateSerialVersionUID())
                .addField(String.class, "method", Modifier.PRIVATE).addField(String.class, "traceId", Modifier.PRIVATE)
                .addSuperinterface(Serializable.class).addMethod(setMethod).addMethod(getMethod).addMethod(getTraceId)
                .addMethod(setTraceId).addMethod(obtainApiAddress).addMethod(obtainResponseClass).build();

        JavaFile javaFile = JavaFile.builder(apiDocument.getBasePackage() + ".request", baseRequest).build();
        Path path = Paths.get(configuration.getOutputPath());
        javaFile.writeTo(path);
    }

    /**
     * Init base response
     *
     * @param apiDocument apiDocument
     * @throws IOException
     */
    protected void initBaseResponse(ApiDocument apiDocument) throws IOException {
        TypeSpec baseResponse = TypeSpec.classBuilder("BaseResponse").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addField(generateSerialVersionUID()).addSuperinterface(Serializable.class).build();

        JavaFile javaFile = JavaFile.builder(apiDocument.getBasePackage() + ".response", baseResponse).build();
        Path path = Paths.get(configuration.getOutputPath());
        javaFile.writeTo(path);
    }

    protected FieldSpec generateField(ApiVariable variable) {
        checkVariable(variable);
        return FieldSpec.builder(toJavaType(variable.getType()), variable.getName(), Modifier.PRIVATE).build();
    }

    protected FieldSpec generateField(TypeName typeName, String property) {
        return FieldSpec.builder(typeName, property, Modifier.PRIVATE).build();
    }

    protected FieldSpec generateSerialVersionUID() {
        return FieldSpec.builder(TypeName.LONG, "serialVersionUID", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("1L").build();
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

    protected MethodSpec generateSetter(String property, TypeName typeName) {
        return MethodSpec.methodBuilder("set" + StringUtils.capitalize(property)).addModifiers(Modifier.PUBLIC)
                .addParameter(typeName, property).addStatement("this.$N = $N", property, property).build();
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

    protected MethodSpec generateGetter(String property, TypeName typeName) {
        return MethodSpec.methodBuilder("get" + StringUtils.capitalize(property)).addModifiers(Modifier.PUBLIC)
                .addStatement("return this.$N", property).returns(typeName).build();
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
        case "integer":
            return Integer.class;
        case "long":
            return Long.class;
        case "float":
            return Double.class;
        case "boolean":
            return Boolean.class;
        case "date":
            return Date.class;
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