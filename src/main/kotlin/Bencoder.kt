import java.lang.StringBuilder

class Bencoder {
    fun decode(input: ByteArray): BElement {
        if (input.isEmpty()) {
            throw Exception("Empty input")
        }

        val pair = extractElement(input, 0)
        return pair.first
    }

    private fun extractElement(input: ByteArray, position: Int): Pair<BElement, Int> {
        val pair = when (input[position].toInt()) {
            105 -> parseNumber(input, position) // 'i'
            108 -> parseList(input, position) // 'l'
            100 -> parseDictionary(input, position) // 'd'
            in 48..57 -> parseByteString(input, position)
            else -> throw Exception("Invalid input")
        }

        print(pair.first.serialize())

        return pair
    }

    private fun parseNumber(input: ByteArray, currentPosition: Int): Pair<BElement, Int> {
        var pos = currentPosition
        pos++
        if(pos >= input.size) throw Exception("Invalid input")
        val builder = StringBuilder()
        if(input[pos].toInt().toChar() == '-') {
            builder.append("-")
            pos++
        }
        var foundEnd = false
        while(pos < input.size) {
            val currentChar = input[pos].toInt().toChar()
            if(currentChar == 'e') {
                foundEnd = true;
                pos++
                break
            }
            if(currentChar in '0'..'9') {
                builder.append(currentChar)
            } else {
                throw Exception("Invalid input")
            }
            pos++
        }

        if(builder.length >= 2 && builder[0] == '-' && builder[1] == '0') {
            throw Exception("Invalid input")
        }

        if(!foundEnd) {
            throw Exception("Invalid input")
        }

        val pair = Pair(BInteger(builder.toString()), pos)
        print(pair.first.serialize())

        return pair
    }

    private fun parseList(input: ByteArray, currentPosition: Int): Pair<BElement, Int> {
        val blist = BList()
        var pos = currentPosition
        pos++ // skip 'l'
        while(pos < input.size) {
            val pair = extractElement(input, pos)
            blist.addElement(pair.first)
            pos = pair.second
            if(input[pos].toInt().toChar() == 'e') {
                pos++
                break
            }
        }

        val pair = Pair(blist, pos)
        print(pair.first.serialize())

        return pair
    }

    private fun parseDictionary(input: ByteArray, currentPosition: Int): Pair<BElement, Int> {
        var pos = currentPosition
        val dictionary = BDictionary()
        pos++ // skip 'd'
        while(pos < input.size) {
            val keyPos = parseByteString(input, pos)
            val valuePos = extractElement(input, keyPos.second)
            pos = valuePos.second
            dictionary.addPair(keyPos.first.serialize(), valuePos.first)
            if(input[pos].toInt().toChar() == 'e') {
                pos++
                break
            }
        }

        val pair = Pair(dictionary, pos)
        print(pair.first.serialize())

        return pair
    }

    private fun parseByteString(input: ByteArray, currentPosition: Int): Pair<BElement, Int> {
        var pos = currentPosition
        val builder = StringBuilder()
        var len = 0;
        // Parse length
        while(pos < input.size) {
            val currentChar = input[pos].toInt().toChar()
            if(currentChar == ':') {
                pos++ // Skip ':'
                break
            }
            if(currentChar !in '0'..'9') {
                throw Exception("Invalid input")
            }
            len = (len * 10) + (currentChar - '0')
            pos++
        }
        if(pos >= input.size) {
            throw Exception("Invalid input")
        }


        val startIndex = pos;
        val endIndex = pos + len - 1

        if(endIndex >= input.size) {
            throw Exception("Invalid input")
        }

        val pair = Pair(BByteString(input.sliceArray(startIndex..endIndex)), endIndex + 1)
        print(pair.first.serialize())

        return pair
    }
}