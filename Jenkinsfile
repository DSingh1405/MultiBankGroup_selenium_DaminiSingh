pipeline {
    agent any

    // ---------------------------------------------------------
    // 1. Pipeline Parameters
    // ---------------------------------------------------------
    parameters {
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browsers in headless mode')
        booleanParam(name: 'REMOTE', defaultValue: true, description: 'Use Remote Selenium Grid')
        string(name: 'GRID_URL', defaultValue: 'http://localhost:4444/wd/hub', description: 'Selenium Grid URL')
        string(name: 'EMAIL_TO', defaultValue: 'recipient@example.com', description: 'Email recipients')
    }

    environment {
        MAVEN_HOME = tool 'Maven-3.9.6'
        JAVA_HOME = tool 'JDK17'
        PATH = "${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${env.PATH}"
    }

    stages {

        // ---------------------------------------------------------
        // 2. Checkout Code
        // ---------------------------------------------------------
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        // ---------------------------------------------------------
        // 3. Run Tests (Parallel Browsers)
        // ---------------------------------------------------------
        stage('Build & Test') {
            parallel {
                
                stage('Chrome Execution') {
                    steps {
                        bat """
                        mvn clean test ^
                            -Dsurefire.suiteXmlFiles=testng.xml ^
                            -Dremote=${REMOTE} ^
                            -DgridUrl=${GRID_URL} ^
                            -Dheadless=${HEADLESS} ^
                            -Dbrowser=chrome
                        """
                    }
                }

                stage('Firefox Execution') {
                    steps {
                        bat """
                        mvn clean test ^
                            -Dsurefire.suiteXmlFiles=testng.xml ^
                            -Dremote=${REMOTE} ^
                            -DgridUrl=${GRID_URL} ^
                            -Dheadless=${HEADLESS} ^
                            -Dbrowser=firefox
                        """
                    }
                }
            }
        }

        // ---------------------------------------------------------
        // 4. Publish Extent Report
        // ---------------------------------------------------------
        stage('Publish Extent Report') {
            steps {
                publishHTML([ 
                    reportName : 'Extent Report',
                    reportDir  : 'target/extent-report',
                    reportFiles: 'index.html',
                    keepAll    : true,
                    alwaysLinkToLastBuild: true
                ])
            }
        }
    }

    // ---------------------------------------------------------
    // 5. Post Actions (Archive + Email)
    // ---------------------------------------------------------
    post {
        always {
            // Archive screenshots + extent reports
            archiveArtifacts artifacts: 'artifacts/**/*.png'
            archiveArtifacts artifacts: 'target/extent-report/**/*.*'

            // Send Email
            emailext(
                subject: "Jenkins Report - ${JOB_NAME} #${BUILD_NUMBER}",
                body: """
                Hi Team,

                The automated execution has completed.

                ✔ Job: ${JOB_NAME}
                ✔ Build No: ${BUILD_NUMBER}
                ✔ Status: ${currentBuild.currentResult}
                ✔ Report: ${BUILD_URL}Extent_Report/

                Regards,
                QA Automation Pipeline
                """,
                to: "${params.EMAIL_TO}"
            )
        }
    }
}
