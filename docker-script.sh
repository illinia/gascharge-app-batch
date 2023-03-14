#!/bin/sh

nohup /usr/local/bin/docker stop gascharge-app-batch > nohup/nohup-app-batch-stop.out 2>&1 ;
nohup /usr/local/bin/docker rm gascharge-app-batch > nohup/nohup-app-batch-rm.out 2>&1 ;
nohup /usr/local/bin/docker rmi gascharge-app-batch > nohup/nohup-app-batch-rmi.out 2>&1 ;
nohup /usr/local/bin/docker build -t gascharge-app-batch k8s/gascharge-app-batch/ > nohup/nohup-app-batch-build.out 2>&1 ;
nohup /usr/local/bin/docker run --name gascharge-app-batch -it -d -p 8401:8401 --privileged --cgroupns=host -v /sys/fs/cgroup:/sys/fs/cgroup:rw gascharge-app-batch /usr/sbin/init > nohup/nohup-app-batch-run.out 2>&1 ;