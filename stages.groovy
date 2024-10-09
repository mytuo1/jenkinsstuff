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




[13:08:59] E/launcher - UnsupportedOperationError: <!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="/assets/displayhelpservlet.css" media="all"/>
  <link href="/assets/favicon.ico" rel="icon" type="image/x-icon" />
  <script src="/assets/jquery-3.1.1.min.js" type="text/javascript"></script>
  <script src="/assets/displayhelpservlet.js" type="text/javascript"></script>
  <script type="text/javascript">
    var json = Object.freeze('{"consoleLink": "\u002fwd\u002fhub","type": "Standalone","class": "org.openqa.grid.web.servlet.DisplayHelpHandler$DisplayHelpServletConfig","version": "3.141.59"}');
  </script>
</head>
<body>

<div id="content">
  <div id="help-heading">
    <h1><span id="logo"></span></h1>
    <h2>Selenium <span class="se-type"></span>&nbsp;v.<span class="se-version"></span></h2>
  </div>

  <div id="content-body">
    <p>
      Whoops! The URL specified routes to this help page.
    </p>
    <p>
      For more information about Selenium <span class="se-type"></span> please see the
      <a class="se-docs">docs</a> and/or visit the <a class="se-wiki">wiki</a>.
      <span id="console-item">
        Or perhaps you are looking for the Selenium <span class="se-type"></span> <a class="se-console">console</a>.
      </span>
    </p>
    <p>
      Happy Testing!
    </p>
  </div>

  <div>
    <footer id="help-footer">
      Selenium is made possible through the efforts of our open source community, contributions from
      these <a href="https://github.com/SeleniumHQ/selenium/blob/master/AUTHORS">people</a>, and our
      <a href="http://www.seleniumhq.org/sponsors/">sponsors</a>.
   </footer>
  </div>
 </div>

</body>
</html>
    at parseHttpResponse (/usr/lib/node_modules/protractor/node_modules/selenium-webdriver/lib/http.js:534:11)
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
