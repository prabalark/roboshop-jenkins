def call() {
  pipeline {

     agent {
         node {
             label 'terraform-workstation'
         }
     }

      options {
          ansiColor('xterm')
      }

      environment {
          NEXUS = credentials('NEXUS')

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
                  sh 'npm install'
                  sh 'echo $TAG_NAME >VERSION'
                  sh 'zip -r ${component}-${TAG_NAME}.zip node_modules server.js VERSION ${schema_dir}'
                  sh 'curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${component}-${TAG_NAME}.zip http://18.207.222.211:8081/repository/${component}/${component}-${TAG_NAME}.zip'
              }
          }

      }

  }

}