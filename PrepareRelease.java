import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

class PrepareRelease {

    private static final Path DEST = Path.of("target/distribution");
    private static final Path SRC = Path.of("src/main/java/com/romanboehm/jsonwheel");

    public static void main(String[] args) throws IOException {
        List<String> withoutPackageDecl = Files.readAllLines(SRC.resolve("JsonWheel.java")).stream()
                .skip(2)
                .collect(Collectors.toList());
        Files.createDirectories(DEST);
        Files.deleteIfExists(DEST.resolve("JsonWheel.java"));
        Files.write(Files.createFile(DEST.resolve("JsonWheel.java")), withoutPackageDecl);
    }

}
