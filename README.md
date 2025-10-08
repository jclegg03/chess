# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)]

https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZMAcygQArthgBRKJagadUFMADWMAKoHYAdyQwAAsYKz80YABbQ2AEN2AOAE8YFAAPJFUwDWMYAFo8gD4ZbSUVdQAuGABvX0oAX0xZeTK1VXyi8koqUSq6qGbgTGEelXzitk5uKCrg9i4UAAoFePdkgBVPdDs0lWwCCDQASkxJhdgC4pHKKpZ19YAFGDdVbEODYe7KDuLEVAwqgAWAAMAGYADpoaoAIhi6mAZhQ0Kq0P6kRiMEyMHkHnQADpoY10BxTK53F5pAAheyOaCaHQ-EotZRtKrGBxOJqlFnqRkjXrs2lQT4iMadM7TWbzbiLQZ6Aw7PYHY6naXfS5dag3Vj3J4vN5oD7XC6dP7oMBVACswOBkJhcNUCKRKLl+kMlCcBKJnFJCS8lMSMAAMhAzEg0PTGYNWpUao1ozz2hr+SoKmgrAgECLRoZxWqZjA5lMlgGOCGw2hFSh9khDicJerOsbbrrnmoDUavibfshzUDgQBGO2wtpO5EwVERaKxTgwXDqfzQDhGQ4AcmYUTkIGCXswxN95JgpZgCisIUjGoT5VUVWq8e51753V66cz2dE41Y+alxcWpdPIRVjWdaqsW3aajQBZ3I8bavO8YjGoyZoAjAIJDlC0IALKjoiyLQgAmtYMBROEzA6IYCChoiy7htiEAwKAj4ANp+DmAC6hJ7j6mBkp4R6BnAECUcKKa5hM36Fmqf6JIJwlAcq9b5k+WpQa2+rwdmjY9v8FqoTaw7YfCuEorJ0DYu4USmbAWJoBAzDADAVBxEgy4gEJ0C7vuPF+jAADi04rmoaDrqkGRZBenRXqycZcsyj7Js+qavlmomfg2BZFgsiz+TE0gQGoABydk7JkYDybWKrpcpkEtjB6mGghXZIb2KEgoCBk4c6E5mAFTE9TEBXTmxgXtLZzDpKVnncbxXhWQxqyJCkOLoO+YriWBP5ZVZKwJBsWyVrs1YKaB5zVdq0F6u2GmIRqyG6SCtoYYZjrGROc1MW5wnDVicS7Ut+1TSSXFA+aUBJK8oDhmYMDAniACc5iWDYADEgKggAHAATHDIAwMjyBmMEYA6AgViGAASigYZZFAcgVZgd2fs2MBMQ8ADyZDrMNAD04SUMOfNQOizrQhOAA0E7zqoi5QBweHixOKCbkgCDy5xxp5htMCYVs-TpYsF1tgAjmTWSKWBTOJSgVQgAkYAoP0iw67iev5icqWRQ+0WImAjuC8LJxRbynTGBUABi4bLv0gwwDoKT+wFtGCxo3mHv0MCBCEYRThiv1rCkE1ZBoXm5JeXuxnesWKIm1W9DAyWrWJTLV9eFS2+49t+34gyB+XSbFKHCgcFH3faHR2eUCnM3N-KhgcC5wXMLt3P+FAQRz9owOmKXntxdFtR+Pee-B02Vt9KP8imB7Vxn+3cgoABwSLILPeN5+Qc3gxw+P4swBnsEr8P6fkHsPE8-8Y6QAYv-A8fFXQGCMAvEKy9V7ryMJvEuDIy7H0-tUR+gwj4tzaLXVMYCQiDDfprc4VRVBWETMsf+6wIC4kqkpBKKlaqXTgg1TS4E7pVExvpDCf8QiMOYaLFEE5vRAwsNYbAyNoDsERHjMOcA7DSDgAoYMoZwwMxaswU+7CWbs05jzAw6gKp2gTjECWUsZYcEaDddaVCtEVn1obNwJs1BgHNqdAxOYqiUQrIscs4Z0ruytu-PuFQfZd0oAHKuMZ+5GB0FUCOs5o5jzjhPIWic0DZKnj5KkNJOT7mnseEJeSt4RWKB-W8GT5AEMScQ62PgL5DGvhBOutilw6zANuRY9TgASwqWEmB-pAwVKqR00SNs7YP3-s-Np4TRRN1qV-DgP9hEAO0L3bBwCUknlAXgsekCtljJkNSDkdIMFRiiTUR+oj0CNJrmw-xpDgiPIjB09K1DaHXnoSIph6AfHTDOqpOqV1uE3VNHo-hgiYRbM+eOUWnEvIyJRhYFA6A8YEyJpi7F5ZrBYEZn47UTFpB2CDHYdYdhTFtAsVCRFQK0AOKapQyULiiVuLUigTxZsTqgr8XXQJRLgmhi5W7ChNS7kcBQNwe2v8GHMt2YQ4OA8DmUyiBAGg7zPmxxSA85lMAABmlgohQPPKnWBlyhRVJ3tK7Bt5DXMOefFIVJDnUrW+RJGhdCQVaQgudHlXDOwqWajpOFj1K5VPRbYAAapQJAxqDXQO9VrH2nq0CLH9eBGZurmVSubokqoGaFmZpVU0kOBy0nLmOfIfV+bmGYjyWcq1FIbUlJ9GUwMj87WYN3qqnBdbgCuqIa8uuw6Y1IzkW4ZcyNsDhi8EGUqflpwaBJTfQxTFfLUp5v1FAdomUup4WlCSvkUBgBymofWR7gUCvVJut5Psr2qGyhel9ozC15sgkmpIP9z2XrXZ+jpazn1ruzQkl56rUmRxgAAKmbTAfdBTDxFKucKG5WDB23n3QOCW+7MYSzxMR0dJ9H11yvcu8K0yz7ES2FewaMQqNgGytOZjyycynq1r6-5DG105rBRw2CHZGphturCmAAio3QmFsi3DsmJYyenHJ6cmNZPEbxFI0wsbkYcAAOxw2BCgYEeM7CgjgKogAbPAOZq6MS6J0pbLdxiuYwG5vuw9SrmH4enIxlArKxNOI5c7FAV7uUQr5d4+9FxyMkM3LiK9Tt6PTlGTRlZFQf3Jp-iFsLkqv20bvvbRL+6-McY-AO4t6zEtXorVB5JVQh7LivePZD5y0O2swxVxMFRqj7tI0md1LSr2Fp+TAHjbRWMxAE68oNEKQ2icguGvskn4XQn3eOGEsmJHQkU9gBAwAkiTw29CPbB3KADmRQrE7+3DtQExpdwkimdAQDwHLGo0JgAXe24p4A93tswA0-UMWkIJwQB0KxI7t5oRg4h1AL7E4FaA604jWRyNDuZggP4HFSBCZgHR5RLHAApCAtFmvIxJqADwDnzROZqkY7wrn3PTk84C7zc4buUCsj5ga04AuLfZdqYn4ZctgQNjyyL03YstIAFYk7QIloX8uUuSrS28zLf6FmK5F+cMra1OkkPV1ZRYWvldgTxJ9aAJw238WXFZKZESuutzAzERY+6ACSHBavxWgzAGtdnDBZJK7kpDa62sduuV2nyzWOD5VUIvUKpU+23MdTUPrkG3VS6qMN1XdcbBaCK9OV304JaCz8xLe20QrJW+nnNPOi1sT7XtysyJKe8-32K9OL3Y6ffeGwPnwwzXaKtftUW7rqfefp7HYNrP04Rs+r+RNmr0XBM6jmyJk94mI0rek+tqHW2Ec7clhz5wx3Tu3fhyi3bx+-sH6BxOZ7r3RYIov1d37D2AckeB3k6H4OuyqGOzDl2C-ofkjqYGitOsjBYAdtjrjlASkIgO4LAMANgHtoQHTIcDANTqQKSgWOSpStSrSm5sYHaHzg+l+FrAgXgHAigOFpdBLsvtPkYHKheigFSIsJQQMNoLPKlg7g6thkwfKg7H4K+l3mqvVjIMwfbPBohsnGHsUhHkDCPmstUC-NoHhtkoMIRh-pppPmRvri0gfJQIMOoYMloYDlfLwaPq3AISwT-KIUkqHBIYIdIbRK2tPO1p2oof2nwZVrgl5ugOoZmmYSRroQNpnvcv4WgIEZEcEToYWmsrKoIS+hBkAlWlUNIJIYYAhkPqHtbh4QoaYNUlYfvLhtzigLEf1s0reFeuoVehUXPtxgvuoBBlVDNuCpwuvtCtpMtlJh1EZF1LCCrMgO8DABAMao5CTuUGNlYMPOgAgCkLbKgMuESohvbEKAxLOP4JQIYDQrMWgPMWNirOgCoHLI0FvEAA

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
