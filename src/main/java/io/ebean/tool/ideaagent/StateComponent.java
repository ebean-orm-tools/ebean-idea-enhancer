package io.ebean.tool.ideaagent;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Maintains the per project activate flag.
 */
@State(name = "ebean11", storages = {@Storage("ebean11.xml")})
public class StateComponent implements ProjectComponent, PersistentStateComponent<PluginState> {

    static final Key<PluginState> STATE_KEY = Key.create("ebean11");

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

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @Override
    public void projectOpened() {
    }

    @Override
    public void projectClosed() {
        setEnabled(false);
    }


    String getAgentPath() {
        return state.agentPath;
    }

    void setAgentPath(String agentPath) {
        this.state.agentPath = agentPath;
    }

    boolean isEnabled() {
        return state.enabled;
    }

    void setEnabled(boolean enabled) {
        this.state.enabled = enabled;
        project.putUserData(STATE_KEY, state);
    }

    @Nullable
    @Override
    public PluginState getState() {
        return state;
    }

    @Override
    public void loadState(PluginState state) {
        XmlSerializerUtil.copyBean(state, this.state);
        project.putUserData(STATE_KEY, state);
    }


}
