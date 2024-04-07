package io.ebean.tool.ideaagent;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Action for toggling on off.
 */
public class ToggleAction extends com.intellij.openapi.actionSystem.ToggleAction {

  @Override
  public @NotNull ActionUpdateThread getActionUpdateThread() {
    return ActionUpdateThread.BGT;
  }

  @Override
  public boolean isSelected(AnActionEvent e) {
    final StateComponent state = StateComponent.get(e.getProject());
    return (state != null) && state.isEnabled();
  }

  @Override
  public void setSelected(AnActionEvent e, boolean selected) {
    final StateComponent state = StateComponent.get(e.getProject());
    if (state != null) {
      state.updateEnabled(selected);
    }
  }

}
