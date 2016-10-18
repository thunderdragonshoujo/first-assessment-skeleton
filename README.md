ftd-chat
=========

Your assignment is to complete a chat server and command line client.

The server and client are already capable of communicating. By starting the client, logging in to the server, and sending an `echo` command, the system functions as a simple echo server, repeating the messages sent back to the sender.

There are several more features the chat server requires, and it is your task to complete them.

## Structure

The Java server can be found in the root of the project. It is a maven project, and can be imported into eclipse as such.

The JavaScript command line client can be found in the `./cli` directory of the project. As with other node projects, you must run `npm install` in the root of the JavaScript project before working with it.

## Tasks

### Commands

1. Implement a `broadcast` command that allows a user to send a message to all other connected users.

2. Implement a `@username` command that allows the user to direct message the specified user.

3. Implement a `users` command that retrieves a list of currently connected users from the server and displays them.

4. When no specific command is given, the last command given should be assumed. If no command has been given yet, a message should be shown to the user that indicates a command is required.

### Messages

1. Implement connection alert messages from the server. All connected users should be alerted when another user connects or disconnects.

2. All message types should be displayed in the specified format.

3. Different message types should be displayed in different colors.

#### Message Display Format

```javascript
echo:
`${timestamp} <${username}> (echo): ${contents}`

broadcast:
`${timestamp} <${username}> (all): ${contents}`

direct message:
`${timestamp} <${username}> (whisper): ${contents}`

connection alert:
`${timestamp}: <${username}> has connected`
`${timestamp}: <${username}> has disconnected`

users:
`${timestamp}: currently connected users:`
(repeated)
`<${username}>`
```
