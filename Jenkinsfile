pipeline {
    agent {
docker { image 'edwinvanrooij/getting-started:first-push-2' }
}




stages {
	stage('test') {
            steps {
sh 'echo "Some echo"'
sh 'curl http://localhost:1000'
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
}
