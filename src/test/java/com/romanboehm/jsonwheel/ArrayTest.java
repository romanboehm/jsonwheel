package com.romanboehm.jsonwheel;

import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.params.provider.Arguments;

class ArrayTest extends JsonWheelTestMatrix {

    @Override
    List<Arguments> args() {
        return List.of(
                arguments(named("empty array", new Arg("[]", Collections.emptyList()))),
                arguments(named("array single string", new Arg("[\"foo\"]", List.of("foo")))),
                arguments(named("array single number", new Arg("[1.1]", List.of(1.1d)))),
                arguments(named("array single boolean", new Arg("[true]", List.of(true)))),
                arguments(named("array single null", new Arg("[null]", Collections.singletonList(null)))),
                arguments(named("array single object", new Arg("[{\"foo\": 1.1}]", List.of(Map.of("foo", 1.1d))))),
                arguments(named("array single array", new Arg("[[1.1]]", List.of(List.of(1.1d))))),
                arguments(named("array multiple strings", new Arg("[\"foo\", \"bar\"]", List.of("foo", "bar")))),
                arguments(named("array multiple numbers", new Arg("[1.1, 0.5]", List.of(1.1d, 0.5d)))),
                arguments(named("array multiple booleans", new Arg("[true, false]", List.of(true, false)))),
                arguments(named("array multiple nulls", new Arg("[null, null]", Arrays.asList(null, null)))),
                arguments(named("array multiple values with nulls", new Arg("[null, null, 1.1, 0.5]", Arrays.asList(null, null, 1.1d, 0.5d)))),
                arguments(named("array multiple objects", new Arg("[{\"foo\": 1.1}, {\"foo\": 0.5}]", List.of(Map.of("foo", 1.1d), Map.of("foo", 0.5d))))),
                arguments(named("array multiple arrays", new Arg("[[1.1, 0.5], [0.5, 1.1]]", List.of(List.of(1.1d, 0.5), List.of(0.5, 1.1))))));
    }
}
