package ui.theme;

import java.awt.Color;
import java.awt.Font;

public class ThemeManager {

    private ThemeManager() {}

    // ── Paleta de colores ──────────────────────────────────────────────
    public static final Color PRIMARY        = new Color(0x0A66C2);
    public static final Color PRIMARY_HOVER  = new Color(0x004182);
    public static final Color BG_GENERAL     = new Color(0xF3F2EF);
    public static final Color PANEL          = Color.WHITE;
    public static final Color TEXT_PRIMARY   = new Color(0x191919);
    public static final Color TEXT_SECONDARY = new Color(0x666666);
    public static final Color TEXT_MUTED     = new Color(0x969696);
    public static final Color TEXT_DESCRIPTION = new Color(0x323232);
    public static final Color BORDER         = new Color(0xDDDDDD);
    public static final Color SUCCESS        = new Color(0x188038);
    public static final Color WARNING        = new Color(0xF9AB00);
    public static final Color ERROR          = new Color(0xD93025);
    public static final Color AVATAR_BG      = new Color(0xC8C8C8);
    public static final Color CARD_ALT_BG    = new Color(0xFAFAFA);
    public static final Color SIDEBAR_ACTIVE_BG = new Color(0xE8F0FE);

    // ── Tamaños de fuente ──────────────────────────────────────────────
    public static final int F_XXL   = 28;
    public static final int F_TITLE = 24;
    public static final int F_SUBT  = 20;
    public static final int F_HEAD  = 16;
    public static final int F_BODY  = 14;
    public static final int F_SMALL = 12;
    public static final int F_XS    = 11;

    // ── Fuentes pre-creadas ────────────────────────────────────────────
    public static final Font F_XXL_BOLD   = new Font("Segoe UI", Font.BOLD,   F_XXL);
    public static final Font F_TITLE_BOLD = new Font("Segoe UI", Font.BOLD,   F_TITLE);
    public static final Font F_SUBT_BOLD  = new Font("Segoe UI", Font.BOLD,   F_SUBT);
    public static final Font F_HEAD_BOLD  = new Font("Segoe UI", Font.BOLD,   F_HEAD);
    public static final Font F_BODY_BOLD  = new Font("Segoe UI", Font.BOLD,   F_BODY);
    public static final Font F_BODY_PLAIN = new Font("Segoe UI", Font.PLAIN,  F_BODY);
    public static final Font F_SMALL_BOLD = new Font("Segoe UI", Font.BOLD,   F_SMALL);
    public static final Font F_SMALL_PLAIN= new Font("Segoe UI", Font.PLAIN,  F_SMALL);
    public static final Font F_XS_PLAIN   = new Font("Segoe UI", Font.PLAIN,  F_XS);

    // ── Espaciado ──────────────────────────────────────────────────────
    public static final int PADDING      = 16;
    public static final int CARD_GAP     = 20;
    public static final int SECTION_GAP  = 32;
    public static final int BORDER_W     = 20;
    public static final int OUTER_MARGIN = 30;

    // ── Radios de borde ────────────────────────────────────────────────
    public static final int RADIUS_PANEL = 15;
    public static final int RADIUS_SMALL = 10;
    public static final int RADIUS_BTN   = 20;
    public static final int RADIUS_AVATAR = 80;

    // ── Dimensiones ────────────────────────────────────────────────────
    public static final int BTN_H        = 35;
    public static final int BTN_W        = 120;
    public static final int BTN_W_SMALL  = 100;
    public static final int SEARCH_W     = 300;
    public static final int INPUT_H      = 35;
}
