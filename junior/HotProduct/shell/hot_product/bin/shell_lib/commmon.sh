#!/bin/bash

function print_msg(){
  help='print_msg 消息内容 [-err | -waring | -info]'
  if [[ $# -eq 1 ]]; then
    echo -e "\033[42m \033[01m $1 \033[0m"
  elif [[ $# -ge 1 ]]; then
    msg=$1
    opt="$2"
    shift
    case "$opt" in
      -err)
        echo -e "\033[41m \033[01m $msg \033[0m"
      ;;
      -waring)
        echo -e "\033[43m \033[01m $msg \033[0m"
      ;;
      -info)
        echo -e "\033[42m \033[01m $msg \033[0m"
      ;;
      *)
        echo -e "\033[41m \033[01m print_msg错误: $help \033[0m"
        exit 1
      ;;
    esac
  else
    echo -e "\033[41m \033[01m print_msg错误: $help \033[0m"
    exit 1
  fi
}
function read_conf(){
  help="read_conf 配置文件地址 配置节名"
  if [[ $# -eq 2 ]] ;then
    value=$(sed "/^$2=/!d;s/.*=//" $1)
    echo $value
  else
    print_msg 'read_conf参数错误:$help' -err
    exit 1
  fi
}

