pipeline {
    agent any

    stages {
        stage('build') {
            steps {
                echo "Hello build!"
            }
        }
        stage('test') {
            steps {
                echo "Hello test!"
            }
        }
        stage('deploy') {
            steps {
                sh 'echo "Hello deploy!"'
		sh 'gradle jar'
		sh 'java -jar build/libs/Rekeningrijden.ja'
                sh 'echo "Bye deploy!'
            }
        }
    }
}
