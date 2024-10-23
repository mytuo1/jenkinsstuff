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



HEALTHCHECK --interval=60s --timeout=10s --start-period=450s --retries=3 CMD \
    test $(grep -o '[0-9]\+ specs' results/test_output.log | awk '{print $1}') -eq $(grep -o '[0-9]\+ failures' results/test_output.log | awk '{print $1}') \
    && echo 'All specs passed.' || \
    (echo $(grep -o '[0-9]\+ specs' results/test_output.log | awk '{print $1}') 'specs run with' $(grep -o '[0-9]\+ failures' results/test_output.log | awk '{print $1}') 'failures.' && exit 1)


    failures=$(grep -m1 -o '[0-9]\+ failures' test-output.log | awk '{print $1}') && \
    if [ "$failures" -eq "0" ]; then \
      echo "All tests passed."; \
      exit 0; \
    else \
      echo "$failures tests failed."; \
      exit 1; \
    fi

# Healthcheck to monitor Protractor test results and kill container if unhealthy
HEALTHCHECK --interval=60s --timeout=10s --start-period=450s --retries=1 CMD \
    grep -q "0 failures" test-output.log || exit 1




        stage('Run Protractor Tests in Docker') {
            steps {
                script {
                    // Build the Docker image
                    def image = docker.build("your-docker-image-name")

                    // Run the container in detached mode (-d) and map port 4444
                    def containerId = sh(script: """
                        docker run -d -p 4444:4444 your-docker-image-name
                    """, returnStdout: true).trim()

                    echo "Started Docker container: ${containerId}"

                    // Wait for the tests to finish (use sleep or another method based on expected runtime)
                    sleep time: 400, unit: 'SECONDS'

                    // Get the test results from the container logs
                    def logOutput = sh(script: """
                        docker logs ${containerId}
                    """, returnStdout: true).trim()

                    // Print the test log for reference
                    echo "Test log output:\n${logOutput}"

                    // Check for test failures in the logs
                    def failureMatch = logOutput =~ /([0-9]+) failures/
                    def failureCount = failureMatch ? failureMatch[0][1].toInteger() : 0

                    // Check for test success
                    if (failureCount == 0) {
                        echo "All tests passed."
                    } else {
                        error "${failureCount} tests failed."
                    }

                    // Stop and remove the container
                    sh "docker stop ${containerId}"
                    sh "docker rm ${containerId}"
                }
            }
        }






---------------------+











            stage('Run Protractor Tests in Docker') {
            steps {
                script {
                    try {
                        // Step 1: Clone the repository containing the tests and Dockerfile
                        git branch: 'main', url: 'https://github.com/your-repo/test-repo.git'

                        // Assume you have a URL variable (for example, passed in through Jenkins parameters)
                        def testUrl = params.TEST_URL

                        // Step 2: Replace placeholders in the settings.js file with static username, password, and the URL variable
                        sh """
                        sed -i 's/REPLACE_WITH_USERNAME/selenium/g' path/to/settings.js
                        sed -i 's/REPLACE_WITH_PASSWORD/selenium/g' path/to/settings.js
                        sed -i 's|REPLACE_WITH_URL|${testUrl}|g' path/to/settings.js
                        """

                        // Step 3: Build the Docker image from the Dockerfile in the repo
                        sh 'docker build -t selenium-tests .'

                        // Step 4: Run the container in detached mode and store the container ID
                        def containerId = sh(script: """
                            docker run -d selenium-tests
                        """, returnStdout: true).trim()

                        echo "Started Docker container: ${containerId}"

                        // Step 5: Wait for tests to complete (adjust duration as necessary)
                        sleep time: 400, unit: 'SECONDS'

                        // Step 6: Fetch the logs from the running container
                        def logOutput = sh(script: """
                            docker logs ${containerId}
                        """, returnStdout: true).trim()

                        // Step 7: Print logs for visibility in Jenkins
                        echo "Test log output:\n${logOutput}"

                        // Step 8: Check for test failures in the logs using regex
                        def failureMatch = logOutput =~ /([0-9]+) failures/
                        def failureCount = failureMatch ? failureMatch[0][1].toInteger() : 0

                        // Step 9: If no failures, log success; otherwise, mark the build as failed
                        if (failureCount == 0) {
                            echo "All tests passed."
                        } else {
                            error "${failureCount} tests failed."
                        }

                        // Step 10: Clean up - stop and remove the container
                        sh "docker stop ${containerId}"
                        sh "docker rm ${containerId}"

                        // Step 11: Remove the Docker image after the run
                        sh 'docker rmi selenium-tests'
                        
                    } catch (Exception e) {
                        // Handle failure
                        echo "Test stage failed: ${e.message}"
                        currentBuild.result = 'UNSTABLE' // or 'FAILURE'
                    }
                }
            }
            post {
                always {
                    echo 'Test stage completed.'
                }
            }
        }

        stage('Post-Testing') {
            steps {
                script {
                    // This stage runs regardless of the test stage result
                    if (currentBuild.result == 'UNSTABLE') {
                        echo 'Tests had some failures, check the logs for details.'
                    } else if (currentBuild.result == 'FAILURE') {
                        echo 'Tests failed completely.'
                    } else {
                        echo 'No issues with tests, proceeding with the next stage.'
                    }
                }
            }
        }



                        settingsContent = settingsContent.replaceAll(/(this\.username\s*=\s*')([^']*)(')/, "\$1selenium\$3")
                        settingsContent = settingsContent.replaceAll(/(this\.password\s*=\s*')([^']*)(')/, "\$1selenium\$3")
                        settingsContent = settingsContent.replaceAll(/(this\.baseUrl\s*=\s*')([^']*)(')/, "\$1${testUrl}\$3")




                        def failureCount = 0
                        def totalSpecs = 0

                        // Split log lines and check each line for failures and specs
                        logOutput.eachLine { line ->
                            if (line.contains("failures")) {
                                // Example line: "2 specs, 1 failures"
                                def parts = line.split(',')
                                failureCount = parts[1].trim().split(' ')[0].toInteger()
                            }

                            if (line.contains("specs")) {
                                // Example line: "2 specs, 1 failures"
                                def parts = line.split(',')
                                totalSpecs = parts[0].trim().split(' ')[0].toInteger()
                            }
                        }
