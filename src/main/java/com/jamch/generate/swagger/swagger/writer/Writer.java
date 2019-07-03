package com.jamch.generate.swagger.swagger.writer;

import java.io.IOException;

import com.jamch.generate.swagger.swagger.entity.api.ApiDocument;

/**
 * Writer
 *
 * @author Jamch[lowhgg@gmail.com]
 * @since 2019/07/03
 */
public interface Writer {

    void write(ApiDocument apiDocument) throws IOException;
}