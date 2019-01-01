package eu.tsufeki.tenkawaphp.languageserver;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public PreferencePage() {
        super("Tenkawa PHP", GRID);
        setPreferenceStore(TenkawaPhpPlugin.getDefault().getPreferenceStore());
    }

    @Override
    public void createFieldEditors() {
        addField(new FileFieldEditor(
            PreferenceConstants.EXECUTABLE_PATH,
            "Path to PHP executable:",
            getFieldEditorParent()
        ));
        addField(new BooleanFieldEditor(
            PreferenceConstants.DIAGNOSTICS_PHPSTAN_ENABLED,
            "Enable PHPStan diagnostics:",
            getFieldEditorParent()
        ));
        addField(new BooleanFieldEditor(
            PreferenceConstants.COMPLETION_AUTO_IMPORT,
            "Enable automatic import (use) of completed classes:",
            getFieldEditorParent()
        ));
        addField(new StringFieldEditor(
            PreferenceConstants.COMPLETION_EXTENSIONS,
            "Additional PHP extensions for completion:",
            getFieldEditorParent()
        ));
    }

    @Override
    public void init(IWorkbench workbench) {
    }
}