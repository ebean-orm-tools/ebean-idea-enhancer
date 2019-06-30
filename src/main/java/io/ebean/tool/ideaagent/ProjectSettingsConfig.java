package io.ebean.tool.ideaagent;

import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ProjectSettingsConfig implements SearchableConfigurable {

  private JCheckBox agentEnabled;
  private JPanel topPanel;

  private Project project;

  ProjectSettingsConfig(@NotNull Project project) {
    this.project = project;
  }

  @NotNull
  @Override
  public String getId() {
    return "settings.io.ebean";
  }

  @Nls(capitalization = Nls.Capitalization.Title)
  @Override
  public String getDisplayName() {
    return "Ebean plugin";
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    initFromSetting();
    return topPanel;
  }

  private void initFromSetting() {
    final StateComponent state = StateComponent.get(project);
    if (state != null) {
      agentEnabled.setSelected(state.isEnabled());
    }
  }

  @Override
  public boolean isModified() {
    final StateComponent state = StateComponent.get(project);
    if (state != null) {
      return agentEnabled.isSelected() != state.isEnabled();
    }
    // we shouldn't ever use this path
    return true;
  }

  @Override
  public void apply() {
    final StateComponent state = StateComponent.get(project);
    state.updateEnabled(agentEnabled.isSelected());
  }

  @Override
  public void reset() {
    initFromSetting();
  }

}
