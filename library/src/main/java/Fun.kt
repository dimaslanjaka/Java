import com.google.gson.GsonBuilder
import java.lang.reflect.Modifier
import java.util.*
import kotlin.reflect.KProperty1

object Fun {
    @JvmStatic
    fun println(vararg args: Any?) {
        val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
        if (args.size > 1) {
            kotlin.io.println(gson.toJson(args.toList()))
        } else {
            kotlin.io.println(args[0])
        }
    }

    @JvmStatic
    fun reflectionToString(obj: Any): String {
        val s = LinkedList<String>()
        var clazz: Class<in Any>? = obj.javaClass
        while (clazz != null) {
            for (prop in clazz.declaredFields.filterNot { Modifier.isStatic(it.modifiers) }) {
                prop.isAccessible = true
                s += "${prop.name}=" + prop.get(obj)?.toString()?.trim()
            }
            clazz = clazz.superclass
        }
        return "${obj.javaClass.simpleName}=[${s.joinToString(", ")}]"
    }

    @JvmStatic
    fun reflectionToString2(obj: Any?): String {
        if (obj == null) {
            return "null"
        }
        val s = mutableListOf<String>()
        var clazz: Class<in Any>? = obj.javaClass
        while (clazz != null) {
            for (prop in clazz.declaredFields.filterNot { Modifier.isStatic(it.modifiers) }) {
                prop.isAccessible = true
                s += "${prop.name}=" + prop.get(obj)?.toString()?.trim()
            }
            clazz = clazz.superclass
        }

        return "${obj.javaClass.simpleName}=[${s.joinToString(", ")}]"
    }

    /**
     * @url https://stackoverflow.com/a/35539628
     */
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <R> readInstanceProperty(instance: Any, propertyName: String): R {
        val property = instance::class.members
            // don't cast here to <Any, R>, it would succeed silently
            .first { it.name == propertyName } as KProperty1<Any, *>
        // force a invalid cast exception if incorrect type here
        return property.get(instance) as R
    }
}