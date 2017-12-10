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
import info.gianlucacosta.diffdetector.web.diff.DiffService;
import info.gianlucacosta.diffdetector.web.storage.ByteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * RESTful web service controller.
 * <p>
 * As a matter of fact, despite the REST syntax, this web service is <i>stateful</i>,
 * as its interface provides operations that internally modify its state.
 * <p>
 * Its root path is <b>/v1/diff</b> and its subpaths are:
 * <p>
 * <ul>
 * <li><b>/{id}/left</b> -> <b>POST</b>, to set the left item having the given id</li>
 * <li></b>/{id}/right</b> -> <b>POST</b>, to set the right item having the given id</li>
 * <li></b>/{id}</b> -> <b>GET</b>, to compare the left and right item having the given id</li>
 * <li></b>/{id}</b> -> <b>DELETE</b>, to remove the left and right item having the given id</li>
 * </ul>
 */
@RestController
@RequestMapping(DiffController.path)
public class DiffController {
    public static final String path = "/v1/diff";

    private final ByteRepository leftRepository;

    private final ByteRepository rightRepository;

    private final DiffService diffService;


    public DiffController(ByteRepository leftRepository, ByteRepository rightRepository, DiffService diffService) {
        this.leftRepository = leftRepository;
        this.rightRepository = rightRepository;
        this.diffService = diffService;
    }


    @PostMapping("/{id}/left")
    @ResponseStatus(HttpStatus.CREATED)
    public void putLeft(@PathVariable String id, @RequestBody ComparisonOperand comparisonOperand) {
        byte[] data =
                comparisonOperand.getData();

        leftRepository.save(id, data);
    }


    @PostMapping("/{id}/right")
    @ResponseStatus(HttpStatus.CREATED)
    public void putRight(@PathVariable String id, @RequestBody ComparisonOperand comparisonOperand) {
        byte[] data =
                comparisonOperand.getData();

        rightRepository.save(id, data);
    }


    @GetMapping("/{id}")
    public ComparisonResult compare(@PathVariable String id) {
        Optional<ComparisonResult> comparisonResultOption =
                leftRepository.find(id).flatMap(left ->
                        rightRepository.find(id).map(right ->
                                diffService.compare(left, right)
                        )
                );

        return comparisonResultOption
                .orElseThrow(DataNotFoundException::new);
    }


    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable String id) {
        leftRepository.remove(id);
        rightRepository.remove(id);

        return HttpStatus.NO_CONTENT;
    }
}
