package pt.isel.mpd.jsonzai;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.List;

import pt.isel.mpd.util.ReflectionUtils;

public class JsonParser {
	
	public <T> T toObject(String src, Class<T> dest) 
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, ParseException{
		if(src == null || dest == null || src.charAt(0) != '{')
			throw new IllegalArgumentException();
		T d = dest.newInstance();
		Field[] fields = d.getClass().getDeclaredFields();
		for(Field f : fields){
			int idx = src.indexOf(f.getName());
			String s = src.substring(idx, src.length());
			int idx1 = s.indexOf(",\"");
			if(s.endsWith("\"")) idx -= 1;
			String [] pair = s.substring(0, idx1).split("[:\"]+");
			s = pair[1];
			for(int i = 2 ; i < pair.length ; i++)
				s += ":" + pair[i]; 
			if(pair[0].equals(f.getName())){
				ReflectionUtils.bindField(d, f, s);
			}
		}
		return d;
	}
	
	

	public <T> List<T> toList(String src, Class<T> dest){
		return null;
	}
}
