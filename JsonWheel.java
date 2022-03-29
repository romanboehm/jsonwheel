import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        final Object inner;

        WheelNode(Object inner) {
            this.inner = inner;
        }

        List<WheelNode> elements() {
            List<WheelNode> list = new ArrayList<>();
            for (Object o : ((List<Object>) inner)) {
                list.add(new WheelNode(o));
            }
            return list;
        }

        WheelNode get(String key) {
            return new WheelNode(((Map<String, Object>) inner).get(key));
        }

        <T> T val(Class<T> clazz) {
            return inner == null ? null : clazz.cast(inner);
        }
    }

    static class Deserializer {
        private static final char[] TRUE_LITERAL = new char[]{'t', 'r', 'u', 'e'};
        private static final char[] FALSE_LITERAL = new char[]{'f', 'a', 'l', 's', 'e'};
        private static final char[] NULL_LITERAL = new char[]{'n', 'u', 'l', 'l'};
        private static final Set<Character> NUMBER_CHARS = new HashSet<>(Arrays.asList('+', '.', '-'));
        private final char[] chars;

        Deserializer(char[] chars) {
            this.chars = chars;
        }

        Object readInternal() {
            int next = next(0);
            switch (chars[next]) {
                case '{':
                    Map<String, Object> map = new HashMap<>();
                    readObjectValue(map, 0);
                    return map;
                case '[':
                    List<Object> list = new ArrayList<>();
                    readArrayValue(list, 0);
                    return list;
                // Per newer RFCs top level non-object, non-array values are allowed.
                case '"':
                    int closingQuote = next('"', next + 1);
                    return buildString(next + 1, closingQuote - 1);
                case 'n':
                    readChars(NULL_LITERAL, next);
                    return null;
                case 't':
                    readChars(TRUE_LITERAL, next);
                    return true;
                case 'f':
                    readChars(FALSE_LITERAL, next);
                    return false;
                default:
                    int numberEnd = readNumber(next);
                    String number = buildString(next, numberEnd);
                    if (number.contains(".")) {
                        return Double.parseDouble(number);
                    } else {
                        return Integer.parseInt(number); // Change to `Long::parseLong` if needed.
                    }
            }
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
                    int nullEnd = readChars(NULL_LITERAL, from);
                    valueConsumer.accept(null);
                    return nullEnd;
                case 't':
                    int trueEnd = readChars(TRUE_LITERAL, from);
                    valueConsumer.accept(true);
                    return trueEnd;
                case 'f':
                    int falseEnd = readChars(FALSE_LITERAL, from);
                    valueConsumer.accept(false);
                    return falseEnd;
                default:
                    int numberEnd = readNumber(from);
                    String number = buildString(from, numberEnd);
                    if (number.contains(".")) {
                        valueConsumer.accept(Double.parseDouble(number));
                    } else {
                        valueConsumer.accept(Integer.parseInt(number)); // Change to `Long::parseLong` if needed.
                    }
                    return numberEnd;
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

        private int readChars(char[] literal, int from) {
            for (int i = 0; i < literal.length && (from + i) < chars.length; i++) {
                if (from + i >= chars.length || chars[from + i] != literal[i]) {
                    throw new JsonWheelException("Invalid literal at " + from);
                }
            }
            return from + (literal.length - 1);
        }

        private int readNumber(int from) {
            int numberEnd = from;
            while (Character.isLetterOrDigit(chars[numberEnd]) || NUMBER_CHARS.contains(chars[numberEnd])) {
                numberEnd++;
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
            for (int i = from; i <= to; i++) {
                builder.append(chars[i]);
            }
            return builder.toString();
        }
    }

    static WheelNode read(String json) {
        char[] chars = json.toCharArray();
        return new WheelNode(new Deserializer(chars).readInternal());
    }
}
