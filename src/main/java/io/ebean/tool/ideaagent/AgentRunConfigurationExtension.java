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
        // we have enabled the plugin, add the agent
        log.info("using Ebean javaagent "+state.agentPath);
        params.getVMParametersList().addParametersString("-javaagent:\""+state.agentPath+"\"");
      }
    }
  }

  @Override
  public boolean isApplicableFor(@NotNull RunConfigurationBase<?> configuration) {

    Project project = configuration.getProject();
    PluginState state = project.getUserData(STATE_KEY);
    return state != null && state.enabled;
  }
}
