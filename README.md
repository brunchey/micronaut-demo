## Micronaut with Axon-Framework example
This is based on the [gift-card demo] (https://github.com/AxonIQ/giftcard-demo) application, but is only an API.
The initial application was generated using the micronaut launch applicaiton (https://micronaut.io/launch/)

## Prerequisites

- Java 11

## Running the application(s) locally

**Requirements**

> You can [download](https://download.axoniq.io/axonserver/AxonServer.zip) a ZIP file with AxonServer as a standalone JAR. This will also give you the AxonServer CLI and information on how to run and configure the server.
>
> Alternatively, you can run the following command to start AxonServer in a Docker container:
>
> ```
> docker run -d --name axonserver -p 8024:8024 -p 8124:8124 axoniq/axonserver
> ```

## API Calls for managing a giftcard
The application supports sending the following http commands to manage a giftcard

List all giftcards
GET from localhost:8080/giftcard 

Issue
POST to localhost:8080/giftcard?id=[your id here]&amount=[int]

Redeem
PUT to localhost:8080/giftcard/{id}/redeem?amount=[int]

Reload
PUT to localhost:8080/giftcard/{id}/reload?amount=[int]

Cancel
PUT to localhost:8080/giftcard/{id}/cancel


## Micronaut 2.5.11 Documentation

- [User Guide](https://docs.micronaut.io/2.5.11/guide/index.html)
- [API Reference](https://docs.micronaut.io/2.5.11/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/2.5.11/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)
