import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class BencoderTest {
    @Test
    fun testNumberDecode() {
        val encoder = Bencoder()
        var element = encoder.decode("i534e".toByteArray())
        assertEquals("534", element.serialize())

        element = encoder.decode("i0e".toByteArray())
        assertEquals("0", element.serialize())

        element = encoder.decode("i-42e".toByteArray())
        assertEquals("-42", element.serialize())

        assertThrows(Exception::class.java) {
            element = encoder.decode("i-053e".toByteArray())
        }

        element = encoder.decode("ie".toByteArray())
        assertEquals("", element.serialize())

        assertThrows(Exception::class.java) {
            element = encoder.decode("i56-3e".toByteArray())
        }

        assertThrows(Exception::class.java) {
            element = encoder.decode("i56".toByteArray())
        }
    }

    @Test
    fun decodeByteString() {
        val encoder = Bencoder()
        var element = encoder.decode("5:abcde".toByteArray())
        assertEquals("abcde", element.serialize())

        assertThrows(Exception::class.java) {
            element = encoder.decode("5:ab".toByteArray())
        }

        assertThrows(Exception::class.java) {
            element = encoder.decode("5:".toByteArray())
        }


        element = encoder.decode("1:ab".toByteArray())
        assertEquals("a", element.serialize())

    }

    @Test
    fun decodeList() {
        val encoder = Bencoder()
        var element = encoder.decode("l1:a5:abcdei5463ee".toByteArray())
        assertEquals("a | abcde | 5463", element.serialize())

        assertThrows(Exception::class.java) {
            element = encoder.decode("l1:a5:abcde563ee".toByteArray())
        }

        element = encoder.decode("ll3:abc2:deei12123ee".toByteArray())
        assertEquals("abc | de | 12123", element.serialize())
    }

    @Test
    fun decodeDictionary() {
        val encoder = Bencoder()
        var element = encoder.decode("d3:bar4:spam3:fooi42ee".toByteArray())
        assertEquals("bar:spam | foo:42", element.serialize())
    }
}