package tsufeki.tenkawaphp;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        var node = DefaultScope.INSTANCE.getNode(TenkawaPhpPlugin.PLUGIN_ID);
        node.put(PreferenceConstants.EXECUTABLE_PATH, "");
        node.putBoolean(PreferenceConstants.DIAGNOSTICS_PHPSTAN_ENABLED, true);
        node.putBoolean(PreferenceConstants.COMPLETION_AUTO_IMPORT, true);
        node.put(PreferenceConstants.COMPLETION_EXTENSIONS, "");
    }
}
