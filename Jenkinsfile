pipeline {

    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Compile Employee Service') {
            steps {
                dir('employee-service') {
                    sh './mvnw clean compile'
                }
            }
        }

        stage('Unit Test Employee Service') {
            steps {
                dir('employee-service') {
                    sh './mvnw test'
                }
            }
        }

        stage('Package Employee Service') {
            steps {
                dir('employee-service') {
                    sh './mvnw package -DskipTests'
                }
            }
        }

        stage('Compile Department Service') {
            steps {
                dir('department-service') {
                    sh './mvnw clean compile'
                }
            }
        }

        stage('Unit Test Department Service') {
            steps {
                dir('department-service') {
                    sh './mvnw test'
                }
            }
        }

        stage('Package Department Service') {
            steps {
                dir('department-service') {
                    sh './mvnw package -DskipTests'
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'employee-service/target/*.jar, department-service/target/*.jar',
                                 fingerprint: true
            }
        }

        stage('Stop Old Containers') {
            steps {
                sh 'docker compose down || true'
            }
        }

        stage('Build and Run Microservices') {
            steps {
                sh 'docker compose up -d --build'
            }
        }
    }
}