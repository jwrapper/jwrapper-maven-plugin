

// basedir	java.io.File	The absolute path to the base directory of the test project.	1.0
// localRepositoryPath	java.io.File	The absolute path to the local repository used for the Maven invocation on the test project.	1.3
// context	java.util.Map	The storage of key-value pairs used to pass data from the pre-build hook script to the post-build hook script.	1.4

def String sevenFolder = "${basedir}/target/extract"
def String sevenResult = "core.zip"

context.put("sevenFolder", sevenFolder)
context.put("sevenResult", sevenResult)

def javaURL = sevenFolder + "/" + sevenResult

println "javaURL: ${javaURL}"

def file = new File(javaURL)

if(file.exists()){
	file.delete()
}

assert !file.exists()
  
