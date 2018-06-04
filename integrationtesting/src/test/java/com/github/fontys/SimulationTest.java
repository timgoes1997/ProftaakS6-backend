package com.github.fontys;

import com.biepbot.stress.StressRunner;
import com.biepbot.stress.StressTest;
import okhttp3.*;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(StressRunner.class)
public class SimulationTest extends BasicLocationTrackerTest {

    @StressTest(
            threads = 100,
            duration = 500 * 1000,
            taskCooldownMs = 1000
    )
    public void trackTest() throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();
        locationX += 0.01 + (double) random.nextInt(100) / (double) 1000;
        locationY += 0.01 + (double) random.nextInt(100) / (double) 1000;

        locationY = locationY % 180;
        locationX = locationX % 90;

        formBuilder.add("lon", String.valueOf(locationY));
        formBuilder.add("lat", String.valueOf(locationX));
        formBuilder.add("license", "XXX-001");

        post(LOCATION_URL, formBuilder.build());
    }

}
