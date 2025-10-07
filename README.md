# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)]

https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkzowUAJ4TcRNAHMYABgB0ATkzGoEAK7YAxABYAzAA4ATFZAxnZGMACzAFBAdVACUUYyQJHUEkCDRMRFRSAFoAPhZqSgAuGABtAAUAeTIAFQBdGAB6BxUoAB00AG8AIibKNGAAWxROgs6YToAaMdwpAHdoGWHRibGUfuAkBEWxgF9MCnyoHLy2cSKQKBRBFABVZoAKHqg+wcnpiTmoGUnV9YQASj2rHYMCOAiEonEUiKxhQYFulAezWeKABYJEYkkEhBuXUBQAYkg0DIYPCoGiYApdDBHsjNAoLsAANYk5owGZIMDBalIgaqYAIBkyKkoAAe8TA0nUINB8ghmKK7VJuzRcqk0ty+xOKCKpLRgOowMyR01hRgwgFlyFVSZ6AAoiLxNgCCl9TRYEbcultEVXGZ3G0uoMpMAYcMxraoPYoEUabyYPzBVShIz0J1dugZJopR6YCqMVCYO1lbL81ic5r2EU0A4EAhXYbcnnIRICudLmAbvdHmjUSXm9i1AoCsIZMTdfI6QzmbIFOrc335YOI1HME3MXOK6d1MvoPXxBvWKa0UoVPbHc7Uib3UcvRgigBWMwARgDnSDEhDQyKnWPylUO6gNMtCJLNZxzNcC0VZpi3BUsDwNU4WUoPU91UY0gVONsrmEBxOURZD5ABTc0MbBcC2AUccLw4BcOCHtVzIsscSHEdiSouiJ0welLmnbMZVg5sim3SNdwgst0IQ7VhJXYj4LdIpf1PB0UCdZJL0Pa9PWQb0YEfF8OjfTFPzDH95BPf8RMA9MQJnOcxIVdi0Rg9F+3LDDtTNWi9VktyDiKdiqggFN1IOOdbzAIpvDMMxX1jQZRm-MZJk6GjOUC4KErGHZgMzWx7CcZxoF4GEAjxOBbT4OBhBgAAZCA4lScKByvIoykqWoGhUKQ1IDOKUFeZR3nmXYr2a9yigQerCXwp5eQG2Z5iI9yB3smE4S7HlBl7AT1yOXEYAJIkkLJeQKSpPrJx42zwMYhVnNVcSNXG6ta1QuTTQAIQouqGrPFSL1dSgwu0u8YF9fSugAWSM0MRgASTQKh+SQYk+vjIl6mgGA3g+BZrNyvjSJ2yDxyEe64N8rUdWabzlokqmYCwjt2Jm+ifKJlzFwomQWdSjihG2zm1T2oczVHTzOT1bimWu-ihZbQtydc+nKxeusfPpz7vqmtA-tUl1RpzcKfWfV9oeDWHOgRpHkFRza+QxrGcfmICM1ljmHoc2j0vQJXdpVxCAqC9A3sp00g+CwHNPgEGIpgKKYoMvmfbQMMI99nLNDsRwXDsFB0ACIJQjzgufscLAms16MSj4W0attKpbTqeouokHqOkeAA5Ob4294O0BGjSxskib6vLmbu5eXu0v7pbJJW26YDW0kJ95QWHuxfbDrHGnTspblel5S6ZcJ+diYVosGPPuTVZrdW6aroovpkMvcL1gHDZvWOTYhzpzY-S2aNeDEmTv3NQKQADkzA1hgBAMEV2NlT72ULKTYAfthYBw8qg0OT0R6MwZMzWirNCI4OOJWGA8RbQAEcHD8juOnNAkwmYoB5rRAEXEpyh0fjAZ+r8wDvzUlHYGGQ47gzNjDL83R7boxAX3YK4C0BQJgDAuBCDcrszPvLISKAECwhQHcUBwV14U2YkUGI-QIA0AlsEFOZ1rEp2Poyd2mjPaKyvvLG+pw1ZcNwfJHh2tjDlwEQbIeRtv5g1NgZf+xkRhAKOoYguMhIHQMEKo-GoE7KL3aAw9Bj0yGITIA4UsPjjimk6IU0snQhFhJEZFaKAZdiZzyjnZwFxiTOGwISZkNVxQwAAOK8mkJXXxppih9Ibs3YwvJer20mAkge1TfHkLWgMoMM1kRzLkegOeWoF7n2hLCFeXc17uI3jkLehId4ESEHYi6HCrpIKybkzx2pvEaxGdXZ+7FgkhTdMInSYiokSMWHE2RM95FJMUSk2B8D0nOOQVBa5aDTkU0wdTJFpDiJnAISgFm3YSEaKxRQiQ1DaEIHoVsxh+D2wsPYuw6WTj3mlM+RRb5yl9a-KBjUgFkSobAtidI4B08bFgMhUolRsKmkaOQSswZdxjGuVMQdS5S9BnwqyVMwYT5JiapQN4SYFhDXPLlq4tYKZVkoB6QkeVKLlZLMQhaq1WBCXjWUTaC1k9LXijuI68UOzDTcI9byJ11Sv61PjvUgyyITK6qfBssY0bvy6u8NGw1Fg4XZwKjIAA7FYMwKAzABFtO4OA5UABs8AcX9LjGkWOw8-FtWqJM6ZHdZnCpTjq3knrB6hTRW681vJ1k93mZ2wYnr-X7hNaWA560ETHK2ra3a2QLlHVQbc+2jj1X7LcWJF5BQ3kPw+U-VltEfmhq0uGwFfKLaSNBe20VyS3UwrURkm627EUnTJoujB9qsG7yEJi11zC8X-uABOki+SPKUJoXQhhTCcWsM5PSzhTKWr+MQ8EM9n8L08t-tEwBgr4mUoUeK1Jkq3aAbwcwi1dxdXjtIcg7mNHdUKqXYOfy4tdVbq0e0XVxq+0VObCUtD5SinNiqdhmO4aE5NMzS4XQOjJozELkgEIYAFO1ggMpgAUhAQk1bBgBHCKAJxwzmWtVKNcDq9RdUzMPlPEd2MEDAAU1AOAEBJpQFHSgOGfAe1-L7U4OQHYaN9U2eC9ArxnOufc557zvn2EurwfEAAasjGQsXoB3GwNFygmWoAAgZcJjSx6MseegFh0JYaAVPn9ECm9Jl8swA7AMJr8QYBoAgMwYAMAbYo0ZuVqAFgX0McXsvDalB6O7vOaLbex1yT7zuUVx577+O-v3XfYrfl0NsvPIIyTxsIl4f5VI+zDswUiohY+iVI2VtaJQaBtbkH0WfuAJRhmwGiH4oFu98h0GyUUoi1S5hGHkNXVQyVnbp72Ufyqzh0GV6-4nbvfMkj0K0lSrph7adsrBi0d5HDGQrHhZKrmxauxurfObru64y+020UHvnmHaMFq+AQEkJ3Lr9pxSVd7dVhHT5jsNaTXGSnfBwGSA611mAooOQJGG3Cmn07Cx8e-XkolFrfuISC1cZjPd539Wa5cfo+Wwcywh9t-L5pEzWmCrzgL-PRFPm8OI4XYw2tYgTJaJMNo0AK8x0z7HglqTYGCygPXC7ptKuuKHq4qrBjcdp6r+n63yBicxFtvxmuDvhITq+XVYYDMoEaRRuTzg7AuZU2pivVJECXFgMAbAOXCBJBSDAWtIj62jNrvXRuzd1B2dmlPHJiznsUiUzHvgH0h3D8peBvZ93xtzuaFNxim9ZsqrXYtjd9yT5gSncHuna-Ka31ehbvxXzod7ZCXz+HojeVI+F6dof5371XahU+jHFGleH9QU9ol2CSWH2OKIGSK8+fa-2sGlK8GNKoOm65+WsGG9uXKjuP8ruACt6hGF2tiYq6O5GNkQBf2EgfSUAwCkMEAAAZhQQYmwvAYeuZjAJzmACOP0ISMgdHIdq4FYFYOgTEmMAAJqODxgXBS7da0TQBIAABeLCzWEA4CzWwQgg-uFG0qi8ugjgkMLmBIFwAA6sEOgDEBRLoDalHuxrmDonovGK9D-ouNkrWGoPIP-q6jEAoDomQCAA4P0JnuHAgDIJQMALwHwJIDoBAAppmDntJpGtehgSZKwbWGpFiJQb1nppCDABIA4KOOgAgFSOcKgMSOXBQmgEbpZDImyJQKoOkZkWgNkWkRsOgOIHjJoG7EAA

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
