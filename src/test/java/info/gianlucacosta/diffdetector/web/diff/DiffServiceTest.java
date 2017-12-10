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

package info.gianlucacosta.diffdetector.web.diff;

import info.gianlucacosta.diffdetector.core.ComparisonResult;
import info.gianlucacosta.diffdetector.core.Diff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class DiffServiceTest {
    private DiffService diffService;

    @BeforeEach
    public void init() {
        diffService =
                new DiffService();
    }


    @Test
    public void computeDiffShouldReturnWhenLeftAndRightHaveDifferentSize() {
        byte[] left =
                "Hello, world!".getBytes();

        byte[] right =
                "This is a very, very ,very long string".getBytes();

        ComparisonResult comparisonResult =
                diffService.compare(left, right);

        assertThat(
                comparisonResult.isSameLength(),
                is(false)
        );


        assertThat(
                comparisonResult.getDiffs(),
                is(empty())
        );
    }


    @Test
    public void computeDiffShouldReturnNoDiffsWhenLeftAndRightAreEqual() {
        byte[] left =
                "ABCDEFGHIJKLMNOPQRSTUVW".getBytes();

        byte[] right =
                left.clone();


        ComparisonResult comparisonResult =
                diffService.compare(left, right);


        assertThat(
                comparisonResult.isSameLength(),
                is(true)
        );


        assertThat(
                comparisonResult.getDiffs(),
                is(empty())
        );
    }


    @Test
    public void computeDiffShouldDetectSingleOneByteDiff() {
        byte[] left =
                "ABCDEFGHIJKLMNOPQRSTUVW".getBytes();

        byte[] right =
                "ABCDXFGHIJKLMNOPQRSTUVW".getBytes();


        ComparisonResult expectedComparisonResult =
                ComparisonResult.ofDiffs(
                        Collections.singletonList(
                                new Diff(4, 1)
                        )
                );


        ComparisonResult comparisonResult =
                diffService.compare(left, right);


        assertThat(
                comparisonResult,
                equalTo(expectedComparisonResult)
        );
    }


    @Test
    public void computeDiffShouldDetectDifferentOneByteDiffs() {
        byte[] left =
                "ABCDEFGHIJKLMNOPQRSTUVW".getBytes();

        byte[] right =
                "ABCDXFGHIJXLXNOPQRSTUVX".getBytes();


        ComparisonResult expectedComparisonResult =
                ComparisonResult.ofDiffs(
                        Arrays.asList(
                                new Diff(4, 1),
                                new Diff(10, 1),
                                new Diff(12, 1),
                                new Diff(22, 1)
                        )
                );


        ComparisonResult comparisonResult =
                diffService.compare(left, right);


        assertThat(
                comparisonResult,
                equalTo(expectedComparisonResult)
        );
    }


    @Test
    public void computeDiffShouldDetectSingleMultiByteDiff() {
        byte[] left =
                "ABCDEFGHIJKLMNOPQRSTUVW".getBytes();

        byte[] right =
                "ABCDXXXXXXKLMNOPQRSTUVW".getBytes();


        ComparisonResult expectedComparisonResult =
                ComparisonResult.ofDiffs(
                        Collections.singletonList(
                                new Diff(4, 6)
                        )
                );


        ComparisonResult comparisonResult =
                diffService.compare(left, right);


        assertThat(
                comparisonResult,
                equalTo(expectedComparisonResult)
        );
    }


    @Test
    public void computeDiffShouldDetectDifferentMultiByteDiff() {
        byte[] left =
                "ABCDEFGHIJKLMNOPQRSTUVW".getBytes();

        byte[] right =
                "ABCDXXXHIJXLMXXXXXSTUXX".getBytes();


        ComparisonResult expectedComparisonResult =
                ComparisonResult.ofDiffs(
                        Arrays.asList(
                                new Diff(4, 3),
                                new Diff(10, 1),
                                new Diff(13, 5),
                                new Diff(21, 2)
                        )
                );


        ComparisonResult comparisonResult =
                diffService.compare(left, right);


        assertThat(
                comparisonResult,
                equalTo(expectedComparisonResult)
        );
    }
}
