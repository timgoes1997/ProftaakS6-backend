pipeline {
    agent any

stages {
	stage('test') {
            steps {
sh 'echo "Some echo"'
sh 'gradle jar; and java -jar build/libs/Rekeningrijden.jar'
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
