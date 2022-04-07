#!/bin/bash
set -o errexit

## 停止旧jar包
pid=`ps -ef | grep bus-arrive-*.jar | grep -v grep | awk '{print $2}'`
echo "*旧jar进程id：$pid"
if [ -n "$pid" ]
then
kill -9 $pid
fi

##运行新jar包

nohup java -jar bus-arrive*.jar > /etc/anlythree/bus-arrive/shForJenkins/nohup.out 2>&1 &
echo 启动成功"

