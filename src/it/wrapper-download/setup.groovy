

// basedir	java.io.File	The absolute path to the base directory of the test project.	1.0
// localRepositoryPath	java.io.File	The absolute path to the local repository used for the Maven invocation on the test project.	1.3
// context	java.util.Map	The storage of key-value pairs used to pass data from the pre-build hook script to the post-build hook script.	1.4

/**
 * JWrapper download local folder.
 */
def String wrapperFolderLocal = System.properties["java.io.tmpdir"] + "/jwrapper"

/**
 * JWrapper download artifact file.
 */
def String wrapperArtifact = "jwrapper-00020654351.jar"

context.put("wrapperFolderLocal", wrapperFolderLocal)
context.put("wrapperArtifact", wrapperArtifact)

def wrapperURL = wrapperFolderLocal + "/" + wrapperArtifact

println "wrapperURL: ${wrapperURL}"

def file = new File(wrapperURL)

if(file.exists()){
	file.delete()
}

assert !file.exists()
  
