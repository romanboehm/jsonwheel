package com.romanboehm.jsonwheel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.InstanceOfAssertFactories.MAP;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class JsonWheelTestMatrix {

    abstract List<Arguments> args();

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("args")
    void topLevel(Arg arg) {
        var actual = JsonWheel.read(arg.in);

        assertThat(actual.inner).isEqualTo(arg.expected);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("args")
    void asSingleValueInObject(Arg arg) {
        var json = """
                {"k": %s}""".formatted(arg.in);

        var actual = JsonWheel.read(json);

        assertThat(actual.inner)
                .asInstanceOf(MAP)
                .containsExactly(
                        entry("k", arg.expected));
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

        assertThat(actual.inner)
                .asInstanceOf(MAP)
                .containsExactly(
                        entry("k1", arg.expected),
                        entry("k2", false),
                        entry("k3", false));
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

        assertThat(actual.inner)
                .asInstanceOf(MAP)
                .containsExactly(
                        entry("k1", false),
                        entry("k2", arg.expected),
                        entry("k3", false));
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

        assertThat(actual.inner)
                .asInstanceOf(MAP)
                .containsExactly(
                        entry("k1", false),
                        entry("k2", false),
                        entry("k3", arg.expected));
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("args")
    void asSingleValueInArray(Arg arg) {
        var json = """
                [
                    %s
                ]""".formatted(arg.in);

        var actual = JsonWheel.read(json);

        assertThat(actual.inner).isEqualTo(Collections.singletonList(arg.expected));
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

        assertThat(actual.inner).isEqualTo(Arrays.asList(arg.expected, null, null));
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

        assertThat(actual.inner).isEqualTo(Arrays.asList(null, null, arg.expected));
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

        assertThat(actual.inner).isEqualTo(Arrays.asList(null, arg.expected, null));
    }

    record Arg(String in, Object expected) {
    }
}
