package net.cazzar.gradle.modsio;

import org.gradle.api.Project;
import org.gradle.api.UnknownDomainObjectException;

public class ModsIOExtension {
    public String apiKey;
    public String modId;
    public String minecraft;

    private Project project;

    public ModsIOExtension(Project project) {
        this.project = project;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getModId() {
        return modId;
    }

}
