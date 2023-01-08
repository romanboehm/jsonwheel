import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

class PrepareRelease {

    private static final Path DEST = Path.of("target/distribution");
    private static final Path SRC = Path.of("src/main/java/com/romanboehm/jsonwheel");

    public static void main(String[] args) throws IOException {
        var withoutPackageDecl = Files.readAllLines(SRC.resolve("JsonWheel.java")).stream()
                .skip(2)
                .toList();
        Files.createDirectories(DEST);
        Files.write(
                DEST.resolve("JsonWheel.java"),
                withoutPackageDecl,
                StandardOpenOption.CREATE
        );
    }
}
