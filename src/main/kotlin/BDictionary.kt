class BDictionary : BElement {
    private val list: ArrayList<Pair<String, BElement>> = arrayListOf()
    fun addPair(key: String, element: BElement) {
        list.add(Pair(key, element))
    }
    override fun serialize(): String {
        return list.joinToString(" | ") {
            "${it.first}:${it.second.serialize()}"
        }
    }
}