package testsuite.utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
//    read base, url, pass, email, load data
    private static Properties properties = new Properties();
    static {
        try{
            InputStream input = ConfigLoader.class.getClassLoader()
                    .getResourceAsStream("config.properties");
            if(input!=null){
                properties.load(input);
            }else {
                throw new RuntimeException("No see config.properties in resource");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getValue(String key){
        return properties.getProperty(key);
    }
}
