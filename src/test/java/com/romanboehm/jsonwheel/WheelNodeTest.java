package com.romanboehm.jsonwheel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.romanboehm.jsonwheel.JsonWheel.JsonWheelException;

class WheelNodeTest {

    @Test
    void access1() {
        var json = """
                {
                    "foo": {
                        "bar": [
                            1,
                            2,
                            3
                        ],
                        "baz": "qux"
                    }
                }""";

        var node = JsonWheel.read(json);

        var bar = node.get("foo").get("bar");
        assertThat(bar.elements()).extracting(wn -> wn.val(Integer.class)).containsExactly(
                1,
                2,
                3
        );

        var baz = node.get("foo").get("baz");
        assertThat(baz.val(String.class)).isEqualTo("qux");
    }

    @Test
    void access2() {
        var json = """
                {
                    "foo": {
                        "bar": [
                            {
                                "k1": "v1"
                            },
                            {
                                "k2": "v2"
                            }
                        ]
                    }
                }""";

        var node = JsonWheel.read(json);

        var bar = node.get("foo").get("bar").elements();
        var k1 = bar.get(0).get("k1");
        assertThat(k1.val(String.class)).isEqualTo("v1");
    }

    @Test
    void wrongAccess() {
        var json = """
                {
                    "foo": "bar"
                }""";

        var node = JsonWheel.read(json);


        assertThatThrownBy(() -> node.get("foo").val(Integer.class))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void accessNull() {
        var json = """
                {
                    "foo": null
                }""";

        var node = JsonWheel.read(json);
        assertThat(node.get("foo").val(String.class)).isNull();
    }

    @Test
    void accessTopLevelNull() {
        var json = "null";
        var node = JsonWheel.read(json);
        assertThat(node.val(Object.class)).isNull();
    }

    @Test
    void accessTopLevelTrue() {
        var json = "true";
        var node = JsonWheel.read(json);
        assertThat(node.val(Boolean.class)).isTrue();
    }

    @Test
    void accessTopLevelFalse() {
        var json = "false";
        var node = JsonWheel.read(json);
        assertThat(node.val(Boolean.class)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"notnull", "fakefalse", "thetrue"})
    void accessInvalidLiteralValue(String input) {
        var json = """
                {
                    "foo": %s,
                    "bar": "ohoh"
                }""".formatted(input);

        assertThatThrownBy(() -> JsonWheel.read(json))
                .isInstanceOf(JsonWheelException.class);
    }

    @Test
    void accessBooleanValue() {
        var json = """
                {
                    "foo": true,
                    "bar": "ohoh"
                }""";

        var root = JsonWheel.read(json);
        var foo = root.get("foo");
        var bar = root.get("bar");
        assertThat(foo.val(Boolean.class)).isTrue();
        assertThat(bar.val(String.class)).isEqualTo("ohoh");
    }
}
