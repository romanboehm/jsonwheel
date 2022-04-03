package com.romanboehm.jsonwheel;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.MAP;

class JsonWheelTest {

    @Test
    void nullTopLevel() {
        var json = """
                null
                """;

        var node = JsonWheel.read(json);

        assertThat(node.inner).isNull();
    }

    @Test
    void stringTopLevel() {
        var json = """
                "foo"
                """;

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo("foo");
    }

    @Test
    void trueTopLevel() {
        var json = """
                true
                """;

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(TRUE);
    }

    @Test
    void falseTopLevel() {
        var json = """
                false
                """;

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(FALSE);
    }

    @Test
    void integerTopLevel() {
        var json = """
                1
                """;

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(1);
    }

    @Test
    void decimalTopLevel() {
        var json = """
                1.0
                """;

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(1.0d);
    }

    @Test
    void nullValue() {
        var json = """
                {
                    "k": null
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                new HashMap<>() {{
                    put("k", null);
                }}
        );
    }

    @Test
    void intValue() {
        var json = """
                {
                    "k": 1
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", 1)
        );
    }

    @Test
    void negativeIntValue() {
        var json = """
                {
                    "k": -1
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", -1)
        );
    }

    @Test
    void intWithExponentValueAsDouble1() {
        var json = """
                {
                    "k1": 5e2,
                    "k2": 5E2,
                    "k3": 5e+2,
                    "k4": 5E+2
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner)
                .isEqualTo(
                        Map.of(
                                "k1", 5e2d,
                                "k2", 5E2d,
                                "k3", 5e+2d,
                                "k4", 5E+2d
                        )
                )
                .asInstanceOf(MAP).allSatisfy((k, v) -> assertThat(v).isEqualTo(500d));
    }

    @Test
    void negativeIntWithExponentValueAsDouble1() {
        var json = """
                {
                    "k1": -5e2,
                    "k2": -5E2,
                    "k3": -5e+2,
                    "k4": -5E+2
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner)
                .isEqualTo(
                        Map.of(
                                "k1", -5e2d,
                                "k2", -5E2d,
                                "k3", -5e+2d,
                                "k4", -5E+2d
                        )
                )
                .asInstanceOf(MAP).allSatisfy((k, v) -> assertThat(v).isEqualTo(-500d));
    }

    @Test
    void integerPositiveSinglePrecisionBoundary() {
        long number = Integer.MAX_VALUE;

        var actual = JsonWheel.read(String.valueOf(number)).val(Integer.class);

        assertThat(actual).isEqualTo(number);
    }

    @Test
    void integerNegativeSinglePrecisionBoundary() {
        long number = Integer.MIN_VALUE;

        var actual = JsonWheel.read(String.valueOf(number)).val(Integer.class);

        assertThat(actual).isEqualTo(number);
    }

    @Test
    void integerPositiveDoublePrecisionBoundary() {
        long number = Long.MAX_VALUE;

        var actual = JsonWheel.read(String.valueOf(number)).val(Long.class);

        assertThat(actual).isEqualTo(number);
    }

    @Test
    void integerNegativeDoublePrecisionBoundary() {
        long number = Long.MIN_VALUE;

        var actual = JsonWheel.read(String.valueOf(number)).val(Long.class);

        assertThat(actual).isEqualTo(number);
    }

    @Test
    void integerOverflowLargePositiveToDoublePrecision() {
        long number = Integer.MAX_VALUE + 1L;

        var actual = JsonWheel.read(String.valueOf(number)).val(Long.class);

        assertThat(actual).isEqualTo(number);
    }

    @Test
    void integerOverflowLargePositiveToArbitraryPrecision() {
        BigInteger number = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.valueOf(1));

        var actual = JsonWheel.read(number.toString()).val(BigInteger.class);

        assertThat(actual).isEqualTo(number);
    }

    @Test
    void integerUnderflowLargeNegativeToArbitraryPrecision() {
        BigInteger number = BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.valueOf(1));

        var actual = JsonWheel.read(number.toString()).val(BigInteger.class);

        assertThat(actual).isEqualTo(number);
    }

    @Test
    void decimalValue() {
        var json = """
                {
                    "k": 1.1
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", 1.1d)
        );
    }

