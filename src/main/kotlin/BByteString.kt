import java.lang.StringBuilder

class BByteString(val byteString: ByteArray) : BElement {

    override fun serialize(): String {
        val builder = StringBuilder()
        byteString.forEach {
            builder.append(it.toInt().toChar())
        }

        return builder.toString()
    }
}