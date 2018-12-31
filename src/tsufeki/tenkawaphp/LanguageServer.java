package tsufeki.tenkawaphp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.lsp4e.server.StreamConnectionProvider;
import org.osgi.service.prefs.Preferences;

public class LanguageServer implements StreamConnectionProvider {

    private static final String MEMORY_LIMIT = "1024M";
    private static final String SCRIPT_PATH = "/tsufeki/tenkawa-php-language-server/bin/tenkawa.php";

    private StreamConnectionProvider provider;
    private Preferences prefs;

    public LanguageServer() {
        prefs = InstanceScope.INSTANCE.getNode(TenkawaPhpPlugin.PLUGIN_ID);

        try {
            List<String> command = new ArrayList<>();

            String executablePath = prefs.get(PreferenceConstants.EXECUTABLE_PATH, "");
            if (executablePath == null || executablePath.equals("")) {
                executablePath = "php";
            }
            command.add(executablePath);
            command.add("-dmemory_limit=" + MEMORY_LIMIT);

            var logLevel = "debug";
            var bundle = TenkawaPhpPlugin.getDefault().getContext().getBundle();
            var url = FileLocator.find(bundle, new Path("vendor"), null);
            var workingDir = new Path(FileLocator.toFileURL(url).getPath());
            command.add(workingDir.append(SCRIPT_PATH).toOSString());
            command.add("--log-stderr");
            command.add("--log-level=" + logLevel);

            provider = new ProcessOverSocketStreamConnectionProvider((int port) -> {
                var cmd = new ArrayList<String>(command);
                cmd.add("--socket=tcp://127.0.0.1:" + port);
                return cmd;
            }, workingDir.toOSString());
        } catch (IOException e) {
            TenkawaPhpPlugin.logError(e);
        }
    }

    @Override
    public InputStream getInputStream() {
        if (provider == null) {
            return null;
        }
        return provider.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() {
        if (provider == null) {
            return null;
        }
        return provider.getOutputStream();
    }

    @Override
    public InputStream getErrorStream() {
        if (provider == null) {
            return null;
        }
        return provider.getErrorStream();
    }

    @Override
    public void start() throws IOException {
        if (provider == null) {
            throw new IOException("Language server can't be started");
        }
        provider.start();
    }

    @Override
    public void stop() {
        if (provider == null) {
            return;
        }
        provider.stop();
    }

    @Override
    public Object getInitializationOptions(URI rootUri) {
        return ServerConfiguration.fromPreferences(prefs);
    }
}
