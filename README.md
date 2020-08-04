# Freja eID Client

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Freja eID Client is a client library aimed to ease integration of relying party back-end systems with Freja eID Relying Party API.

## Features
This client library provides a set of classes, interfaces and utility methods designed for accomplishing one of the following use cases:

* Authentication Services API Client
  + initiation of an authentication request
  + fetching a single authentication result based on authentication reference
  + fetching multiple authentication results
  + cancelling authentication request
  
* Signature Services API Client
  + initiation of a signature request
  + fetching a single signature result based on signature reference
  + fetching multiple signature results
  + cancelling signature request
  
* Custom Identifier Management API Client
  + setting a custom identifier for a user
  + deleting a custom identifier

* Organisation ID Management API Client
  + adding an organisation identifier for a user
  + deleting an organisation identifier for a user
  + fetching result of adding an organisation identifier for a user
  + cancelling adding organisation identifier for a user

* Integrator Relying Party Management API Client
  + using all available services as an Integrator Relying Party

## Add Freja eID Client to your build
To add a dependency on Freja eID Client using Maven, use the following:
```xml
<dependency>
    <groupId>com.verisec.frejaeid</groupId>
    <artifactId>FrejaEidClient</artifactId>
    <version>2.4.1</version>
</dependency>
```

## Examples
### Init connection to API (test environment)
```java
SslSettings sslSettings = SslSettings.create("/path/to/keystore.jks", "SuperSecretKeystorePassword", "/path/to/server/certificate.crt");
```
### Init, monitor and cancel authentication request
Create authentication client
```java
AuthenticationClientApi authenticationClient = AuthenticationClient.create(sslSettings, FrejaEnvironment.TEST).build();
```
Initiate request
```java
InitiateAuthenticationRequest request = InitiateAuthenticationRequest.createDefaultWithEmail("email@example.com");
String reference = authenticationClient.initiate(request);
```
Poll for result
```java
int maxWaitingTimeInSeconds = 120;
AuthenticationResult result = authenticationClient.pollForResult(AuthenticationResultRequest.create(reference), maxWaitingTimeInSeconds);
```
Cancel request
```java
authenticationClient.cancel(CancelAuthenticationRequest.create(reference));
```
### Init, monitor and cancel signature request
Create sign client
```java
SignClientApi signClient = SignClient.create(sslSettings, FrejaEnvironment.TEST).build();
```
Initiate request
```java
InitiateSignRequest request = InitiateSignRequest.createDefaultWithEmail("email@example.com", "Title", "Text to be signed");
String reference = signClient.initiate(request);
```
Poll for result
```java
int maxWaitingTimeInSeconds = 180;
SignResult result = signClient.pollForResult(SignResultRequest.create(reference), maxWaitingTimeInSeconds);
```
Cancel request
```java
signClient.cancel(CancelSignRequest.create(reference));
```
