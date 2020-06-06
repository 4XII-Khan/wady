package framework.utils;

import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.testng.Reporter;

/**
 * @program: Duck
 * @description: TODO
 * @author:
 * @create: 2020-02-06 00:56
 */
public class ReportUtil {

    private static String reportName = "自动化测试报告";

    private static String splitTimeAndMsg = "===";

    public static void log(String msg) {
        long timeMillis = Calendar.getInstance().getTimeInMillis();
        Reporter.log(timeMillis + splitTimeAndMsg + msg, true);
    }

    public static String getReportName() {
        return reportName;
    }

    public static String getSpiltTimeAndMsg() {
        return splitTimeAndMsg;
    }

    public static void setReportName(String reportName) {
        if (StringUtils.isNotBlank(reportName)) {
            ReportUtil.reportName = reportName;
        }
    }
}