pipeline {
    options {
        skipDefaultCheckout true
    }
    agent any
    tools {
        gradle 'gradle'
    }
    stages {
        stage('start') {
            steps {
                slackSend (
                    channel: '#gascharge',
                    color: '#FFFF00',
                    message: "Started: Job ${env.JOB_NAME} [${env.BUILD_NUMBER}]"
                )
            }
        }
        stage('Checkout') {
            steps {
                echo 'test1'
                git branch: 'main',
                    credentialsId: 'gascharge-app-reservation-access-token',
                    url: 'https://github.com/illinia/gascharge-app-batch.git'
            }
        }
        stage('Gradle project build') {
            steps {
                sh '''
                    echo 'Gradle 프로젝트 빌드 시작'
                    ./gradlew clean bootJar -Pdev
                    cp /var/jenkins_home/application-charge.yml /var/jenkins_home/workspace/gascharge-app-batch/application-charge.yml
                '''
            }
        }
        stage('ssh publisher') {
            steps {
                sshPublisher(
                    publishers: [
                        sshPublisherDesc(
                            configName: 'gascharge-app-ssh-server',
                            transfers: [
                                sshTransfer(
                                    remoteDirectory: 'k8s/gascharge-app-batch',
                                    removePrefix: 'build/libs',
                                    sourceFiles: 'build/libs/gascharge-app-batch-0.0.1-SNAPSHOT.jar'
                                ),
                                sshTransfer(
                                    remoteDirectory: 'k8s/gascharge-app-batch',
                                    sourceFiles: 'application-charge.yml, Dockerfile, docker-script.sh'
                                ),
                                sshTransfer(
                                    execCommand: 'chmod +x k8s/gascharge-app-batch/docker-script.sh'
                                ),
                                sshTransfer(
                                    execCommand: 'bash k8s/gascharge-app-batch/docker-script.sh'
                                )
                            ]
                        )
                    ]
                )
            }
        }
    }
    post {
        success {
            slackSend (
                channel: '#gascharge',
                color: '#00FF00',
                message: """
                    Success ${env.JOB_NAME} [${env.BUILD_NUMBER}
                """
            )
        }
        failure {
            slackSend (
                channel: '#gascharge',
                color: '#FF0000',
                message: """
                    Failed ${env.JOB_NAME} [${env.BUILD_NUMBER}
                """
            )
        }
        unstable {
            slackSend (
                channel: '#gascharge',
                color: '#FF0000',
                message: """
                    Unstable ${env.JOB_NAME} [${env.BUILD_NUMBER}
                """
            )
        }
    }
}