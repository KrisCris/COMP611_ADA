# A Java chat room application
---

## Introdution
---
This is an application of a client/server chat system with GUI, which is based on 100% Venalla Java.
As it is named, it provides functionalities for chatting with people using this app in real-time.
And it implements the MVC pattern.

## Components
---
#### Directory structure
- assignment_1
    - Model
        - Client.java
        - Server.java
        - Message.java
    - View
        - Login.java
        - ChatRoom.java
        - GBC.java
        - CommonFunc.java
    - Controller
        - ClientController.java
    - Test
        - ClientDemo.java
        - ServerDemo.java
- readme.pdf
#### Description
##### Client side
- ClientController.java
    - It is the controller of the client application.
    - It observes a model and 2 views.
    - Model : Client.
    - View : Login and ChatRoom.
    - This class contains a large amount of methods modifing data in Model, calling methods in GUI to change the way it looks and listening for every event from the GUI.
    - It also maintains an inner class called *MessageHandler* which implements Runnable and will be put into a separate thread listening for messages from the server.
- Client.java
    - This class is designed as a basic model of Client.
    - It holds serveral essential data for the application functionality. Like a socket and IO streams for client-server communications, a username, user and message history lists, etc.
    - And it will provide data to the controller if necessary.
- Login.java
    - It provides users a login interface.
    - It maintains a lot of GUI components and methods.
- ChatRoom.java
    - It provides users a GUI to chat with each other.
    - It maintains a lot of GUI components and methods.
- GBC.java
    - A class that extended GridBagConstraints.
    - It provides several methods that make creating a constraint easier.
- CommonFunc.java
    - A interface provides some common methods for the two GUI class above.
- ClientDemo.java
    - The test class for Client.
##### Server side
- Server.java
    - The Server class contains most of the methods that a chat server needs.
    - It has a inner-class listenning for requests from a client.
    - It maintains some data structures and variables like maps that storing each username with its socket and a String that decides whether the server should be shutdown.
- ServerDemo.java
    - The test class for Server.

##### Miscellaneous
- Message.java
    - A class provides the model of message.

#### More information
- ClientController.java is the controller of Login.java, ChatRoom.java and Client.java.
- ServerController.java should be the controller of ServerCLI.java and Server.java.
- MVC pattern on Client side is fully implemented.
- However, due to the limit of time, I am not able to implement MVC pattern for server side. So **ServerCLI.java** and **ServerController.java** currently are not available, so that I will not place them in the zip file for assignment. 
- Class GBC in GBC.java implemented the *GridBagConstraints* as it is recommanded in Core Java, in order to make my life easier position components in the GridBagLayout
- Any other details about codes should be mentioned in these **.java* files as code comments.

## Features
---
#### Client/Server protocol
  - It uses the TCP protocol.
#### Design Pattern
  - The Client application is using the MVC Pattern.
#### Functionalities
##### Server
 - Forwarding messages from one client to another.
 - Sending notifications to clients, like the server shutdown warning.
 - Notifing every client if someone is online/offline.
 - Able to deal with different types of client request like switching between different modes.
 
##### Client
 - Chat with each person that on the same mode (Typically Online) .
 - Easily switch chat among different person by clicking their name.
 - Can switch between different modes by clicking the username button in the top-left side corner.
 - You can switch to "Stealth Mode" with a custom code, on which only clients sharing the same stealth code can see each other. And you can switch between different stealth group  or online again easily clicking the same mode-switching button.
 - It is easy to tell if the one you are chatting with is offline.
 - It will automatically receive notifications from the server.

## User documentation
---
>***This application is developed on [JDK 1.8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)***

#### How to run this project
1. Open this project with a JAVA IDE.
2. Run the Server.java file.
3. Run the Client.java file.
4. After they started up correctly, everything should be straight forward.
**Note:**
**1. You can run multiple client applications as you configured your IDE correctly.**
**2. The server/client is automatically running on `127.0.0.1:2333`.**

#### How to quit this application
##### Server
1. Input `q` in the CLI.
2. Then it will setup a timer and sending notifications to all the clients.
3. After time out, any clients online will be forced to logout.
4. It will shutdown automatically after all the clients logged out.
##### Client
1. Simple click the close button system provides.
2. Or click the username button and select **Offline** to logout.
3. Both actions above will result in client logout and login page show up.
4. Then you can shutdown this app by turn off the login page.

#### Using this app
##### Login
 - You can enter a random name for login. But there should be any blanks.
![login image](https://github.com/KrisCris/COMP611_ADA/raw/master/src/assignment_1/image:pdf/1.png)
##### Chatroom
 - When you login, you can see the number of current online clients and the name of each of them.
![Logged_in_1](https://github.com/KrisCris/COMP611_ADA/raw/master/src/assignment_1/image:pdf/2.png)
 - You can click anyone available on the left side to start a chat.
 - And when clients you are not currently chat with sent you a message, their name will be highlighted with Italic and green. E.g. zsk sent me a message.
![chat_notify_3](https://github.com/KrisCris/COMP611_ADA/raw/master/src/assignment_1/image:pdf/4.png)
 - You can logout by closing the chat room or clicking your username on the top-right corner.
![Logout](https://github.com/KrisCris/COMP611_ADA/raw/master/src/assignment_1/image:pdf/5.png)
 - Or you can switch to the stealth mode.
![stealth_1](https://github.com/KrisCris/COMP611_ADA/raw/master/src/assignment_1/image:pdf/6.png)
![stealth_2](https://github.com/KrisCris/COMP611_ADA/raw/master/src/assignment_1/image:pdf/7.png)
 - When you are in stealth mode, the online number label will turn dark.
 - Only people using the same stealth code can see each other.
 - You can also switch to other stealth group if you want by choosing the stealth mode again. Or you can go online.
![stealth_3](https://github.com/KrisCris/COMP611_ADA/raw/master/src/assignment_1/image:pdf/8.png)

## UML diagrams
---
 - Class Diagram
![class](https://github.com/KrisCris/COMP611_ADA/raw/master/src/assignment_1/image:pdf/classes.png)
 - The Dependency among all the classes
![dependency_classes](https://github.com/KrisCris/COMP611_ADA/raw/master/src/assignment_1/image:pdf/dependency_class.png)
- Client-Server communication
![client-activity](https://github.com/KrisCris/COMP611_ADA/raw/master/src/assignment_1/image:pdf/client_activity.png)
- Server shutdown 
- ![server-activity](https://github.com/KrisCris/COMP611_ADA/raw/master/src/assignment_1/image:pdf/Server.png)










