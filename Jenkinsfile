pipeline {
    agent {
        dockerfile {
            args '-v ${HOME}/.m2:/home/jenkins/.m2 -v ${HOME}/.grails:/home/jenkins/.grails -v ${HOME}/.m2:/home/jenkins/.ivy2'
        }
    }
    environment {
        HOME = '/home/jenkins'
        JAVA_TOOL_OPTIONS = '-Duser.home=/home/jenkins'
    }
    stages {
        stage('clean') {
            steps {
                sh 'grails -DARTIFACT_BUILD_NUMBER=${BUILD_NUMBER} -Dgrails.work.dir=${WORKSPACE}//target clean --non-interactive --plain-output'
            }
        }
        stage('package') {
            steps {
                sh 'grails -DARTIFACT_BUILD_NUMBER=${BUILD_NUMBER} -Dgrails.work.dir=${WORKSPACE}//target prod war --non-interactive --plain-output'
            }
        }
    }

    post {
        success {
            archiveArtifacts artifacts: '**/*.war,**/*.war.md5', fingerprint: true, onlyIfSuccessful: true
        }
    }
}
