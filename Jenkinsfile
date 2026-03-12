pipeline {
    agent any

    environment {
        IMAGE_NAME = 'registry.default.svc.cluster.local:5000/demo-app'
        IMAGE_TAG = "${BUILD_NUMBER}"
        CHART_FILE = 'helm/demo-app/values.yaml'
        GIT_USER_NAME = 'jenkins'
        GIT_USER_EMAIL = 'jenkins@example.local'
        JAVA_HOME = '/opt/java/openjdk'
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

        stage('Build') {
            steps {
                dir('demo-app') {
                    sh '''
                    export JAVA_HOME=/opt/java/openjdk
                    export PATH=$JAVA_HOME/bin:$PATH
                    mvn clean package -DskipTests
                    '''
                }
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'demo-app/target/*.jar', fingerprint: true
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
                    echo $GIT_USERNAME
                    '''
                    sh '''
                    git remote set-url origin https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/le-se-de/devops-github-project.git
                    git push origin HEAD:master
                    '''
                }
            }
        }
    }
}