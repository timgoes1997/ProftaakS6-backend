package com.github.fontys;

import com.biepbot.stress.StressRunner;
import com.biepbot.stress.StressTest;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(StressRunner.class)
public class LocationStressTest extends BasicLocationTrackerTest{
    @StressTest(
            threads = 1000,
            duration = 50 * 1000,
            errorMargin = 0.05
    )
    public void stressTestHeavy() throws IOException {
        basicStress();
    }

    @StressTest(
            threads = 100,
            duration = 5 * 1000,
            errorMargin = 0.05
    )
    public void stressTestMedium() throws IOException {
        basicStress();
    }

    @StressTest(
            threads = 25,
            duration = 1 * 1000,
            errorMargin = 0.05
    )
    public void stressTestLow() throws IOException {
        basicStress();
    }
}
