pipeline {
    agent any

    environment {
        // Define environment variables
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials') // DockerHub credentials stored in Jenkins
        SSH_KEY = credentials('ec2-ssh-key') // SSH key stored in Jenkins to connect to EC2
        IMAGE_NAME = 'iamdebjit3107/ekalavya'
        EC2_USER = 'ubuntu'
        EC2_HOST = '3.111.84.98'
    }

    stages {
        stage('Clone repository') {
            steps {
                // Clone a specific branch from the GitHub repository
                git branch: 'dev', url: 'https://github.com/rajdeeppal/Ekalavya.git'
            }
        }


        stage('Build with Maven') {
            steps {
                // Run Maven build
                sh 'mvn clean package -DskipTests=true'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build Docker image and tag it with Jenkins build ID
                    sh "docker build -t ${env.IMAGE_NAME}:${env.BUILD_ID} ."
                }
            }
        }

        stage('Push Docker Image to DockerHub') {
            steps {
                script {
                    // Log in to DockerHub and push the image
                    sh "echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin"
                    sh "docker push ${env.IMAGE_NAME}:${env.BUILD_ID}"
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                script {
                    // SSH into the EC2 instance, pull the image from DockerHub, and run the container
                    sh """
                        ssh -o StrictHostKeyChecking=no -i ${SSH_KEY} ${EC2_USER}@${EC2_HOST} \
                        "docker pull ${env.IMAGE_NAME}:${env.BUILD_ID} && \
                        docker stop ekalavya-app || true && \
                        docker rm ekalavya-app || true && \
                        docker run --restart unless-stopped -d --name ekalavya-app -p 61002:61002 ${env.IMAGE_NAME}:${env.BUILD_ID}"
                    """
                }
            }
        }
    }

    post {
        always {
            // Clean up Docker images after the build
            sh "docker rmi ${IMAGE_NAME}:${env.BUILD_ID} || true"
        }
    }
}
