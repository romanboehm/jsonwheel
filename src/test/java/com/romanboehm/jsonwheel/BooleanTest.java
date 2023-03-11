package com.romanboehm.jsonwheel;

import org.junit.jupiter.params.provider.Arguments;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class BooleanTest extends JsonWheelTestMatrix {

    @Override
    List<Arguments> args() {
        return List.of(
                arguments(named("true", new Arg("true", wn -> assertThat(wn.val(Boolean.class)).isTrue()))),
                arguments(named("false", new Arg("false", wn -> assertThat(wn.val(Boolean.class)).isFalse())))
        );
    }
}
