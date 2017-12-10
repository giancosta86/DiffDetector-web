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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Service that actually performs comparisons to detect diffs
 */
@Service
public class DiffService {
    public ComparisonResult compare(byte[] left, byte[] right) {
        if (left.length != right.length) {
            return ComparisonResult.ofDifferentLengths();
        }

        DiffTracker diffTracker =
                new DiffTracker();


        IntStream
                .range(
                        0,
                        left.length
                )
                .forEach(offset -> {
                    byte leftByte =
                            left[offset];

                    byte rightByte =
                            right[offset];


                    diffTracker.inputBytes(
                            leftByte,
                            rightByte
                    );
                });


        List<Diff> diffs =
                diffTracker.getDiffs();


        return ComparisonResult.ofDiffs(
                diffs
        );
    }
}
