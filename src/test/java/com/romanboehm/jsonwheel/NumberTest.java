package com.romanboehm.jsonwheel;

import org.junit.jupiter.params.provider.Arguments;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class NumberTest extends JsonWheelTestMatrix {

    @Override
    List<Arguments> args() {
        return List.of(
                arguments(named("positive integer", new Arg("1", wn -> assertThat(wn.val(Integer.class)).isEqualTo(1)))),
                arguments(named("negative integer", new Arg("-1", wn -> assertThat(wn.val(Integer.class)).isEqualTo(-1)))),
                arguments(named("positive integer boundary", new Arg("2147483647", wn -> assertThat(wn.val(Integer.class)).isEqualTo(Integer.MAX_VALUE)))),
                arguments(named("negative integer boundary", new Arg("-2147483648", wn -> assertThat(wn.val(Integer.class)).isEqualTo(Integer.MIN_VALUE)))),
                arguments(named("positive double precision integer", new Arg("2147483648", wn -> assertThat(wn.val(Long.class)).isEqualTo(Integer.MAX_VALUE + 1L)))),
                arguments(named("negative double precision integer", new Arg("-2147483649", wn -> assertThat(wn.val(Long.class)).isEqualTo(Integer.MIN_VALUE - 1L)))),
                arguments(named("positive double precision integer boundary", new Arg("9223372036854775807", wn -> assertThat(wn.val(Long.class)).isEqualTo(Long.MAX_VALUE)))),
                arguments(named("negative double precision integer boundary", new Arg("-9223372036854775808", wn -> assertThat(wn.val(Long.class)).isEqualTo(Long.MIN_VALUE)))),
                arguments(named("positive arbitrary precision integer", new Arg("9223372036854775808", wn -> assertThat(wn.val(BigInteger.class)).isEqualTo(new BigInteger("9223372036854775808"))))), // Long.MAX_VALUE + 1
                arguments(named("negative arbitrary precision integer", new Arg("-9223372036854775809", wn -> assertThat(wn.val(BigInteger.class)).isEqualTo(new BigInteger("-9223372036854775809"))))), // Long.MIN_VALUE - 1
                arguments(named("positive decimal", new Arg("1.2", wn -> assertThat(wn.val(Double.class)).isEqualTo(1.2d)))),
                arguments(named("negative decimal", new Arg("-1.2", wn -> assertThat(wn.val(Double.class)).isEqualTo(-1.2d)))),
                arguments(named("positive decimal boundary high", new Arg("1.7976931348623157E308", wn -> assertThat(wn.val(Double.class)).isEqualTo(Double.MAX_VALUE)))),
                arguments(named("positive decimal boundary low", new Arg("4.9E-324", wn -> assertThat(wn.val(Double.class)).isEqualTo(Double.MIN_VALUE)))),
                arguments(named("positive arbitrary precision decimal high", new Arg("1.7976931348623157E309", wn -> assertThat(wn.val(BigDecimal.class)).isEqualTo(new BigDecimal("1.7976931348623157E309"))))), // Double.MAX_VALUE * 10
                arguments(named("positive arbitrary precision decimal low", new Arg("4.9E-325", wn -> assertThat(wn.val(BigDecimal.class)).isEqualTo(new BigDecimal("4.9E-325"))))), // Double.MIN_VALUE / 10
                arguments(named("negative arbitrary precision decimal high", new Arg("-1.7976931348623157E309", wn -> assertThat(wn.val(BigDecimal.class)).isEqualTo(new BigDecimal("-1.7976931348623157E309"))))), // -Double.MAX_VALUE * 10
                arguments(named("negative arbitrary precision decimal low", new Arg("-4.9E-325", wn -> assertThat(wn.val(BigDecimal.class)).isEqualTo(new BigDecimal("-4.9E-325"))))), // -Double.MIN_VALUE / 10
                arguments(named("scientific notation exponent capitalized", new Arg("5E2", wn -> assertThat(wn.val(Double.class)).isEqualTo(500d)))),
                arguments(named("scientific notation exponent non-capitalized", new Arg("5e2", wn -> assertThat(wn.val(Double.class)).isEqualTo(500d)))),
                arguments(named("scientific notation exponent with positive sign", new Arg("5e+2", wn -> assertThat(wn.val(Double.class)).isEqualTo(500d)))),
                arguments(named("scientific notation exponent with negative sign", new Arg("5e-2", wn -> assertThat(wn.val(Double.class)).isEqualTo(0.05d)))),
                arguments(named("positive integer scientific notation positive exponent", new Arg("5e2", wn -> assertThat(wn.val(Double.class)).isEqualTo(500d)))),
                arguments(named("negative integer scientific notation positive exponent", new Arg("-5e2", wn -> assertThat(wn.val(Double.class)).isEqualTo(-500d)))),
                arguments(named("positive integer scientific notation negative exponent", new Arg("5e-2", wn -> assertThat(wn.val(Double.class)).isEqualTo(0.05d)))),
                arguments(named("negative integer scientific notation negative exponent", new Arg("-5e-2", wn -> assertThat(wn.val(Double.class)).isEqualTo(-0.05d)))),
                arguments(named("positive decimal scientific notation positive exponent", new Arg("5.1e2", wn -> assertThat(wn.val(Double.class)).isEqualTo(510d)))),
                arguments(named("negative decimal scientific notation positive exponent", new Arg("-5.1e2", wn -> assertThat(wn.val(Double.class)).isEqualTo(-510d)))),
                arguments(named("positive decimal scientific notation negative exponent", new Arg("5.1e-2", wn -> assertThat(wn.val(Double.class)).isEqualTo(0.051d)))),
                arguments(named("negative decimal scientific notation negative exponent", new Arg("-5.1e-2", wn -> assertThat(wn.val(Double.class)).isEqualTo(-0.051d))))
        );
    }
}
