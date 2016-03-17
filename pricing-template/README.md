# Caplin project templates -- PREVIEW

* This is a preview of what is to come and can change any time
* Currently all the builds point to an internal artifactory for dependency resolution
 * Either set up an artifactory with Caplin blades and point to this location
 * Or change the build to pick up the dependencies from a folder

# Caplin Pricing Adapter Template

1. clone repo
2. change the project/adapter name in settings.gradle
3. run "gradle assemble"
4. deploy the zip file created in `build/distributions/` into your Deployment Framework
5. start the deployment
