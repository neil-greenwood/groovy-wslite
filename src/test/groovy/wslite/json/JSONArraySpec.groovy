/* Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wslite.json

import spock.lang.*

class JSONArraySpec extends Specification {

    void 'should be a Collection'() {
        expect:
        new JSONArray() instanceof Collection
    }

    void 'should answer to the Groovy truth'() {
        given:
        JSONArray json1 = new JSONArray()
        JSONArray json2 = new JSONArray('''["foo","bar"]''')
        JSONArray json3 = new JSONArray('[]')

        expect:
        !json1
        json2
        !json3
    }

    void 'can be constructed from a GString'() {
        given:
        def someVar = 'GStrings are cool'

        when:
        JSONArray result = new JSONArray("""["foo", "Bar says ${someVar}"]""")

        then:
        'Bar says GStrings are cool' == result[1]
        '''["foo","Bar says GStrings are cool"]''' == result.toString()
    }

    void 'can be constructed from a List with GString as a value'() {
        given:
        def someVar = 'bar'
        def someList = ['foo', "foo is ${someVar}"]

        when:
        JSONArray result = new JSONArray(someList)

        then:
        'foo is bar' == result[1]
        '''["foo","foo is bar"]''' == result.toString()
    }

    void 'properly wraps embedded json objects'() {
        given:
        def jsonMap = ['foo', [bar: 'baz'], 'buzz']

        when:
        def result = new JSONArray(jsonMap)

        then:
        'foo' == result[0]
        result[1] instanceof wslite.json.JSONObject
        'baz' == result[1].bar
    }

    void 'properly wraps embedded json arrays'() {
        given:
        def jsonMap = ['foo', ['bar', 'baz', 'fizz'], 'buzz']

        when:
        def result = new JSONArray(jsonMap)

        then:
        'foo' == result[0]
        result[1] instanceof wslite.json.JSONArray
        'fizz' == result[1][2]
    }

    void 'size'() {
        expect:
        result == new JSONArray(map).size()

        where:
        result  | map
        0       | []
        1       | [null]
        1       | ['foo']
        5       | ['foo', 'bar', ['one', 'two'], [path: '/users'], 'done']
    }

    void 'is empty'() {
        expect:
        result == new JSONArray(map).isEmpty()

        where:
        result  | map
        true    | []
        false   | [null]
        false   | ['foo']
        false   | ['foo', 'bar', ['one', 'two'], [path: '/users'], 'done']
    }

    void 'contains'() {
        expect:
        result == new JSONArray(map).contains(object)

        where:
        result  | object                       | map
        true    | 'foo'                        | ['foo']
        false   | 'foo'                        | [null]
        true    | "${'foo'}"                   | ['foo']
    }

    void 'retain all'() {
        when:
        def result = new JSONArray([1, 2, 'foo'])
        result.retainAll([1, 'foo'])

        then:
        2 == result.size()
        !result.contains(2)
        result.contains(1)
        result.contains('foo')
    }

    void 'remove'() {
        when:
        def result = new JSONArray([1,2,3,4,2,5,2])
        result.remove(2)

        then:
        4 == result.size()
    }

    void 'contains all'() {
        when:
        def result = new JSONArray([1,2,3,4])

        then:
        !result.containsAll([2,3,5])
        result.containsAll([1,4])
    }

    void 'add all'() {
        when:
        def result = new JSONArray([1])
        result.addAll([2,3])

        then:
        3 == result.size()
    }

    void 'remove all'() {
        when:
        def result = new JSONArray([1,2,3,4,5])
        result.removeAll([2,3])

        then:
        3 == result.size()
    }

    void 'clear'() {
        when:
        def result = new JSONArray([1,2,3])
        result.clear()

        then:
        0 == result.size()
        result.isEmpty()
    }

}
