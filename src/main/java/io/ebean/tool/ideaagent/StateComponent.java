package io.ebean.tool.ideaagent;

import com.intellij.openapi.components.NamedComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Maintains the per project activate flag.
 */
@State(name = "ebeanPlugin11", storages = {@Storage("ebean-plugin.xml")})
public class StateComponent implements NamedComponent, PersistentStateComponent<PluginState> {

  static StateComponent get(Project project) {
    return project == null ? null : project.getComponent(StateComponent.class);
  }

  private final Logger log = Logger.getInstance(ToggleAction.class);

  static final Key<PluginState> STATE_KEY = Key.create("ebeanPlugin11");

  private final Project project;

  private final PluginState state = new PluginState();

  public StateComponent(Project project) {
    this.project = project;
  }

  @Override
  @NotNull
  public String getComponentName() {
    return "Ebean11Action";
  }

  boolean isEnabled() {
    return state.enabled;
  }

  private String getAgentPath() {
    return state.agentPath;
  }

  private void setAgentPath(String agentPath) {
    this.state.agentPath = agentPath;
  }

  private void setEnabledAndStore(boolean enabled) {
    this.state.enabled = enabled;
    project.putUserData(STATE_KEY, state);
  }

  @Nullable
  @Override
  public PluginState getState() {
    return state;
  }

  @Override
  public void loadState(@NotNull PluginState state) {
    XmlSerializerUtil.copyBean(state, this.state);
    project.putUserData(STATE_KEY, state);
  }

  /**
   * Toggle the enabled state.
   */
  void updateEnabled(boolean selected) {
    if (selected) {
      File agentJar = AgentJarFile.find(getAgentPath());
      if (agentJar != null) {
        log.debug("setting ebean agent jar to: " + agentJar);
        setAgentPath(agentJar.getAbsolutePath());
      }
    }
    setEnabledAndStore(selected);
  }

  /**
   * Find the ebean-agent jar and update the agentPath state.
   *
   * @return The agentPath if the agent was found and set.
   */
  String updateAgentPath() {
    File agentJar = AgentJarFile.find(null);
    if (agentJar == null) {
      return null;
    }
    log.info("setting ebean agent jar to: " + agentJar);
    setAgentPath(agentJar.getAbsolutePath());
    project.putUserData(STATE_KEY, state);
    return agentJar.getAbsolutePath();
  }
}
