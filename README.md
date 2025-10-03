# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)]
(https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)
(https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkzowUAJ4TcRNAHMYABgB0ATkzGoEAK7YAxABYAzAA4ATFZAxnZGMACzAFBAdVACUUYyQJHUEkCDRMRFRSAFoAPhZqSgAuGABtAAUAeTIAFQBdGAB6BxUoAB00AG8AIibKNGAAWxROgs6YToAaMdwpAHdoGWHRibGUfuAkBEWxgF9MCnyoHLy2cSKQKBRBFABVZoAKHqg+wcnpiTmoGUnV9YQASj2rHYMCOAiEonEUiKxhQYFulAezWeKABYJEYkkEhBuXUBQAYkg0DIYPCoGiYApdDBHsjNAoLsAANYk5owGZIMDBalIgaqYAIBkyKkoAAe8TA0nUINB8ghmKK7VJuzRcqk0ty+xOKCKpLRgOowMyR01hRgwgFlyFVSZ6AAoiLxNgCCl9TRYEbcultEVXGZ3G0uoMpMAYcMxraoPYoEUabyYPzBVShIz0J1dugZJopR6YCqMVCYO1lbL81ic5r2EU0A4EAhXYbcnnIRICudLmAbvdHmjUSXm9i1AoCsIZMTdfI6QzmbIFOrc335YOI1HME3MXOK6d1MvoPXxBvWKa0UoVPbHc7Uib3UcvRgigBWMwARgDnSDEhDQyKnWPylUO6gNMtCJLNZxzNcC0VZpi3BUsDwNU4WUoPU91UY0gVONsrmEBxOURZD5ABTc0MbBcC2AUccLw4BcOCHtVzIsscSHEdiSouiJ0welLmnbMZVg5sim3SNdwgst0IQ7VhJXYj4LdIpf1PB0UCdZJL0Pa9PWQb0YEfF8OjfTFPzDH95BPf8RMA9MQJnOcxIVdi0Rg9F+3LDDtTNWi9VktyDiKdiqggFN1IOOdbzAIpvDMMxX1jQZRm-MZJk6GjOUC4KErGHZgMzWx7CcZxoF4GEAjxOBbT4OBhBgAAZCA4lScKByvIoykqWoGhUKQ1IDOKUFeZR3nmXYr2a9yigQerCXwp5eQG2Z5iI9yB3smE4S7HlBl7AT1yOXEYAJIkkLJeQKSpPrJx42zwMYhVnNVcSNXG6ta1QuTTQAIQouqGrPFSL1dSgwu0u8YF9fSugAWSM0MRgASTQKh+SQYk+vjIl6mgGA3g+BZrNynynsks4GQ7diZvojNNDsRwXDsFB0ACIJQnpxmfscLAmok+SSj4W0attKpbTqeouokHq+lo9L0By6n8pcC5iWcbBCWZGrxRgABxXlpC5omeeKTXBZF4xeTaVLgml1Iqby2nnBkAB2KwzBQMwAltdw4HKgA2eBSdUbXBhgNIQeYbnTTa6oTbNyW0qC9AA1NwYADleXxuW7d0FBawgGYmaQEIwCznO84AKQgQktbjZxwlARkQ4yMP9Yj0prg6+ok5Qc2pfjtAA2wBBgCzqA4AgSaoEmTu4b4dPbYKuwh-zwuF6pRBLlgYBsAHwgkhSYO9eOCO+YFoWRfUWWgA)

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
