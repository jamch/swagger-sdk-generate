package com.jamch.generate.swagger.writer.java;

import java.io.IOException;

import com.jamch.generate.swagger.swagger.entity.api.ApiDocument;
import com.jamch.generate.swagger.swagger.writer.Writer;
import com.jamch.generate.swagger.swagger.writer.WriterConfiguration;
import com.jamch.generate.swagger.swagger.writer.java.JavaWriter;

import org.junit.jupiter.api.Test;

public class JavaWriterTest {

    @Test
    public void testWriter() throws IOException {
        WriterConfiguration configuration = new WriterConfiguration();
        configuration.setOutputPath("D:\\WorkSpace\\Java\\swagger-sdk-generate\\target");

        ApiDocument apiDocument = new ApiDocument();
        apiDocument.setBasePackage("com.swj");

        Writer writer = new JavaWriter(configuration);
        writer.write(apiDocument);
    }
}