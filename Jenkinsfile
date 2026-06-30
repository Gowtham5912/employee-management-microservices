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
    }
}