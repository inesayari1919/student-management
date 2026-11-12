pipeline {
    agent any

    stages {
        stage('Build et Test') {
            steps {
                echo 'Compilation et exécution des tests...'
                bat '.\\mvnw clean test'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                script {
                    def scannerHome = tool 'SonarQubeScanner'
                    withSonarQubeEnv('MonSonarQube') {
                        bat 'mvn clean verify sonar:sonar'
                    }
                }
            }
        }
        stage('Déploiement vers Nexus') {
            steps {
                echo 'Envoi du projet vers Nexus...'
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                    bat 'mvn deploy -DskipTests -Dnexus.username=%NEXUS_USER% -Dnexus.password=%NEXUS_PASSWORD%'
                }
            }
        }
    }
}
