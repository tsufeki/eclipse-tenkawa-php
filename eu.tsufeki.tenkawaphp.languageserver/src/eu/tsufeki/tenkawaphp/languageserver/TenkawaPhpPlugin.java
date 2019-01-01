package eu.tsufeki.tenkawaphp.languageserver;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class TenkawaPhpPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "eu.tsufeki.tenkawaphp.languageserver";
    private static TenkawaPhpPlugin plugin;
    private BundleContext context;

    public TenkawaPhpPlugin() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        this.context = context;
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        this.context = null;
        super.stop(context);
    }

    public BundleContext getContext() {
        return context;
    }

    public static TenkawaPhpPlugin getDefault() {
        return plugin;
    }

    public static void logError(Throwable e) {
        getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, e.getMessage(), e));
    }

    public static void logInfo(String message) {
        getDefault().getLog().log(new Status(IStatus.INFO, PLUGIN_ID, message));
    }
}
