pipeline {
    agent any

    environment {
        IMAGE_NAME = 'localhost:5000/demo-app'
        IMAGE_TAG = "${BUILD_NUMBER}"
        CHART_FILE = 'helm/demo-app/values.yaml'
        GIT_USER_NAME = 'jenkins'
        GIT_USER_EMAIL = 'jenkins@example.local'
    }

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                dir('demo-app') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} demo-app'
            }
        }

        stage('Docker Push') {
            steps {
                sh 'docker push ${IMAGE_NAME}:${IMAGE_TAG}'
            }
        }

        stage('Update Helm Values') {
            steps {
                sh "sed -i 's/tag: .*/tag: \"${IMAGE_TAG}\"/' ${CHART_FILE}"
                sh 'git config user.name "${GIT_USER_NAME}"'
                sh 'git config user.email "${GIT_USER_EMAIL}"'
                sh 'git add ${CHART_FILE}'
                sh 'git commit -m "chore: update image tag to ${IMAGE_TAG}" || true'
            }
        }

        stage('Push GitOps Change') {
            when {
                expression { return env.BRANCH_NAME != null }
            }
            steps {
                sh 'git push origin HEAD:${BRANCH_NAME}'
            }
        }
    }
}
