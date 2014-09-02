package com.lyncode.jtwig.performance;

import com.lyncode.jtwig.acceptance.AbstractJtwigAcceptanceTest;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is not a real performance test. In fact, this is a test
 * which allows us to prove Jtwig can handle multiple requests at a time
 * (concurrency) properly (kinda Smoke test). So, is something that allows
 * us to go to the next level in terms of evaluating Jtwig performance.
 */
@Controller
public class SmokePerformanceTest extends AbstractJtwigAcceptanceTest {
    private static final int CALLS = 1000;
    private CountDownLatch counter = new CountDownLatch(CALLS);
    private ExecutorService executorService = Executors.newFixedThreadPool(100);

    @Test
    public void canHandleMultipleRequestsAtOnce() throws Exception {
        serverReceivesGetRequest("/performance");
        System.out.println("Starting...");

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

        System.out.println("Waiting to finish...");
        counter.await();
        System.out.println("Finished");
    }

    @RequestMapping("/performance")
    public String test (ModelMap modelMap) {
        modelMap.addAttribute("test", "hello");
        return "performance/index";
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
}
