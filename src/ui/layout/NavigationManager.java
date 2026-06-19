package ui.layout;

public class NavigationManager {
    private static NavigationManager instance;
    private CardNavigator cardNavigator;

    private NavigationManager() {}

    public static NavigationManager getInstance() {
        if (instance == null) {
            instance = new NavigationManager();
        }
        return instance;
    }

    public void setCardNavigator(CardNavigator cardNavigator) {
        this.cardNavigator = cardNavigator;
    }

    public void navigateTo(String panelName) {
        if (cardNavigator != null) {
            cardNavigator.show(panelName);
        }
    }
}
