pipeline {
    agent any
    triggers {
        pollSCM('* * * * *')

    }
    stages {
        stage('Compile') {
            steps {
                sh './gradlew compileJava'
            }
        }
        stage('Unit test') {
            steps {
                sh './gradlew test'
            }
        }
        stage('Code coverage') {
            steps {
                sh './gradlew jacocoTestCoverageVerification'
                publishHTML(target: [
                    reportDir: 'build/reports/jacoco/test/html',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Report'
                ])
            }
        }
        stage('Static code analysis') {
            steps {
                sh './gradlew checkstyleMain'
                publishHTML(target: [
                    reportDir: 'build/reports/checkstyle/',
                    reportFiles: 'main.html',
                    reportName: 'Checkstyle Report'
                ])
            }
        }
        stage('Package') {
            steps {
                sh './gradlew build'
            }
        }
        stage('Docker build') {
            steps {
                sh 'docker build -t titoo/calculator .'
            }
        }
        stage('Docker push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                    sh 'echo $DOCKERHUB_PASSWORD | docker login -u $DOCKERHUB_USERNAME --password-stdin'
                    sh 'docker push titoo/calculator'
                }
            }
        }
        stage('Deploy to staging') {
            steps {
                sh 'docker run -d --rm -p 8765:8080 --name calculator titoo/calculator'
            }
        }
        stage("Acceptance test") {
            steps {
                sleep 60
                sh '''
                for i in {1..10}; do
                    if curl -s http://localhost:8765/health; then
                        exit 0
                    fi
                    sleep 6
                done
                exit 1
                '''
                sh "chmod +x acceptance_test.sh && ./acceptance_test.sh"
            }
        }
    }
    post {
        always {
            sh 'docker stop calculator || true'
            cleanWs()
        }
    }
}
