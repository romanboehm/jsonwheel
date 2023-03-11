package com.romanboehm.jsonwheel;

import com.romanboehm.jsonwheel.JsonWheel.WheelNode;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.InstanceOfAssertFactories.MAP;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class JsonWheelTestMatrix {

    abstract List<Arguments> args();

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("args")
    void topLevel(Arg arg) {
        var actual = JsonWheel.read(arg.in);

        assertThat(actual).satisfies(arg.assertion);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("args")
    void asSingleValueInObject(Arg arg) {
        var json = """
                            {"k": %s}""".formatted(arg.in);

        var actual = JsonWheel.read(json);

        assertThat(actual.get("k")).satisfies(arg.assertion);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("args")
    void asValueInObjectStart(Arg arg) {
        var json = """
                            {
                                "k1": %s,
                                "k2": false,
                                "k3": false
                            }""".formatted(arg.in);

        var actual = JsonWheel.read(json);

        assertThat(actual).satisfies(wn -> {
            assertThat(wn.get("k1")).satisfies(arg.assertion);
            assertThat(wn.get("k2").val(Boolean.class)).isFalse();
            assertThat(wn.get("k3").val(Boolean.class)).isFalse();
        });
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("args")
    void asValueInObjectMiddle(Arg arg) {
        var json = """
                            {
                                "k1": false,
                                "k2": %s,
                                "k3": false
                            }""".formatted(arg.in);

        var actual = JsonWheel.read(json);

        assertThat(actual).satisfies(wn -> {
            assertThat(wn.get("k1").val(Boolean.class)).isFalse();
            assertThat(wn.get("k2")).satisfies(arg.assertion);
            assertThat(wn.get("k3").val(Boolean.class)).isFalse();
        });
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("args")
    void asValueInObjectEnd(Arg arg) {
        var json = """
                            {
                                "k1": false,
                                "k2": false,
                                "k3": %s
                            }""".formatted(arg.in);

        var actual = JsonWheel.read(json);

        assertThat(actual).satisfies(wn -> {
            assertThat(wn.get("k1").val(Boolean.class)).isFalse();
            assertThat(wn.get("k2").val(Boolean.class)).isFalse();
            assertThat(wn.get("k3")).satisfies(arg.assertion);
        });
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("args")
    void asSingleValueInArray(Arg arg) {
        var json = """
                            [
                                %s
                            ]""".formatted(arg.in);

        var actual = JsonWheel.read(json);

        assertThat(actual.elements()).singleElement().satisfies(arg.assertion);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("args")
    void asValueInArrayStart(Arg arg) {
        var json = """
                            [
                                %s,
                                null,
                                null
                            ]""".formatted(arg.in);

        var actual = JsonWheel.read(json);

        assertThat(actual.elements()).satisfies(elements -> {
            assertThat(elements.size()).isEqualTo(3);
            assertThat(elements.get(0)).satisfies(arg.assertion);
            assertThat(elements.get(1).val(Object.class)).isNull();
            assertThat(elements.get(2).val(Object.class)).isNull();
        });
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("args")
    void asValueInArrayEnd(Arg arg) {
        var json = """
                            [
                                null,
                                null,
                                %s
                            ]""".formatted(arg.in);

        var actual = JsonWheel.read(json);

        assertThat(actual.elements()).satisfies(elements -> {
            assertThat(elements.size()).isEqualTo(3);
            assertThat(elements.get(0).val(Object.class)).isNull();
            assertThat(elements.get(1).val(Object.class)).isNull();
            assertThat(elements.get(2)).satisfies(arg.assertion);
        });
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("args")
    void asValueInArrayMiddle(Arg arg) {
        var json = """
                            [
                                null,
                                %s,
                                null
                            ]""".formatted(arg.in);

        var actual = JsonWheel.read(json);

        assertThat(actual.elements()).satisfies(elements -> {
            assertThat(elements.size()).isEqualTo(3);
            assertThat(elements.get(0).val(Object.class)).isNull();
            assertThat(elements.get(1)).satisfies(arg.assertion);
            assertThat(elements.get(2).val(Object.class)).isNull();
        });
    }

    record Arg(String in, Consumer<? super WheelNode> assertion) {
    }
}
