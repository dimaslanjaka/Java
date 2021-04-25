import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import static com.dimaslanjaka.library.helper.File.getFileExtension;
import static org.junit.Assert.assertEquals;

public class FileTest {
    @Test
    //@Description("test uri has extension on last dimas.java.component uri")
    public void getExtension() throws MalformedURLException {
        assertEquals(
                Optional.of("zip"),
                getFileExtension(new URL("http://www.example.com/stuff.zip")));

        assertEquals(
                Optional.of("zip"),
                getFileExtension(new URL("http://www.example.com/stuff.zip")));

        assertEquals(
                Optional.of("zip"),
                getFileExtension(new URL("http://www.example.com/a/b/c/stuff.zip")));

        assertEquals(
                Optional.empty(),
                getFileExtension(new URL("http://www.example.com")));

        assertEquals(
                Optional.empty(),
                getFileExtension(new URL("http://www.example.com/")));

        assertEquals(
                Optional.empty(),
                getFileExtension(new URL("http://www.example.com/.")));
    }
}
