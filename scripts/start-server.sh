#!/bin/bash

echo "--------------- 서버 배포 시작 -----------------"
docker stop deepleaf-server || true
docker rm deepleaf-server || true
docker pull 061051246406.dkr.ecr.ap-northeast-2.amazonaws.com/deepleaf-server:latest
docker run -d --name deepleaf-server -p 80:8080 061051246406.dkr.ecr.ap-northeast-2.amazonaws.com/deepleaf-server
echo "--------------- 서버 배포 끝 -----------------"