    @Test
    void negativeDecimalValue() {
        var json = """
                {
                    "k": -1.1
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", -1.1d)
        );
    }

    @Test
    void decimalWithExponentValueAsDouble1() {
        var json = """
                {
                    "k1": 5.1e2,
                    "k2": 5.1E2,
                    "k3": 5.1e+2,
                    "k4": 5.1E+2
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner)
                .isEqualTo(
                        Map.of(
                                "k1", 5.1e2d,
                                "k2", 5.1E2d,
                                "k3", 5.1e+2d,
                                "k4", 5.1E+2d
                        )
                )
                .asInstanceOf(MAP).allSatisfy((k, v) -> assertThat(v).isEqualTo(510d));
    }

    @Test
    void decimalWithExponentValueAsDouble2() {
        var json = """
                {
                    "k1": 5.1e-2,
                    "k2": 5.1E-2
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner)
                .isEqualTo(
                        Map.of(
                                "k1", 5.1e-2d,
                                "k2", 5.1E-2d
                        )
                )
                .asInstanceOf(MAP).allSatisfy((k, v) -> assertThat(v).isEqualTo(0.051d));
    }

    @Test
    void negativeDecimalWithExponentValueAsDouble() {
        var json = """
                {
                    "k1": -5.1e2,
                    "k2": -5.1E2,
                    "k3": -5.1e+2,
                    "k4": -5.1E+2
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner)
                .isEqualTo(
                        Map.of(
                                "k1", -5.1e2d,
                                "k2", -5.1E2d,
                                "k3", -5.1e+2d,
                                "k4", -5.1E+2d
                        )
                )
                .asInstanceOf(MAP).allSatisfy((k, v) -> assertThat(v).isEqualTo(-510d));
    }

    @Test
    void decimalOverflowLargePositive() {
        BigDecimal number = BigDecimal.valueOf(Double.MAX_VALUE).add(BigDecimal.valueOf(0.5d));

        var actual = JsonWheel.read(number.toString()).val(BigDecimal.class);

        assertThat(actual).isEqualTo(number);
    }

    @Test
    void decimalOverflowLargeNegative() {
        BigDecimal number = BigDecimal.valueOf(-Double.MAX_VALUE).subtract(BigDecimal.valueOf(0.5d));

        var actual = JsonWheel.read(number.toString()).val(BigDecimal.class);

        assertThat(actual).isEqualTo(number);
    }

    @Test
    void decimalUnderflowSmallPositive() {
        BigDecimal number = BigDecimal.valueOf(Double.MIN_VALUE).divide(BigDecimal.valueOf(2d), RoundingMode.HALF_UP);

        var actual = JsonWheel.read(number.toString()).val(BigDecimal.class);

        assertThat(actual).isEqualTo(number);
    }

    @Test
    void decimalUnderflowSmallNegative() {
        BigDecimal number = BigDecimal.valueOf(-Double.MIN_VALUE).divide(BigDecimal.valueOf(2d), RoundingMode.HALF_UP);

        var actual = JsonWheel.read(number.toString()).val(BigDecimal.class);

        assertThat(actual).isEqualTo(number);
    }

    @Test
    void doublePositiveDoublePrecisionBoundaryLarge() {
        double number = Double.MAX_VALUE;

        var actual = JsonWheel.read(String.valueOf(number)).val(Double.class);

        assertThat(actual).isEqualTo(number);
    }

    @Test
    void doubleNegativeDoublePrecisionBoundaryLarge() {
        double number = -Double.MAX_VALUE;

        var actual = JsonWheel.read(String.valueOf(number)).val(Double.class);

        assertThat(actual).isEqualTo(number);
    }

    @Test
    void doublePositiveDoublePrecisionBoundarySmall() {
        double number = Double.MIN_VALUE;

        var actual = JsonWheel.read(String.valueOf(number)).val(Double.class);

        assertThat(actual).isEqualTo(number);
    }

    @Test
    void doubleNegativeDoublePrecisionBoundarySmall() {
        double number = -Double.MIN_VALUE;

        var actual = JsonWheel.read(String.valueOf(number)).val(Double.class);

        assertThat(actual).isEqualTo(number);
    }

    @Test
    void falseValue() {
        var json = """
                {
                    "k": false
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", false)
        );
    }

    @Test
    void trueValue() {
        var json = """
                {
                    "k": true
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", true)
        );
    }

