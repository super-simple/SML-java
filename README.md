# ss-java

ss json.
simply json.

```ssjson
{
    /*
        多行注释 multiline comment
    */
    attr1[
        "aaa" // 单行注释 
        'aaa'
        123
        []
        {}
    ]
    attr2{
        attr1 "var1" //可选的单引号或者双引号
        attr2 "var2"
        "attr 3" 123 // 123是数字类型
        'attr 4' "123\ //多行字符串
        \123\ //多行字符串
        \123"
        attr5 {
            attr1 "var1"
        }
        attr6 []
    }
}
```