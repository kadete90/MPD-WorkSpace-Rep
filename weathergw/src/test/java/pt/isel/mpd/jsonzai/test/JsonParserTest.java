package pt.isel.mpd.jsonzai.test;

import java.io.IOException;

import junit.framework.TestCase;
import pt.isel.mpd.githubapi.GithubUser;
import pt.isel.mpd.jsonzai.JsonParser;
import pt.isel.mpd.jsonzai.JsonParserFromHttp;

public class JsonParserTest extends TestCase{

	public void test_read_from_toObject() throws IOException {
		String path = "https://api.github.com/users/achiu";
		JsonParser parser = new JsonParser();
		GithubUser user = JsonParserFromHttp.parseJson();

    }
}
