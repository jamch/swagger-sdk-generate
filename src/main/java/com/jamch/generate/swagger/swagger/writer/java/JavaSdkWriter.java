package com.jamch.generate.swagger.swagger.writer.java;

import com.jamch.generate.swagger.swagger.entity.api.ApiDocument;
import com.jamch.generate.swagger.swagger.writer.Writer;
import com.jamch.generate.swagger.swagger.writer.WriterConfiguration;

/**
 * Java http sdk Writer
 *
 * @author Jamch[lowhgg@gmail.com]
 * @since 2019/07/03
 */
public class JavaSdkWriter extends JavaRequestAndResponseWriter {

    public JavaSdkWriter(WriterConfiguration configuration) {
        super(configuration);
    }

    @Override
    public void write(ApiDocument apiDocument) {

    }

}