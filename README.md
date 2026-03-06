# DevOps Practice GitHub Project

Windows + WSL2 + kind 환경에서 바로 실습할 수 있는 예제 프로젝트입니다.

구성:
- Spring Boot 애플리케이션
- Dockerfile
- Jenkinsfile
- Helm Chart
- ArgoCD Application manifest

## 로컬 실행

### 1. 앱 빌드
```bash
cd demo-app
./mvnw clean package
```

### 2. Docker 이미지 빌드
```bash
docker build -t localhost:5000/demo-app:0.0.1 ./demo-app
```

### 3. 로컬 registry push
```bash
docker push localhost:5000/demo-app:0.0.1
```

### 4. Helm 배포
```bash
helm upgrade --install demo-app ./helm/demo-app -n demo --create-namespace \
  --set image.repository=localhost:5000/demo-app \
  --set image.tag=0.0.1
```

### 5. ArgoCD 적용
```bash
kubectl apply -f argocd/demo-app-application.yaml
```

## Jenkins 흐름
1. GitHub Push
2. Jenkins Pipeline 실행
3. Maven build
4. Docker image build / push
5. values.yaml 의 tag 갱신
6. Git commit & push
7. ArgoCD sync

## 엔드포인트
- `/`
- `/health`
- `/api/info`
