#!/bin/bash

echo "--------------- 서버 배포 시작 -----------------"

# 1. EC2 프라이빗 IP 조회 (IMDSv2 대응)
# 토큰을 먼저 발행받아야 메타데이터 조회가 가능합니다.
TOKEN=$(curl -X PUT "http://169.254.169.254/latest/api/token" -H "X-aws-ec2-metadata-token-ttl-seconds: 21600")
PRIVATE_IP=$(curl -H "X-aws-ec2-metadata-token: $TOKEN" -s http://169.254.169.254/latest/meta-data/local-ipv4)

docker stop deepleaf-server || true
docker rm deepleaf-server || true
docker pull 061051246406.dkr.ecr.ap-northeast-2.amazonaws.com/deepleaf-server:latest

# 2. 도커 실행 시 환경 변수로 IP 전달 (-e 옵션 추가)
docker run -d \
  --name deepleaf-server \
  -p 80:8080 \
  -e EC2_PRIVATE_IP=$PRIVATE_IP \
  061051246406.dkr.ecr.ap-northeast-2.amazonaws.com/deepleaf-server:latest

echo "--------------- 서버 배포 끝 (IP: $PRIVATE_IP) -----------------"