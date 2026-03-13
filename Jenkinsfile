pipeline {

    agent {
        kubernetes {
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: docker
    image: docker:24
    command:
    - cat
    tty: true
    securityContext:
      privileged: true
    volumeMounts:
    - name: dockersock
      mountPath: /var/run/docker.sock
  volumes:
  - name: dockersock
    hostPath:
      path: /var/run/docker.sock
"""
        }
    }

    environment {
        IMAGE_NAME = 'demo-app'
        IMAGE_TAG = "${BUILD_NUMBER}"
        CHART_FILE = 'helm/demo-app/values.yaml'
        GIT_USER_NAME = 'jenkins'
        GIT_USER_EMAIL = 'jenkins@example.local'
    }

    tools {
        maven 'maven3'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Jar') {
            steps {
                dir('demo-app') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'demo-app/target/*.jar', fingerprint: true
            }
        }

        stage('Docker Build') {
            steps {
                container('docker') {
                    dir('demo-app') {
                        sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                    }
                }
            }
        }

        stage('Update Helm Values') {
            steps {
                sh """
                sed -i 's/tag: .*/tag: "${IMAGE_TAG}"/' ${CHART_FILE}
                git config user.name "${GIT_USER_NAME}"
                git config user.email "${GIT_USER_EMAIL}"
                git add ${CHART_FILE}
                git commit -m "chore: update image tag to ${IMAGE_TAG}" || true
                """
            }
        }

        stage('Push GitOps Change') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'tkn',
                    usernameVariable: 'GIT_USERNAME',
                    passwordVariable: 'GIT_PASSWORD'
                )]) {
                    sh '''
                    git remote set-url origin https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/le-se-de/devops-github-project.git
                    git push origin HEAD:master
                    '''
                }
            }
        }

    }
}