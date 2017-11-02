import vorpal from 'vorpal'
import { words } from 'lodash'
import { connect } from 'net'
import { Message } from './Message'

export const cli = vorpal()

let username
let server

cli
  .delimiter(cli.chalk['yellow']('ftd~$'))

cli
  .mode('connect <username>')
  .delimiter(cli.chalk['green']('connected>'))
  .init(function (args, callback) {
    username = args.username
    server = connect({ host: 'localhost', port: 8080 }, () => {
      server.write(new Message({ username, command: 'connect' }).toJSON() + '\n')
      callback()
    })

    server.on('data', (buffer) => {
      this.log(Message.fromJSON(buffer).toString())
    })

    server.on('end', () => {
      cli.exec('exit')
    })
  })
  .action(function (input, callback) {
    const [ command, ...rest ] = words(input)
    const contents = rest.join(' ')

    let atUser
    if(command.startsWith('@')) {
      console.log('it matches')
     let atUser = command.replace('@','')
     console.log(atUser)
  }


    if (command === 'disconnect') {
      server.end(new Message({ username, command }).toJSON() + '\n')
    } else if (command === 'echo') {
      server.write(new Message({ username, command, contents }).toJSON() + '\n')
    } else if (command === 'broadcast'){
      server.write(new Message({username, command, contents}).toJSON() + '\n')
    } else if (command === 'users'){
        //server.write('Listing Users' + '\n')
        server.write(new Message({username, command }).toJSON() + '\n')
     } else if (atUser == 'username'){
      server.write(new Message({atUser, command,contents}).toJSON() + '\n') 
    } else {
      this.log(`Command <${command}> was not recognized`)
    }

    callback()
  })
