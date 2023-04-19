package com.romanboehm.jsonwheel;

import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;

import org.junit.jupiter.params.provider.Arguments;

class BooleanTest extends JsonWheelTestMatrix {

    @Override
    List<Arguments> args() {
        return List.of(
                arguments(named("true", new Arg("true", true))),
                arguments(named("false", new Arg("false", false))));
    }
}
