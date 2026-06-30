pipeline {

    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Compile') {
            steps {
                dir('employee-service') {
                    sh './mvnw clean compile'
                }
            }
        }

        stage('Unit Test') {
            steps {
                dir('employee-service') {
                    sh './mvnw test'
                }
            }
        }

        stage('Package') {
            steps {
                dir('employee-service') {
                    sh './mvnw package -DskipTests'
                }
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'employee-service/target/*.jar',
                                 fingerprint: true
            }
        }

        stage('Build Docker Image') {
        steps {
            dir('employee-service') {
                sh 'docker build -t employee-service:1.0 .'
            }
        }
    }

    stage('Remove Old Container') {
        steps {
            sh 'docker rm -f employee-service || true'
        }
    }

    stage('Run Docker Container') {
        steps {
            sh '''
            docker run -d \
            --name employee-service \
            --network employee-network \
            -p 8082:8080 \
            employee-service:1.0 \
            --spring.profiles.active=docker
            '''
        }
    }
    }
}