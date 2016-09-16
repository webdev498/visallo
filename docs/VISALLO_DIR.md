Visallo looks for `/config` and `/lib` subdirectories
in the following locations:
* specified by system property `VISALLO_DIR`
* specified by environment variable `VISALLO_DIR`
* `${user.home}/.visallo`
* `${appdata}/Visallo`
* `/opt/visallo` or `C:\opt\visallo`

If required, this order can be overridden using a system property or
environment variable `VISALLO_CONFIGURATION_LOADER_SEARCH_ORDER`
specifying a comma seperated list of: `systemProperty,env,userHome,appdata,defaultDir`.
