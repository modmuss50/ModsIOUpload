package net.cazzar.gradle.modsio;

import groovy.lang.Closure;
import org.gradle.api.Project;
import org.gradle.util.ConfigureUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModsIOExtension {
    public String key;

    public String changelog = "";
    public String tag = "release";
    public String modid;
    public String minecraft;
    public boolean current = true;

    public List<ModsIOProject> projectList = new ArrayList<ModsIOProject>();

    public ModsIOExtension(Project plugin) {
        //For some reason Gradle forces this constructor
    }

    public void project(Closure<ModsIOProject> closure) {
        ModsIOProject project = new ModsIOProject();
        ConfigureUtil.configure(closure, project);
        projectList.add(project);
    }

    public class ModsIOProject {
        public File artifact = null;
        public String changelog = ModsIOExtension.this.changelog;
        public String tag = ModsIOExtension.this.tag;
        public String modid = ModsIOExtension.this.modid;
        public String minecraft = ModsIOExtension.this.minecraft;
        public boolean current = ModsIOExtension.this.current;
    }
}
