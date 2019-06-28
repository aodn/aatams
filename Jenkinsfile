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
                    args '-v ${HOME}/.m2:/home/builder/.m2 -v ${HOME}/.grails:/home/builder/.grails -v ${HOME}/.ivy2:/home/builder/.ivy2'
                    additionalBuildArgs '--build-arg BUILDER_UID=${JENKINS_UID:-9999}'
                }
            }
            environment {
                HOME = '/home/builder'
                JAVA_TOOL_OPTIONS = '-Duser.home=/home/builder'
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
