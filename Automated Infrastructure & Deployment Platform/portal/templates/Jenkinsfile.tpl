pipeline {
  agent any

  environment {
    APP_NAME = "{{appName}}"
    ENVIRONMENT = "{{environment}}"
    VAULT_ADDR = "{{vaultAddr}}"
  }

  stages {
    stage('Lint') {
      steps {
        sh 'make lint'
      }
    }
    stage('Test') {
      steps {
        sh 'make test'
      }
    }
    stage('Terraform Plan') {
      steps {
        sh "terraform -chdir=terraform/environments/{{environment}} init"
        sh "terraform -chdir=terraform/environments/{{environment}} plan"
      }
    }
    stage('Helm Deploy') {
      steps {
        sh "helm upgrade --install {{appName}} helm/charts/platform-portal --set image.repository={{imageRepo}} --set image.tag={{imageTag}}"
      }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: 'terraform/**/*.tf', fingerprint: false
    }
  }
}
