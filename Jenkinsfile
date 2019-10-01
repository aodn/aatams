pipeline {
    agent none
    stages {
        stage('container') {
            agent {
                dockerfile {
                    args '-v ${HOME}/.m2:/home/builder/.m2 -v ${HOME}/.grails:/home/builder/.grails -v ${HOME}/.ivy2:/home/builder/.ivy2'
                    additionalBuildArgs '--build-arg BUILDER_UID=$(id -u)'
                    reuseNode true
                }
            }
            stages {
                stage('set_version') {
                    when { not { branch "master" } }
                    steps {
                        sh './bumpversion.sh build'
                    }
                }
                stage('release') {
                    when { branch 'master' }
                    steps {
                        withCredentials([usernamePassword(credentialsId: env.CREDENTIALS_ID, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                            sh './bumpversion.sh release'
                        }
                    }
                }
                stage('package') {
                    steps {
                        sh 'grails clean'
                        sh 'grails prod war'
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
