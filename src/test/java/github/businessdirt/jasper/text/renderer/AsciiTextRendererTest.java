package github.businessdirt.jasper.text.renderer;

import github.businessdirt.jasper.text.Style;
import github.businessdirt.jasper.text.Text;
import github.businessdirt.jasper.text.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AsciiTextRendererTest {

    private AsciiTextRenderer renderer;

    @BeforeEach
    public void setUp() {
        renderer = new AsciiTextRenderer();
    }

    @Test
    @DisplayName("Simple Literal Text Rendering should work")
    public void testSimpleLiteral() {
        Text text = Text.literal("Hello, World!");
        String expected = "Hello, World!\033[0m";
        assertEquals(expected, renderer.render(text));
    }

    @Test
    @DisplayName("Text Rendering with Color should work")
    public void testColor() {
        Text text = Text.literal("Hello, World!").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(255, 0, 0)));
        String expected = "\033[38;2;255;0;0mHello, World!\033[0m";
        assertEquals(expected, renderer.render(text));
    }

    @Test
    @DisplayName("Text Rendering with Bold Style should work")
    public void testBold() {
        Text text = Text.literal("Hello, World!").setStyle(Style.EMPTY.withBold(true));
        String expected = "\033[1mHello, World!\033[0m";
        assertEquals(expected, renderer.render(text));
    }

    @Test
    @DisplayName("Text Rendering with Italic Style should work")
    public void testItalic() {
        Text text = Text.literal("Hello, World!").setStyle(Style.EMPTY.withItalic(true));
        String expected = "\033[3mHello, World!\033[0m";
        assertEquals(expected, renderer.render(text));
    }

    @Test
    @DisplayName("Text Rendering with Underlined Style should work")
    public void testUnderlined() {
        Text text = Text.literal("Hello, World!").setStyle(Style.EMPTY.withUnderlined(true));
        String expected = "\033[4mHello, World!\033[0m";
        assertEquals(expected, renderer.render(text));
    }

    @Test
    @DisplayName("Text Rendering with Strikethrough Style should work")
    public void testStrikethrough() {
        Text text = Text.literal("Hello, World!").setStyle(Style.EMPTY.withStrikethrough(true));
        String expected = "\033[9mHello, World!\033[0m";
        assertEquals(expected, renderer.render(text));
    }

    @Test
    @DisplayName("Text Rendering with Multiple Styles should work")
    public void testMultipleStyles() {
        Text text = Text.literal("Hello, World!").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(0, 255, 0)));
        String expected = "\033[1;38;2;0;255;0mHello, World!\033[0m";
        assertEquals(expected, renderer.render(text));
    }

    @Test
    @DisplayName("Text Rendering with Siblings (Appended Texts) should work")
    public void testSiblings() {
        Text text = Text.literal("Red ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(255, 0, 0)))
                .append(Text.literal("Green ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0, 255, 0))))
                .append(Text.literal("Blue").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0, 0, 255))));
        String expected = "\033[38;2;255;0;0mRed \033[0m\033[38;2;0;255;0mGreen \033[0m\033[38;2;0;0;255mBlue\033[0m";
        assertEquals(expected, renderer.render(text));
    }
}
