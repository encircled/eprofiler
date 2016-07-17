package cz.encircled.eprofiler.ui.fx.tab;

import cz.encircled.eprofiler.ui.fx.FxApplication;
import javafx.scene.control.Tab;

/**
 * @author Vlad on 17-Jul-16.
 */
public abstract class AbstractProfilerTab extends Tab {

    protected ProfilerTab description;

    protected FxApplication fxApplication;

    public AbstractProfilerTab(ProfilerTab description, FxApplication fxApplication) {
        super(description.name());
        this.description = description;
        this.fxApplication = fxApplication;

        fxApplication.filePath.addListener((observable, oldValue, newValue) -> {
            if (fxApplication.isActiveTab(description)) {
                repaint();
            }
        });
    }

    public abstract void repaint();

}
