package net.cazzar.gradle.modsio;

import net.cazzar.gradle.modsio.tasks.UploadTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ModsIOPlugin implements Plugin<Project> {
    /**
     * Apply this plugin to the given target object.
     *
     * @param target The target object
     */
    public void apply(Project target) {
        target.getTasks().create("uploadToModsIO", UploadTask.class);
    }
}
