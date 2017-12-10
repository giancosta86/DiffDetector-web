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

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple, in-memory implementation of ByteRepository.
 * It lacks a few important features:
 * <p>
 * <ul>
 * <li>Items never expire - it is the user that must manually remove them</li>
 * <li>The data reside in local memory - which is rather fragile</li>
 * </ul>
 * <p>
 * A more robust implementation could be based on a distributed cache, which could also
 * probably allow to introduce expiration policies.
 * <p>
 * The class is thread-safe, as it just employs basic methods of ConcurrentHashMap.
 */
@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InMemoryByteRepository implements ByteRepository {
    private final Map<String, byte[]> bytesMap =
            new ConcurrentHashMap<>();


    @Override
    public void save(String id, byte[] bytes) {
        Objects.requireNonNull(id);

        if (id.isEmpty()) {
            throw new IllegalArgumentException();
        }

        bytesMap.put(id, bytes);
    }


    @Override
    public Optional<byte[]> find(String id) {
        Objects.requireNonNull(id);

        if (id.isEmpty()) {
            throw new IllegalArgumentException();
        }


        return Optional.ofNullable(
                bytesMap.get(id)
        );
    }


    @Override
    public void remove(String id) {
        Objects.requireNonNull(id);

        if (id.isEmpty()) {
            throw new IllegalArgumentException();
        }


        bytesMap.remove(id);
    }
}
