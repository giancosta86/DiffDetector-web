/*^
  ===========================================================================
  Diff Detector - Web
  ===========================================================================
  Copyright (C) 2017 Gianluca Costa
  ===========================================================================
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  ===========================================================================
*/

package info.gianlucacosta.diffdetector.web;

import info.gianlucacosta.diffdetector.core.ComparisonOperand;
import info.gianlucacosta.diffdetector.core.ComparisonResult;
import info.gianlucacosta.diffdetector.core.Diff;
import info.gianlucacosta.diffdetector.web.diff.DiffService;
import info.gianlucacosta.diffdetector.web.storage.InMemoryByteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class DiffControllerTest {
    private DiffController diffController;

    private final String testId =
            "test";

    private final byte[] left =
            "ABCDEFGHIJKLMNOPQRSTUVW".getBytes();

    private final byte[] right =
            "ABCDXXXHIJXLMXXXXXSTUXX".getBytes();


    private final ComparisonResult expectedComparisonResult =
            ComparisonResult.ofDiffs(
                    Arrays.asList(
                            new Diff(4, 3),
                            new Diff(10, 1),
                            new Diff(13, 5),
                            new Diff(21, 2)
                    )
            );


    @BeforeEach
    public void init() {
        diffController =
                new DiffController(
                        new InMemoryByteRepository(),
                        new InMemoryByteRepository(),
                        new DiffService()
                );
    }


    @Test
    public void diffsShouldNotBeComputedIfBothLeftAndRightAreMissing() {
        assertThrows(DataNotFoundException.class, () -> {
            diffController.compare(testId);
        });
    }


    @Test
    public void diffsShouldNotBeComputedIfOnlyLeftIsProvided() {
        diffController.putLeft(
                testId,
                new ComparisonOperand(left)
        );

        assertThrows(DataNotFoundException.class, () -> {
            diffController.compare(testId);
        });
    }


    @Test
    public void diffsShouldNotBeComputedIfOnlyRightIsProvided() {
        diffController.putRight(
                testId,
                new ComparisonOperand(right)
        );

        assertThrows(DataNotFoundException.class, () -> {
            diffController.compare(testId);
        });
    }


    @Test
    public void diffsShouldBeCorrectlyComputedIfBothLeftAndRightAreProvided() {
        diffController.putLeft(
                testId,
                new ComparisonOperand(left)
        );

        diffController.putRight(
                testId,
                new ComparisonOperand(right)
        );

        ComparisonResult comparisonResult =
                diffController.compare(testId);


        assertThat(
                comparisonResult,
                equalTo(expectedComparisonResult)
        );

        diffController.delete(testId);
    }
}
