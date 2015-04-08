package pt.isel.mpd.jsonzai;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class JsonParser {
	
	public <T> T toObject(String src, Class<T> dest) throws InstantiationException, IllegalAccessException{
		T d = dest.newInstance(); // Da excepção se nao existir ctor sem parametros
		Field[] fields = d.getClass().getFields();
		HashMap<String, Object> map = getJson(src);
        for(Field f : fields){
        	bindField(d, f, map);
        }
		return d;
	}

	private static HashMap<String, Object> getJson(String src) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String jsonObject = src.substring(1, src.length()-1);
		String delims = ",";
		StringTokenizer st = new StringTokenizer(jsonObject, delims);
		while(st.hasMoreTokens()){
			String line = (String) st.nextElement();
			if(line.substring(1, line.lastIndexOf(":") - 1).equals("location"))
				line += "," + st.nextToken();
			Pair<String,Object> p = getPair(line);
			map.put(p.key, p.value);
		}
		return map;
	}
	
	private static Pair<String, Object> getPair(String line) {
		String [] tokens = line.split(":");
		String key = tokens[0].substring(1, tokens[0].length()-1);
		System.out.println(key);
		if(tokens.length > 2)
			return new Pair<String,Object>(
					key,
					tokens[1].substring(1, tokens[1].length()) + ":" +
					tokens[2].substring(0, tokens[2].length()-1));
		if(!tokens[1].contains("\"")){
			if(tokens[1].equals("false") || tokens[1].equals("true"))
				return new Pair<String,Object>(key,Boolean.getBoolean(tokens[1]));
			else if(tokens[1].equals("null"))
				return new Pair<String, Object>(key, tokens[1]);
			return new Pair<String,Object>(key,Integer.parseInt(tokens[1]));
		}
		return new Pair<String, Object>(key, tokens[1].substring(1, tokens[1].length()-1));
	}

	public static <T> boolean bindField(T target, Field f, Map<String, Object> vals)
            throws IllegalArgumentException, IllegalAccessException {
        String fName = f.getName();
        if (vals.containsKey(fName)) {
            Class<?> fType = f.getType();
            Object fValue = vals.get(fName);
            f.setAccessible(true);
            if (fType.isPrimitive())
                fType = f.get(target).getClass();
            if (fType.isAssignableFrom(fValue.getClass())) {
                f.set(target, fValue);
                return true;
            }
        }
        return false;
    }

	public <T> List<T> toList(String src, Class<T> dest){
		return null;
	}
}
