package com.company;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by Ivan on 3/5/2016.
 */
@XmlRootElement
public class Config
{
    @XmlTransient
    public final static String PATH = "config.xml";
    private boolean returnCode = true;
    private boolean systemOut = true;
    private boolean fileReport = true;
    private String reportSuffix = "report for ";
    private String reportAppendix = ".txt";

    public boolean isReturnCode() {
        return returnCode;
    }

    @XmlElement
    public void setReturnCode(boolean returnCode) {
        this.returnCode = returnCode;
    }

    public boolean isSystemOut() {
        return systemOut;
    }

    @XmlElement
    public void setSystemOut(boolean systemOut) {
        this.systemOut = systemOut;
    }

    public String getReportSuffix() {
        return reportSuffix;
    }

    @XmlElement
    public void setReportSuffix(String reportSuffix) {
        this.reportSuffix = reportSuffix;
    }

    public String getReportAppendix() {
        return reportAppendix;
    }

    @XmlElement
    public void setReportAppendix(String reportAppendix) {
        this.reportAppendix = reportAppendix;
    }

    public boolean isFileReport() {
        return fileReport;
    }

    @XmlElement
    public void setFileReport(boolean fileReport) {
        this.fileReport = fileReport;
    }
}
