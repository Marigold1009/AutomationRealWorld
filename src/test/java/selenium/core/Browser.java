package selenium.core;

import utils.logging.LogFactory;

public enum Browser {
    FIREFOX,
    CHROME,
    EDGE,
    IE,
    SAFARI,
    OPERA;

    public static Browser lookup(String b) throws Exception {
        for (Browser browser : Browser.values()) {
            if (browser.name().equalsIgnoreCase(b)) {
                return browser;
            }
        }
        throw new Exception("The selected browser " + b + " is not an applicable choice");
    }

    public static Browser getBrowser() {
        if (System.getProperty("browser") != null) {
            try {
                return lookup(System.getProperty("browser"));
            } catch (Exception e) {
                LogFactory.getWriter().write("Provided Browser does not match options. Using Chrome instead. " + e);
            }
        }
        // Default Chrome
        return CHROME;
    }
}