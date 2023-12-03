# 베이스 이미지 선택
FROM node:20

# 작업 디렉토리 설정
WORKDIR /usr/src/app

# 종속성 파일 복사 (package.json 및 package-lock.json)
COPY package*.json ./be

# 종속성 설치
RUN npm install

# 애플리케이션 소스 코드 복사
COPY . .

# TypeScript 컴파일
RUN npm run build

# 애플리케이션 실행 포트 지정
EXPOSE 8000

# 애플리케이션 실행 명령어
CMD ["node", "dist/src/main"]
