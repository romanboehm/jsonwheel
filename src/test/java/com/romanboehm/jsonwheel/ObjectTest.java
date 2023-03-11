package com.romanboehm.jsonwheel;

import com.romanboehm.jsonwheel.JsonWheel.WheelNode;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ObjectTest extends JsonWheelTestMatrix {

    @Override
    List<Arguments> args() {
        return List.of(
                            arguments(named(
                                                "empty object",
                                                new Arg(
                                                                    "{}",
                                                                    wn -> assertThat(wn.val(Object.class)).isNotNull()
                                                )
                            )),
                            arguments(named(
                                                "object single kv string",
                                                new Arg(
                                                                    "{\"foo\": \"bar\"}",
                                                                    wn -> assertThat(wn.get("foo").val(String.class)).isEqualTo("bar")
                                                )
                            )),
                            arguments(named(
                                                "object single kv number",
                                                new Arg(
                                                                    "{\"foo\": 1.1}",
                                                                    wn -> assertThat(wn.get("foo").val(Double.class)).isEqualTo(1.1d)
                                                )
                            )),
                            arguments(named(
                                                "object single kv boolean",
                                                new Arg(
                                                                    "{\"foo\": true}",
                                                                    wn -> assertThat(wn.get("foo").val(Boolean.class)).isTrue()
                                                )
                            )),
                            arguments(named(
                                                "object single kv null",
                                                new Arg(
                                                                    "{\"foo\": null}",
                                                                    wn -> assertThat(wn.get("foo").val(Object.class)).isNull()
                                                )
                            )),
                            arguments(named(
                                                "object single kv array",
                                                new Arg(
                                                                    "{\"foo\": [1.1]}",
                                                                    wn -> assertThat(wn.get("foo").elements())
                                                                                        .asList()
                                                                                        .singleElement()
                                                                                        .isInstanceOfSatisfying(WheelNode.class, iwn ->
                                                                                                            assertThat(iwn.val(Double.class)).isEqualTo(1.1d)
                                                                                        )
                                                )
                            )),
                            arguments(named(
                                                "object single kv object",
                                                new Arg(
                                                                    "{\"foo\": {\"bar\": 1.1}}",
                                                                    wn -> assertThat(wn.get("foo").get("bar").val(Double.class)).isEqualTo(1.1d)
                                                )
                            )),
                            arguments(named(
                                                "object multiple kv",
                                                new Arg("{\"foo\": \"bar\", \"baz\": \"qux\", \"blerg\": 0.5}",
                                                                    wn -> {
                                                                        assertThat(wn.get("foo").val(String.class)).isEqualTo("bar");
                                                                        assertThat(wn.get("baz").val(String.class)).isEqualTo("qux");
                                                                        assertThat(wn.get("blerg").val(Double.class)).isEqualTo(0.5d);
                                                                    }
                                                )
                            ))
        );
    }
}