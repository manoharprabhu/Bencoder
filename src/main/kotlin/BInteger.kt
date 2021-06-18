data class BInteger(val value: String) : BElement {
    override fun serialize(): String {
        return value
    }
}