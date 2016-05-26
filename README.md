ModsIOUpload
===========
This is a fork of ModsIOUpload that has been updated to newer gradle versions and supports all the features of mods.io

How to use:
-----------
The following is a basic example of use,

```
     buildscript {
        repositories {
            jcenter()
            maven {
                name = "forge"
                url = "http://files.minecraftforge.net/maven"
            }
    		maven {
                name = "Modmuss50"
                url = "http://maven.modmuss50.me/"
            }
        }
        dependencies {
            classpath 'net.minecraftforge.gradle:ForgeGradle:2.2-SNAPSHOT'
    		classpath 'ModsIOUpload:ModsIOUpload:+'
        }
    }
    apply plugin: 'net.minecraftforge.gradle.forge'
    apply plugin: 'net.cazzar.gradle.modsio'


    version = "Mod Version"

    minecraft {
        version = "forge version"
    }

    group = 'ModName'

    modsIO {
        key = "Your API KEY" //this is your mods.io API key, find in settings

        project {
            artifact = jar.archivePath //this is the artifact to use. //TODO clean this
            modid = "Project ID" //found in url of mod
            minecraft = "Minecraft Version"
            changelog = "ChangeLog text"
            tag = "alpha" this is the tag that is set
            current = true //Should this file be the recommended file on mods.io
        }
    }
```

//TODO write some more info here at some point