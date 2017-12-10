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

import info.gianlucacosta.diffdetector.core.ComparisonResult;
import info.gianlucacosta.diffdetector.core.Diff;
import info.gianlucacosta.diffdetector.sdk.DiffServiceClient;
import info.gianlucacosta.diffdetector.sdk.SimpleDiffServiceClient;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
public class DiffControllerHttpTest {
    private static final String localEndpoint =
            "http://localhost:8080";


    @Test
    public void theControllerShouldServeHttpRequestsForDataWithEqualBytes() {
        byte[] left =
                "ABCDEFGHIJKLMNOPQRSTUVW".getBytes();

        byte[] right =
                left.clone();


        SimpleDiffServiceClient diffServiceClient =
                new SimpleDiffServiceClient(localEndpoint);

        Optional<ComparisonResult> comparisonResultOption =
                diffServiceClient.compare(
                        left,
                        right
                );


        assertThat(
                comparisonResultOption.get().isSameLength(),
                equalTo(true)
        );


        assertThat(
                comparisonResultOption.get().getDiffs(),
                is(empty())
        );
    }


    @Test
    public void theControllerShouldServeHttpRequestsForDifferentByteLength() {
        byte[] left =
                "ABCDEFGHIJKLMNOPQRSTUVW".getBytes();

        byte[] right =
                "XXXX".getBytes();


        SimpleDiffServiceClient diffServiceClient =
                new SimpleDiffServiceClient(localEndpoint);

        Optional<ComparisonResult> comparisonResultOption =
                diffServiceClient.compare(
                        left,
                        right
                );


        assertThat(
                comparisonResultOption.get().isSameLength(),
                equalTo(false)
        );


        assertThat(
                comparisonResultOption.get().getDiffs(),
                is(empty())
        );
    }


    @Test
    public void theControllerShouldServeHttpRequestsForDataWithDiffs() {
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

        SimpleDiffServiceClient diffServiceClient =
                new SimpleDiffServiceClient(localEndpoint);

        Optional<ComparisonResult> comparisonResultOption =
                diffServiceClient.compare(
                        left,
                        right
                );

        assertThat(
                comparisonResultOption,
                equalTo(Optional.of(expectedComparisonResult))
        );
    }


    @Test
    public void theControllerShouldNotProvideResultsIfOnlyLeftIsProvided() {
        String id =
                UUID.randomUUID().toString();

        byte[] left =
                "ABCDEFGHIJKLMNOPQRSTUVW".getBytes();

        DiffServiceClient diffServiceClient =
                new DiffServiceClient(localEndpoint);

        assertThat(
                diffServiceClient.setLeft(id, left),
                equalTo(HttpStatus.SC_CREATED)
        );


        Optional<ComparisonResult> comparisonResultOption =
                diffServiceClient.compare(id);

        assertThat(
                comparisonResultOption.isPresent(),
                is(false)
        );
    }


    @Test
    public void theControllerShouldNotProvideResultsIfOnlyRightIsProvided() {
        String id =
                UUID.randomUUID().toString();

        byte[] right =
                "ABCDEFGHIJKLMNOPQRSTUVW".getBytes();

        DiffServiceClient diffServiceClient =
                new DiffServiceClient(localEndpoint);

        assertThat(
                diffServiceClient.setRight(id, right),
                equalTo(HttpStatus.SC_CREATED)
        );


        Optional<ComparisonResult> comparisonResultOption =
                diffServiceClient.compare(id);

        assertThat(
                comparisonResultOption.isPresent(),
                is(false)
        );
    }
}