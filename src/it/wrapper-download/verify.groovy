

// basedir	java.io.File	The absolute path to the base directory of the test project.	1.0
// localRepositoryPath	java.io.File	The absolute path to the local repository used for the Maven invocation on the test project.	1.3
// context	java.util.Map	The storage of key-value pairs used to pass data from the pre-build hook script to the post-build hook script.	1.4


def wrapperFolderLocal = context.get("wrapperFolderLocal")
def wrapperArtifact = context.get("wrapperArtifact")

def wrapperURL = wrapperFolderLocal + "/" + wrapperArtifact

println "wrapperURL: ${wrapperURL}"

def file = new File(wrapperURL)

assert file.exists()
