pipeline {
    agent any

stages {
	stage('test') {
            steps {
sh 'echo "Some echo"'
sh 'docker run -p 80:80 edwinvanrooij/getting-started:first-push-2'
sh 'echo "done"'
}
}
}

}
    //stages {
        //stage('helloworld') {
            //steps {
		//sh 'gradle jar; java -jar build/libs/Rekeningrijden.jar'
            //}
        //}
    //}
	//post {
	       //success {
//sh 'cp build/libs/Rekeningrijden.jar ~/'
	       //}
	   //}
