node {
    notify('Started')

    stage 'checkout'
    git 'https://github.com/aniruthmp/scc-example.git'

    dir('producer') {
        try {
            stage 'compiling, testing, packaging'
            sh 'mvn clean verify'

            stage 'publish test results'
            // publishHTML(target: [allowMissing         : true,
            //                      alwaysLinkToLastBuild: false,
            //                      keepAll              : true,
            //                      reportDir            : 'target/site/jacoco/',
            //                      reportFiles          : 'index.html',
            //                      reportName           : 'Code Coverage',
            //                      reportTitles         : ''])
            jacoco classPattern: 'target/classes',
                    execPattern: 'target/**.exec',
                    sourcePattern: 'src/main/java'

            stage 'archival'
            step([$class     : 'JUnitResultArchiver',
                  testResults: 'target/surefire-reports/TEST-*.xml'])
            archiveArtifacts 'target/producer-*-SNAPSHOT.jar'

            stage name: 'Deploy', concurrency: 1
            pushToCloudFoundry cloudSpace: 'pcfdev-space',
                    credentialsId: '9e1d8176-3ed0-4f5a-abc5-3935fff17107',
                    manifestChoice: [manifestFile: 'src/main/deployment/manifest.yml'],
                    organization: 'pcfdev-org',
                    selfSigned: true,
                    target: 'https://api.local.pcfdev.io'

        }
        catch (err) {
            notify("Error: ${err}")
            currentBuild.result = 'FAILURE'
        }
        notify('Success')
    }
}

def notify(status) {
    emailext(
            to: "aniruth.sarathy@gmail.com",
            subject: "${status}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
            body: """<p>${status}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
        <p>Check console output at <a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a></p>""",
    )
}