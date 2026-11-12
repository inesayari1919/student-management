pipeline {
    agent any

    stages {
        stage('Build et Test') {
            steps {
                echo 'Compilation et exécution des tests...'
                bat '.\\mvnw clean test'
            }
        }
        stage('Déploiement vers Nexus') {
            steps {
                echo 'Envoi du projet vers Nexus...'
                bat '.\\mvnw deploy -DskipTests'
            }
        }
    }
}