import java.util.concurrent.*;
import java.util.*;

import static java.util.concurrent.Executors.*;


public class WebCrawler {
    class HtmlParser {

        public String[] getUrls(String startUrl) {
            return new String[3];
        }
    }

//todo implement html parser
public List<String> crawl(String startUrl, HtmlParser htmlParser) {

    // find hostname
    int index = startUrl.indexOf('/', 7);
    String hostname = (index != -1) ? startUrl.substring(0, index) : startUrl;

    // multi-thread
    Crawler crawler = new Crawler(startUrl, hostname, htmlParser);
    crawler.map = new ConcurrentHashMap<>(); // reset result as static property belongs to class, it will go through all of the test cases
    crawler.result = crawler.map.newKeySet();
    Thread thread = new Thread(crawler);
    thread.start();

    crawler.joinThread(thread); // wait for thread to complete
    return new ArrayList<>(crawler.result);
}
}

class Crawler implements Runnable {
    
    String startUrl;
    String hostname;
    WebCrawler.HtmlParser htmlParser;
    public static ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
    public static Set<String> result = map.newKeySet();

    public Crawler(String startUrl, String hostname, WebCrawler.HtmlParser htmlParser) {
        this.startUrl = startUrl;
        this.hostname = hostname;
        this.htmlParser = htmlParser;
    }


    @Override
    public void run() {
        if (this.startUrl.startsWith(hostname) && !this.result.contains(this.startUrl)) {
            this.result.add(this.startUrl);
            List<Thread> threads = new ArrayList<>();
            for (String s : htmlParser.getUrls(startUrl)) {
                if(result.contains(s)) continue;
                Crawler crawler = new Crawler(s, hostname, htmlParser);
                Thread thread = new Thread(crawler);
                thread.start();
                threads.add(thread);
            }
            for (Thread t : threads) {
                joinThread(t); // wait for all threads to complete
            }
        }
    }

    public static void joinThread(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private String getHostName(String url) {
        url = url.substring(7);
        String[] parts = url.split("/");
        return parts[0];
    }
}
