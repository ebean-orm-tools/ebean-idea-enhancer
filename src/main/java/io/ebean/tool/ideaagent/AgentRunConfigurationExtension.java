package io.ebean.tool.ideaagent;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.RunConfigurationExtension;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import static io.ebean.tool.ideaagent.StateComponent.STATE_KEY;

public class AgentRunConfigurationExtension extends RunConfigurationExtension {

  private static final Logger log = Logger.getInstance(AgentRunConfigurationExtension.class);

  @Override
  public <T extends RunConfigurationBase> void updateJavaParameters(T configuration, JavaParameters params, RunnerSettings runnerSettings) throws ExecutionException {

    Project project = configuration.getProject();
    PluginState state = project.getUserData(STATE_KEY);

    if (state != null && state.enabled) {
      if (AgentJarFile.exists(state.agentPath)) {
        runVmWithJavaAgent(params, state.agentPath);

      } else {
        log.info("Ebean agentPath not set? Finding ebean-agent.jar ...");
        final StateComponent stateComponent = StateComponent.get(project);
        final String agentPath = stateComponent.updateAgentPath();
        if (agentPath != null) {
          runVmWithJavaAgent(params, agentPath);
        } else {
          log.error("ebean-agent not set or found? agentPath:" + state.agentPath);
        }
      }
    }
  }

  /**
   * Add the -javaagent VM argument to run with the ebean-agent.
   */
  private void runVmWithJavaAgent(JavaParameters params, String agentPath) {
    log.info("using Ebean javaagent " + agentPath);
    params.getVMParametersList().addParametersString("-javaagent:\"" + agentPath + "\"");
  }

  @Override
  public boolean isApplicableFor(@NotNull RunConfigurationBase<?> configuration) {

    return isEnabled(configuration.getProject());
  }

  private boolean isEnabled(Project project) {
    PluginState state = project.getUserData(STATE_KEY);
    return state != null && state.enabled;
  }
}
