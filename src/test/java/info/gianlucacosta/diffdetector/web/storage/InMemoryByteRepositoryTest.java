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

package info.gianlucacosta.diffdetector.web.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class InMemoryByteRepositoryTest {
    private InMemoryByteRepository byteRepository;

    @BeforeEach
    public void init() {
        byteRepository =
                new InMemoryByteRepository();
    }


    @Test
    public void storingAndRetrievingDataShouldWork() {
        String id =
                "test";

        byte[] originalData =
                "Hello, world!".getBytes();


        byteRepository.save(id, originalData);

        byte[] retrievedData =
                byteRepository.find(id).get();

        assertThat(
                retrievedData,
                equalTo(originalData)
        );
    }


    @Test
    public void retrievingNonStoredDataShouldReturnEmpty() {
        String id =
                "test";

        Optional<byte[]> retrievedData =
                byteRepository.find(id);

        assertThat(
                retrievedData.isPresent(),
                is(false)
        );
    }


    @Test
    public void storingAndRetrievingEmptyArraysShouldWork() {
        String id =
                "test";

        byte[] originalData =
                new byte[]{};


        byteRepository.save(id, originalData);

        byte[] retrievedData =
                byteRepository.find(id).get();

        assertThat(
                retrievedData,
                equalTo(originalData)
        );
    }


    @Test
    public void storingViaNullIdShouldFail() {
        String id =
                null;

        assertThrows(NullPointerException.class, () -> {
            byteRepository.save(
                    id,
                    new byte[]{}
            );
        });
    }


    @Test
    public void storingViaEmptyIdShouldFail() {
        String id =
                "";

        assertThrows(IllegalArgumentException.class, () -> {
            byteRepository.save(
                    id,
                    new byte[]{}
            );
        });
    }


    @Test
    public void retrievingViaNullIdShouldFail() {
        String id =
                null;

        assertThrows(NullPointerException.class, () -> {
            byteRepository.find(
                    id
            );
        });
    }


    @Test
    public void retrievingViaEmptyIdShouldFail() {
        String id =
                "";

        assertThrows(IllegalArgumentException.class, () -> {
            byteRepository.find(
                    id
            );
        });
    }


    @Test
    public void removingDataShouldWork() {
        String id =
                "test";

        byte[] originalData =
                "Hello, world!".getBytes();


        byteRepository.save(id, originalData);

        byteRepository.remove(id);

        Optional<byte[]> retrievedDataOption =
                byteRepository.find(id);

        assertThat(
                retrievedDataOption.isPresent(),
                is(false)
        );
    }


    @Test
    public void removingViaNullIdShouldFail() {
        String id =
                null;

        assertThrows(NullPointerException.class, () -> {
            byteRepository.remove(
                    id
            );
        });
    }


    @Test
    public void removingViaEmptyIdShouldFail() {
        String id =
                "";

        assertThrows(IllegalArgumentException.class, () -> {
            byteRepository.remove(
                    id
            );
        });
    }
}
