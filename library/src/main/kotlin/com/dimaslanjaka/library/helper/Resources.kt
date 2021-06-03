@file:Suppress("ConvertSecondaryConstructorToPrimary", "unused", "MemberVisibilityCanBePrivate")

package com.dimaslanjaka.library.helper

import java.util.*

/**
 * Resource Loader
 * ```java
 * Resources res = new Resources("filename.properties"); // java
 * ```
 * ```kotlin
 * val res = Resources("filename.properties") // kotlin
 * ```
 */
class Resources {
    var file: String? = null;

    constructor(file: String) {
        this.file = file;
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getProp(key: String): T {
        val props = toProperties()
        return (props.getProperty(key) as T) ?: throw RuntimeException("could not find property $key")
    }

    fun toProperties(): Properties {
        return javaClass.classLoader.getResourceAsStream(file).use {
            Properties().apply { load(it) }
        }
    }

    override fun toString(): String {
        val props = javaClass.classLoader.getResourceAsStream(this.file).use {
            Properties().apply { load(it) }
        }
        return props.toString()
    }
}