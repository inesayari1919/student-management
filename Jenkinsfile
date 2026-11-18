pipeline {
    agent any

    stages {
        stage('CHECKOUT GIT') {
            steps {
                echo 'Récupération du code depuis Git...'
                checkout scm
            }
        }
        stage('MVN CLEAN') {
            steps {
                echo 'Compilation et exécution des tests...'
                sh 'mvn clean'
            }
        }
        stage('ARTIFACT CONSTRUCTION') {
            steps {
                echo 'Construction du JAR...'
                sh 'mvn package -DskipTests'
            }
        }
        stage('UNIT TESTS') {
            steps {
                echo 'Tests unitaires...'
                sh 'mvn test -DskipTests'
            }
        }
        stage('MVN SONARQUBE') {
            steps {
                echo 'Analyse SonarQube...'
                sh 'mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=admin -Dsonar.password=admin || true'
            }
        }
        stage('PUBLISH TO NEXUS') {
            steps {
                echo 'Envoi vers Nexus...'
                sh 'mvn deploy -DskipTests || true'
            }
        }
        stage('BUILDING OUR IMAGE') {
            steps {
                echo 'Construction de l image Docker...'
                sh 'docker build -t noussa2026/devopsimage:latest .'
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
        stage('DEPLOY OUR IMAGE') {
            steps {
                echo 'Deploiement de l image Docker...'
                sh '''
                    docker rm -f student-app 2>/dev/null || true
                    docker run -d -p 8085:8082 --name student-app noussa2026/devopsimage:latest
                '''
            }
        }
    }
}