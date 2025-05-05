package kakkoiichris.nazonoshiro.script

class Parser(private val source: Source, private val lexer: Lexer) {
    private var token = lexer.next()

    fun parse(): Program {
        val decls = mutableListOf<Decl>()

        while (!atEndOfFile()) {
            decls += decl()
        }

        return Program(decls)
    }

    private fun here() =
        token.location

    private fun match(char: Char) =
        peek() == char

    private fun match(predicate: (Char) -> Boolean) =
        predicate(peek())

    private fun atEndOfFile() =
        match(TokenType.End)

    private fun isNumberStart(char: Char) =
        char.isDigit()

    private fun isNumber(char: Char) =
        char.isDigit()

    private fun isWordStart(char: Char) =
        char.isLetter() || char == '_'

    private fun isWord(char: Char) =
        char.isLetterOrDigit() || char == '_'

    private fun step(amount: Int = 1) = repeat(amount) {
        if (match('\n')) {
            row++
            col = 1
        }
        else {
            col++
        }

        pos++
    }

    private fun skip(char: Char) =
        if (match(char)) {
            step()

            true
        }
        else false

    private fun mustSkip(char: Char) {
        if (!skip(char)) {
            TODO()
        }
    }
}

class Program(val decls: List<Decl>) : Iterator<Decl> by decls.iterator()

interface Decl {
    class Const
    class Object
    class Entity
    class Area
    class Item
    interface Visitor
}

interface Stmt {
    interface Visitor
}

interface Expr {
    interface Visitor
}