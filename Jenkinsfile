pipeline {
    agent none
    stages {
        stage('clean') {
            agent { label 'master' }
            steps {
                sh 'git clean -fdx'
            }
        }
        stage('container') {
            agent {
                dockerfile {
                    args '-v ${HOME}/.m2:/home/jenkins/.m2 -v ${HOME}/.grails:/home/jenkins/.grails -v ${HOME}/.ivy2:/home/jenkins/.ivy2'
                }
            }
            environment {
                HOME = '/home/jenkins'
                JAVA_TOOL_OPTIONS = '-Duser.home=/home/jenkins'
            }
            stages {
                stage('package') {
                    steps {
                        sh 'grails -DARTIFACT_BUILD_NUMBER=${BUILD_NUMBER} -Dgrails.work.dir=${WORKSPACE}//target clean --non-interactive --plain-output'
                        sh 'grails -DARTIFACT_BUILD_NUMBER=${BUILD_NUMBER} -Dgrails.work.dir=${WORKSPACE}//target prod war --non-interactive --plain-output'
                    }
                }
            }
            post {
                 success {
                     archiveArtifacts artifacts: '**/*.war', fingerprint: true, onlyIfSuccessful: true
                 }
            }
        }
    }
}
