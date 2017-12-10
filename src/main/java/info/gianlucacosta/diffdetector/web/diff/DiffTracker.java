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

import info.gianlucacosta.diffdetector.core.Diff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Internal class that tracks byte-per-byte diffs
 */
class DiffTracker {
    private boolean inputFinished;
    private int offset;

    private Optional<Integer> diffStartOffsetOption =
            Optional.empty();

    private final List<Diff> diffs =
            new ArrayList<>();


    public void inputBytes(byte left, byte right) {
        inputFinished =
                false;

        if (left == right) {
            tryToCreateDiff();
        } else {
            if (!diffStartOffsetOption.isPresent()) {
                diffStartOffsetOption =
                        Optional.of(offset);
            }
        }

        offset++;
    }


    private void tryToCreateDiff() {
        diffStartOffsetOption.ifPresent(diffStartOffset -> {
            int diffLength =
                    offset - diffStartOffset;

            Diff diff =
                    new Diff(
                            diffStartOffset,
                            diffLength
                    );

            diffs.add(diff);

            diffStartOffsetOption =
                    Optional.empty();
        });
    }


    public List<Diff> getDiffs() {
        if (!inputFinished) {
            tryToCreateDiff();

            inputFinished =
                    true;
        }

        return Collections.unmodifiableList(diffs);
    }
}
