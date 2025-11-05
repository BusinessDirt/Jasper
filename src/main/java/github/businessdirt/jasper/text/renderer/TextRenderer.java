package github.businessdirt.jasper.text.renderer;

import github.businessdirt.jasper.text.Text;

/**
 * A functional interface for rendering a {@link Text} component to a specific format.
 * @param <S> the type of the rendered output (e.g., a {@link String})
 */
public interface TextRenderer<S> {

    /**
     * Renders a {@link Text} component.
     * @param text the text component to render
     * @return the rendered output
     */
    S render(Text text);
}
