package io.ebean.tool.ideaagent;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import java.io.File;

/**
 * Action for toggling on off.
 */
public class ToggleAction extends com.intellij.openapi.actionSystem.ToggleAction {

  private final Logger log = Logger.getInstance(ToggleAction.class);

  @Override
  public boolean isSelected(AnActionEvent e) {

    Project project = e.getProject();
    if (project != null && project.hasComponent(StateComponent.class)) {
      StateComponent state = project.getComponent(StateComponent.class);
      return state.isEnabled();
    }
    return false;
  }

  @Override
  public void setSelected(AnActionEvent e, boolean selected) {

    Project project = e.getProject();
    if (project != null && project.hasComponent(StateComponent.class)) {

      StateComponent state = project.getComponent(StateComponent.class);
      state.setEnabled(selected);

      if (selected) {
        File agentJar = AgentJarFile.find(state.getAgentPath());
        if (agentJar != null) {
          log.debug("setting ebean agent jar to: " + agentJar);
          state.setAgentPath(agentJar.getAbsolutePath());
        }
      }
//      final String path = state.getAgentPath();
//      if (path == null || !new File(path).exists()) {
//        final File agentJar = AgentJarFile.find();
//        if (agentJar != null) {
//          log.info("setting ebean agent jar to: " + agentJar);
//          state.setAgentPath(agentJar.getAbsolutePath());
//        }
//      }
    }
  }


}
