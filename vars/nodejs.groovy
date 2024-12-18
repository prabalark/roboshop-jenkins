def call() {
  pipeline {

     agent {
         node {
             label 'terraform-workstation'
         }
     }

      stages {

          stage('Code Quality') {
              steps {
                  //sh 'ls -l'
                  //sh 'sonar-scanner -Dsonar.projectKey=${component} -Dsonar.host.url=http://172.31.83.190:9000 -Dsonar.login=admin -Dsonar.password=admin123 -Dsonar.qualitygate.wait=true'
                sh 'echo Code Quality'
              }
          }

          stage('Unit Test Cases') {
              steps {
                sh 'echo Unit tests'
                //sh 'npm test'
              }
          }

          stage('CheckMarx SAST Scan') {
              steps {
                sh 'echo Checkmarx Scan'
              }
          }

          stage('CheckMarx SCA Scan') {
              steps {
                  sh 'echo Checkmarx SCA Scan'
              }
          }
          stage('Release Application') {
              when {
                  expression {
                     env.TAG_NAME ==~ ".*"
                  }
              }
              steps {
                  sh 'env'
                  sh 'curl -v -u admin:admin123 --upload-file server.js http://54.211.245.96:8081/repository/cart/server.js'
              }
          }

      }

  }

}