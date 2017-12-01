package app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 */
public class PropertyReader {

    private static Properties prop;

    //load properties
    public static void init(String file) {
        InputStream input = null;
        try {
            prop = new Properties();
            input = PropertyReader.class.getClassLoader().getResourceAsStream(file);
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String getPropertyValue(String key){
        return prop.getProperty(key);
    }
}
