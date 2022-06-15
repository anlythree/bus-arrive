#!/usr/bin/expect

##jar包目录
var jarPath = "/"
##jar包名称
var jarName = ""

##连接到远程服务器
spawn ssh anlyconnect@43.132.237.240 -p 233

##捕捉密码并登录
expect"*assword"
send"Anly233\r"

expect"Lastlogin:*"

##查看已经在运行的jar包，并kill掉
send"ps -ef|grep bus-arrive\r"


send"exit\r"

interact
