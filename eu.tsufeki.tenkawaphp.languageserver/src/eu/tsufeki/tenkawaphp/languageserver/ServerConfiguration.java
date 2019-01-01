package eu.tsufeki.tenkawaphp.languageserver;

import org.osgi.service.prefs.Preferences;

public class ServerConfiguration {

    @SuppressWarnings("unused")
    private static class Diagnostics {

        private boolean phpstan;
    }

    @SuppressWarnings("unused")
    private static class Completion {

        private boolean autoImport;
        private String[] extensions = new String[0];
    }

    private Diagnostics diagnostics = new Diagnostics();
    private Completion completion = new Completion();

    public static ServerConfiguration fromPreferences(Preferences prefs) {
        var config = new ServerConfiguration();
        config.diagnostics.phpstan = prefs.getBoolean(PreferenceConstants.DIAGNOSTICS_PHPSTAN_ENABLED, true);
        config.completion.autoImport = prefs.getBoolean(PreferenceConstants.COMPLETION_AUTO_IMPORT, true);

        var extensions = prefs.get(PreferenceConstants.COMPLETION_EXTENSIONS, "");
        config.completion.extensions = extensions.trim().split("\\s*,\\s*");

        return config;
    }
}
