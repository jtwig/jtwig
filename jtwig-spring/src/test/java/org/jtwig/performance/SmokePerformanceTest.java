package org.jtwig.performance;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.jtwig.acceptance.AbstractJtwigAcceptanceTest;
import org.jtwig.acceptance.functions.TranslateTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Locale.ENGLISH;
import org.apache.commons.lang3.math.NumberUtils;
import static org.joda.time.DateTime.now;

/**
 * This is not a real performance test. In fact, this is a test
 * which allows us to prove Jtwig can handle multiple requests at a time
 * (concurrency) properly (kinda Smoke test). So, is something that allows
 * us to go to the next level in terms of evaluating Jtwig performance.
 */
@Controller
@Ignore // We probably should have a separate module just for non-functional tests
public class SmokePerformanceTest extends AbstractJtwigAcceptanceTest {
    private static final int CALLS = 1000;
    private CountDownLatch counter = new CountDownLatch(CALLS);
    private ExecutorService executorService = Executors.newFixedThreadPool(100);

    @Test
    public void canHandleMultipleRequestsAtOnce() throws Exception {
        serverReceivesGetRequest("/performance");
        System.out.println("Starting...");
        DateTime startDate = now();

        for (int i=0;i < CALLS;i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        GetMethod method = new GetMethod("http://localhost:" + thePort() + "/performance");
                        httpClient().executeMethod(method);
                        counter.countDown();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        System.out.println("Jobs queued... waiting to finish...");
        counter.await();
        Seconds seconds = new Period(startDate, now()).toStandardSeconds();
        System.out.println(String.format("Finished in %s seconds (Speed: %d requests per second)",
                seconds.toString(), requestsPerSecond(seconds)));
    }

    private int requestsPerSecond(Seconds seconds) {
        int value = seconds.get(DurationFieldType.seconds());
        if (value == 0) return Integer.MAX_VALUE;
        return CALLS / value;
    }

    @RequestMapping("/performance")
    public String test (ModelMap modelMap) {
        modelMap.addAttribute("summary", "Example");
        modelMap.addAttribute("presentations", presentations());
        return "performance/index";
    }

    private List<Elem> presentations() {
        ArrayList<Elem> list = new ArrayList<>();
        for (int i=0;i<10;i++)
            list.add(elem("elem"+i, "value"+i));
        return list;
    }

    private Elem elem(String title, String test) {
        return new Elem(title, test);
    }

    @Bean
    public MessageSource messageSource () {
        TranslateTest.InMemoryMessageSource inMemoryMessageSource = new TranslateTest.InMemoryMessageSource();
        inMemoryMessageSource.add("example.title", "Performance test");
        return inMemoryMessageSource;
    }

    @Bean
    public LocaleResolver localeResolver () {
        return new FixedLocaleResolver(ENGLISH);
    }

    protected HttpClient httpClient() {
        MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams connectionManagerParams = new HttpConnectionManagerParams();
        connectionManagerParams.setDefaultMaxConnectionsPerHost(100);
        HostConfiguration localhost = new HostConfiguration();
        localhost.setHost("localhost");
        connectionManagerParams.setMaxConnectionsPerHost(localhost, 100);
        connectionManagerParams.setMaxTotalConnections(100);
        httpConnectionManager.setParams(connectionManagerParams);
        return new HttpClient(httpConnectionManager);
    }

    public class Elem {
        public String title;
        public String speakerName;

        public Elem(String title, String speakerName) {
            this.title = title;
            this.speakerName = speakerName;
        }
    }
}
