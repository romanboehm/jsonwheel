package com.romanboehm.jsonwheel;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;

import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class StringTest extends JsonWheelTestMatrix {

    @Override
    List<Arguments> args() {
        return List.of(
                arguments(named("simple string", new Arg("\"hello, world\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("hello, world")))),
                arguments(named("empty string", new Arg("\"\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("")))),
                arguments(named("whitespace", new Arg("\" \"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo(" ")))),
                arguments(named("quotes", new Arg("\"quo\\\"tes\\\"\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("quo\"tes\"")))),
                arguments(named("just quote", new Arg("\"\\\"\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("\"")))),
                arguments(named("colon", new Arg("\"co:lon\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("co:lon")))),
                arguments(named("comma", new Arg("\"com,ma\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("com,ma")))),
                arguments(named("opening brace", new Arg("\"bra{ce\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("bra{ce")))),
                arguments(named("closing brace", new Arg("\"bra}ce\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("bra}ce")))),
                arguments(named("opening bracket", new Arg("\"bra[cket\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("bra[cket")))),
                arguments(named("closing bracket", new Arg("\"bra]cket\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("bra]cket")))),
                arguments(named("solidus", new Arg("\"soli\\/dus\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("soli/dus")))),
                arguments(named("reverse solidus", new Arg("\"reverse soli\\\\dus\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("reverse soli\\dus")))),
                arguments(named("just reverse solidus", new Arg("\"\\\\\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("\\")))),
                arguments(named("horizontal tab", new Arg("\"horizontal\\ttab\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("horizontal\ttab")))),
                arguments(named("carriage return", new Arg("\"carriage\\rreturn\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("carriage\rreturn")))),
                arguments(named("line feed", new Arg("\"line\\nfeed\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("line\nfeed")))),
                arguments(named("form feed", new Arg("\"form\\ffeed\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("form\ffeed")))),
                arguments(named("backspace", new Arg("\"back\\bspace\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("back\bspace")))),
                arguments(named("all controls", new Arg("\"\\b\\f\\n\\r\\t\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("\b\f\n\r\t")))),
                arguments(named("non-ASCII 1", new Arg("\"Straße\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("Straße")))),
                arguments(named("non-ASCII 2", new Arg("\"自由\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("自由")))),
                arguments(named("non-ASCII 3", new Arg("\"🧪\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("🧪")))),
                arguments(named("escaped non-ASCII 1", new Arg("\"Stra\\u00dfe\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("Straße")))),
                arguments(named("escaped non-ASCII 2", new Arg("\"\\u81ea\\u7531\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("自由")))),
                arguments(named("escaped non-ASCII 3", new Arg("\"\\uD83E\\uDDEA\"", wn -> Assertions.assertThat(wn.val(String.class)).isEqualTo("🧪"))))
        );
    }
}
