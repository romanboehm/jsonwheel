import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

class PrepareRelease {

    public static void main(String... args) throws IOException {
        var src = Path.of(args[0]);
        var dest = Path.of(args[1]);
        var withoutPackageDecl = Files.readAllLines(src).stream()
                                      .skip(2)
                                      .toList();
        Files.createDirectories(dest.getParent());
        Files.write(
                            dest,
                            withoutPackageDecl,
                            StandardOpenOption.CREATE
        );
    }
}
