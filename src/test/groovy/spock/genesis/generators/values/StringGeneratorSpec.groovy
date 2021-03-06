package spock.genesis.generators.values

import spock.lang.Specification
import spock.lang.Unroll

class StringGeneratorSpec extends Specification {

    def 'test make string generation'() {
        setup:
            def generator = new StringGenerator(length, potentialCharacters)
        when:
            String result = generator.next()
        then:
            Set potential = potentialCharacters.toSet()
            result.size() <= length
            result.every {
                it in potential
            }

        where:
            length | potentialCharacters
            0      | 'a'
            1      | 'a'
            100    | 'abc5$@'
            100    | ('A'..'z').collect()
    }

    def 'default random string generation'() {
        setup:
            def generator = new StringGenerator()
            Set potential = CharacterGenerator.DEFAULT_CHARACTERS.toSet()*.toString()
        when:
            String result = generator.next()
        then:
            result.size() <= StringGenerator.DEFAULT_LENGTH_LIMIT
            result.every {
                it in potential
            }
        where:
            iteration << (0..1000)
    }

    @Unroll
    def 'pattern #pattern generation'() {
        setup:
            def generator = new StringGenerator(~pattern)
        when:
            Set<String> results = generator.take(100).realized.toSet()
        then:
            results.size() > 1
            results.each { assert it ==~ pattern }
        where:
            pattern << [
                    /\d\w\d/,
                    /\w*/,
                    /[1-9][0-9]{2}-\d{3}-\d{4}/,
                    /\d{3}-\d{3}-\d{4}\s(x|(ext))\d{3,5}/,
                    '(https?|ftp|file)://[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|]\\d',
                    /[A-Z][a-z]+( [A-Z][a-z]+)?/
            ]
    }
}
