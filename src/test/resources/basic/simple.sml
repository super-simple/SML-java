{//数据模式下,顶层对象无需有名字
    attribute1[
        "aaa"
        aaaa
        []
        {}
    ]
    attribute2{
        attribute3(value3) //暗示这是一个简单属性
        attribute4(value4)
        attribute5{//暗示这是一个对象
            attr1(value1)
        }
        "attribute 6"{ // 数据模式下,属性名字可以包含空白字符串,用双引号或者单引号包裹均可
            attr2(value2)
        }
    }
}