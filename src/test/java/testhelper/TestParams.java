package testhelper;

import constant.Constant;

public class TestParams {

    private static final String HEADLESS = "headless";
    public static final boolean IS_HEADLESS = isHeadless();
    private static final String LOGGING = "logging";
    public static final boolean DEBUGGING = isLogger();
    private static final String BROWSER = "browser";
    private static final String RECORDING = "recording";
    public static final boolean SCREEN_RECORDING = isRecording();
    private static final String URL = "url";
    private static final String LOGGING_API_RESPONSE = "loggingApiResponse";
    private static final String ALLOW_API_ERROR = "allowApiError";

    private static boolean isLogger() {
        if (System.getProperty(LOGGING) != null) {
            return System.getProperty(LOGGING).equalsIgnoreCase(Constant.API_PARAM_VALUE_TRUE);
        }
        return false;
    }

    private static boolean isRecording() {
        if (System.getProperty(RECORDING) != null) {
            return System.getProperty(RECORDING).equalsIgnoreCase(Constant.API_PARAM_VALUE_TRUE);
        }
        return false;
    }

    private static boolean isHeadless() {
        if (System.getProperty(HEADLESS) != null) {
            if (System.getProperty(HEADLESS).equalsIgnoreCase(Constant.API_PARAM_VALUE_TRUE)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isLoggingApiResponse() {
        if (System.getProperty(LOGGING_API_RESPONSE) != null) {
            if (System.getProperty(LOGGING_API_RESPONSE)
                    .equalsIgnoreCase(Constant.API_PARAM_VALUE_TRUE)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAllowApiError() {
        if (System.getProperty(ALLOW_API_ERROR) != null) {
            if (System.getProperty(ALLOW_API_ERROR).equalsIgnoreCase(Constant.API_PARAM_VALUE_TRUE)) {
                return true;
            }
        }
        return false;
    }
}