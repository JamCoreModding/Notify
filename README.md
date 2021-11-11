Unobtrusively notify users when mod updates are available

## Mod Authors

Adding support for Notify is a trivial process.

Inside your `fabric.mod.json`, add a new key under the `custom` field:
  ```json
  "custom": {
      "notify_version_url": "https://raw.githubusercontent.com/JamCoreModding/Meta/main/data/mod_versions.json"
  }
  ```
  
  The URL should link directly to a JSON file (if you are using GitHub to host as I am doing above, make sure you use the raw link) that follows the schema documented below. The URL above is a good example to follow. I recommend hosting it on a simple GitHub repository as I am doing.
 
 
_That's it!_

Your mod now supports Notify, it's that easy! When Fabric is launched, Notify will get all the mods that have support and parse their JSON files and check that against the version entered in their `fabric.mod.json`.

### JSON 'Schema'

The JSON file you link for Notify to use should follow this format:

```json
{
    "your_first_mods_id": {
        "1.16.5": "1.0.5", # The latest version for 1.16.5
        "*": "1.0.6" # The version to use if the specific Minecraft version is not specified
    },
    "your_second_mods_id": {
        "*": "0.0.1"
    }
}
```

If you do not provide a version for a specific Minecraft version, Notify will use the version specified using the wildcard operator (`*`).
If you haven't specified a version or a wildcard version, or if there is a different failure, Notify logs the error in the console but does not obstruct the user.
