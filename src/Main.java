import static java.lang.Thread.sleep;


/*
Java!!
to compile java to .class files. -cp is include file
  javac Main.java WebCrawler.java  -cp "jsoup-1.15.1.jar"

to run it
  java -classpath ~/:jsoup-1.15.1.jar Main
    which tells it to compile with main as the root and to look in home and specifically for file jsoup under current directory

 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        while(true) {
            try {
                System.out.println("Starting a run!");
                new WebCrawler().getPageLinks("http://www.google.com/");
                System.out.println("Finishing run. sleeping 10 seconds");
                sleep(10000);
            } catch (Exception e) {
                System.out.println("Exception! sleeping 10 secs");
                sleep(10000);
            }
        }
    }
}


/*
1) Basic java web crawler in memory on single machine(pull from onine)
2) setup linux azure vm and have web crawler run there
3)setup  sql server
  Output table, columns: urlpage, attached links
  1 row for remaining jobs
  in memory dedup only, and then check if already exists in db(add to db if it does)
and  have it call into and store state in sql server
4) move to job and subjobs(full website, vs sub website)
5) spread across machines/containers, move to distributed queue system(can use azure one?)
6) add auto retry work. Confirm by killing one



 */