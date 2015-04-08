package pt.isel.mpd.util;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReflectionUtils {
	public static <T> boolean bindField(T target, Field f, String value)
            throws IllegalArgumentException, IllegalAccessException, InstantiationException, ParseException {
            Class<?> fType = f.getType();
            f.setAccessible(true);
            //f.set(target, value);
            Object inst = value;
            if (fType.isPrimitive()){
            	fType = f.get(target).getClass();
            }
            inst = convertType(fType,value);
            if (fType.isAssignableFrom(inst.getClass())) {
                f.set(target, inst);
                return true;
            }
        return false;
    }
	
	static Object convertType(Class<?> type, String value) throws ParseException{

		if(type == Integer.class)
			return Integer.parseInt(value);
		if(type == Boolean.class)
			return Boolean.parseBoolean(value);
		if(type == java.util.Date.class){
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			Date d = format.parse(value);
			return d;
		}
		return value;
		
	}
}
