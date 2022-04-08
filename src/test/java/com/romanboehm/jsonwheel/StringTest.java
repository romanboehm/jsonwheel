package com.romanboehm.jsonwheel;

import org.junit.jupiter.params.provider.Arguments;

import java.util.List;

import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class StringTest extends JsonWheelTestMatrix {

    @Override
    List<Arguments> args() {
        return List.of(
                arguments(named("simple string", new Arg("\"hello, world\"", "hello, world"))),
                arguments(named("empty string", new Arg("\"\"", ""))),
                arguments(named("whitespace", new Arg("\" \"", " "))),
                arguments(named("quotes", new Arg("\"quo\\\"tes\\\"\"", "quo\"tes\""))),
                arguments(named("just quote", new Arg("\"\\\"\"", "\""))),
                arguments(named("colon", new Arg("\"co:lon\"", "co:lon"))),
                arguments(named("comma", new Arg("\"com,ma\"", "com,ma"))),
                arguments(named("opening brace", new Arg("\"bra{ce\"", "bra{ce"))),
                arguments(named("closing brace", new Arg("\"bra}ce\"", "bra}ce"))),
                arguments(named("opening bracket", new Arg("\"bra[cket\"", "bra[cket"))),
                arguments(named("closing bracket", new Arg("\"bra]cket\"", "bra]cket"))),
                arguments(named("solidus", new Arg("\"soli\\/dus\"", "soli/dus"))),
                arguments(named("reverse solidus", new Arg("\"reverse soli\\\\dus\"", "reverse soli\\dus"))),
                arguments(named("just reverse solidus", new Arg("\"\\\\\"", "\\"))),
                arguments(named("horizontal tab", new Arg("\"horizontal\\ttab\"", "horizontal\ttab"))),
                arguments(named("carriage return", new Arg("\"carriage\\rreturn\"", "carriage\rreturn"))),
                arguments(named("line feed", new Arg("\"line\\nfeed\"", "line\nfeed"))),
                arguments(named("form feed", new Arg("\"form\\ffeed\"", "form\ffeed"))),
                arguments(named("backspace", new Arg("\"back\\bspace\"", "back\bspace"))),
                arguments(named("all controls", new Arg("\"\\b\\f\\n\\r\\t\"", "\b\f\n\r\t"))),
                arguments(named("non-ASCII 1", new Arg("\"Straße\"", "Straße"))),
                arguments(named("non-ASCII 2", new Arg("\"自由\"", "自由"))),
                arguments(named("non-ASCII 3", new Arg("\"🧪\"", "🧪"))),
                arguments(named("escaped non-ASCII 1", new Arg("\"Stra\\u00dfe\"", "Straße"))),
                arguments(named("escaped non-ASCII 2", new Arg("\"\\u81ea\\u7531\"", "自由"))),
                arguments(named("escaped non-ASCII 3", new Arg("\"\\uD83E\\uDDEA\"", "🧪")))
        );
    }
}
