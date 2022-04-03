import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Copyright (c) 2022 Roman Böhm. Subject to the Apache License 2.0.
 * See also https://github.com/rmnbhm/jsonwheel.
 */
class JsonWheel {

    static class JsonWheelException extends RuntimeException {
        JsonWheelException(String msg) {
            super(msg);
        }
    }

    static class WheelNode {
        Object inner;

        WheelNode setInner(Object inner) {
            this.inner = inner;
            return this;
        }

        List<WheelNode> elements() {
            List<WheelNode> list = new ArrayList<>();
            for (Object o : ((List<Object>) inner)) {
                list.add(new WheelNode().setInner(o));
            }
            return list;
        }

        WheelNode get(String key) {
            return new WheelNode().setInner(((Map<String, Object>) inner).get(key));
        }

        <T> T val(Class<T> clazz) {
            return inner == null ? null : clazz.cast(inner);
        }
    }

    static class Deserializer {
        private static final List<Character> NUMBER_CHARS = Arrays.asList('+', '-', '.', 'e', 'E');
        private static final Map<Character, Character> ESCAPE_LOOKUP = new HashMap<>();

        static {
            ESCAPE_LOOKUP.put('n', '\n');
            ESCAPE_LOOKUP.put('t', '\t');
            ESCAPE_LOOKUP.put('f', '\f');
            ESCAPE_LOOKUP.put('r', '\r');
            ESCAPE_LOOKUP.put('/', '/');
            ESCAPE_LOOKUP.put('\\', '\\');
            ESCAPE_LOOKUP.put('"', '"');
        }

        private final char[] chars;

        Deserializer(char[] chars) {
            this.chars = chars;
        }

        WheelNode readInternal() {
            WheelNode wheelNode = new WheelNode();
            readValue(o -> wheelNode.setInner(o), 0);
            return wheelNode;
        }

        private int readValue(Consumer<Object> valueConsumer, int from) {
            switch (chars[from]) {
                case '{':
                    Map<String, Object> map = new HashMap<>();
                    valueConsumer.accept(map);
                    return readObjectValue(map, from);
                case '[':
                    List<Object> list = new ArrayList<>();
                    valueConsumer.accept(list);
                    return readArrayValue(list, from);
                case '"':
                    int closingQuote = next('"', from + 1);
                    valueConsumer.accept(buildString(from + 1, closingQuote - 1));
                    return closingQuote;
                case 'n':
                    valueConsumer.accept(null);
                    return from + 3;
                case 't':
                    valueConsumer.accept(true);
                    return from + 3;
                case 'f':
                    valueConsumer.accept(false);
                    return from + 4;
                default:
                    int numberEnd = readNumber(from);
                    valueConsumer.accept(parseNumber(from, numberEnd));
                    return numberEnd;
            }
        }

        private Number parseNumber(int from, int to) {
            String n = buildString(from, to);
            try {
                if (n.contains(".") || n.toLowerCase().contains("e")) {
                    BigDecimal bd = new BigDecimal(n);
                    if (bd.compareTo(BigDecimal.valueOf(bd.doubleValue())) == 0) { // n within 64 bit precision?
                        return Double.parseDouble(n);
                    }
                    return bd; // Use arbitrary precision
                }
                BigInteger bi = new BigInteger(n);
                if (bi.compareTo(BigInteger.valueOf(bi.intValue())) == 0) { // n within 32 bit precision?
                    return Integer.parseInt(n);
                }
                if (bi.compareTo(BigInteger.valueOf(bi.longValue())) == 0) { // n within 64 bit precision?
                    return Long.parseLong(n);
                }
                return bi; // Use arbitrary precision
            } catch (NumberFormatException ignored) {
                throw new JsonWheelException("Invalid n literal at " + from + ": " + n);
            }
        }

        private int readObjectValue(Map<String, Object> map, int from) {
            int next = next(from + 1);

            // Check if empty object literal.
            if (chars[next] == '}') {
                return next;
            }

            // Consume object literal's fields.
            int delim = from;
            do {
                int keyStart = next('"', delim) + 1;
                int keyEnd = next('"', keyStart) - 1;
                String key = buildString(keyStart, keyEnd);
                int colon = next(':', keyEnd);
                int valueStart = next(colon + 1);
                int valueEnd = readValue(v -> map.put(key, v), valueStart);
                delim = next(valueEnd + 1);
            } while (chars[delim] == ',');

            return delim;
        }

        private int readArrayValue(List<Object> list, int from) {
            int next = next(from + 1);

            // Check if empty array literal.
            if (chars[next] == ']') {
                return next;
            }

            // Consume array literal's fields.
            int delim = from;
            do {
                int valueEnd = readValue(v -> list.add(v), next(delim + 1));
                delim = next(valueEnd + 1);
            } while (chars[delim] == ',');

            return delim;
        }

        private int readNumber(int from) {
            int numberEnd = from;
            while (Character.isDigit(chars[numberEnd]) || NUMBER_CHARS.contains(chars[numberEnd])) {
                numberEnd++;
                if (numberEnd == chars.length) {
                    break;
                }
            }
            numberEnd--; // Move back to last known number char.
            if (numberEnd < from) {
                throw new JsonWheelException("Invalid number literal at " + from);
            }
            return numberEnd;
        }

        private int next(char c, int from) {
            char prev = '\0';
            for (; from < chars.length; from++) {
                char current = chars[from];
                if ('\\' != prev && c == current) {
                    return from;
                }
                prev = chars[from];
            }
            throw new JsonWheelException("Could not find " + c + ", checking from " + from);
        }

        private int next(int from) {
            for (; from < chars.length; from++) {
                if (!Character.isWhitespace(chars[from])) {
                    return from;
                }
            }
            throw new JsonWheelException("Could not find non-whitespace, checking from " + from);
        }

        private String buildString(int from, int to) {
            if (from < 0 || to >= chars.length) {
                throw new JsonWheelException("Out of bounds building String from " + from + " to " + to);
            }
            StringBuilder builder = new StringBuilder();
            while (from <= to) {
                if (chars[from] == '\\' && from + 1 <= to) {
                    from++; // Skip backslash. Then check:
                    // a) Codepoint in u-syntax.
                    switch (chars[from]) {
                        case 'u':
                            int cpStart = from + 1; // Skip "u".
                            int cpEnd = cpStart + 3;
                            if (cpEnd > to) {
                                throw new JsonWheelException("Invalid codepoint at " + from);
                            }
                            builder.appendCodePoint(Integer.parseInt(buildString(cpStart, cpEnd), 16));
                            from = cpEnd;
                            break;
                        // b) Backspace.
                        case 'b':
                            builder.deleteCharAt(Math.max(builder.length() - 1, 0));
                            break;
                        // c) Other escaped characters for which we can use the lookup table.
                        default:
                            Character escapeLookup = ESCAPE_LOOKUP.get(chars[from]);
                            if (escapeLookup == null) {
                                throw new JsonWheelException("Invalid escape sequence at " + from + ": " + chars[from]);
                            }
                            builder.append(escapeLookup);
                    }
                } else {
                    builder.append(chars[from]);
                }
                from++;
            }
            return builder.toString();
        }
    }

    static WheelNode read(String json) {
        char[] chars = json.toCharArray();
        return new Deserializer(chars).readInternal();
    }
}
