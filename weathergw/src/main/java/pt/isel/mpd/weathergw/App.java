/*
 * Copyright (C) 2015 Miguel Gamboa at CCISEL
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.isel.mpd.weathergw;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Consumer;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import pt.isel.mpd.githubapi.GithubUser;
import pt.isel.mpd.jsonzai.Pair;

/**
 *
 * @author Miguel Gamboa at CCISEL
 */
public class App {

	public static void print(List<WeatherInfo> src){
		for (WeatherInfo l : src) {
			System.out.println(l);
		}
	}

	public static void main(String [] args) throws ParseException, ClientProtocolException, IOException, InstantiationException, IllegalAccessException{
		// List<WeatherInfo> l = WeatherParser.parseWeather();
		String USER_PATH = "https://api.github.com/users/achiu";
		try(CloseableHttpClient httpclient = HttpClients.createDefault()){
			HttpGet httpget = new HttpGet(USER_PATH);
			System.out.println("executing request " + httpget.getURI());
			String responseBody = httpclient.execute(httpget, new BasicResponseHandler());
			GithubUser user = toObject(responseBody, GithubUser.class);	
			System.out.println(user);
		}
	}
	
	public static <T> T toObject(String src, Class<T> dest) throws InstantiationException, IllegalAccessException{
		T d = dest.newInstance(); // Da excepção se nao existir ctor sem parametros
		Field[] fields = d.getClass().getDeclaredFields();
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
}


