# SML-java

sml java implementation

[SML-specification](https://github.com/super-simple/SML-specification)

## 实现

### 数据模式

支持序列化和反序列化数据,功能和jackson类似

```sml
{//数据模式下,顶层对象无需有名字
    attribute1[
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
```

### 配置模式

只支持反序列化,读取成对应的实体

```sml
sml(){} //保留,用于以后扩展
document(foo="bar" bar="foo"){
    //body 只包含节点
    body{
        //h1 只包含值
        h1{
            hello document
        }
        h2{
            hello document
        }
    }
}
```