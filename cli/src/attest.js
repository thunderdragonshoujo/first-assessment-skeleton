var _ = require ('lodash')

var str = "@username";
var n = str.search("@");
//console.log(n)



if(str.startsWith('@')) {
    //console.log('it matches')
   let mod = str.replace('@','')
   //console.log(mod)
}
function getDmUser(){
let input = 'echo , broadcast, @ftd, connect, disconnect, user'
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
console.log(getDmUser())

