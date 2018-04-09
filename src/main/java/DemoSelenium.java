import cn.edu.hfut.dmic.webcollector.crawldb.DBManager;
import cn.edu.hfut.dmic.webcollector.crawler.Crawler;
import cn.edu.hfut.dmic.webcollector.fetcher.Executor;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BerkeleyDBManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ImeActivationFailedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;


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

    public static void run() {

    }


    public static void main(String[] args) throws Exception {

        Executor executor = new Executor() {

            public void execute(CrawlDatum datum, CrawlDatums next) throws Exception {

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

                webDriver.get(datum.url());


                WebElement webElement = webDriver.findElement(By.cssSelector("#zhaofangpager"));
                String cssSelector = String.format("div>span>a.num-unit:nth-child(%d)", 3);
                WebElement e1 = webElement.findElement(By.cssSelector(cssSelector));
                LOGGER.info("The page number is " + e1.getText() + "URL IS " + e1.getAttribute("href"));

                webDriver.close();

            }
        };

        //创建一个基于伯克利DB的DBManager
        DBManager manager = new BerkeleyDBManager("crawl");
        //创建一个Crawler需要有DBManager和Executor
        Crawler crawler = new Crawler(manager, executor);


        crawler.addSeed("http://bj.baletu.com/zhaofang");
        crawler.start(1);


    }

}