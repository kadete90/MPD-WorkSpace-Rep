package pt.isel.mpd.jsonzai;

import java.util.List;

import org.apache.http.HttpEntity;

import pt.isel.mpd.githubapi.GithubUser;
import pt.isel.mpd.util.HttpGw;
import pt.isel.mpd.weathergw.WeatherInfo;
import pt.isel.mpd.weathergw.WeatherParserFromStream;

public class JsonParserFromHttp {

	static String path = "http://api.github.com/users/achiu";
	
	public static GithubUser parseJson(){
        return HttpGw.getData(path, (HttpEntity resp) -> JsonParserFromStream.parseJson(resp.getContent()));
    }
}
