# SML-java

sml java implementation

[SML-specification](https://github.com/super-simple/SML-specification)

## notice

## 实现

### 数据模式

只实现了数据模式和配置模式,不支持文档模式 支持序列化和反序列化数据,功能和jackson类似

```text
sml()
books[
    /*
        多行注释
    */
    book{ //单行注释
        name{a}
        publishDate{20120803}
    }    
    book{
        name{b}
        publishDate{20120804}
    }
]
```

### 配置模式

只支持反序列化,读取成对应的实体

```text
sml() //保留,用于以后扩展
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
    ul[
        li{123}
        li{123}
        li{123}
        li{123}
    ]
}
```