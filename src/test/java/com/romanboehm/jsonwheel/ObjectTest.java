package com.romanboehm.jsonwheel;

import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.params.provider.Arguments;

class ObjectTest extends JsonWheelTestMatrix {

    @Override
    List<Arguments> args() {
        return List.of(
                arguments(named("empty object", new Arg("{}", Collections.emptyMap()))),
                arguments(named("object single kv string", new Arg("{\"foo\": \"bar\"}", Map.of("foo", "bar")))),
                arguments(named("object single kv number", new Arg("{\"foo\": 1.1}", Map.of("foo", 1.1d)))),
                arguments(named("object single kv boolean", new Arg("{\"foo\": true}", Map.of("foo", true)))),
                arguments(named("object single kv null", new Arg("{\"foo\": null}", new HashMap<>() {
                    {
                        put("foo", null);
                    }
                }))),
                arguments(named("object single kv array", new Arg("{\"foo\": [1.1]}", Map.of("foo", List.of(1.1d))))),
                arguments(named("object single kv object", new Arg("{\"foo\": {\"bar\": 1.1}}", Map.of("foo", Map.of("bar", 1.1d))))),
                arguments(
                        named("object multiple kv", new Arg("{\"foo\": \"bar\", \"baz\": \"qux\", \"blerg\": 0.5}", Map.of("foo", "bar", "baz", "qux", "blerg", 0.5d)))));
    }
}
