package kakkoiichris.nazonoshiro.script

private const val NUL = '\u0000'

class Lexer(private val source: Source) : Iterator<Token<*>> {
    private var pos = 0
    private var row = 1
    private var col = 1

    override fun hasNext() =
        pos <= source.text.length

    override fun next(): Token<*> {
        while (!atEndOfFile()) {
            if (match(Char::isWhitespace)) {
                skipWhitespace()

                continue
            }

            if (match('#')) {
                skipComment()

                continue
            }

            return when {
                match(::isNumberStart) -> number()

                match(::isWordStart)   -> word()

                match('"')             -> string()

                else                   -> symbol()
            }
        }

        return Token(here(), TokenType.End)
    }

    private fun here() =
        Location(source.name, pos, row, col)

    private fun peek() =
        if (pos in source.text.indices) source.text[pos] else NUL

    private fun match(char: Char) =
        peek() == char

    private fun match(predicate: (Char) -> Boolean) =
        predicate(peek())

    private fun atEndOfFile() =
        match(NUL)

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

    private fun skipWhitespace() {
        do {
            step()
        }
        while (match(Char::isWhitespace))
    }

    private fun skipComment() {
        do {
            step()
        }
        while (!match('\n'))
    }

    private fun StringBuilder.take() {
        append(peek())

        step()
    }

    private fun number(): Token<TokenType.Value> {
        val location = here()

        val result = buildString {
            do {
                take()
            }
            while (match(::isNumber))

            if (match('.')) {
                do {
                    take()
                }
                while (match(::isNumber))
            }
        }

        return Token(location, TokenType.Value(result.toDouble()))
    }

    private fun word(): Token<*> {
        val location = here()

        val result = buildString {
            do {
                take()
            }
            while (match(::isWord))
        }

        val type = when {
            result == "true"                  -> TokenType.Value(true)

            result == "false"                 -> TokenType.Value(false)

            TokenType.Keyword.isValid(result) -> TokenType.Keyword.valueOf(result)

            else                              -> TokenType.Name(result)
        }

        return Token(location, type)
    }

    private fun string(): Token<TokenType.Value> {
        val location = here()

        mustSkip('"')

        if (skip('"')) {
            return Token(location, TokenType.Value(""))
        }

        val result = buildString {
            do {
                when {
                    match('\\') -> append(escape())

                    else        -> take()
                }
            }
            while (!skip('"'))
        }

        return Token(location, TokenType.Value(result))
    }

    private fun escape(): Char {
        mustSkip('\\')

        return when {
            skip('\\') -> '\\'

            skip('n')  -> '\n'

            skip('t')  -> '\t'

            skip('0')  -> NUL

            else       -> TODO()
        }
    }

    private fun symbol(): Token<TokenType.Symbol> {
        val location = here()

        val type = when {
            skip('+') -> when {
                skip('=') -> TokenType.Symbol.PLUS_EQUAL
                else      -> TokenType.Symbol.PLUS
            }

            skip('-') -> when {
                skip('=') -> TokenType.Symbol.DASH_EQUAL
                else      -> TokenType.Symbol.DASH
            }

            skip('*') -> when {
                skip('=') -> TokenType.Symbol.STAR_EQUAL
                else      -> TokenType.Symbol.STAR
            }

            skip('/') -> when {
                skip('=') -> TokenType.Symbol.SLASH_EQUAL
                else      -> TokenType.Symbol.SLASH
            }

            skip('%') -> when {
                skip('=') -> TokenType.Symbol.PERCENT_EQUAL
                else      -> TokenType.Symbol.PERCENT
            }

            skip('<') -> when {
                skip('=') -> TokenType.Symbol.LESS_EQUAL
                else      -> TokenType.Symbol.LESS
            }

            skip('>') -> when {
                skip('=') -> TokenType.Symbol.GREATER_EQUAL
                else      -> TokenType.Symbol.GREATER
            }

            skip('!') -> when {
                skip('=') -> TokenType.Symbol.EXCLAMATION_EQUAL
                else      -> TokenType.Symbol.EXCLAMATION
            }

            skip('=') -> when {
                skip('=') -> TokenType.Symbol.DOUBLE_EQUAL
                else      -> TokenType.Symbol.EQUAL
            }

            skip('&') -> when {
                skip('&') -> TokenType.Symbol.DOUBLE_AMPERSAND
                else      -> TODO()
            }

            skip('|') -> when {
                skip('|') -> TokenType.Symbol.DOUBLE_PIPE
                else      -> TODO()
            }

            skip('(') -> TokenType.Symbol.LEFT_PAREN

            skip(')') -> TokenType.Symbol.RIGHT_PAREN

            skip('[') -> TokenType.Symbol.LEFT_SQUARE

            skip(']') -> TokenType.Symbol.RIGHT_SQUARE

            skip('{') -> TokenType.Symbol.LEFT_BRACE

            skip('}') -> TokenType.Symbol.RIGHT_BRACE

            skip(',') -> TokenType.Symbol.COMMA

            skip(':') -> TokenType.Symbol.COLON

            skip(';') -> TokenType.Symbol.SEMICOLON

            else      -> TODO()
        }

        return Token(location, type)
    }
}

data class Token<T : TokenType>(val location: Location, val type: T)

data class Location(val name: String, val pos: Int, val row: Int, val col: Int)

interface TokenType {
    val rep: String

    data class Value(val value: Any) : TokenType {
        override val rep = value.toString()
    }

    data class Name(val name: String) : TokenType {
        override val rep = name
    }

    enum class Keyword : TokenType {
        CONST,
        OBJECT,
        ENTITY,
        AREA,
        ITEM,
        GOTO,
        VAR,
        IF,
        ELSE,
        WHILE,
        DO,
        BREAK,
        CONTINUE,
        FN,
        RETURN,;

        override val rep = name.lowercase()

        companion object {
            fun isValid(name: String) =
                entries.any { it.rep == name }
        }
    }

    enum class Symbol(override val rep: String) : TokenType {
        PLUS_EQUAL("+="),
        PLUS("+"),
        DASH_EQUAL("-="),
        DASH("-"),
        STAR_EQUAL("*="),
        STAR("*"),
        SLASH_EQUAL("/="),
        SLASH("/"),
        PERCENT_EQUAL("%="),
        PERCENT("%"),
        LESS_EQUAL("<="),
        LESS("<"),
        GREATER_EQUAL(">="),
        GREATER(">"),
        DOUBLE_EQUAL("=="),
        EQUAL("="),
        EXCLAMATION_EQUAL("!="),
        EXCLAMATION("!"),
        DOUBLE_AMPERSAND("&&"),
        DOUBLE_PIPE("||"),
        LEFT_PAREN("("),
        RIGHT_PAREN(")"),
        LEFT_SQUARE("["),
        RIGHT_SQUARE("]"),
        LEFT_BRACE("{"),
        RIGHT_BRACE("}"),
        COMMA(","),
        COLON(":"),
        SEMICOLON(";"),
    }

    data object End : TokenType {
        override val rep = "EOF"
    }
}