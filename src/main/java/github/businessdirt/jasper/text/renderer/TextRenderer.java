package github.businessdirt.jasper.text.renderer;

import github.businessdirt.jasper.text.Text;

public interface TextRenderer<S> {

    S render(Text text);
}
