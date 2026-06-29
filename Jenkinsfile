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

    }

}