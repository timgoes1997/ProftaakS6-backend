package com.github.fontys;

import com.biepbot.stress.StressRunner;
import com.biepbot.stress.StressTest;
import okhttp3.FormBody;
import okhttp3.Response;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(StressRunner.class)
public class LocationStressTest extends BasicLocationTrackerTest{
    private AtomicInteger license = new AtomicInteger(0);
    private final int max = 9;

    @StressTest(
            threads = 500,
            duration = 50 * 1000,
            taskCooldownNs = 100,
            errorMargin = 0.05
    )
    public void stressTestHeavy() throws IOException {
        basicStress();
    }

    @StressTest(
            threads = 100,
            duration = 50 * 1000,
            taskCooldownNs = 100,
            errorMargin = 0.05
    )
    public void stressTestMedium() throws IOException {
        basicStress();
    }

    @StressTest(
            threads = 25,
            duration = 15 * 1000,
            taskCooldownNs = 100,
            errorMargin = 0.05
    )
    public void stressTestLow() throws IOException {
        basicStress();
    }

    protected void basicStress() throws IOException {
        int i = license.incrementAndGet();
        license.set(i % max);

        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("lon", "9");
        formBuilder.add("lat", "50");
        formBuilder.add("license", "XXX-00" + license.get());

        Response r = post(url, formBuilder.build());
    }
}
