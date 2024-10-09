stage('Run Selenium Tests') {
    environment {
        USERNAME = credentials('jenkins-username')  // Jenkins Credentials plugin for secure username
        PASSWORD = credentials('jenkins-password')  // Jenkins Credentials plugin for secure password
        ENVIRONMENT_URL = ""  // The URL for the current environment
    }

    steps {
        script {
            // Set the environment URL (this can be dynamic)
            ENVIRONMENT_URL = "http://my-environment-url.com"  // You can pass this dynamically if needed
        }

        // Run the Selenium tests inside a Docker container
        sh '''
        docker run --rm \
            -v $(pwd):/usr/src/app \
            -w /usr/src/app \
            selenium/standalone-chrome:3.141.59 \
            /bin/bash -c "
                git clone git@github.com:your-repo/selenium-tests-repo.git /usr/src/app &&
                cd /usr/src/app &&
                
                // Replace placeholders in config.js with environment variables
                sed -i 's/REPLACE_USERNAME/${USERNAME}/g' src/test/config.js &&
                sed -i 's/REPLACE_PASSWORD/${PASSWORD}/g' src/test/config.js &&
                sed -i 's|REPLACE_URL|${ENVIRONMENT_URL}|g' src/test/config.js &&
                
                // Install dependencies and run tests
                npm install &&
                npm test
            "
        '''
    }
}




[13:18:30] E/launcher - SessionNotCreatedError: Expected browser binary location, but unable to find binary in default location, no 'moz:firefoxOptions.binary' capability provided, and no binary flag set on the command line
Build info: version: '3.141.59', revision: 'e82be7d358', time: '2018-11-14T08:25:53'
System info: host: '1579f914ecc4', ip: '172.17.0.2', os.name: 'Linux', os.arch: 'amd64', os.version: '5.15.153.1-microsoft-standard-WSL2', java.version: '17.0.2'
Driver info: driver.version: unknown
remote stacktrace:
    at Object.checkLegacyResponse (/usr/lib/node_modules/protractor/node_modules/selenium-webdriver/lib/error.js:546:15)
    at parseHttpResponse (/usr/lib/node_modules/protractor/node_modules/selenium-webdriver/lib/http.js:509:13)
    at /usr/lib/node_modules/protractor/node_modules/selenium-webdriver/lib/http.js:441:30
    at processTicksAndRejections (node:internal/process/task_queues:95:5)
From: Task: WebDriver.createSession()
    at Function.createSession (/usr/lib/node_modules/protractor/node_modules/selenium-webdriver/lib/webdriver.js:769:24)
    at Function.createSession (/usr/lib/node_modules/protractor/node_modules/selenium-webdriver/firefox/index.js:521:41)
    at createDriver (/usr/lib/node_modules/protractor/node_modules/selenium-webdriver/index.js:170:33)
    at Builder.build (/usr/lib/node_modules/protractor/node_modules/selenium-webdriver/index.js:632:16)
    at Hosted.getNewDriver (/usr/lib/node_modules/protractor/built/driverProviders/driverProvider.js:53:33)
    at Runner.createBrowser (/usr/lib/node_modules/protractor/built/runner.js:195:43)
    at /usr/lib/node_modules/protractor/built/runner.js:339:29
    at _fulfilled (/usr/lib/node_modules/protractor/node_modules/q/q.js:834:54)
    at /usr/lib/node_modules/protractor/node_modules/q/q.js:863:30
    at Promise.promise.promiseDispatch (/usr/lib/node_modules/protractor/node_modules/q/q.js:796:13)
[13:18:30] E/launcher - Process exited with error code 199
