package com.romanboehm.jsonwheel;

import org.junit.jupiter.params.provider.Arguments;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ArrayTest extends JsonWheelTestMatrix {

    @Override
    List<Arguments> args() {
        return List.of(
                            arguments(named(
                                                "empty array",
                                                new Arg(
                                                                    "[]",
                                                                    lwn -> assertThat(lwn.elements()).isEmpty()
                                                )
                            )),
                            arguments(named(
                                                "array single string",
                                                new Arg(
                                                                    "[\"foo\"]",
                                                                    lwn -> assertThat(lwn.elements()).singleElement()
                                                                                                     .satisfies(wn -> assertThat(wn.val(String.class)).isEqualTo("foo"))
                                                )
                            )),
                            arguments(named(
                                                "array single number",
                                                new Arg(
                                                                    "[1.1]",
                                                                    lwn -> assertThat(lwn.elements()).singleElement()
                                                                                                     .satisfies(wn -> assertThat(wn.val(Double.class)).isEqualTo(1.1d))
                                                )
                            )),
                            arguments(named(
                                                "array single boolean",
                                                new Arg(
                                                                    "[true]",
                                                                    lwn -> assertThat(lwn.elements()).singleElement()
                                                                                                     .satisfies(wn -> assertThat(wn.val(Boolean.class)).isEqualTo(true))
                                                )
                            )),
                            arguments(named(
                                                "array single null",
                                                new Arg(
                                                                    "[null]",
                                                                    lwn -> assertThat(lwn.elements()).singleElement()
                                                                                                     .satisfies(wn -> assertThat(wn.val(Object.class)).isNull())
                                                )
                            )),
                            arguments(named(
                                                "array single object",
                                                new Arg(
                                                                    "[{\"foo\": 1.1}]",
                                                                    lwn -> assertThat(lwn.elements()).singleElement().satisfies(wn -> assertThat(
                                                                                        wn.get("foo").val(Double.class)).isEqualTo(1.1d))
                                                )
                            )),
                            arguments(named(
                                                "array single array",
                                                new Arg(
                                                                    "[[1.1]]",
                                                                    lwn -> assertThat(lwn.elements()).singleElement()
                                                                                                     .satisfies(wn -> assertThat(wn.elements()).singleElement()
                                                                                                                                               .satisfies(wni -> assertThat(
                                                                                                                                                                   wni.val(Double.class)).isEqualTo(
                                                                                                                                                                   1.1d)))
                                                )
                            )),
                            arguments(named(
                                                "array multiple strings",
                                                new Arg(
                                                                    "[\"foo\", \"bar\"]",
                                                                    lwn -> assertThat(lwn.elements()).satisfies(l -> {
                                                                        assertThat(l.size()).isEqualTo(2);
                                                                        assertThat(l.get(0).val(String.class)).isEqualTo("foo");
                                                                        assertThat(l.get(1).val(String.class)).isEqualTo("bar");
                                                                    })
                                                )
                            )),
                            arguments(named(
                                                "array multiple numbers",
                                                new Arg(
                                                                    "[1.1, 0.5]",
                                                                    lwn -> assertThat(lwn.elements()).satisfies(l -> {
                                                                        assertThat(l.size()).isEqualTo(2);
                                                                        assertThat(l.get(0).val(Double.class)).isEqualTo(1.1d);
                                                                        assertThat(l.get(1).val(Double.class)).isEqualTo(0.5d);
                                                                    })
                                                )
                            )),
                            arguments(named(
                                                "array multiple booleans",
                                                new Arg(
                                                                    "[true, false]",
                                                                    lwn -> assertThat(lwn.elements()).satisfies(l -> {
                                                                        assertThat(l.size()).isEqualTo(2);
                                                                        assertThat(l.get(0).val(Boolean.class)).isEqualTo(true);
                                                                        assertThat(l.get(1).val(Boolean.class)).isEqualTo(false);
                                                                    })
                                                )
                            )),
                            arguments(named(
                                                "array multiple nulls",
                                                new Arg(
                                                                    "[null, null]",
                                                                    lwn -> assertThat(lwn.elements()).satisfies(l -> {
                                                                        assertThat(l.size()).isEqualTo(2);
                                                                        assertThat(l.get(0).val(Object.class)).isNull();
                                                                        assertThat(l.get(1).val(Object.class)).isNull();
                                                                    })
                                                )
                            )),
                            arguments(named(
                                                "array multiple values with nulls",
                                                new Arg(
                                                                    "[null, null, 1.1, 0.5]",
                                                                    lwn -> assertThat(lwn.elements()).satisfies(l -> {
                                                                        assertThat(l.size()).isEqualTo(4);
                                                                        assertThat(l.get(0).val(Object.class)).isNull();
                                                                        assertThat(l.get(1).val(Object.class)).isNull();
                                                                        assertThat(l.get(2).val(Double.class)).isEqualTo(1.1d);
                                                                        assertThat(l.get(3).val(Double.class)).isEqualTo(0.5d);
                                                                    })
                                                )
                            )),
                            arguments(named(
                                                "array multiple objects",
                                                new Arg(
                                                                    "[{\"foo\": 1.1}, {\"foo\": 0.5}]",
                                                                    lwn -> assertThat(lwn.elements()).satisfies(l -> {
                                                                        assertThat(l.size()).isEqualTo(2);
                                                                        assertThat(l.get(0).get("foo").val(Double.class)).isEqualTo(1.1d);
                                                                        assertThat(l.get(1).get("foo").val(Double.class)).isEqualTo(0.5d);
                                                                    })
                                                )
                            )),
                            arguments(named(
                                                "array multiple arrays",
                                                new Arg(
                                                                    "[[1.1, 0.5], [0.5, 1.1]]",
                                                                    lwn -> assertThat(lwn.elements()).satisfies(l -> {
                                                                        assertThat(l.size()).isEqualTo(2);
                                                                        assertThat(l.get(0).elements()).satisfies(i -> {
                                                                            assertThat(i.size()).isEqualTo(2);
                                                                            assertThat(i.get(0).val(Double.class)).isEqualTo(1.1d);
                                                                            assertThat(i.get(1).val(Double.class)).isEqualTo(0.5d);
                                                                        });
                                                                        assertThat(l.get(1).elements()).satisfies(i -> {
                                                                            assertThat(i.size()).isEqualTo(2);
                                                                            assertThat(i.get(0).val(Double.class)).isEqualTo(0.5d);
                                                                            assertThat(i.get(1).val(Double.class)).isEqualTo(1.1d);
                                                                        });
                                                                    })
                                                )
                            ))
        );
    }
}