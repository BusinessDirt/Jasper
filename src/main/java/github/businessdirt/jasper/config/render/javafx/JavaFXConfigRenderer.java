package github.businessdirt.jasper.config.render.javafx;

import github.businessdirt.jasper.config.ConfigHandler;
import github.businessdirt.jasper.config.render.ConfigRenderer;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class JavaFXConfigRenderer<C extends ConfigHandler> extends ConfigRenderer<C, Parent> {

    /**
     * Renders the configuration properties into a JavaFX Parent node.
     * This node can be added to a Scene to be displayed in a window.
     *
     * @param config      The configuration handler containing the properties.
     * @param searchQuery A string to filter which properties are displayed (currently unused).
     * @return A JavaFX Parent node representing the configuration UI.
     */
    @Override
    public Parent render(C config, String searchQuery) {
        // Use a VBox as the root layout pane. It arranges elements vertically.
        VBox root = new VBox(10); // 10 is the spacing between elements
        root.setPadding(new Insets(15));

        // Iterate over each property from the ConfigHandler
        /*
        for (PropertyData property : config.getProperties()) {
            // For each property, create a horizontal container for its label and control
            HBox propertyLayout = new HBox(10);
            propertyLayout.setPadding(new Insets(0, 0, 5, 0));

            // Create a label for the property name
            Label nameLabel = new Label(property.options().name());
            nameLabel.setPrefWidth(150); // Set a preferred width to align controls
            nameLabel.setFont(Font.font("System", FontWeight.BOLD, 12));

            // Add the label to the horizontal layout
            propertyLayout.getChildren().add(nameLabel);

            // Create the appropriate control based on the property's type
            if (property.options().type() == PropertyType.TEXT) {
                TextField textField = new TextField(property.getValue(String.class));
                // When the text changes, update the underlying property
                textField.textProperty().addListener((obs, oldVal, newVal) -> {
                    property.setValue(newVal);
                });
                propertyLayout.getChildren().add(textField);
            } else if (property.options().type() == PropertyType.SLIDER) {
                TextField intField = new TextField(Objects.requireNonNull(property.getValue(Integer.class)).toString());
                // When the text changes, update the underlying property
                intField.textProperty().addListener((obs, oldVal, newVal) -> {
                    try {
                        property.setValue(Integer.parseInt(newVal));
                    } catch (NumberFormatException e) {
                        // Handle invalid input if necessary, e.g., show an error
                    }
                });
                propertyLayout.getChildren().add(intField);
            } else if (property.options().type() == PropertyType.SWITCH) {
                CheckBox checkBox = new CheckBox();
                checkBox.setSelected(Boolean.TRUE.equals(property.getValue(Boolean.class)));
                // When the checkbox is toggled, update the underlying property
                checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    property.setValue(newVal);
                });
                propertyLayout.getChildren().add(checkBox);
            }

            // Add the layout for this property to the main vertical layout
            root.getChildren().add(propertyLayout);
        }

        */

        return root;
    }
}