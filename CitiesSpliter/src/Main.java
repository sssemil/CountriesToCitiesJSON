

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static java.nio.file.StandardOpenOption.*;

public class Main
{
	public static void main(String[] args) throws Exception
	{
		String source = "https://raw.githubusercontent.com/KnopersPL/CountriesToCitiesJSON/master/countriesToCities.json";
		Path outPath = Paths.get("../");
		HttpsURLConnection hugeFile = (HttpsURLConnection)new URL(source).openConnection();
		JsonParser parser = new JsonParser();
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(hugeFile.getInputStream(), "UTF-8"));)
		{
			JsonObject root = parser.parse(reader).getAsJsonObject();
			Gson gson = new Gson();
			root.entrySet().forEach(entry ->
			{
				Path filepath = outPath.resolve(entry.getKey() + ".json");
				try(FileChannel fc = FileChannel.open(filepath, CREATE, TRUNCATE_EXISTING, WRITE);)	
				{
					ByteBuffer buffer = Charset.forName("UTF-8").encode(gson.toJson(entry.getValue()));
					fc.write(buffer);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			});
		}
	}
}
