
import java.io.IOException;
import java.util.concurrent.*;
import java.util.*;

import static java.util.concurrent.Executors.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.sql.*;

//

public class WebCrawler {

    //todo fix the mismatch
    public void ConnectAzure()
    {
        // Declare the JDBC object.
        Connection conn = null;

        try {
            // Establish the connection.
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //conn = DriverManager.getConnection(connectionUrl, user, pass);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DBInfo.connectionString);
            System.out.println("Finished Connecting to database...");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Connect()
    {
        ConnectAzure();
    }

    private HashSet<String> links;
    int maxLinksFetched = 1000;
    int linksFetchedSoFar = 0;

    public WebCrawler() {
        links = new HashSet<String>();
    }

    public void getPageLinks(String URL) {
        //4. Check if you have already crawled the URLs
        //(we are intentionally not checking for duplicate content in this example)
        if (links.contains(URL) )
            return;

        if(linksFetchedSoFar >= maxLinksFetched) {
            System.out.println("Reached max size. Returning");
            return;
        }
        try {
            //4. (i) If not add it to the index
            if (links.add(URL)) {
                System.out.println(URL);
            }

            //2. Fetch the HTML code
            Document document = Jsoup.connect(URL).get();
            //3. Parse the HTML to extract links to other URLs
            Elements linksOnPage = document.select("a[href]");

            //5. For each extracted URL... go back to Step 4.
            for (Element page : linksOnPage) {
                getPageLinks(page.attr("abs:href"));
            }
        } catch (IOException e) {
            System.err.println("For '" + URL + "': " + e.getMessage());
        }
        linksFetchedSoFar++;
    }

    private String getHostName(String url) {
        url = url.substring(7);
        String[] parts = url.split("/");
        return parts[0];
    }
}
