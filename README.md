[![Apache License](https://img.shields.io/badge/license-Apache%202.0-orange.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Support](https://img.shields.io/badge/Support-Community%20(no%20active%20support)-orange.svg)](https://docs.mendix.com/developerportal/app-store/app-store-content-support)
[![Studio](https://img.shields.io/badge/Studio%20version-8.0%2B-blue.svg)](https://appstore.home.mendix.com/link/modeler/)
![GitHub release](https://img.shields.io/github/release/JelteMX/mendix-jsontotree-module)
![GitHub issues](https://img.shields.io/github/issues/JelteMX/mendix-jsontotree-module)

# JSON To Tree Module for Mendix

![Icon](/assets/AppStoreIcon.png)

You have a simple JSON that has a nested structure (children) and you want to import this into Mendix?

## Java Actions

- Convert JSON to Mendix Object Tree (non-persistent)

This will create a tree of non-persistent Node objects that each have a parent, and are bound to a Tree object. See Domain-model:

![Domain](/assets/DomainModel.png)

How you follow up with that is up to you. The module includes a few examples of how you can create a Tree structure. This is showcased in the test-project:

[https://treecreatortestapp-sandbox.mxapps.io/](https://treecreatortestapp-sandbox.mxapps.io/)

## Libraries used

- Jackson Core
    - Version 2.10.2
    - License: Apache 2.0
    - [link](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core/2.10.2)
- Jackson Annotations
    - Version 2.10.2
    - License: Apache 2.0
    - [link](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations/2.10.2)
- Jackson Databind
    - Version 2.10.2
    - License: Apache 2.0
    - [link](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind/2.10.2)

## License

Apache 2
