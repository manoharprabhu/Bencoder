import java.lang.StringBuilder

class BList : BElement {
    private val list: ArrayList<BElement> = arrayListOf()
    fun addElement(element: BElement) {
        list.add(element)
    }
    override fun serialize(): String {
        return list.joinToString(" | ") {
             it.serialize()
        }
    }
}