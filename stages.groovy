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
