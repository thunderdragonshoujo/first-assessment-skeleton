import vorpal from 'vorpal'
import { words } from 'lodash'
import { connect } from 'net'
import { Message } from './Message'
import {timestamp } from './timestamp'

export const cli = vorpal()
const chalk = require('chalk')
let username
let server
let lastCommand

function isDmCommand(input){
  return input.includes('@')
  }

function getDmUser(input){
  let input2 = input.replace('@','W')
  let result = _.words(input2)
  let WuserArray = result.filter(w => w.startsWith('W'))
  let Wuser = WuserArray[0]
  let startIdx = Wuser.indexOf('W')
  let endIdx = Wuser.indexOf(' ',startIdx)
  let dmuser = Wuser.substring(startIdx +1)
  //console.log(Wuser)
  //console.log(startIdx + ' ' + endIdx )
  //console.log(dmuser)
  return dmuser
  }

cli
  .delimiter(cli.chalk['yellow']('ftd~$'))

cli
  .mode('connect <username> <host> <port>')
  .delimiter(cli.chalk['green']('connected>'))
  .init(function (args, callback) {
    username = args.username
    server = connect({ host:args.host,port:args.port }, () => {
      server.write(new Message({ username, command: 'connect' }).toJSON() + '\n')
      callback()
    })

    server.on('data', (buffer) => {
     let msg = String( Message.fromJSON(buffer).toString())
     if(msg.includes('echo')){ 
      console.log(chalk.red(('>> ') + (msg)))
     }
     if(msg.includes('all')){ 
      console.log(chalk.yellow(('>> ') + (msg)))
     }
     if(msg.includes('whisper')){ 
      console.log(chalk.purple(('>> ') + (msg)))
     }
     if(msg.includes('connect')){ 
      console.log(chalk.green(('>> ') + (msg)))
     }
     if(msg.includes('disconnect')){ 
      console.log(chalk.orange(('>> ') + (msg)))
     }
    })

    server.on('end', () => {
      cli.exec('exit')
    })
  })
  .action(function (input, callback) {
    const [ command, ...rest ] = words(input)
    const contents = rest.join(' ')
    //console.log(input)

    lastCommand = command

if(isDmCommand(input)){
  let command = 'username'
  let username = getDmUser(input)
}
    
    if (command === 'disconnect') {
      server.end(new Message({ username, command }).toJSON() + '\n')
    } else if (command === 'echo') {
      server.write(new Message({username, command, contents }).toJSON() + '\n')
    } else if (command === 'broadcast'){
      server.write(new Message({username, command, contents}).toJSON() + '\n')
    } else if (command === 'users'){
        server.write(new Message({username, command }).toJSON() + '\n')
     } else if (command === 'username'){
      server.write(new Message({username, command,contents}).toJSON() + '\n') 
    }else if (command == null || command == ''){
      let command = lastCommand
      console.log('command was null' + lastCommand)
      server.write(new Message({username, command,contents}).toJSON() + '\n') 
    }else {
      this.log(`Command <${command}> was not recognized`)
      console.log('trying last command ' + lastCommand)
      let command = lastCommand
        server.write(new Message({username, command,contents}).toJSON() + '\n')
    }

    callback()
  })
