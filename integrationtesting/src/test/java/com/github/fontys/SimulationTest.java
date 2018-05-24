package com.github.fontys;

import com.biepbot.stress.StressRunner;
import com.biepbot.stress.StressTest;
import okhttp3.*;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(StressRunner.class)
public class SimulationTest extends BasicLocationTrackerTest {

    @StressTest(
            threads = 100,
            duration = 500 * 1000,
            taskCooldownMs = 1000
    )
    public void trackTest() throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();
        locationX += 0.01 + (double)random.nextInt(100) / (double)1000;
        locationY += 0.01 + (double)random.nextInt(100) / (double)1000;

        locationY = locationY % 180;
        locationX = locationX % 90;

        formBuilder.add("lon", String.valueOf(locationY));
        formBuilder.add("lat", String.valueOf(locationX));
        formBuilder.add("license", "XXX-001");


        Response r = post(url, formBuilder.build());
    }

    @StressTest(
            threads = 10000,
            duration = 50 * 1000,
            errorMargin = 0.05
    )
    public void stressTest10000() throws IOException {
        basicStress();
    }

    @StressTest(
            threads = 1000,
            duration = 50 * 1000,
            errorMargin = 0.05
    )
    public void stressTest1000() throws IOException {
        basicStress();
    }

    @StressTest(
            threads = 100,
            duration = 50 * 1000,
            errorMargin = 0.05
    )
    public void stressTest100() throws IOException {
        basicStress();
    }
}
