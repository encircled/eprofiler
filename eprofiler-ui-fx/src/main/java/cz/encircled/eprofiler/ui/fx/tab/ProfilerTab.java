package cz.encircled.eprofiler.ui.fx.tab;

/**
 * @author Vlad on 17-Jul-16.
 */
public enum ProfilerTab {

    CALL_TREE(0),
    METHOD_DETAIL(1),
    HOT_METHODS(2);

    public int index;

    ProfilerTab(int index) {
        this.index = index;
    }

    public static ProfilerTab getTabByIndex(int index) {
        for (ProfilerTab profilerTab : values()) {
            if (profilerTab.index == index) {
                return profilerTab;
            }
        }
        return null;
    }

}
