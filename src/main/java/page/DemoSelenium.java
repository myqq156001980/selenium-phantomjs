package page;

import cn.edu.hfut.dmic.webcollector.crawldb.DBManager;
import cn.edu.hfut.dmic.webcollector.crawler.Crawler;
import cn.edu.hfut.dmic.webcollector.fetcher.Executor;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BerkeleyDBManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.*;


public class DemoSelenium {
    private final static Logger LOGGER = Logger.getLogger(DemoSelenium.class);

    private static Integer isNumber(String s) {
        Integer r;
        try {

            r = Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
        return r;
    }

    public static void run(){

    }


    public static void main(String[] args) throws Exception {
        Set<String> urlSet = new HashSet<>();
        Queue<String> urlQueue = new LinkedList<>();
        final Integer[] max = {-1};

        Executor executor = new Executor() {
            private volatile  Integer[] max = {-1};
            private volatile  Integer index = 1;
            public void execute(CrawlDatum datum, CrawlDatums next) throws Exception {
                LOGGER.info(Thread.currentThread().getName());
                Thread.sleep(1000);

                DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                desiredCapabilities.setJavascriptEnabled(true);
                String[] phantomJsArgs = {
                        "--web-security=false",
                        "--ssl-protocol=any",
                        "--ignore-ssl-errors=true",
                        "--webdriver-loglevel=ERROR",
                };
                desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomJsArgs);

//                desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/usr/local/matrix/bin/phantomjs");
                desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/Users/fpschina/Downloads/test/phantomjs");
                PhantomJSDriver webDriver = new PhantomJSDriver(desiredCapabilities);

                LOGGER.info("Current url : " + datum.url());
                webDriver.get(datum.url());
                urlSet.add(datum.url());
                String preUrl = "";


                List<WebElement> webElement = webDriver.findElements(By.cssSelector(".pagination a"));
                String cssSelector = String.format("div>span>a.num-unit:nth-child(%d)", index);

                for (WebElement w :
                        webElement) {
                    System.out.println(w.getAttribute("href") + "======");

                }

//
//                while (true) {
//                    WebElement webElement = webDriver.findElement(By.cssSelector("#zhaofangpager"));
//                    String cssSelector = String.format("div>span>a.num-unit:nth-child(%d)", index);
//                    WebElement e1;
//                    try {
//
//                        e1 = webElement.findElement(By.cssSelector(cssSelector));
//                    } catch (Exception e) {
//                        LOGGER.warn("the next usr is " + preUrl);
//                        break;
//
//                    }
//                    Integer pageNumber = isNumber(e1.getText());
//                    if (pageNumber != null && pageNumber > max[0]) {
//                        max[0] = pageNumber;
//                        urlSet.add(e1.getAttribute("href"));
//                        System.out.println(e1.getAttribute("href"));
//                    }
//                    index++;
//                    preUrl = e1.getAttribute("href");
//
//                }

                webDriver.close();

            }
        };

        urlQueue.add("http://bj.mogoroom.com/list/xz20_chaoyangqu/sq339_beiyuan?order=2");
        //创建一个基于伯克利DB的DBManager
        DBManager manager = new BerkeleyDBManager("crawl");
        //创建一个Crawler需要有DBManager和Executor
        Crawler crawler = new Crawler(manager, executor);

        crawler.addSeed(urlQueue.poll());
        crawler.start(1);

        while (!urlQueue.isEmpty()) {
            crawler.addSeed(urlQueue.poll());
            crawler.start(1);
        }

        Map<String,String> s = new HashMap<>();
        s.put("1", "2");

        System.out.println(s.get("12"));


        System.out.println(urlSet);

    }

}