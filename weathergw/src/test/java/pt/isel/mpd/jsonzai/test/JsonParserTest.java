package pt.isel.mpd.jsonzai.test;

import java.io.IOException;
import java.text.ParseException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import junit.framework.TestCase;
import pt.isel.mpd.githubapi.GithubUser;
import pt.isel.mpd.jsonzai.JsonParser;
import pt.isel.mpd.jsonzai.JsonParserFromHttp;

public class JsonParserTest extends TestCase{

	public void test_read_from_toObject() 
			throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, ParseException {
		String USER_PATH = "https://api.github.com/users/achiu";
		try(CloseableHttpClient httpclient = HttpClients.createDefault()){
			HttpGet httpget = new HttpGet(USER_PATH);
			System.out.println("executing request " + httpget.getURI());
			String responseBody = httpclient.execute(httpget, new BasicResponseHandler());
			JsonParser parser = new JsonParser();
			GithubUser user = parser.toObject(responseBody, GithubUser.class);	
			assertEquals(24772, user.getId());
			assertEquals("achiu", user.getLogin());
			assertEquals("San Francisco, CA", user.getLocation());
			assertEquals("achiu@github.com", user.getEmail());
			assertEquals("Tue Sep 16 03:24:44 BST 2008", user.getCreated_at().toString());
		}

    }
}