    @Test
    void stringValue() {
        var json = """
                {
                    "k": "1"
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", "1")
        );
    }

    @Test
    void emptyStringValue() {
        var json = """
                {
                    "k": ""
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", "")
        );
    }

    @Test
    void stringValueQuotes() {
        var json = """
                {
                    "k": "quo\\"tes\\""
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", "quo\"tes\"")
        );
    }

    @Test
    void stringValueColon() {
        var json = """
                {
                    "k": "co:lon"
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", "co:lon")
        );
    }

    @Test
    void stringValueComma() {
        var json = """
                {
                    "k": "com,ma"
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", "com,ma")
        );
    }

    @Test
    void stringValueOpeningBrace() {
        var json = """
                {
                    "k": "bra{ce"
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", "bra{ce")
        );
    }

    @Test
    void stringValueClosingBrace() {
        var json = """
                {
                    "k": "bra}ce"
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", "bra}ce")
        );
    }

    @Test
    void stringValueOpeningBracket() {
        var json = """
                {
                    "k": "bra[cket"
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", "bra[cket")
        );
    }

    @Test
    void stringValueClosingBracket() {
        var json = """
                {
                    "k": "bra]cket"
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", "bra]cket")
        );
    }

    @Test
    void stringValueSolidus() {
        var json = """
                {
                    "k": "soli\\/dus"
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", "soli/dus")
        );
    }

    @Test
    void stringValueReverseSolidus() {
        var json = """
                {
                    "k": "reverse\\\\solidus"
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", "reverse\\solidus")
        );
    }

    @Test
    void stringValueHorizontalTab() {
        var json = """
                {
                    "k": "horizontal\\ttab"
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", "horizontal\ttab")
        );
    }

    @Test
    void stringValueCarriageReturn() {
        var json = """
                {
                    "k": "carriage\\rreturn"
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", "carriage\rreturn")
        );
    }

    @Test
    void stringValueBackspace() {
        var json = """
                {
                    "k": "back\\bspace"
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", "bacspace")
        );
    }

    @Test
    void stringValueLinefeed() {
        var json = """
                {
                    "k": "line\\nfeed"
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", "line\nfeed")
        );
    }

    @Test
    void stringValueFormFeed() {
        var json = """
                {
                    "k": "form\\ffeed"
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", "form\ffeed")
        );
    }

    @Test
    void stringValueNonAscii() {
        var json = """
                {
                    "k": "Straße"
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", "Straße")
        );
    }

    @Test
    void stringValueEscapedNonAscii() {
        var json = """
                {
                    "k": "Stra\\u00dfe"
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("k", "Straße")
        );
    }

    @Test
    void stringValueCodepoints1() {
        var json = """
                {
                    "k1": "\\u81ea\\u7531",
                    "k2": "自由"
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).asInstanceOf(MAP).allSatisfy((k, v) -> assertThat(v).isEqualTo("自由"));
    }

    @Test
    void stringValueCodepoints2() {
        var json = """
                {
                    "k1": "\\uD83E\\uDDEA",
                    "k2": "🧪"
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).asInstanceOf(MAP).allSatisfy((k, v) -> assertThat(v).isEqualTo("🧪"));
    }

    @Test
    void multipleValues() {
        var json = """
                {
                    "k1": "1",
                    "k2": 2,
                    "k3": 3.3
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of(
                        "k1", "1",
                        "k2", 2,
                        "k3", 3.3
                )
        );
    }

    @Test
    void emptyObject() {
        var json = """
                { }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isInstanceOfSatisfying(Map.class, m -> assertThat(m).isEmpty());
    }

    @Test
    void nestedObject() {
        var json = """
                {
                    "outerkey": {
                        "innerkey": "innervalue"
                    }
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("outerkey", Map.of(
                        "innerkey", "innervalue"
                ))
        );
    }

    @Test
    void nestedEmptyObject() {
        var json = """
                {
                    "outerkey": {
                    }
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of("outerkey", Map.of(

                ))
        );
    }

    @Test
    void nestedObjects1() {
        var json = """
                {
                    "outerkey": {
                        "innerkey1": {
                            "k": "v"
                        },
                        "innerkey2": "innervalue2"
                    }
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of(
                        "outerkey", Map.of(
                                "innerkey1", Map.of(
                                        "k", "v"
                                ),
                                "innerkey2", "innervalue2"
                        )
                )
        );
    }

    @Test
    void nestedObjects2() {
        var json = """
                {
                    "outerkey": {
                        "innerkey1": "innervalue1",
                        "innerkey2": {
                            "k": "v"
                        }
                    }
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of(
                        "outerkey", Map.of(
                                "innerkey1", "innervalue1",
                                "innerkey2", Map.of(
                                        "k", "v"
                                )
                        )
                )
        );
    }

    @Test
    void nestedObjects3() {
        var json = """
                {
                    "outerkey1": {
                        "innerkey1": "innervalue1"
                    },
                    "outerkey2": {
                        "innerkey2": "innervalue2"
                    }
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of(
                        "outerkey1", Map.of(
                                "innerkey1", "innervalue1"
                        ),
                        "outerkey2", Map.of(
                                "innerkey2", "innervalue2"
                        )
                )
        );
    }

    @Test
    void emptyArray() {
        var json = """
                [ ]""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isInstanceOfSatisfying(List.class, l -> assertThat(l).isEmpty());
    }

    @Test
    void intArray() {
        var json = """
                [
                    1,
                    2,
                    3
                ]""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                List.of(
                        1,
                        2,
                        3
                )
        );
    }

    @Test
    void booleanArray() {
        var json = """
                [
                    false,
                    true,
                    false
                ]""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                List.of(
                        false,
                        true,
                        false
                )
        );
    }

    @Test
    void objectArray() {
        var json = """
                [
                    {
                        "k1": "v1"
                    },
                    {
                        "k2": "v2"
                    },
                    {
                        "k3": "v3"
                    }
                ]""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                List.of(
                        Map.of(
                                "k1", "v1"
                        ),
                        Map.of(
                                "k2", "v2"
                        ),
                        Map.of(
                                "k3", "v3"
                        )
                )
        );
    }

    @Test
    void nestedArray() {
        var json = """
                [
                    1,
                    2,
                    [
                        "1",
                        "2"
                    ],
                    [
                        {
                            "k": "v"
                        }
                    ]
                ]""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                List.of(
                        1,
                        2,
                        List.of(
                                "1",
                                "2"
                        ),
                        List.of(
                                Map.of(
                                        "k", "v"
                                )
                        )
                )
        );
    }

