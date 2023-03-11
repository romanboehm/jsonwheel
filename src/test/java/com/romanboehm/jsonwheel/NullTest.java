package com.romanboehm.jsonwheel;

import org.junit.jupiter.params.provider.Arguments;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class NullTest extends JsonWheelTestMatrix {

    @Override
    List<Arguments> args() {
        return List.of(
                arguments(named("null", new Arg("null", wn -> assertThat(wn.val(Object.class)).isNull())))
        );
    }
}
