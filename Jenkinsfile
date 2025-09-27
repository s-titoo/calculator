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
                sh './gradlew jacocoTestReport jacocoTestCoverageVerification'
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
        stage('Acceptance test') {
            steps {
                sh '''
                for i in $(seq 1 10); do
                    if curl -s http://localhost:8765/actuator/health; then
                        exit 0
                    fi
                    sleep 6
                done
                echo 'Health check failed after 10 attempts'
                exit 1
                '''
                sh 'chmod +x acceptance_test.sh && ./acceptance_test.sh'
            }
        }
    }
    post {
        always {
            script {
                try {
                    sh 'docker stop calculator || true'
                } catch (Exception e) {
                    echo "Docker stop failed: ${e.message}"
                }
                try {
                    sh 'docker rm calculator || true'
                } catch (Exception e) {
                    echo "Docker remove failed: ${e.message}"
                }
                try {
                    cleanWs()
                } catch (Exception e) {
                    echo "Workspace cleanup failed: ${e.message}"
                }
            }
        }
    }
}
