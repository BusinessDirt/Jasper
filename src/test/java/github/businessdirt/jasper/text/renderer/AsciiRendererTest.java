package github.businessdirt.jasper.text.renderer;

import github.businessdirt.jasper.text.Style;
import github.businessdirt.jasper.text.Text;
import github.businessdirt.jasper.text.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AsciiRendererTest {

    private AsciiRenderer renderer;

    @BeforeEach
    public void setUp() {
        renderer = new AsciiRenderer();
    }

    @Test
    public void testSimpleLiteral() {
        Text text = Text.literal("Hello, World!");
        String expected = "Hello, World!\033[0m";
        assertEquals(expected, renderer.render(text));
    }

    @Test
    public void testColor() {
        Text text = Text.literal("Hello, World!").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(255, 0, 0)));
        String expected = "\033[38;2;255;0;0mHello, World!\033[0m";
        assertEquals(expected, renderer.render(text));
    }

    @Test
    public void testBold() {
        Text text = Text.literal("Hello, World!").setStyle(Style.EMPTY.withBold(true));
        String expected = "\033[1mHello, World!\033[0m";
        assertEquals(expected, renderer.render(text));
    }

    @Test
    public void testItalic() {
        Text text = Text.literal("Hello, World!").setStyle(Style.EMPTY.withItalic(true));
        String expected = "\033[3mHello, World!\033[0m";
        assertEquals(expected, renderer.render(text));
    }

    @Test
    public void testUnderlined() {
        Text text = Text.literal("Hello, World!").setStyle(Style.EMPTY.withUnderlined(true));
        String expected = "\033[4mHello, World!\033[0m";
        assertEquals(expected, renderer.render(text));
    }

    @Test
    public void testStrikethrough() {
        Text text = Text.literal("Hello, World!").setStyle(Style.EMPTY.withStrikethrough(true));
        String expected = "\033[9mHello, World!\033[0m";
        assertEquals(expected, renderer.render(text));
    }

    @Test
    public void testMultipleStyles() {
        Text text = Text.literal("Hello, World!").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(0, 255, 0)));
        String expected = "\033[1;38;2;0;255;0mHello, World!\033[0m";
        assertEquals(expected, renderer.render(text));
    }

    @Test
    public void testSiblings() {
        Text text = Text.literal("Red ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(255, 0, 0)))
                .append(Text.literal("Green ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0, 255, 0))))
                .append(Text.literal("Blue").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0, 0, 255))));
        String expected = "\033[38;2;255;0;0mRed \033[0m\033[38;2;0;255;0mGreen \033[0m\033[38;2;0;0;255mBlue\033[0m";
        assertEquals(expected, renderer.render(text));
    }

    @Test
    public void testObfuscated() {
        Text text = Text.literal("Secret").setStyle(Style.EMPTY.withObfuscated(true));
        String rendered = renderer.render(text);
        assertEquals(13, rendered.length()); // "Secret".length + "\033[0m".length
        assertNotEquals("Secret\033[0m", rendered);
    }
}
