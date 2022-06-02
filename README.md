Unobtrusively notify users when mod updates are available

Notify aims to not spam you telling you updates are available, rather it optionally logs mod's status to the console,
for servers, or it adds a badge to mod menu (if it is installed).

Notify works on both the client and the server. If you are using it on a server, it is recommended to set
the `verboseLogging` option to `true`.

# Dependencies

- Fabric loader >= 0.12.5
- Cloth Config API

# Suggested Mods

- Mod Menu - adds badges to mods:

![Demo of Mod Menu Compatibility](mod_menu_demo.png)

# Mod Authors

There are multiple ways you can support Notify. If you want to 'set and forget', I recommend using
the `gradle.properties` or Modrinth/CurseForge API options.

## Using a JSON File (NO LONGER RECOMMENDED, BUT SUPPORTED):

Inside your `fabric.mod.json`, add a new key under the `custom` field:

  ```json
"custom": {
    "notify_version_url": "https://raw.githubusercontent.com/JamCoreModding/Meta/main/data/mod_versions.json"
}
  ```

The URL should link directly to a JSON file (if you are using GitHub to host as I am doing above, make sure you use the
raw link) that follows the schema documented below. The URL above is a good example to follow. I recommend hosting it on
a simple GitHub repository as I am doing.

#### JSON 'Schema'

The JSON file you link for Notify to use should follow this format:

```json
{
  "your_first_mods_id": {
    "1.16.5": "1.0.5",
    #
    The
    latest
    version
    for
    1.16.5
    "*": "1.0.6"
    #
    The
    version
    to
    use
    if
    the
    specific
    Minecraft
    version
    is
    not
    specified
  },
  "your_second_mods_id": {
    "*": "0.0.1"
  }
}
```

If you do not provide a version for a specific Minecraft version, Notify will use the version specified using the
wildcard operator (`*`). If you haven't specified a version or a wildcard version, or if there is a different failure,
Notify logs the error in the console but does not obstruct the user.

### Using a Gradle Properties File:

To use a `gradle.properties` file, you must set two different values in your `fabric.mod.json`:

```json
"custom": {
"notify_gradle_properties_url": "https://raw.githubusercontent.com/JamCoreModding/Notify/main/gradle.properties",
"notify_gradle_properties_key": "mod_version"
}
```

The values shown above link to this mods `gradle.properties` file, and use `mod_version` as the key. For example, this
is the file:

```gradle.properties
#Mod properties
mod_version=1.1.0
maven_group=io.github.jamalam360
archives_base_name=notify
```

In this instance, using the above config, Notify would find `1.1.0` as the version.

Using `gradle.properties` is ideal if you update that file whenever you update your mod anyway.

### Using the Modrinth/CurseForge API

Currently, only the Modrinth API is supported, but I am working on the CurseForge API. To use this method you must
simply have a field in your `fabric.mod.json` set to the URL of your mod on Modrinth or CurseForge, for example:

```json
"contact": {
"homepage": "your-modrinth-url"
}
```

```json
"contact": {
"any-custom-field": "your-modrinth-url"
}
```

You can use any field name as long as you have a Modrinth URL within the field.

Notify will automatically get the latest version of the mod available on Modrinth or CurseForge.

### _That's it!_

Your mod now supports Notify, it's that easy!
When Fabric is launched, Notify will get all the mods that have support and parse their versions and check that against
the version entered in their `fabric.mod.json`. It then adds a badge against their mod on modmenu to show if they are
up-to-date or not:
