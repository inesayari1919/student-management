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
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'admin', passwordVariable: 'Azerty123@')]) {
                    bat """
                        echo <settings><servers><server><id>nexus-snapshots</id><username>%NEXUS_USER%USERNAME%</username><password>%NEXUS_PASSWORD%</password></server><server><id>nexus-releases</id><username>%NEXUS_USER%</username><password>%NEXUS_PASSWORD%</password></server></servers></settings> > settings.xml
                        mvn deploy -DskipTests -s settings.xml
                    """
                }
            }
        }
    }
}
