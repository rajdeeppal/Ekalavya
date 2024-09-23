pipeline {
    agent any

    environment {
        // Define environment variables
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials') // DockerHub credentials stored in Jenkins
        SSH_KEY = credentials('ec2-ssh-key') // SSH key stored in Jenkins to connect to EC2
        IMAGE_NAME = 'your-dockerhub-username/your-app-name'
        EC2_USER = 'ec2-user'
        EC2_HOST = 'your-ec2-instance-ip'
    }

    stages {
        stage('Clone repository') {
            steps {
                // Clone the GitHub repository
                git 'https://github.com/your-github-username/your-repository.git'
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
                        ssh -o StrictHostKeyChecking=no -i ${SSH_KEY} ${EC2_USER}@${EC2_HOST} << EOF
                        docker pull ${env.IMAGE_NAME}:${env.BUILD_ID}
                        docker stop your-app-container || true
                        docker rm your-app-container || true
                        docker run -d --name your-app-container -p 8080:8080 ${env.IMAGE_NAME}:${env.BUILD_ID}
                        EOF
                    """
                }
            }
        }
    }

    post {
        always {
            // Clean up Docker images after build
            sh 'docker rmi ${IMAGE_NAME}:${env.BUILD_ID} || true'
        }
    }
}
