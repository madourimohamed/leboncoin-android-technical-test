pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AndroidRecruitmentTestApp"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":data")
include(":core:common")
include(":core:domain")
include(":core:network")
include(":core:database")
include(":core:data")
include(":core:analytics")
include(":core:testing")
include(":core:ui")
