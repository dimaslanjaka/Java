package dimas.kotlin.component

@Suppress("unused")
inline fun <reified T> mergeArrays(vararg arrays: Array<T>): Array<T?> {
    var result: Array<T?> = arrayOfNulls(0);
    for (array in arrays) {
        result += array;
    }
    return result
}

@Suppress("unused")
inline fun <reified T> mergeArrays2(vararg arrays: Array<T>): Array<T> {
    val list: MutableList<T> = ArrayList()
    for (array in arrays) {
        list.addAll(array.map { i -> i })
    }
    return list.toTypedArray()
}

@Suppress("unused")
fun <T> mergeArrays3(vararg arrays: Array<T>): Array<T?>? {
    var totalSize = 0
    for (array in arrays) {
        totalSize += array.size
    }
    var dest: Array<T?>? = null
    var destPos = 0
    for (array in arrays) {
        if (dest == null) {
            dest = array.copyOf(totalSize)
            destPos = array.size
        } else {
            System.arraycopy(array, 0, dest, destPos, array.size)
            destPos += array.size
        }
    }
    return dest
}