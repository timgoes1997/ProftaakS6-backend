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
                echo "Hello deploy!"
		gradle jar; java -jar build/libs/Rekeningrijden.jar
                echo "Bye deploy!"
            }
        }
    }
}
