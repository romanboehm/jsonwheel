# JSON Wheel

Have you ever written scripts in Java 11+ and needed to operate on some JSON string? Have you ever needed to extract *just that one deeply-nested value* of an `application/json` response without modelling the whole hierarchy? Have you
ever felt someone should indeed reinvent that wheel and provide the JVM universe with yet another JSON deserializer?

If the answer to any of those is "yes" then look no further, JSON Wheel's got you covered! **It's a 200 lines
single-source-file hackable JSON deserializer written in plain Java.**

If the answer is "no" I still had fun writing it. :)

## It is ...

* small
* deserialization-only
* hackable
* compatible to JDK 11+
* dependency-less
* not tooo slow

## It is not ...

* typed (except for its own API's types)
* safe for malformed input
* safe for malicious input (at least I don't give that guarantee)

## Usage
### Within Your Script
1. Copy the **content** of [the most recent version of JsonWheel.java](https://github.com/rmnbhm/jsonwheel/blob/0.3/JsonWheel.java) as-is into your existing script.
2. Remove imports if colliding with your pre-existing ones.
### Within Your Project
1. Copy [the most recent version of JsonWheel.java](https://github.com/rmnbhm/jsonwheel/blob/0.3/JsonWheel.java) as a file into your project's source directory, e.g. `/src/main/java`.
2. Adjust the package declaration, i.e. introduce one, if needed.
### With JBang's `//SOURCES`
1. Create a JBang script, e.g. with `jbang init`.
2. Add the `//SOURCES` JBang directive, as [demonstrated here](https://carbon.now.sh/6c3f872f48f310534645566e2564971a). If you care for your script to not break, you best set a version directly in the link and don't use the `main` blob.

Then you can ...

### 1) Deserialize JSON objects

```java
var json = """
    {
        "foo": 1,
        "bar": "baz",
        "qux": null
    }""";

var node = JsonWheel.read(json);
var foo = node.get("foo").val(Integer.class); // 1
var bar = node.get("bar").val(String.class);  // "baz"
var qux = node.get("qux").val(String.class);  // null
```
### 2) Deserialize JSON arrays

```java
var json = """
    [
        1,
        2,
        3
    ]""";

var node = JsonWheel.read(json);
var list = node.elements()
        .stream()
        .map(e -> e.val(Integer.class))
        .toList();  // 1 2 3
```

### 3) Deserialize "complex" JSON

```java
var json = """
    {
        "foo": {
            "bar" : [
                1,
                2,
                3
            ],
            "baz": "qux"
        }
    }""";

var node = JsonWheel.read(json);
var bar = node.get("foo").get("bar").elements()
        .stream()
        .map(e -> e.val(Integer.class))
        .toList(); //   1 2 3
var baz = node.get("foo").get("baz").val(String.class); //  "qux"
```