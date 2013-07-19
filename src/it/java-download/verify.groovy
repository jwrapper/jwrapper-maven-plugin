

// basedir	java.io.File	The absolute path to the base directory of the test project.	1.0
// localRepositoryPath	java.io.File	The absolute path to the local repository used for the Maven invocation on the test project.	1.3
// context	java.util.Map	The storage of key-value pairs used to pass data from the pre-build hook script to the post-build hook script.	1.4


def javaFolderLocal = context.get("javaFolderLocal")
def javaArtifact = context.get("javaArtifact")

def javaURL = javaFolderLocal + "/" + javaArtifact

println "javaURL: ${javaURL}"

def file = new File(javaURL)

assert file.exists()
