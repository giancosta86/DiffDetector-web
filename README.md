# DiffDetector-web

*RESTful web services for Diff Detector*


## Introduction


**DiffDetector-web** is the component in [Diff Detector](https://github.com/giancosta86/DiffDetector)'s architecture actually providing and running the web services.

It is a Spring Boot app, so it can be be run just like any self-contained Java application created with Gradle's *application* plugin; for example, it can be executed within a virtual machine in the cloud, without installing an external Java EE server.


![Screenshot](https://github.com/giancosta86/DiffDetector-web/blob/master/screenshots/server.png "Server running in the console and listening on the default port")


## Running the app on your PC

To start the server:

1. Download and decompress the zip archive from the [latest release](https://github.com/giancosta86/DiffDetector-web/releases/latest) page
2. Run the file *bin/DiffDetector-web* (on UNIX) or *bin/DiffDetector-web.bat* (on Windows)


## Server endpoint

When active, the app starts a server listening on **port 8080**.


## The comparison web service - REST reference

As a matter of fact, despite the REST syntax, this web service is *stateful*, as its interface provides operations that internally modify the service's state.


### URLs

Its root path is **/v1/diff** and its subpaths are:

* **/{id}/left** -> **POST**, to set the left item having the given id
* **/{id}/right** -> **POST**, to set the right item having the given id
* **/{id}** -> **GET**, to compare the left and right item having the given id
* **/{id}** -> **DELETE**, to remove the left and right item having the given id. It should always be called after calling the comparison method


### Message formats

#### Setting left and right - Request message

```json
{
    "base64Data": "binary data encoded in standard base64"
}
```


#### Comparison - Response message

```json
{
    "sameLength": true, //Boolean. If false, diffs are always empty
    "diffs": [
        //Zero or more diffs
        {
            "offset": 9, //An integer
            "length": 1 //An integer
        }
    ]
}
```



## Further references

* [Diff Detector](https://github.com/giancosta86/DiffDetector)