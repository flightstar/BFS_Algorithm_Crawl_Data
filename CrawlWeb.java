import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlWeb {
	private static Queue<String> queue = new LinkedList<>();
	private static Set<String> mark = new HashSet();
	private static String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	
	public static void bfs(String root) throws IOException{
		queue.add(root);
		while (!queue.isEmpty())
		{
			// Retrieves and remove head element
			String crawledURL = queue.poll();
			System.out.println("Site crawled: " + crawledURL);
			boolean ok = false;
			URL url = null;
			BufferedReader br = null;
			while (!ok){
				try {
					url = new URL(crawledURL);
					br = new BufferedReader(new InputStreamReader(url.openStream()));
					ok = true;
				} catch (MalformedURLException e) {
					System.out.println("Malformed URL: " + crawledURL);
					crawledURL = queue.poll();
					ok = false; // Browse continue
				} catch (IOException e) {
					System.out.println("IOException For URL: " + crawledURL);
					crawledURL = queue.poll();
					ok = false; // Browse continue
				}
			}
			StringBuilder stringBuilder = new StringBuilder();
			while ((crawledURL = br.readLine()) != null){
				stringBuilder.append(crawledURL);
			}
			crawledURL = stringBuilder.toString();
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(crawledURL);
			while (matcher.find())
			{
				String w = matcher.group();
				if (!mark.contains(w))
				{
					mark.add(w);
					System.out.println("Site Added For Crawling: " + w);
					queue.add(w);
				}
			}
		}
	}
	
	public static void showResult(){
		System.out.println("Result: ");
		System.out.println("Website crawled: " + mark.size());
		for (String data: mark)
		{
			System.out.println(data);
		}
	}
	public static void main(String[] args) throws IOException {
		bfs("https://docs.oracle.com");
		showResult();
	}
}
