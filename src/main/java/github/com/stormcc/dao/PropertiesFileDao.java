package github.com.stormcc.dao;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Create By: Jimmy Song
 * Create At: 2025-01-16 17:11
 */
@Slf4j
public class PropertiesFileDao {
    private static final String FILE_PATH ="/Users/jimmysong/workspace/github/imageWebDemo/src/main/resources/imageWeb.properties";
    private static final int AGE_DEFAULT= 10;
    private static final String NAME_DEFAULT= "LiLei";
    private static final boolean ENABLE_DO_DEFAULT= false;

    public static boolean enableDo(){
        Properties properties = properties();
        if ( properties == null ) {
            return ENABLE_DO_DEFAULT;
        }
        String enableDo = properties.getProperty("imageweb.enableDo");
        if ( enableDo == null ) {
            return ENABLE_DO_DEFAULT;
        }
        try {
            return Boolean.parseBoolean(enableDo);
        } catch (Exception e) {
            log.error("return:{}, parse Exception enableDo:{}, ", ENABLE_DO_DEFAULT, enableDo, e);
            return ENABLE_DO_DEFAULT;
        }
    }

    public static String getName(){
        Properties properties = properties();
        if ( properties == null ) {
            return NAME_DEFAULT;
        }
        String name = properties.getProperty("imageweb.name");
        if ( name == null ) {
            log.info("return:{}, name is null", NAME_DEFAULT);
            return NAME_DEFAULT;
        }
        return name;
    }

    public static int getAge(){
        Properties properties = properties();
        if ( properties == null ) {
            return AGE_DEFAULT;
        }
        String age = properties.getProperty("imageweb.age");
        if ( age == null ) {
            return AGE_DEFAULT;
        }
        try {
            return Integer.parseInt(age);
        } catch (Exception e) {
            log.error("return:{}, parse Exception age:{}, ", AGE_DEFAULT, age, e);
            return AGE_DEFAULT;
        }
    }


    private static Properties properties(){
        Properties prop = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(FILE_PATH)){
            prop.load(fileInputStream);
            return prop;
        } catch (FileNotFoundException e) {
            log.error("return null， file:{}, not exists, FileNotFoundException ", FILE_PATH, e);
            return null;
        } catch (IOException e) {
            log.error("return null， file:{}, not exists, IOException ", FILE_PATH, e);
            return null;
        }
    }
}
