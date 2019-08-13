pipeline {
    agent { label 'master' }
    stages {
        stage('clean') {
            steps {
                sh 'git clean -fdx'
            }
        }
        stage('set_version') {
            steps {
                sh 'bumpversion patch'
            }
        }
        stage('release') {
            when { branch 'master' }
            steps {
                withCredentials([usernamePassword(credentialsId: env.CREDENTIALS_ID, passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                    sh '''
                        export VERSION=$(bump2version --list --allow-dirty release | grep new_version= | sed -r s,"^.*=",,)
                        git push origin master
                        git push origin refs/tags/v$VERSION
                    '''
                }
            }
        }
        stage('container') {
            agent {
                dockerfile {
                    args '-v ${HOME}/.m2:/home/builder/.m2 -v ${HOME}/.grails:/home/builder/.grails -v ${HOME}/.ivy2:/home/builder/.ivy2'
                    additionalBuildArgs '--build-arg BUILDER_UID=${JENKINS_UID:-9999}'
                    reuseNode true
                }
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
