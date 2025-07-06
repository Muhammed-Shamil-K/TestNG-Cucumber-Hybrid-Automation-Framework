
# TestNG-Cucumber Hybrid Automation Framework
### Introduction:
This project is containing sample test scenarios for automating various workflows of testing.

### Pre-requisites:
<p></p> 
Install the following things to make use of the framework . Please watch the tutorial links for the detailed instructions.

1. #### Java (Java 8 or above):
   Refer the link https://www.liquidweb.com/kb/how-to-install-java-windows-ubuntu-macos/
2. #### Maven (3.6 or above):
   Refer the link https://www.studytonight.com/java-examples/how-to-install-maven-on-windows-linux-and-mac
3. #### GIT:
   Refer the link https://kinsta.com/knowledgebase/install-git/
4. #### IDE
   * IntelliJ IDEA Community (preferred) - Install from https://www.jetbrains.com/idea/download/?section=linux
   * Eclipse - Install from https://www.eclipse.org/downloads/


### Project Setup
Manually the framework can be installed by following the below steps

Create a folder where you want to clone the project code.
Right click on the folder and open terminal from that location
Clone the  project by running the below command in terminal

`git clone `

Go to IDE and open the cloned project by using File → Open menu option
Refresh the maven for downloading all dependencies
Install the framework using the command mvn clean install

Now the core framework jar will be created inside your local maven repository which can be added as a dependency inside the test project and used for automating your test cases.



### Test Execution
We can run our test cases mainly by two methods

##### Using testng.xml file
Right click on the testng.xml file(eg: regression.xml) at the location /test/resources/testNGsuites/ and select Run option which will run the test classes defined in <test> tag.
##### Using maven command
Run the below maven command to execute the tests defined under the previously created suite testSuite.xml
`mvn clean test -Dtestng.suitexml=regression.xml`
