stage('Déploiement vers Nexus') {
    steps {
        echo 'Envoi du projet vers Nexus...'
        withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
            bat '''
                echo ^<settings^> > settings.xml
                echo   ^<servers^> >> settings.xml
                echo     ^<server^> >> settings.xml
                echo       ^<id^>nexus-snapshots^</id^> >> settings.xml
                echo       ^<username^>%NEXUS_USER%^</username^> >> settings.xml
                echo       ^<password^>%NEXUS_PASSWORD%^</password^> >> settings.xml
                echo     ^</server^> >> settings.xml
                echo     ^<server^> >> settings.xml
                echo       ^<id^>nexus-releases^</id^> >> settings.xml
                echo       ^<username^>%NEXUS_USER%^</username^> >> settings.xml
                echo       ^<password^>%NEXUS_PASSWORD%^</password^> >> settings.xml
                echo     ^</server^> >> settings.xml
                echo   ^</servers^> >> settings.xml
                echo ^</settings^> >> settings.xml
                
                mvn deploy -DskipTests -s settings.xml
            '''
        }
    }
}
