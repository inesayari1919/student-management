pipeline {
    agent any
    stages {
        stage('CHECKOUT GIT') {
            steps {
                checkout([$class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[url: 'https://github.com/inesayari1919/student-management.git']]
                ])
            }
        }
        stage('MVN CLEAN') {
            steps {
                sh 'mvn clean'
            }
        }
        stage('ARTIFACT CONSTRUCTION') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
        stage('UNIT TESTS') {
            steps {
                sh 'mvn test -DskipTests'
            }
        }
        stage('MVN SONARQUBE') {
            steps {
                sh 'mvn sonar:sonar -Dsonar.host.url=http://sonarqube:9000 -Dsonar.login=admin -Dsonar.password=Azerty123@'
            }
        }
        stage('PUBLISH TO NEXUS') {
            steps {
                sh 'mvn deploy -DskipTests'
            }
        }
        stage('BUILDING OUR IMAGE') {
            steps {
                sh 'docker build -t noussa2026/devopsimage:latest .'
            }
        }
        stage('PUSH TO DOCKERHUB') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push noussa2026/devopsimage:latest
                    '''
                }
            }
        }
        stage('Test AWS Credentials') {
            steps {
                echo 'Vérification de la connexion AWS...'
                withCredentials([
                    string(credentialsId: 'aws-access-key-id', variable: 'AWS_ACCESS_KEY_ID'),
                    string(credentialsId: 'aws-secret-access-key', variable: 'AWS_SECRET_ACCESS_KEY'),
                    string(credentialsId: 'aws-session-token', variable: 'AWS_SESSION_TOKEN')
                ]) {
                    sh '''
                        export AWS_DEFAULT_REGION=us-west-2
                        aws sts get-caller-identity
                    '''
                }
            }
        }
        stage('SETUP TERRAFORM') {
            steps {
                withCredentials([
                    string(credentialsId: 'aws-access-key-id', variable: 'AWS_ACCESS_KEY_ID'),
                    string(credentialsId: 'aws-secret-access-key', variable: 'AWS_SECRET_ACCESS_KEY'),
                    string(credentialsId: 'aws-session-token', variable: 'AWS_SESSION_TOKEN')
                ]) {
                    sh '''
                        export AWS_DEFAULT_REGION=us-west-2
                        cd terraform
                        terraform init
                        terraform apply -auto-approve
                    '''
                }
            }
        }
        stage('UPDATE KUBECONFIG') {
            steps {
                withCredentials([
                    string(credentialsId: 'aws-access-key-id', variable: 'AWS_ACCESS_KEY_ID'),
                    string(credentialsId: 'aws-secret-access-key', variable: 'AWS_SECRET_ACCESS_KEY'),
                    string(credentialsId: 'aws-session-token', variable: 'AWS_SESSION_TOKEN')
                ]) {
                    sh '''
                        export AWS_DEFAULT_REGION=us-west-2
                        aws eks update-kubeconfig --region us-west-2 --name student-eks-cluster
                    '''
                }
            }
        }
        stage('DEPLOY TO AWS KUBERNETES') {
            steps {
                withCredentials([
                    string(credentialsId: 'aws-access-key-id', variable: 'AWS_ACCESS_KEY_ID'),
                    string(credentialsId: 'aws-secret-access-key', variable: 'AWS_SECRET_ACCESS_KEY'),
                    string(credentialsId: 'aws-session-token', variable: 'AWS_SESSION_TOKEN')
                ]) {
                    sh '''
                        export AWS_DEFAULT_REGION=us-west-2
                        kubectl apply -f k8s/
                        kubectl rollout restart deployment/student-deployment
                        kubectl get pods
                        kubectl get svc
                    '''
                }
            }
        }
    }
}
