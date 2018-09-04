package Main.extras

import java.util.*

class RandomNumber {
    companion object {
        fun ClosedRange<Int>.random() =
                Random().nextInt((endInclusive + 1) - start) +  start
    }
}