[![Maven](https://github.com/servicecatalog/oscm-basyx/actions/workflows/maven.yml/badge.svg)](https://github.com/servicecatalog/oscm-basyx/actions)
[![codecov](https://codecov.io/gh/servicecatalog/oscm-basyx/branch/main/graph/badge.svg)](https://codecov.io/gh/servicecatalog/oscm-basyx)

# oscm-basyx
The OSCM BaSyx Connector. This tool is meant to enhance OSCM for discovering and importing service data privided from asset administration shells.

## Required
Latest OSCM is up running on your server <OSCM_HOST> (install dir referred as /docker). Checkout [this Readme](https://github.com/servicecatalog/oscm-dockerbuild#quick-start-oscm-with-docker) for getting started with OSCM.

## Prepare
Install the [Basyx off-the-shelf components](https://wiki.eclipse.org/BaSyx_/_Documentation_/_Components#Off-the-Shelf-Components) Registry and AAS Server on your OSCM docker host.
Create the configuration under /docker/config/basyx/aasServer and /docker/config/basyx/registry.

The scripts contained in the oscm-scripts directory shall help you integrating the two services in your OSCM docker network. Ensure the volume mounts in the docker-compose yaml file are satisfied and copy all into your /docker directory, where OSCM is installed.

## Environment
Following environment variables are required.

``` 
OSCM_HOST=<fully qualified named of your OSCM server>
AAS_REGISTRY_HOST=<Host or IP>:9082

## OSCM user (needs user roles service manager + technology manager)
API_USER_KEY=<User key, e.g. 10000>
API_PASS=<OSCM password> 
```
Add the variables in /docker/var.env

## Building from Source
Clone this repo and import it in your ```<workspace>.```

### Eclipse
1. Locate the pom.xml file and choose *Run As->Run Configurations...* from the context menu.
2. Name the Maven Build configuration, enter Goals: *clean install*.
3. On the tab *Environment* add the variables above and press *Run*.

### IntelliJ
1. Enter Settings CTRL+ALT+S, search and select "Maven Runner".
2. In the field *Environment variables*, place the above variables as semicolon separated list in form var1=value1;var2=value2; etc.
3. Open the Maven Tool window and run *clean* and *install* from the Maven lifecycle tree.

## Deploy
1. Add the variables in var.env.
2. Restart OSCM.
3. Deploy oscm-basyx discovery.
```
docker cp <workspace>/oscm-basyx/target/discovery*.war oscm-app:/opt/apache-tomee/webapps/discovery.war
```
## Usage
1. Expose the above variables in your shell environement (export ```OSCM_HOST=...``` etc.).
2. Deploy your AAS as aasx file in the registry, therfore place it in /docker/config/basyx/registry.
3. Ensure it loads with ```http://$AAS_REGISTRY_HOST/registry/api/v1/registry```
4. Run ```import.sh <TS_ID>```
