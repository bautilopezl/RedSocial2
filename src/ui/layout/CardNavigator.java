package ui.layout;

import java.awt.CardLayout;

public class CardNavigator {
    private final ContentPanel contentPanel;
    private final CardLayout cardLayout;

    public CardNavigator(ContentPanel contentPanel) {
        this.contentPanel = contentPanel;
        this.cardLayout = new CardLayout();
        this.contentPanel.setLayout(this.cardLayout);
    }

    public void show(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }
}
