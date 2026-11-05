package fr.leboncoin.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun VersionCatalog.library(alias: String): Provider<MinimalExternalModuleDependency> =
    findLibrary(alias).orElseThrow { IllegalArgumentException("Unknown library alias: $alias") }

internal fun VersionCatalog.pluginId(alias: String): String =
    findPlugin(alias).orElseThrow { IllegalArgumentException("Unknown plugin alias: $alias") }
        .get().pluginId

internal fun VersionCatalog.version(alias: String): String =
    findVersion(alias).orElseThrow { IllegalArgumentException("Unknown version alias: $alias") }
        .requiredVersion
