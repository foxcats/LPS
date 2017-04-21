package Filter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import Queue.Qnode;

public class UriFilter {

	
	public boolean checkValidity(String uri)
	{
		try {
			URL url;
			url = new URL(uri);
			URLConnection con = url.openConnection();
			HttpURLConnection exitCode = (HttpURLConnection)con;
			exitCode.getResponseCode();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public int checkNode(Qnode node)
	{
		
		if(!checkValidity(node.getData().getUri()))
		{
			return 0;
		}
		
		if(node.getData().getDepth()==0)
		{
			return 2;
		}
		
		return 1;
		
		
		
	}
}