    @Test
    void nestedEmptyArray() {
        var json = """
                [
                    1,
                    2,
                    [

                    ]
                ]""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                List.of(
                        1,
                        2,
                        List.of(
                        )
                )
        );
    }

    @Test
    void complex() {
        var json = """
                {
                    "k1": "1",
                    "k2": 2,
                    "k3": [
                        false,
                        true,
                        false
                    ],
                    "k4": 3.3,
                    "k5": -3.3,
                    "k6": {
                        "innerkey1": "innervalue1",
                        "innerkey2": "inner\\"value2",
                        "innerkey3": "inner:value3",
                        "innerkey4": "inner{value}4",
                        "innerkey5": "inner[value]5",
                        "innerkey6": "inner,value6",
                        "innerkey7": "",
                        "innerkey8": null
                    },
                    "k7": [
                        1,
                        -2,
                        3.3
                    ]
                }""";

        var node = JsonWheel.read(json);

        assertThat(node.inner).isEqualTo(
                Map.of(
                        "k1", "1",
                        "k2", 2,
                        "k3", List.of(
                                false,
                                true,
                                false
                        ),
                        "k4", 3.3,
                        "k5", -3.3,
                        "k6", new HashMap<>() {{
                            put("innerkey1", "innervalue1");
                            put("innerkey2", "inner\"value2");
                            put("innerkey3", "inner:value3");
                            put("innerkey4", "inner{value}4");
                            put("innerkey5", "inner[value]5");
                            put("innerkey6", "inner,value6");
                            put("innerkey7", "");
                            put("innerkey8", null);
                        }},
                        "k7", List.of(
                                1,
                                -2,
                                3.3d
                        )
                )
        );
    }

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
    void accessTopLevel() {
        var json = """
                true""";

        var node = JsonWheel.read(json);
        assertThat(node.val(Boolean.class)).isTrue();
    }
}
