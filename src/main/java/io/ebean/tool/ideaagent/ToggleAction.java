package io.ebean.tool.ideaagent;

import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Action for toggling on off.
 */
public class ToggleAction extends com.intellij.openapi.actionSystem.ToggleAction {

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
