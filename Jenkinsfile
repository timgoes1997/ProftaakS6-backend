pipeline {
    agent any

    stages {
        stage('helloworld') {
            steps {
		sh 'gradle jar'
		sh 'java -jar build/libs/Rekeningrijden.jar'
            }
        }
    }
	post {
	       success {
		sh 'echo "The stuff has succeeded!" > ~/infofile'
	       }
	   }
}
