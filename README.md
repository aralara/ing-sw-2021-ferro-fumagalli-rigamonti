 # Masters of Renaissance
 
 <img src="https://github.com/aralara/ing-sw-2021-ferro-fumagalli-rigamonti/blob/master/src/main/resources/imgs/bkgs/bkg_loading.png" width="50%" height="50%" align="right">
 
 **Masters of Renaissance** is board game designed by _Simone Luciani_ and _Nestore Magone_ and published by [_Cranio Creations_](https://www.craniocreations.it/).
 
 This project has been commissioned for the **Final Test of Software Engineering** course at [**Politecnico di Milano**] (a.a. 2020-2021) with the goal of recreating the board game as a Java Application.
 
 
 ## Group Members
* [Lara Ferro](https://github.com/aralara) | lara.ferro@mail.polimi.it 
* [Stefano Fumagalli](https://github.com/stefuma19) | stefano14.fumagalli@mail.polimi.it
* [Davide Rigamonti](https://github.com/davide-rigamonti-polimi) | davide2.rigamonti@mail.polimi.it

## Implemented Functionalities

| Functionality           | Status        |
| ----------------------- |:-------------:|
| Complete Rules          | ![YES][TICK]  |
| CLI                     | ![YES][TICK]  |
| GUI                     | ![YES][TICK]  |
| Socket                  | ![YES][TICK]  |
| Multiple Games          | ![YES][TICK]  |
| Persistency             | ![YES][TICK]  |
| Parameter Editor        | ![NO][CROSS]  |
| Local Game              | ![YES][TICK]  |
| Disconnection Recovery  | ![NO][CROSS]  |

## Execution Instructions

* The project is built into an Uber-JAR comprehensive of CLI, GUI and Server.
* All of the execution options are available from a small Command Line Interface integrated into the JAR, therefore no additional arguments are needed in order to execute the application.
* The application can be run utilizing the following command:

    ```shell
      > java -jar MastersOfRenaissance.jar
    ```
    
## Requirements and Disclaimers

* In order to properly run the CLI version of the game a console capable of correctly displaying the **UTF-8** encoding and the **ANSI** color standard is needed.
* The default port for the Server is 1919.
* It is possible to play multiple games at once with only one server, however only one game can be created at a given time and each client that establishes a connection to the server while another user is creating a game, will be put in a waiting state until the game is created or aborted.
* If a user creates a game with multiple players and (while the server is waiting for the required players in order to start) a client inside the lobby disconnects, the game will be aborted but the lobby won't be deleted until the next user connects, causing a premature disconnection for the said user.

## Test-Cases Coverage 

## Utilized Software

## License

[**Masters of Renaissance**](https://craniointernational.com/products/masters-of-renaissance/) is property of [_Cranio Creations_] and all of the copyrighted graphical assets used in this project were supplied by [**Politecnico di Milano**] in collaboration with their rights' holders.

[TICK]: https://github.com/aralara/ing-sw-2021-ferro-fumagalli-rigamonti/blob/master/github/tick.png
[CROSS]: https://github.com/aralara/ing-sw-2021-ferro-fumagalli-rigamonti/blob/master/github/cross.png
[_Cranio Creations_]: https://www.craniocreations.it/
[**Politecnico di Milano**]: https://www.polimi.it/
