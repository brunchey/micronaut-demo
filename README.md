## Micronaut with Axon-Framework example
This is based on the [gift-card demo] (https://github.com/AxonIQ/giftcard-demo) application, but is only an API.
The initial application was generated using the micronaut launch applicaiton (https://micronaut.io/launch/)

The application supports sending the following http commands to manage a giftcard

Issue
localhost:8080/giftcard?id=[your id here]&amount=[int]

Redeem
localhost:8080/giftcard/{id}/redeem?amount=[int]

Reload
localhost:8080/giftcard/{id}/reload?amount=[int]

Cancel
localhost:8080/giftcard/{id}/cancel


## Micronaut 2.5.11 Documentation

- [User Guide](https://docs.micronaut.io/2.5.11/guide/index.html)
- [API Reference](https://docs.micronaut.io/2.5.11/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/2.5.11/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)
