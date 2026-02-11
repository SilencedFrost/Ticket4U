rootProject.name = "ticket-4u-services-reactive"

// Define the directory where your service subprojects live using the projectDir property
val servicesDir = settings.rootDir.resolve("services")

// Check if the directory exists and is a directory
if (servicesDir.isDirectory) {
    // Iterate over all items in the directory
    servicesDir.listFiles()
        ?.filter { it.isDirectory && !it.name.startsWith(".") } // Filter for valid project directories
        ?.forEach { service ->
            // Include the project using the include() function and string template
            include("services:${service.name}")
        }
}