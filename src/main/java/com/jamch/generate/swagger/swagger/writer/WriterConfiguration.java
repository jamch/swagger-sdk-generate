package com.jamch.generate.swagger.swagger.writer;

/**
 * Writer base Configuration
 *
 * @author Jamch[lowhgg@gmail.com]
 * @since 2019/07/03
 */
public class WriterConfiguration {

    /**
     * 生成的目录
     */
    private String outputPath;

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }


}